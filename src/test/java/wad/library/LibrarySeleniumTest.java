package wad.library;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


public class LibrarySeleniumTest {
    
    private String port;
    private WebDriver driver;
    private String baseUrl;
    
    @Before
    public void setUp() throws Exception {
        driver = new HtmlUnitDriver();
        port = System.getProperty("jetty.port", "8080");
        baseUrl = "http://localhost:" + port+"/wepa/";
    }
    
    @Test
    public void rootLoadsMenu(){
        driver.get(baseUrl);
        Assert.isTrue(
                sourceContains("Browse library"),
                "Root should serve a page with a 'Browse library' button."
                );
    }
    
    /* Login and Logout buttons */
    
    @Test
    public void loginButtonWhenUserNotLoggedIn(){
        driver.get(baseUrl);
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
        driver.get(baseUrl);
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
        driver.get(baseUrl);
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
    
    /* Menu buttons */
    
    @Test
    public void onlyBrowseAndRegisterForUnauthorizedUsers(){
        driver.get(baseUrl);
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
    
    /* Login page */
    
    @Test
    public void loginPageContainsUsernameAndPasswordFields(){
        driver.get(baseUrl+"/app/login");
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
        driver.get(baseUrl+"/app/login");
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
        driver.get(baseUrl+"/app/login");
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
                sourceContains("Bad credentials"),
                "Login page should contain an error after a failed login.");
    }
    
    /* Browse library */
    
    @Test
    public void browseLibraryShownWithoutLoggingIn(){
        driver.get(baseUrl+"/app/books");
        Assert.isTrue(
                sourceContains("books matching the query"),
                "The 'Browse books' page should be viewable without logging in.");
    }
    
    @Test
    public void browseLibraryViewButtonShownForEveryone(){
        driver.get(baseUrl+"/app/books");
        Assert.isTrue(
                sourceContains("View"),
                "The 'View' button should be visible without logging in.");
    }
    
    @Test
    public void browseLibraryEditAndDeleteNotShownForEveryone(){
        driver.get(baseUrl+"/app/books");
        Assert.isFalse(
                sourceContains("Edit"),
                "The 'Edit' button should NOT be visible without logging in.");
        Assert.isFalse(
                sourceContains("Delete"),
                "The 'Delete' button should NOT be visible without logging in.");
    }
    
    @Test
    public void browseLibraryEditAndDeleteShownWhenLoggedIn(){
        login();
        driver.get(baseUrl+"/app/books");
        Assert.isTrue(
                sourceContains("Edit"),
                "The 'Edit' button should be visible after logging in.");
        Assert.isTrue(
                sourceContains("Delete"),
                "The 'Delete' button should be visible after logging in.");
    }
    
    @Test
    public void browseLibraryCorrectPageInfoOnFirstPage(){
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books?pageNumber=2");
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
        driver.get(baseUrl+"/app/books");
        WebElement e = findByLinkText("Pelle H Ermanni");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/author/Pelle%20H%20Ermanni"),
                "Clicking on an author's name should redirect the user to an url of the form '/books/author/<author>");
    }
    
    @Test
    public void browseLibraryClickingOnPublisherRedirectsCorreclty(){
        driver.get(baseUrl+"/app/books");
        WebElement e = findByLinkText("Gaudeamus");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/publisher/Gaudeamus"),
                "Clicking on a publisher's name should redirect the user to an url of the form '/books/publisher/<publisher>");
    }
    
    @Test
    public void browseLibraryClickingOnPublicationYearRedirectsCorreclty(){
        driver.get(baseUrl+"/app/books");
        WebElement e = findByLinkText("2000");
        e.click();
        Assert.isTrue(
                driver.getCurrentUrl().endsWith("books/publicationyear/2000"),
                "Clicking on a publication year should redirect the user to an url of the form 'books/publicationyear/<publicationyear>");
    }
    
    /* Search */
    
    @Test
    public void searchTitleSearchWorks(){
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
        driver.get(baseUrl+"/app/books");
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
    
    /* Filter URLs */
    
    //TODO;
    
    /* Book details */
    
    //TODO
    
    /* Add a book */
    
    //TODO
    
    /* Edit a book */
    
    //TODO
    
    /* Import a book */
    
    //TODO
    
    /* Register */
    
    //TODO
    
    /* Usermanagement */
    
    //TODO
    
    /* Admin */
    
    //TODO
    
    /* Helper methods */
    
    private void login(){
        driver.get(baseUrl+"/app/login");
        WebElement e = findById("j_username");
        e.clear();
        e.sendKeys("mockUserForSeleniumThisIsBad");
        e = findById("j_password");
        e.clear();
        e.sendKeys("h63Eaa6Eq58X92IBccDFmYN9B1i0o7wtCwfOtEimmq2vqLKSzkU6gmpterjNpfLp");
        e.submit();
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

}