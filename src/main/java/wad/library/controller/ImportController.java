package wad.library.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static wad.library.controller.BookController.IMPORT_RESULT_LIMIT;
import wad.library.domain.Book;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;
import wad.library.util.IsbnConverter;

/**
 * Controller for book import related requests.
 * @author ljleppan
 */
public class ImportController {
    
    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;
    
    
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
     * DO NOT CALL DIRECTLY
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
     * @return      A view with the results.
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
    
    /**
     * DO NOT CALL DIRECTLY
     * 
     * Imports a book from Open Library.
     * 
     * <p>Queries the Autowired {@link wad.library.service.OpenLibraryService} for
     * a books matching the search and saves the resulting book. Returns a view of the
     * found book.</p>
     * 
     * <p>Security: Logged in.</p>
     * @param model Instance of Model for given HTTP request and related response.
     * @param id    Open Library ID of the requested book.
     * @return      A view showing the saved book's details.
     */
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
