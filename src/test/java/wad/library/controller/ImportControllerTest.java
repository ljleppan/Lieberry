package wad.library.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import wad.library.service.BookService;
import wad.library.service.OpenLibraryService;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import wad.library.domain.Book;

@RunWith(SpringJUnit4ClassRunner.class) 
@WebAppConfiguration 
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/spring-base.xml" }) 
public class ImportControllerTest {
    
    @Mock
    BookService bs;
    
    @Mock
    OpenLibraryService ols;
        
    @InjectMocks
    private ImportController importController;
    
    private MockMvc mockMvc;
    
    private Book book1;
    private Book book2;
    
    private List<Book> bookList;
    
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(importController).setViewResolvers(viewResolver()).build();
        createBooks();
    }
    
    @Test
    public void get_Import() throws Exception{
        mockMvc.perform(get("/import"))
                .andExpect(status().isOk())
                .andExpect(view().name("import"));
    }
    
    @Test
    public void post_Import_Isbn() throws Exception{
        when(ols.retrieveByIsbn(any(String.class))).thenReturn(book1);
        
        mockMvc.perform(post("/import")
                .param("query", "1")
                .param("field", "isbn"))
                .andExpect(view().name("add"));
        
        verify(ols, times(1)).retrieveByIsbn(any(String.class));
        verifyNoMoreInteractions(ols, bs);
    }
    
    @Test
    public void post_Import_Author() throws Exception{
        when(ols.retrieveByAuthor(any(String.class), any(Integer.class))).thenReturn(bookList);
        
        mockMvc.perform(post("/import")
                .param("query", "Author")
                .param("field", "author"))
                .andExpect(view().name("importresults"));
        
        verify(ols, times(1)).retrieveByAuthor(any(String.class), any(Integer.class));
        verifyNoMoreInteractions(ols, bs);
    }
    
    @Test
    public void post_Import_Title() throws Exception{
        when(ols.retrieveByTitle(any(String.class), any(Integer.class))).thenReturn(bookList);
        
        mockMvc.perform(post("/import")
                .param("query", "Title")
                .param("field", "title"))
                .andExpect(view().name("importresults"));
        
        verify(ols, times(1)).retrieveByTitle(any(String.class), any(Integer.class));
        verifyNoMoreInteractions(ols, bs);
    }
    
    @Test
    public void post_Import_Id_Success() throws Exception{
        when(ols.retrieveByOpenLibraryId(any(String.class))).thenReturn(book1);
        when(bs.findByIsbn(any(String.class))).thenReturn(null);
        
        mockMvc.perform(post("/import/OL1W"));
        
        verify(ols, times(1)).retrieveByOpenLibraryId(any(String.class));
        verify(bs, times(1)).findByIsbn(any(String.class));
        verify(bs, times(1)).create(any(Book.class));
        verifyNoMoreInteractions(ols, bs);
    }
    
    @Test
    public void post_Import_Id_IsbnInUse() throws Exception{
        when(ols.retrieveByOpenLibraryId(any(String.class))).thenReturn(book1);
        when(bs.findByIsbn(any(String.class))).thenReturn(book1);
        
        mockMvc.perform(post("/import/OL1W"))
                .andExpect(view().name("add"))
                .andExpect(model().attributeExists("isbnInUse"));
        
        verify(ols, times(1)).retrieveByOpenLibraryId(any(String.class));
        verify(bs, times(1)).findByIsbn(any(String.class));
        verifyNoMoreInteractions(ols, bs);
    }
    
    @Test
    public void post_Import_Id_IsbnIsNull() throws Exception{
        
        when(ols.retrieveByOpenLibraryId("OL1W")).thenReturn(book2);
        when(bs.findByIsbn(any(String.class))).thenReturn(null);
        
        mockMvc.perform(post("/import/OL1W"))
                .andExpect(view().name("add"))
                .andExpect(model().attributeExists("isbnNull"));
        
        verify(ols, times(1)).retrieveByOpenLibraryId(any(String.class));
        verify(bs, times(1)).findByIsbn(any(String.class));
        verifyNoMoreInteractions(ols, bs);
    
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
        book2.setIsbn("");
        book2.setOlId("OL2W");
        book2.setPublicationYear(2002);
        book2.setPublisher("Publisher2");
        book2.setTitle("Book 2");
        authors = new ArrayList<String>();
        authors.add("Author 2");
        book2.setAuthors(authors);
        
        bookList = new ArrayList<Book>();
        bookList.add(book1);
        bookList.add(book2);
    }
}