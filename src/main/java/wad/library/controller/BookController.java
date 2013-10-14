package wad.library.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.library.domain.Book;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;

@Controller
public class BookController {
    
    public static final int PAGE_SIZE = 5;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    OpenLibraryService openLibraryService;
    
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
    
    @RequestMapping(value="books", method=RequestMethod.POST)
    public String create(Model model, @Valid Book book, BindingResult bindingResult){
        System.out.println("POST to books");
        if (bindingResult.hasErrors()){
            System.out.println("Invalid form, book NOT added");
            model.addAttribute("book", book);
            return "add";
        }
        System.out.println("Valid form, book added");
        bookService.create(book);
        return "redirect:/app/books/"+book.getIsbn();
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.GET)
    public String getBook(Model model, @PathVariable String isbn){
        System.out.println("GET to books/"+isbn);
        Book book = bookService.findByIsbn(isbn);       
        model.addAttribute("book", book);
        return "book";
    }
    
    @RequestMapping(value="books/{isbn}", method=RequestMethod.PUT)
    public String updateBook(Model model, @PathVariable String isbn, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult){
        System.out.println("PUT to books/"+isbn);
        if (bindingResult.hasErrors()){
            model.addAttribute("book", book);
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
        Book temp = new Book();
        temp.setAuthors(new ArrayList<String>());
        temp.setPublicationYear(new GregorianCalendar().get(Calendar.YEAR));
        model.addAttribute("book", temp);
        return "add";
    }
    
    @RequestMapping(value="import", method=RequestMethod.GET)
    public String importBooksForm(){
        System.out.println("GET to import");
        return "import";
    }
    
    @RequestMapping(value="import", method=RequestMethod.POST)
    public String findBookToImport(Model model, @RequestParam String isbn){
        String newIsbn = isbn.replaceAll("[^0-9]", "");
        if (isbn.toLowerCase().endsWith("x")){
            newIsbn = newIsbn.concat("X");
        }
        Book book = openLibraryService.retrieve(isbn);
        model.addAttribute("book", book);
        return "add";
    }
}