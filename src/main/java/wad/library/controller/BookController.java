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
        System.out.println("GET to books");
        model.addAttribute("books", bookService.findAll());
        return "books";
    }
    
    @RequestMapping(value="books", method=RequestMethod.POST)
    public String createBook(@Valid @ModelAttribute(value="book") Book book, BindingResult bindingResult){
        System.out.println("POST to books");
        if (bindingResult.hasErrors()){
            return "redirect:/app/books";
        }
        bookService.create(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.GET)
    public String getBook(Model model, @PathVariable String isbn){
        System.out.println("GET to books/"+isbn);
        Book book = bookService.findByIsbn(isbn);
        if (book == null){
            model.addAttribute("isbn", isbn);
            return "redirect:/app/retrieve";
        }
        model.addAttribute("book", book);
        return "books";
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.PUT)
    public String updateBook(@PathVariable String isbn, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        System.out.println("PUT to books/"+isbn);
        if (bindingResult.hasErrors()){
            return "redirect:/app/books";
        }
        bookService.update(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.DELETE)
    public String deleteBook(@PathVariable String isbn){
        System.out.println("DELETE to books/"+isbn);
        bookService.delete(isbn);
        return "redirect:/app/books";
    }
    
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String addBooksForm(Model model){
        System.out.println("GET to add");
        return "add";
    }
}