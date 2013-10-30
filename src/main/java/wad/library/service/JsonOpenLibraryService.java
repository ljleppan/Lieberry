package wad.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wad.library.domain.Book;
import wad.library.domain.openlibrary.OpenLibraryBook;
import wad.library.domain.openlibrary.OpenLibrarySearchResult;
import wad.library.domain.openlibrary.OpenLibrarySearchResultList;
import wad.library.util.BookConverter;

@Service
public class JsonOpenLibraryService implements OpenLibraryService {
    
    Logger log = LoggerFactory.getLogger(JsonOpenLibraryService.class);
    
    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    
    @PostConstruct
    private void init(){
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        mapper = new ObjectMapper();
    }

    @Override
    public Book retrieveByIsbn(String isbn) {
        if(isbn.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=data&format=json";
        String json = fetchJson(url);
        List<Book> books = retrieveMultipleBooks(url, 1, 1);
        return books.get(0);
    }

    @Override
    public List<Book> retrieveByAuthor(String query, int pageNumber, int perPage) {
        if(query.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/search.json?author="+query;
        return retrieveMultipleBooks(url, pageNumber, perPage);  
    }

    @Override
    public List<Book> retrieveByTitle(String query, int pageNumber, int perPage) {
        if(query.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/search.json?title="+query;
        return retrieveMultipleBooks(url, pageNumber, perPage);        
    }
    
    private List<Book> retrieveMultipleBooks(String url, int pageNumber, int perPage){
        String json = fetchJson(url);
        System.out.println(json);
        
        /* No matching book in OL */
        if (json.equals("{}")){
            return null;
        }
        
        try{
            OpenLibrarySearchResultList results = parseToOpenLibrarySearchResultList(json);
            List<String> editionKeys = getEditionKeysFromResults(results);
            
            int startIndex = perPage * pageNumber - 1;
            int endIndex = startIndex + perPage;
            
            if (startIndex >= editionKeys.size()){
                return null;
            }
            if (endIndex >= editionKeys.size()){
                endIndex = editionKeys.size()-1;
            }
            
            List<String> currPageEditionKeys = editionKeys.subList(startIndex, endIndex);
            
            List<OpenLibraryBook> olBooks = retrieveByEditionKeys(currPageEditionKeys);
            
            List<Book> books = new ArrayList<Book>();
            for( OpenLibraryBook olBook : olBooks){
                Book book = BookConverter.olBookToBook(olBook);
                books.add(book);
            }
            
            return books;
        } 
        catch( IOException ex){
            return null;
        }
    }
    
    private List<OpenLibraryBook> retrieveByEditionKeys(List<String> keys) throws IOException{
        String url = createUrlFromKeyList(keys);
        String json = fetchJson(url.toString());
        Map<String, OpenLibraryBook> map = parseToOpenLibraryBookMap(json);
        
        List<OpenLibraryBook> olBooks = new ArrayList<OpenLibraryBook>();
        for (OpenLibraryBook olBook : map.values()){
            olBooks.add(olBook);
        }
        return olBooks;
    }
    
    private String fetchJson(String url){
        return restTemplate.getForObject(url, String.class);
    }
    
    private List<String> getEditionKeysFromResults(OpenLibrarySearchResultList results){
        List<String> keys = new ArrayList<String>();
        for (OpenLibrarySearchResult result : results.getDocs()){
            for (String key : result.getEdition_key()){
                if (!keys.contains(key)){
                    keys.add(key);
                }
            }
        }
        return keys;
    }
    
    private Map<String, OpenLibraryBook> parseToOpenLibraryBookMap(String json) throws IOException{
        return mapper.readValue(json, new TypeReference<Map<String, OpenLibraryBook>>(){});
    }
    
    private OpenLibrarySearchResultList parseToOpenLibrarySearchResultList(String json) throws IOException{
        return mapper.readValue(json, OpenLibrarySearchResultList.class);
    }

    private String createUrlFromKeyList(List<String> keys) {
        StringBuilder url = new StringBuilder();
        url.append("http://openlibrary.org/api/books?bibkeys=");
        url.append("OLID:");
        url.append(keys.get(0));
        for (int i = 1; i < keys.size(); i++) {
            url.append(",OLID:");
            url.append(keys.get(i));
        }
        url.append("&jscmd=data&format=json");
        return url.toString();
    }
}
