package wad.library.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String search(){
        return "search";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public String doSearch(Model model, @RequestParam String field, @RequestParam String query) {
        List<Book> results;
        if ("isbn".equals(field)) {
            results = bookService.findAllByIsbn(query);
        } else if ("author".equals(field)) {
            results = bookService.findAllByAuthor(query);
        } else if ("publisher".equals(field)) {
            results = bookService.findAllByPublisher(query);
        } else if ("publicationYear".equals(field)) {
            try {
                results = bookService.findAllByPublicationYear(Integer.parseInt(query));
            } catch (Exception e) {
                results = new ArrayList<Book>();
            }
        } else {
            results = bookService.findAllByTitle(query);
        }

        model.addAttribute("book", results);
        return "redirect:/app/search";
    }
}
