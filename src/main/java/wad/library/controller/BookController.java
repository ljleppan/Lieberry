package wad.library.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.library.domain.Book;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;

@Controller
public class BookController {
    
    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;
    
    @RequestMapping(value="books", method=RequestMethod.GET)
    public String getBooks(Model model){
        model.addAttribute("books", bookService.findAll());
        for (Book b : bookService.findAll()){
            System.out.println(b.getTitle());
        }
        return "books";
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.GET)
    public String getBook(Model model, @PathVariable String isbn){
        Book book = bookService.findByIsbn(isbn);
        if (book == null){
            model.addAttribute("isbn", isbn);
            return "redirect:/app/retrieve";
        }
        model.addAttribute("book", book);
        return "book";
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.POST)
    public String createBook(@PathVariable String isbn, @Valid @ModelAttribute Book book, BindingResult result){
        if (result.hasErrors()){
            return "redirect:/app/books";
        }
        bookService.create(book);
        return "redirect:/app/book/"+book.getIsbn();
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.PUT)
    public String updateBook(@PathVariable String isbn, @Valid @ModelAttribute Book book, BindingResult result){
        if (result.hasErrors()){
            return "redirect:/app/books";
        }
        bookService.update(book);
        return "redirect:/app/book/"+book.getIsbn();
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.DELETE)
    public String createBook(@PathVariable String isbn){
        bookService.delete(isbn);
        return "redirect:/app/books";
    }
}