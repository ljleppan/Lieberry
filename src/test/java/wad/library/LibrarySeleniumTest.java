package wad.library;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wad.library.domain.Book;
import wad.library.repository.BookRepository;
import wad.library.service.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/spring-base.xml")
public class LibrarySeleniumTest {
    
    private String port;
    private WebDriver driver;
    private String baseUrl;
    private String rootUrl;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    BookRepository bookRepository;
    
    @Before
    public void setUp() throws Exception {
        clearDatabase();
        initDatabase();
        driver = new HtmlUnitDriver();
        port = System.getProperty("jetty.port", "8080");
        baseUrl = "http://localhost:" + port+"/wepa/app";
        rootUrl = "http://localhost:" + port;
        go("/logout");
    }
    
    @After
    public void tearDown(){
        driver.close();
    }
    
    @Test
    public void rootLoadsMenu(){
        driver.get(rootUrl+"/wepa/app");
        System.out.println("OVER HERE: "+driver.getCurrentUrl());
        Assert.isTrue(
                sourceContains("Browse library"),
                "Root should serve a page with a 'Browse library' button."
                );
    }
    
    // Login and Logout buttons 
    
    @Test
    public void loginButtonWhenUserNotLoggedIn(){
        goBase();
        Assert.isTrue(
                sourceContains("Login"),
                "Menu should contain a 'Login' button when the user is not logged in."
                );
    }
    
    @Test
    public void logoutButtonWhenUserLoggedIn(){
        login();
        Assert.isTrue(
                sourceContains("Logout"),
                "Menu should contain a 'Logout' button if user is logged in."
                );
    }
    
    @Test
    public void noLogoutButtonWhenUserNotLoggedIn(){
        goBase();
        Assert.isFalse(
                sourceContains("Logout</a>"),
                "Menu should NOT contain a 'Logout' button if user is not logged in."
                );
    }
    
    @Test
    public void noLoginButtonWhenUserLoggedIn(){
        login();
        Assert.isFalse(
                sourceContains("Login"),
                "Menu should NOT contain a 'Login' button if user is logged in."
                );
    }
    
    @Test
    public void loginButtonTakesUserToLoginPage(){
        goBase();
        WebElement e = findById("login");
        e.click();
        Assert.isTrue(
                titleContains("Login"),
                "Pressing the 'Login' button should bring up the login page."
                );
    }
    
    @Test
    public void logoutButtonTakesUserToMenu(){
        login();
        WebElement e = findById("logout");
        e.click();
        Assert.isTrue(
                sourceContains("Browse library"),
                "Pressing the 'Logout' button should take the user to the menu.");
    }
    
    // Menu buttons 
    
    @Test
    public void onlyBrowseAndRegisterForUnauthorizedUsers(){
        goBase();
        Assert.isTrue(
                sourceContains("Browse library"),
                "Menu should always contain a 'Browse library' button."
                );
        Assert.isTrue(
                sourceContains("Register"),
                "Menu should always contain a 'Register' button."
                );
        Assert.isFalse(
                sourceContains("Add a book"),
                "Menu should NOT contain an 'Add a book' button if user is not logged in."
                );
        Assert.isFalse(
                sourceContains("Import a book"),
                "Menu should NOT contain an 'Import a book' button if user is not logged in."
                );
    }
    
    @Test
    public void showAddAndImportForLoggedInUsers(){
        login();
        Assert.isTrue(
                sourceContains("Add a book"),
                "Menu should contain an 'Add a book' button if user is logged in."
                );
        Assert.isTrue(
                sourceContains("Import a book"),
                "Menu should contain an 'Import a book' button if user is logged in."
                );
        Assert.isFalse(
                sourceContains("Register"),
                "Menu should NOT contain a 'Register' button if user is logged in."
                );
    }
    
    @Test
    public void noAddOrImportAfterLoggingOut(){
        login();
        WebElement e = findById("logout");
        e.click();
        Assert.isFalse(
                sourceContains("Add a book"),
                "Menu should not contain an 'Add a book' button after logging out.");
        
        Assert.isFalse(
                sourceContains("Import a book"),
                "Menu should not contain an 'Import a book' button after logging out.");
    }
    
