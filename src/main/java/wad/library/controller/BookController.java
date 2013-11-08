package wad.library.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.library.domain.Book;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;
import wad.library.util.IsbnConverter;

/**
 * Controller for to non-search book-related requests.
 * 
 * @author Loezi
 */
@Controller
public class BookController {
    
    /**
     * Items shown per page on books.jsp.
     */
    public static final int PAGE_SIZE = 5;
    
    /**
     * Maximum number of import query results.
     */
    public static final int IMPORT_RESULT_LIMIT = 5;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Displays a view of all saved books.
     * 
     * <p>Return the view "books", containing the requested page of all books in the
     * database. If no pageNumber is specified, return the first {@link #PAGE_SIZE}
     * books.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param pageNumber The requested page of a multipage dataset.
     * @return "books".
     */
    @RequestMapping(value="books", method=RequestMethod.GET)
    public String getBooks(
            Model model,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber){
        System.out.println("GET to books");
        
        Page<Book> page = bookService.findAll(pageNumber, PAGE_SIZE);
        model.addAttribute("books", page.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        
        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Creates a new book based on probided data.
     * 
     * <p>The provided info is validated against {@link wad.library.domain.Book}.
     * In case of invalid input, the user is returned to the "add" view, populated
     * by the input data. If the input is valid, the book is saved and the user is
     * shown the details of the created book.</p>
     * 
     * <p>Security: Logged in.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param book The book to be validated and saved.
     * @param bindingResult Results of input validation.
     * @return Details for the saved book or "add" in case of invalid input.
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="books", method=RequestMethod.POST)
    public String create(Model model, @Valid Book book, BindingResult bindingResult){
        System.out.println("POST to books");   
        if (bindingResult.hasErrors()){
            System.out.println("Invalid form, book NOT added");
            model.addAttribute("book", book);
            return "add";
        }
        if (bookService.findByIsbn(book.getIsbn()) != null){
            System.out.println("ISBN exists, book NOT added");
            model.addAttribute("book", book);
            model.addAttribute("isbnInUse", true);
            return "add";
        }
        System.out.println("Valid form, book added");
        bookService.create(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Displays the details of a given book.
     * 
     * </p>Returns a view with the detailed information of a given book. For 
     * non-existing books, the returned view is empty.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param isbn The ISBN that identifies the requested book.
     * @return "book".
     */
    @RequestMapping(value="books/{isbn}", method=RequestMethod.GET)
    public String getBook(Model model, @PathVariable String isbn){
        System.out.println("GET to books/"+isbn);
        Book book = bookService.findByIsbn(isbn);       
        model.addAttribute("book", book);
        return "book";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Updates a book with given validated data. 
     * 
     * <p>If the input is invalid, the user is shown the view "edit" populated
     * with the invalid input. If the input is valid, the input data overwrites
     * previous data for the specified ISBN.</p>
     * 
     * <p>Security: Logged in.</p>
     *
     * @param model Instance of Model for given HTTP request and related response.
     * @param isbn ISBN of the book to be updated.
     * @param book The updated data.
     * @param bindingResult Results of data validation.
     * @return Edited book's details, or "edit" if invalid input.
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="books/{isbn}", method=RequestMethod.POST)
    public String updateBook(Model model, @PathVariable String isbn, @Valid Book book, BindingResult bindingResult){
        System.out.println("POST to books/"+isbn);
        if (bindingResult.hasErrors()){
            System.out.println("Invalid form, book NOT updated");
            model.addAttribute("book", book);
            return "edit";
        }
        System.out.println("Valid form, book updated");
        bookService.update(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Delete the book specified by given ISBN.
     * 
     * <p>After deletion, the user is redirected to the "books" views.</p>
     * 
     * <p>Security: Logged in.</p>
     * 
     * @param isbn The ISBN of the book that should be deleted.
     * @return Redirect to "books".
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="books/{isbn}", method=RequestMethod.DELETE)
    public String deleteBook(@PathVariable String isbn){
        System.out.println("DELETE to books/"+isbn);
        bookService.delete(isbn);
        return "redirect:/app/books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Displays a form for adding books.
     * 
     * <p>The "Publication year" field is prepopulated with the current year.</p>
     * 
     * </p>Security: Logged in.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @return "add".
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String addBooksForm(Model model){
        System.out.println("GET to add");
        Book temp = new Book();
        temp.setAuthors(new ArrayList<String>());
        temp.setPublicationYear(new GregorianCalendar().get(Calendar.YEAR));
        model.addAttribute("book", temp);
        return "add";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Displays a form for editing an existing book.
     * 
     * <p>The form is prepopulated by the data belonging to the book with the requested ISBN.
     * If no such book exists, the fields will be empty.</p>
     * 
     * <p>Security: Logged in.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param isbn ISBN used for prepopulating the form.
     * @return "edit".
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="edit/{isbn}", method=RequestMethod.GET)
    public String editBookForm(Model model, @PathVariable String isbn){
        System.out.println("GET to edit");
        Book book = bookService.findByIsbn(isbn);
        model.addAttribute("book", book);
        return "edit";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Shows the form for importing a book.
     * 
     * <p>Security: Logged in.</p>
     * 
     * @return "import".
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="import", method=RequestMethod.GET)
    public String importBooksForm(){
        System.out.println("GET to import");
        return "import";
    }
    
    /**
     * 
     * Queries OpenLibrary for books and returns of view of the results for import.
     * 
     * <p>Queries the Autowired {@link wad.library.service.OpenLibraryService} for
     * a list of books matching the search and return a view containing the results.
     * If the user queries for an ISBN, returns an "add" view populated by the 
     * first matching result of the query.</p>
     * 
     * <p>Security: Logged in.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param field The field to be queried. Can be "isbn", "author" or "title".
     * @return A view with 
     */
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="import", method=RequestMethod.POST)
    public String findBooksToImport(Model model, @RequestParam String query, @RequestParam String field){
        System.out.println("POST for import");
        List<Book> books;
        if (field.equals("isbn")){
            String isbn = IsbnConverter.stringToIsbn(query);
            Book book = openLibraryService.retrieveByIsbn(isbn);
            model.addAttribute("book", book);
            return "add";
        }
        else if (field.equals("author")){
            books = openLibraryService.retrieveByAuthor(query, IMPORT_RESULT_LIMIT);
        }
        else{
            books = openLibraryService.retrieveByTitle(query, IMPORT_RESULT_LIMIT);
        }
        model.addAttribute("maxResults", IMPORT_RESULT_LIMIT);
        model.addAttribute("books", books);
        return "importresults";
    }
    
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @RequestMapping(value="import/{id}", method=RequestMethod.POST)
    public String importBook(Model model, @PathVariable String id){
        Book book = openLibraryService.retrieveByOpenLibraryId(id);
        if (bookService.findByIsbn(book.getIsbn()) != null){
            model.addAttribute("isbnInUse", true);
            model.addAttribute("book", book);
            return "add";
        }
        if (book.getIsbn() == null){
            model.addAttribute("isbnNull", true);
            model.addAttribute("book", book);
            return "add";
        }
        bookService.create(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
}