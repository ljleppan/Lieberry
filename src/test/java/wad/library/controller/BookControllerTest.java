package wad.library.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import wad.library.service.BookService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import wad.library.domain.Book;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class) 
@WebAppConfiguration 
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/spring-base.xml" }) 
public class BookControllerTest {
    
    @Mock
    private BookService bookService;
    
    @InjectMocks
    private BookController bookController;
    
    private MockMvc mockMvc;
    
    private Book book1;
    private Book book2;
    private Book book3;
    
    private List<Book> bookList;
    private Page<Book> bookPage;
    
    private ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }    
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).setViewResolvers(viewResolver()).build();
        createBooks();
    }
    
    @Test
    public void get_Books() throws Exception{     
        when(bookService.findAll(1, 5)).thenReturn(bookPage);
        
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", is(bookList)));   
                
        verify(bookService, times(1)).findAll(1, 5);
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void get_Books_Id() throws Exception{       
        when(bookService.findByIsbn("1")).thenReturn(book1);
        
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", is(book1)));
        
        verify(bookService, times(1)).findByIsbn("1");
        verifyNoMoreInteractions(bookService);
        
    }
    
    @Test
    public void get_Add() throws Exception{
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }
    
    @Test
    public void get_Edit_Id() throws Exception{
        when(bookService.findByIsbn("1")).thenReturn(book1);
        
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attribute("book", is(book1)));
        
        verify(bookService, times(1)).findByIsbn("1");
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void post_Books_InvalidForm() throws Exception{
        when(bookService.findByIsbn(any(String.class))).thenReturn(null);
        
        mockMvc.perform(post("/books")
                .param("title", "")
                .param("authors[0]", "")
                .param("isbn", "")
                .param("publisher", "")
                .param("publicationYear", ""))
                .andExpect(view().name("add"));
        
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void post_Books_IsbnInUse() throws Exception{
        when(bookService.findByIsbn(any(String.class))).thenReturn(book1);
        
        mockMvc.perform(post("/books")
                .param("title", "Title")
                .param("authors[0]", "Author")
                .param("isbn", "1")
                .param("publisher", "Pub")
                .param("publicationYear", "2002"))
                .andExpect(view().name("add"));
        
        verify(bookService, times(1)).findByIsbn("1");
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void post_Books_Success() throws Exception{
        when(bookService.findByIsbn(any(String.class))).thenReturn(null);
        when(bookService.create(any(Book.class))).thenReturn(book1);
        
        mockMvc.perform(post("/books")
                .param("title", "Title")
                .param("authors[0]", "Author")
                .param("isbn", "1")
                .param("publisher", "Pub")
                .param("publicationYear", "2002"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/app/books/1"));
        
        verify(bookService, times(1)).findByIsbn("1");
        verify(bookService, times(1)).create(any(Book.class));
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void post_Book_Id_InvalidInput() throws Exception{        
        mockMvc.perform(post("/books/1")
                .param("title", "")
                .param("authors[0]", "")
                .param("isbn", "")
                .param("publisher", "")
                .param("publicationYear", ""))
                .andExpect(view().name("edit"));
        
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void post_Book_Id_ValidInput() throws Exception{
        when(bookService.update(any(Book.class))).thenReturn(book1);
        
        mockMvc.perform(post("/books/1")
                .param("title", "Title")
                .param("authors[0]", "Author")
                .param("isbn", "1")
                .param("publisher", "Pub")
                .param("publicationYear", "2002"))
                .andExpect(view().name("redirect:/app/books/1"));
        
        verify(bookService, times(1)).update(any(Book.class));
        verifyNoMoreInteractions(bookService);
    }
    
    @Test
    public void delete_Book_Id() throws Exception{
        mockMvc.perform(delete("/books/1)"))
                .andExpect(view().name("redirect:/app/books"));
        
        verify(bookService, times(1)).delete(any(String.class));
        verifyNoMoreInteractions(bookService);
    }
    
    
    private void createBooks(){
        List<String> authors = new ArrayList<String>();
        
        book1 = new Book();
        book1.setIsbn("1");
        book1.setOlId("OL1W");
        book1.setPublicationYear(2001);
        book1.setPublisher("Publisher1");
        book1.setTitle("Book 1");
        authors.add("Author 1");
        book1.setAuthors(authors);
        
        book2 = new Book();
        book2.setIsbn("2");
        book2.setOlId("OL2W");
        book2.setPublicationYear(2002);
        book2.setPublisher("Publisher2");
        book2.setTitle("Book 2");
        authors = new ArrayList<String>();
        authors.add("Author 2");
        book2.setAuthors(authors);
        
        book3 = new Book();
        book3.setIsbn("3");
        book3.setOlId("OL3W");
        book3.setPublicationYear(2003);
        book3.setPublisher("Publisher3");
        book3.setTitle("Book 3");
        authors = new ArrayList<String>();
        authors.add("Author 3a");
        authors.add("Author 3b");
        book3.setAuthors(authors);
        
        bookList = new ArrayList<Book>();
        
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        
        bookPage = new PageImpl(bookList, new PageRequest(1, BookController.PAGE_SIZE), 3);
    }
}