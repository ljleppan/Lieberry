package wad.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wad.library.domain.Book;
import wad.library.domain.OpenLibraryBook;
import wad.library.domain.OpenLibraryListElement;

@Service
public class JsonOpenLibraryService implements OpenLibraryService {
    
    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    
    @PostConstruct
    private void init(){
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        mapper = new ObjectMapper();
    }

    @Override
    public Book retrieve(String isbn) {
        System.out.println("Retrieving "+isbn);
        String url = "http://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=data&format=json";
        String json = restTemplate.getForObject(url, String.class);
        
        /* No matching book in OL */
        if (json.equals("{}")){
            return new Book();
        }
        
        System.out.println("Retrieved json string: "+json);
        try {
            System.out.println("Binding json string to olBook map");
            Map<String, OpenLibraryBook> olBooks = mapper.readValue(json, new TypeReference<Map<String, OpenLibraryBook>>(){});
            System.out.println("Result map: "+olBooks);
            return olBookToBook(olBooks.get("ISBN:"+isbn), isbn);
        } catch (IOException ex) {
            System.out.println("Virhe: "+ex);
            Logger.getLogger(JsonOpenLibraryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Book();
    }    

    private Book olBookToBook(OpenLibraryBook olBook, String isbn) {
        Book book = new Book();
        
        book.setIsbn(isbn);
        
        String title = "";
        if (olBook.getTitle() != null){
            title = olBook.getTitle();
        }
        if (olBook.getSubtitle() != null){
            title = title+olBook.getSubtitle();
        }
        book.setTitle(title);
        
        if (olBook.getAuthors() != null){
            List<String> authors = new ArrayList<String>();
            for (OpenLibraryListElement author : olBook.getAuthors()){
                authors.add(author.getName());
            }
            book.setAuthors(authors);
        }
        
        if (olBook.getPublishers() != null && !olBook.getPublishers().isEmpty()){
            String publisher = olBook.getPublishers().get(0).getName();
            book.setPublisher(publisher);
        }
        
        if (olBook.getPublish_date() != null){
            String pubDate = olBook.getPublish_date();
            pubDate = pubDate.substring(pubDate.length() - 4);
            System.out.println("pubdate = "+pubDate);
            try{
                book.setPublicationYear(Integer.parseInt(pubDate));
            } catch (Exception ex){
                System.out.println("Exception: "+ex);
                book.setPublicationYear(new GregorianCalendar().get(GregorianCalendar.YEAR));
            }
        }

        System.out.println("Transformed given olBook to following book: "+book);
        return book;
    }
}