    // Login page 
    
    @Test
    public void loginPageContainsUsernameAndPasswordFields(){
        go("/login");
        Assert.isTrue(
                findById("j_password") != null,
                "Login page should contain a input with id 'j_password'"
                );
        Assert.isTrue(
                findById("j_username") != null,
                "Login page should contain a input with id 'j_username'"
                );
    }
    
    @Test
    public void loginPageShouldNotHaveAnErrorBeforeFirstLoginAttempt(){
        go("/login");
        Assert.isFalse(
                sourceContains("Bad Credentials"),
                "Login page should NOT contain an error before any login attempts.");
    }
    
    @Test
    public void loginWithCorrectCredentialsTakesUserToMenu(){
        login();
        
        Assert.isTrue(
                sourceContains("Browse library"),
                "User should be redirected to the menu after logging in."
                );
    }
    
    @Test
    public void loginWithBadCredentialsTakesUserToLoginPageWithError(){
        go("/login");
        WebElement e = findById("j_username");
        e.clear();
        e.sendKeys("mockUserForSeleniumThisIsBad");
        e = findById("j_password");
        e.clear();
        e.sendKeys("bad_password");
        e.submit();
        
        Assert.isTrue(
                titleContains("Login"),
                "User should return to Login page if login fails."
                );
        
        Assert.isTrue(
                sourceContains("not successful"),
                "Login page should contain an error after a failed login.");
    }
    
    // Browse library 
    
    @Test
    public void browseLibraryShownWithoutLoggingIn(){
        go("/books");
        Assert.isTrue(
                sourceContains("books matching the query"),
                "The 'Browse books' page should be viewable without logging in.");
    }
    
    @Test
    public void browseLibraryViewButtonShownForEveryone(){
        go("/books");
        Assert.isTrue(
                sourceContains("View"),
                "The 'View' button should be visible without logging in.");
    }
    
    @Test
    public void browseLibraryEditAndDeleteNotShownForEveryone(){
        go("/books");
        Assert.isFalse(
                sourceContains("/wepa/app/edit/"),
                "The 'Edit' button should NOT be visible without logging in.");
        Assert.isFalse(
                sourceContains("Delete"),
                "The 'Delete' button should NOT be visible without logging in.");
    }
    
    @Test
    public void browseLibraryEditAndDeleteShownWhenLoggedIn(){
        login();
        go("/books");
        Assert.isTrue(
                sourceContains("Edit"),
                "The 'Edit' button should be visible after logging in.");
        Assert.isTrue(
                sourceContains("Delete"),
                "The 'Delete' button should be visible after logging in.");
    }
    
    @Test
    public void browseLibraryCorrectPageInfoOnFirstPage(){
        go("/books");
        Assert.isFalse(
                sourceContains("Prev") == null,
                "There should be no 'Prev' button on the first page.");
        Assert.isTrue(
                sourceContains("Next") != null,
                "There should be a 'Next' button on the first page.");
        Assert.isTrue(
                sourceContains("page 1 of "),
                "The first page should have an index of 1");
    }
    
    @Test
    public void browseLibraryCorrectPageInfoOnSecondPage(){
        go("/books?pageNumber=2");
        Assert.isFalse(
                sourceContains("Next"),
                "There should be no 'Next' button on the second page.");
        Assert.isTrue(
                sourceContains("Prev") != null,
                "There should be a 'Prev' button on the second page.");
        Assert.isTrue(
                sourceContains("page 2 of "),
                "The first page should have an index of 2");
    }
    
