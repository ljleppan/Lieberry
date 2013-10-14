package wad.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wad.library.domain.Book;

@Service
public class JsonOpenLibraryService implements OpenLibraryService {
    
    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    
    @PostConstruct
    private void init(){
        restTemplate = new RestTemplate();
    }

    @Override
    public Book retrieve(String isbn) {
        System.out.println("Retrieving "+isbn);
        String url = "http://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=data&format=json";
        String json = restTemplate.getForObject(url, String.class);
        System.out.println("lol");
        Book book = processToBook(json);
        return book;
    }

    private Book processToBook(String json) {
        String[] a = json.split("[{]");
        for (String s : a){
            System.out.println(s);
        }
        return new Book();
    }
    
}
