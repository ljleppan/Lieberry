package wad.library.controller;

import org.junit.After;
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
import wad.library.service.BookService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 *
 * @author ljleppan
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@WebAppConfiguration 
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/spring-base.xml" }) 
public class BookControllerTest {
    
    @Mock
    private BookService bookService;
    
    @InjectMocks
    private BookController bookController;
    
    private MockMvc mockMvc;
    
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
        
    }
    
    @After
    public void tearDown() {
    } 
    
    @Test
    public void getBooks() throws Exception{        
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"));
                
    }
    
    @Test
    public void getBooksId() throws Exception{
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book"));
    }
    
    @Test
    public void getAdd() throws Exception{
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }
    
    @Test
    public void getEditId() throws Exception{
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }
    
    @Test
    public void getImport() throws Exception{
        mockMvc.perform(get("/import"))
                .andExpect(status().isOk())
                .andExpect(view().name("import"));
    }
    
}