    @Test
    public void browseLibraryClickingOnAuthorRedirectsCorrectly(){
        go("/books");
        WebElement e = findByLinkText("Pelle H Ermanni");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/author/Pelle%20H%20Ermanni"),
                "Clicking on an author's name should redirect the user to an url of the form '/books/author/<author>");
    }
    
    @Test
    public void browseLibraryClickingOnPublisherRedirectsCorreclty(){
        go("/books");
        WebElement e = findByLinkText("Gaudeamus");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/publisher/Gaudeamus"),
                "Clicking on a publisher's name should redirect the user to an url of the form '/books/publisher/<publisher>");
    }
    
    @Test
    public void browseLibraryClickingOnPublicationYearRedirectsCorreclty(){
        go("/books");
        WebElement e = findByLinkText("2000");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/publicationyear/2000"),
                "Clicking on a publication year should redirect the user to an url of the form 'books/publicationyear/<publicationyear>");
    }
    
    // Search 
    
    @Test
    public void searchTitleSearchWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("A happy day"),
                "First page should not contain the book 6th book in alphabetical order.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("A happy day, or a bidé day");
        e.submit();
        
        Assert.isTrue(
                sourceContains("A happy day, or a bidé day"),
                "Searching for title 'A happy day, or a bidé day', the results should contain the only book with query in the title.");
        Assert.isFalse(
                sourceContains("circuitry"),
                "Searching for title 'A happy day, or a bidé day', the results NOT should contain the text 'circuitry'.");
    }
    
    @Test
    public void searchPartialTitleSearchWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("A happy day"),
                "First page should not contain the book 6th book in alphabetical order.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("A happy day");
        e.submit();
        
        Assert.isTrue(
                sourceContains("A happy day"),
                "Searching for title 'A happy day', the results should contain the only book with query in the title.");
        Assert.isFalse(
                sourceContains("circuitry"),
                "Searching for title 'A happy day', the results should NOT contain the text 'circuitry'.");
    }
    
    @Test
    public void searchAuthorWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("Vinski"),
                "First page should not contain any books by 'Vinski'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("Vinski");
        e = findById("author");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("Vinski"),
                "Searching for author 'Vinski', the results should contain the only book by the author 'Vinski'.");
        Assert.isFalse(
                sourceContains("Vaadolf"),
                "Searching for author 'Vinski', the results should NOT contain the text 'Vaadolf'.");
    }
    
    @Test
    public void searchPartialAuthorWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("Vinski"),
                "First page should not contain any books by 'Vinski'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("in");
        e = findById("author");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("Vinski"),
                "Searching for author 'in', the results should contain the only book by the author 'Vinski'.");
        Assert.isFalse(
                sourceContains("Vaadolf"),
                "Searching for author 'in', the results should NOT contain the text 'Vaadolf'.");
    }
    
    @Test
    public void searchIsbnWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("98678"),
                "First page should not contain the book with ISBN '98678'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("98678");
        e = findById("isbn");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("98678"),
                "Searching for ISBN '98678', the results should contain the only book with the ISBN '98678'.");
        Assert.isFalse(
                sourceContains("978472579"),
                "Searching for ISBN '978472579', the results should NOT contain the only book with the ISBN '98678'.");
    }
    
    @Test
    public void searchPartialIsbnWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("978472579"),
                "First page should not contain the book with ISBN '978472579'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("4");
        e = findById("isbn");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("978472579"),
                "Searching for ISBN '4', the results should contain the book with the ISBN '978472579'.");
        
        Assert.isFalse(
                sourceContains("9780"),
                "Searching for ISBN '4', the results should NOT contain the book with the ISBN '9780'.");
    }
    
    @Test
    public void searchPublisherWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("98678"),
                "First page should not contain the book with ISBN '98678'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("Fuuu");
        e = findById("publisher");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("98678"),
                "Searching for publisher 'Fuuu', the results should contain the book with the ISBN '98678'.");
        Assert.isFalse(
                sourceContains("9780"),
                "Searching for publisher 'Fuuu', the results should NOT contain the book with the ISBN '9780'.");
    }
    
    @Test
    public void searchPartialPublisherWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("98678"),
                "First page should not contain the book with ISBN '98678'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("uu");
        e = findById("publisher");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("98678"),
                "Searching for publisher 'uu', the results should contain the book with the ISBN '98678'.");
        Assert.isFalse(
                sourceContains("9780"),
                "Searching for publisher 'uu', the results should NOT contain the book with the ISBN '9780'.");
    }
    
    @Test
    public void searchPublicationYearWorks(){
        go("/books");
        Assert.isFalse(
                sourceContains("98678"),
                "First page should not contain the book with ISBN '98678'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("1995");
        e = findById("publicationYear");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("98678"),
                "Searching for publication year '1995', the results should contain the book with the ISBN '98678'.");
        Assert.isFalse(
                sourceContains("242134"),
                "Searching for publication year '1995', the results should NOT contain the book with the ISBN '242134'.");
    }
    
    @Test
    public void searchPublicationYearOnlyReturnCompleteMatches(){
        go("/books");
        Assert.isFalse(
                sourceContains("98678"),
                "First page should not contain the book with ISBN '98678'.");
        
        WebElement e = findByName("query");
        e.clear();
        e.sendKeys("20");
        e = findById("publicationYear");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("0 books matching the query"),
                "Searching for publication year '20', the results should NOT contain any books.");
    }
    
    // Filter URLs 
    
    @Test
    public void filterAuthorReturnsBooksByFilteredAuthor(){
        go("/books/author/Pelle%20H%20Ermanni");
        Assert.isTrue(
                sourceContains("for 2 books matching the query"),
                "Filtering by author 'Pelle H Ermanni' should produce exactly two books.");
        Assert.isTrue(
                sourceContains("242134"),
                "Filtering by author 'Pelle H Ermanni', the results should contain the ISBN '242134'.");
        Assert.isTrue(
                sourceContains("75234212349"),
                "Filtering by author 'Pelle H Ermanni', the results should contain the ISBN '75234212349'.");
    }
    
    @Test
    public void filterAuthorDoesNotReturnPartialMatches(){
        go("/books/author/Pelle");
        Assert.isTrue(
                sourceContains("for 0 books matching the query"),
                "Filtering by author 'Pelle' should produce exactly zero books.");
    }
    
    @Test
    public void filterPublisherReturnsBooksByFilteredPublisher(){
        go("/books/publisher/Fuuu");
        Assert.isTrue(
                sourceContains("for 3 books matching the query"),
                "Filtering by publisher 'Fuuu' should produce exactly three books.");
        Assert.isTrue(
                sourceContains("242134"),
                "Filtering by publisher 'Fuuu', the results should contain the ISBN '242134'.");
        Assert.isTrue(
                sourceContains("75234212349"),
                "Filtering by publisher 'Fuuu', the results should contain the ISBN '75234212349'.");
        Assert.isTrue(
                sourceContains("98678"),
                "Filtering by publisher 'Fuuu', the results should contain the ISBN '98678'.");
    }
    
    @Test
    public void filterPublisherDoesNotReturnPartialMatches(){
        go("/books/publisher/Fu");
        Assert.isTrue(
                sourceContains("for 0 books matching the query"),
                "Filtering by publisher 'Fu' should produce exactly zero books.");
    }
    
    @Test
    public void filterPublicationYearReturnBooksFilteredByPublicationYear(){
        go("/books/publicationyear/1995");
        Assert.isTrue(
                sourceContains("for 1 books matching the query"),
                "Filtering by publication year '1995' should return exactly one book.");
        Assert.isTrue(
                sourceContains("98678"),
                "Filtering by publication year '1995', the results should contain the ISBN '98678'");
        
    }
    
    @Test
    public void filterPublicationYearDoesNotReturnPartialMatches(){
        go("/books/publicationyear/19");
        Assert.isTrue(
                sourceContains("for 0 books matching the query"),
                "Filtering by publication year '19' should return exactly zero books.");
    }
    
    // Book details //
    
    @Test
    public void bookDetailsContainsTheRequiredInformation(){
        go("/books/72579");
        Assert.isTrue(
                sourceContains("<img src=\"https://covers.openlibrary.org/b/isbn/72579-M.jpg\""),
                "Book's details should include an image of the book's cover, retrieved from OpenLibrary.");
        Assert.isTrue(
                sourceContains("Introduction to circuitry"),
                "Book's details should include the book's title.");
        Assert.isTrue(
                sourceContains("Cherryl A Rousel"),
                "Book's details should include the author's name.");
        Assert.isTrue(
                sourceContains("<a href=\"/wepa/app/books/author/Cherryl A Rousel\""),
                "Book's details should include a link all the books by the same author.");
        Assert.isTrue(
                sourceContains("72579"),
                "Book's details should include the book's ISBN.");
        Assert.isTrue(
                sourceContains("Gaudeamus"),
                "Book's details should include the publisher's name.");
        Assert.isTrue(
                sourceContains("<a href=\"/wepa/app/books/publisher/Gaudeamus\""),
                "Book's details should include a link all the books by the same publisher.");
        Assert.isTrue(
                sourceContains("1999"),
                "Book's details should include the publication year.");
        Assert.isTrue(
                sourceContains("<a href=\"/wepa/app/books/publicationyear/1999\""),
                "Book's details should include a link all the books published during the same year.");
    }
    
    // Add and edit books //
    
    @Test
    public void addBookAuthorizationLimitsUserAccess(){
        go("/add");
        Assert.isFalse(
                sourceContains("Lieberry - Add a book"),
                "Unauthorized users attempting to access the 'Add a book' page should be redirected to the 'Login' page.");
        login();
        Assert.isTrue(
                sourceContains("Lieberry - Add a book"),
                "Authorized users attempting to access the 'Add a book' page should see the requested page.");
    }
    
    @Test
    public void addBookPageContainsAllNecessaryInputs(){
        login();
        go("/add");
        Assert.isTrue(
                sourceContains("name=\"title\""),
                "An element with the name 'title' should be present.");
        Assert.isTrue(
                sourceContains("name=\"authors[0]\""),
                "An element with the name 'authors[0]' should be present.");
        Assert.isTrue(
                sourceContains("name=\"isbn\""),
                "An element with the name 'isbn' should be present.");
        Assert.isTrue(
                sourceContains("name=\"publisher\""),
                "An element with the name 'publisher' should be present.");
        Assert.isTrue(
                sourceContains("name=\"publicationYear\""),
                "An element with the name 'publicationYear' should be present.");
        Assert.isTrue(
                sourceContains("type=\"submit\""),
                "An element with the type 'submit' should be present.");
        Assert.isTrue(
                sourceContains("value=\"Add\""),
                "An element with the value 'Add' should be present.");
        Assert.isTrue(
                sourceContains("value=\"Remove\""),
                "An element with the value 'Remove' should be present.");
    }
    
    @Test
    public void addBookSubmittingAndDeleting(){
        login();
        go("/add");
        input("title", "TestBook");
        input("author0", "TestAuthor");
        input("isbn", "123456789");
        input("publisher", "TestPublisher");
        WebElement e = input("publicationYear", "2012");
        e.submit();
        Assert.isTrue(
                driver.getTitle().equals("Lieberry - TestBook"),
                "Submitting a book should take the user to that book's detailed view.");
        Assert.isTrue(
                sourceContains("TestBook"),
                "Created book should have the provided title.");
        Assert.isTrue(
                sourceContains("TestAuthor"),
                "Created book should have the provided author.");
        Assert.isTrue(
                sourceContains("123456789"),
                "Created book should have the provided ISBN.");
        Assert.isTrue(
                sourceContains("TestPublisher"),
                "Created book should have the provided publisher.");
        Assert.isTrue(
                sourceContains("2012"),
                "Created book should have the provided author.");
        go("/books/author/TestAuthor");
        Assert.isTrue(
                sourceContains("123456789"),
                "Created book should be visible in library browser.");
        
        e = findById("command");
        e.submit();
        
        go("/books/author/TestAuthor");
        
        Assert.isFalse(
                sourceContains("123456789"),
                "Pressing the 'Delete' button on the book listing should delete the book.");
    }
    
    @Test
    public void addBookSubmittingBookWithExistingIsbn(){
        login();
        go("/add");
        input("title", "aaaaaaaaaaaaaaaaa");
        input("author0", "TestAuthor");
        input("isbn", "9780");
        input("publisher", "TestPublisher");
        WebElement e = input("publicationYear", "2012");
        e.submit();
        
        Assert.isTrue(
                sourceContains("The provided ISBN already exists in the database."),
                "User should not be able to add a book with an ISBN that is already in use.");
        
        e = findByLinkText("here");
        e.click();
        Assert.isTrue(
                sourceContains("Edit a book"),
                "Clicking the link 'here' should take the user to the 'Edit a book' page.");
        Assert.isTrue(
                sourceContains("Mein Kompfy Chair"),
                "The 'Edit a book' page should contain the information of the book with the same ISBN as the one entered on the 'Add a book' page.");
        
        go("/books");
        Assert.isFalse(
                sourceContains("aaaaaaaaaaaaaaaaa"),
                "A book should NOT be present in the database after a failed insert.");
    }
    
    @Test
    public void editBookSavesTheEditedInfo(){
        login();
        go("/add");
        input("title", "TestBook");
        input("author0", "TestAuthor");
        input("isbn", "111111");
        input("publisher", "TestPublisher");
        WebElement e = input("publicationYear", "2012");
        e.submit();
        
        go("/edit/9780");
        input("title", "EditTestBook");
        input("author0", "EditTestAuthor");
        input("isbn", "111111");
        input("publisher", "EditTestPublisher");
        e = input("publicationYear", "2012");
        e.submit();
        
        Assert.isTrue(
                driver.getTitle().equals("Lieberry - EditTestBook"),
                "Submitting a book should take the user to that book's detailed view.");
        Assert.isTrue(
                sourceContains("EditTestBook"),
                "Edited book should have the provided title.");
        Assert.isTrue(
                sourceContains("EditTestAuthor"),
                "Edited book should have the provided author.");
        Assert.isTrue(
                sourceContains("111111"),
                "Edited book should have the provided ISBN.");
        Assert.isTrue(
                sourceContains("EditTestPublisher"),
                "Edited book should have the provided publisher.");
        Assert.isTrue(
                sourceContains("2012"),
                "Edited book should have the provided author.");
        go("/books/author/EditTestAuthor");
        Assert.isTrue(
                sourceContains("111111"),
                "Edited book should be visible in library browser.");
    }

        //TODO: Add tests for dynamic adding and removing of author fields.
    
    // Import a book //
    
    @Test
    public void importAuthorizationLimitsUserAccess(){
        go("/import");
        Assert.isFalse(
                sourceContains("Lieberry - Import a book"),
                "Unauthorized users attempting to access the 'Import a book' page should be redirected to the 'Login' page.");
        login();
        Assert.isTrue(
                sourceContains("Lieberry - Import a book"),
                "Authorized users attempting to access the 'Import a book' page should see the requested page.");
    }
    
    @Test
    public void importPageContainsRelevantFields(){
        login();
        go("/import");
        Assert.isTrue(
                sourceContains("name=\"query\""),
                "The import page should contain an element with name 'query'.");
        Assert.isTrue(
                sourceContains("id=\"title\""),
                "The import page should contain an element with id 'title'.");
        Assert.isTrue(
                sourceContains("id=\"author\""),
                "The import page should contain an element with id 'author'.");
        Assert.isTrue(
                sourceContains("id=\"isbn\""),
                "The import page should contain an element with name 'isbn'.");
        Assert.isTrue(
                sourceContains("type=\"submit\""),
                "The import page should contain an element with type 'submit'.");
        
    }
    
    @Test
    public void importRequestingBookWithISBN(){
        login();
        go("/import");
        WebElement e = input("query", "9780980200447");
        e = findById("isbn");
        e.click();
        e.submit();
        
        Assert.isTrue(
                sourceContains("Slow reading"),
                "Importing the ISBN '9780980200447', the resulting page should contain the text 'Slow reading'.");
        Assert.isTrue(
                sourceContains("John Miedema"),
                "Importing the ISBN '9780980200447', the resulting page should contain the text 'John Miedema'.");
        Assert.isTrue(
                sourceContains("9780980200447"),
                "Importing the ISBN '9780980200447', the resulting page should contain the text '9780980200447'.");
        Assert.isTrue(
                sourceContains("Litwin Books"),
                "Importing the ISBN '9780980200447', the resulting page should contain the text 'Litwin Books'.");
        Assert.isTrue(
                sourceContains("2009"),
                "Importing the ISBN '9780980200447', the resulting page should contain the text '2009'.");
    }
    
        //Unable to test non-existant ISBNs, OpenLibrary contains records with nonexistant ISBNs. See f.ex. openlibrary.org/isbn/1
    
    // Register //
    
        //TODO
    
    // Usermanagement //
    
        //TODO
    
    // Admin //
    
        //TODO
    
    // Helper methods //
    
    private void go(String url){
        driver.get(baseUrl+url);
    }
    
    private void goBase(){
        driver.get(baseUrl);
    }
    
    private void login(){
        go("/login");
        WebElement e = findById("j_username");
        e.clear();
        e.sendKeys("mockUserForSeleniumThisIsBad");
        e = findById("j_password");
        e.clear();
        e.sendKeys("h63Eaa6Eq58X92IBccDFmYN9B1i0o7wtCwfOtEimmq2vqLKSzkU6gmpterjNpfLp");
        e.submit();
    }
    
    private WebElement input(String id, String input){
        WebElement e = findById(id);
        e.clear();
        e.sendKeys(input);
        return e;
    }
    
    private Boolean titleContains(String query){
        return driver.getTitle().contains(query);
    }
    
    private Boolean sourceContains(String query){
        return driver.getPageSource().contains(query);
    }
    
    private WebElement findByLinkText(String text){
        return driver.findElement(By.linkText(text));
    }
    
    private WebElement findById(String id){
        return driver.findElement(By.id(id));
    }
    
    private WebElement findByName(String name){
        return driver.findElement(By.name(name));
    }
    
    private void clearDatabase(){
        bookRepository.deleteAll();
    }
    
    private void initDatabase(){
        Book b = new Book();
        List<String> authors = new ArrayList<String>();
        authors.add("Cherryl A Rousel");
        b.setAuthors(authors);
        b.setIsbn("72579");
        b.setPublicationYear(1999);
        b.setTitle("Introduction to circuitry");
        b.setPublisher("Gaudeamus");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Abby Day");
        b.setAuthors(authors);
        b.setIsbn("978472579");
        b.setPublicationYear(2000);
        b.setTitle("A happy day, or a bidé day");
        b.setPublisher("Gaudeamus");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Vaadolf Kitler");
        b.setAuthors(authors);
        b.setIsbn("9780");
        b.setPublicationYear(2000);
        b.setTitle("Mein Kompfy Chair");
        b.setPublisher("Mofola");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Pelle H Ermanni");
        b.setAuthors(authors);
        b.setIsbn("75234212349");
        b.setPublicationYear(2005);
        b.setTitle("Kyllä se on oikeasti tämän värinen!");
        b.setPublisher("Fuuu");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Pelle H Ermanni");
        b.setAuthors(authors);
        b.setIsbn("242134");
        b.setPublicationYear(2005);
        b.setTitle("Parhaat plättyreseptini");
        b.setPublisher("Fuuu");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Michael Proven");
        b.setAuthors(authors);
        b.setIsbn("3452");
        b.setPublicationYear(1998);
        b.setTitle("Dropping emus: my studies on neuroscience");
        b.setPublisher("Pingviini");
        bookService.create(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Vinski");
        authors.add("Moto");
        authors.add("Turbo");
        authors.add("Santtu");
        b.setAuthors(authors);
        b.setIsbn("98678");
        b.setPublicationYear(1995);
        b.setTitle("\"Eiköhän se riitä, että se jyrää tän Leipiksen uuden dildon!\" ja muita sutkautuksia");
        b.setPublisher("Fuuu");
        bookService.create(b);
    }

}