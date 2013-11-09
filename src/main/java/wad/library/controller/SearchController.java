package wad.library.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.library.domain.Book;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;

/**
 * Controller for search related requests.
 * @author ljleppan
 */
@Controller
public class SearchController {

    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;

    /**
     * DO NOT CALL DIRECTLY
     * 
     * Searches the database and return a view of the results.
     * 
     * <p>When searching for a year, the results only contain an exact match.
     * For other kinds of queries, results contain all books for which the queried
     * field contains the queried text. Results are paged and only the requested page is
     * shown.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param field The field to be queried.
     * @param query The query string.
     * @return "books", populated by the requested page of the search results.
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String search(
            Model model,
            @RequestParam String field,
            @RequestParam String query)
    {
        if (query == null || query.isEmpty()){
            return "redirect:/app/books";
        }
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Do nuthing :'(
        }
        
        if ("publicationYear".equals(field)){
            return "redirect:/app/books/publicationyear/"+query;
        } else {
            return "redirect:/app/books/search/"+field+"/"+query;
        }
    }
    
        /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the exact title of the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/title/{query}", method=RequestMethod.GET)
    public String findByTitle(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        if (query == null || query.isEmpty()){
            query = "";
        }
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }

        Page<Book> results = bookService.findAllByTitle(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the exact author of the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/author/{query}", method=RequestMethod.GET)
    public String findByAuthor(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        if (query == null || query.isEmpty()){
            query = "";
        }
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        
        Page<Book> results = bookService.findAllByAuthor(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());

        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the exact publisher of the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/publisher/{query}", method=RequestMethod.GET)
    public String findByPublisher(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        if (query == null || query.isEmpty()){
            query = "";
        }
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        
        Page<Book> results = bookService.findAllByPublisher(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the exact publication year of the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/publicationyear/{query}", method=RequestMethod.GET)
    public String findByPublicationYear(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        Page<Book> results;
        try{
            results = bookService.findAllByPublicationYear(Integer.parseInt(query), pageNumber, BookController.PAGE_SIZE);
        } catch (Exception e){
            results = null;
        }
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
  
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the title partially matching the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/search/title/{query}", method=RequestMethod.GET)
    public String findByTitleLike(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {        
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        Page<Book> results = bookService.findAllByTitleLike(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the author partially matching the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/search/author/{query}", method=RequestMethod.GET)
    public String findByAuthorLike(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        Page<Book> results = bookService.findAllByAuthorLike(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());

        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the publisher partially matching the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/search/publisher/{query}", method=RequestMethod.GET)
    public String findByPublisherLike(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        Page<Book> results = bookService.findAllByPublisherLike(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    /**
     * DO NOT CALL DIRECTLY
     *
     * Displays all books with the isbn partially matching the query.
     * 
     * <p>Results are paged.</p>
     * 
     * <p>Security: Anonymous.</p>
     * 
     * @param model Instance of Model for given HTTP request and related response.
     * @param query The query string.
     * @param pageNumber The page of the results to display. If no page is specified, the first page is displayed.
     * @return "books", populated by the search results.
     */
    @RequestMapping(value = "books/search/isbn/{query}", method=RequestMethod.GET)
    public String findIsbnLike(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //Weeeeee
        }
        Page<Book> results = bookService.findAllByIsbnLike(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
}
