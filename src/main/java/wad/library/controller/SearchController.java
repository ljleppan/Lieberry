package wad.library.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;
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

@Controller
public class SearchController {

    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String search(
            Model model,
            @RequestParam String field,
            @RequestParam String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        Page<Book> results;
        if ("isbn".equals(field)) {
            results = bookService.findAllByIsbnLike(query, pageNumber, BookController.PAGE_SIZE);
        } else if ("author".equals(field)) {
            results = bookService.findAllByAuthorLike(query, pageNumber, BookController.PAGE_SIZE);
        } else if ("publisher".equals(field)) {
            results = bookService.findAllByPublisherLike(query, pageNumber, BookController.PAGE_SIZE);
        } else if ("publicationYear".equals(field)) {
            int year;
            try {
                year = Integer.parseInt(query);
            } catch (Exception e) {
                year = new GregorianCalendar().get(Calendar.YEAR);
            }
            results = bookService.findAllByPublicationYear(year, pageNumber, BookController.PAGE_SIZE);
        } else {
            results = bookService.findAllByTitleLike(query, pageNumber, BookController.PAGE_SIZE);
        }

        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    @RequestMapping(value = "books/title/{query}", method=RequestMethod.GET)
    public String findByTitle(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        Page<Book> results = bookService.findAllByTitle(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
    @RequestMapping(value = "books/author/{query}", method=RequestMethod.GET)
    public String findByAuthor(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        Page<Book> results = bookService.findAllByAuthor(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());

        return "books";
    }
    
    @RequestMapping(value = "books/publisher/{query}", method=RequestMethod.GET)
    public String findByPublisher(
            Model model,
            @PathVariable String query,
            @RequestParam(required=false, defaultValue="1") int pageNumber)
    {
        Page<Book> results = bookService.findAllByPublisher(query, pageNumber, BookController.PAGE_SIZE);
        model.addAttribute("books", results.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", results.getTotalPages());
        model.addAttribute("totalItems", results.getTotalElements());
        return "books";
    }
    
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


}
