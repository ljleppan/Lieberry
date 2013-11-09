package wad.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wad.library.domain.Book;
import wad.library.domain.openlibrary.OpenLibraryBook;
import wad.library.domain.openlibrary.OpenLibrarySearchResult;
import wad.library.domain.openlibrary.OpenLibrarySearchResultList;
import wad.library.util.BookConverter;

/**
 * An impelementation of the {@link OpenLibraryService} that uses the OpenLibrary JSON API.
 * @author ljleppan
 */
@Service
public class JsonOpenLibraryService implements OpenLibraryService {
    
    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    
    /**
     * Initializes a JSON mapper for internal use.
     */
    @PostConstruct
    private void init(){
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        mapper = new ObjectMapper();
    }

    /**
     * Retrieves a single book based on the ISBN.
     * @param isbn  ISBN.
     * @return      The first matching result.
     */
    @Override
    public Book retrieveByIsbn(String isbn) {
        if(isbn.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=data&format=json";
        String json = fetchJson(url);
        return retrieveSingleBook(url);

    }

    /**
     * Retrieves a {@link java.util.List} of books whose author's name matches at least partially with the query.
     * 
     * @param query  The query string.
     * @param number The number of results.
     * @return       A {@link java.util.List} of books matching the requested page of the results.
     */
    @Override
    public List<Book> retrieveByAuthor(String query, int number) {
        if(query.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/search.json?author="+query;
        return retrieveMultipleBooks(url, number);  
    }

    /**
     * Retrieves a {@link java.util.List} of books whose title matches at least partially with the query.
     * 
     * 
     * @param query     The query string.
     * @param number    The number of results.
     * @return          A {@link java.util.List} of books matching the requested page of the results.
     */
    @Override
    public List<Book> retrieveByTitle(String query, int number) {
        if(query.equals("")){
            return null;
        }
        String url = "http://openlibrary.org/search.json?title="+query;
        return retrieveMultipleBooks(url, number);        
    }
    
    /**
     * Queries the OpenLibrary API for a JSON string of multiple books.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param url           The API url to query.
     * @param limit     The maximum number of results to return.
     * @return              A page (list) of books matching the query.
     */
    private List<Book> retrieveMultipleBooks(String url, int limit){
        String json = fetchJson(url);
        System.out.println(json);
        
        if (limit < 0){
            limit = 0;
        }
        
        /* No matching book in OL */
        if (json.equals("{}")){
            return null;
        }
        
        try{
            OpenLibrarySearchResultList results = parseToOpenLibrarySearchResultList(json);
            results.setDocs(trimResults(results, limit));
            List<String> editionKeys = getEditionKeysFromResults(results, limit);
            
            List<OpenLibraryBook> olBooks = retrieveByEditionKeys(editionKeys);
            
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
    
    /**
     * Retrieves a list of OpenLibraryBooks based on the provided list of OpenLibrary keys.
     * @param keys          List of keys.
     * @return              List of OpenLibraryBook
     * @throws IOException  IOException from {@link #parseToOpenLibraryBookMap(java.lang.String)}
     */
    private List<OpenLibraryBook> retrieveByEditionKeys(List<String> keys) throws IOException{
        String url = createUrlFromKeyList(keys);
        String json = fetchJson(url.toString());
        Map<String, OpenLibraryBook> map = parseToOpenLibraryBookMap(json);
        
        List<OpenLibraryBook> olBooks = new ArrayList<OpenLibraryBook>();
        for (OpenLibraryBook olBook : map.values()){
            olBooks.add(olBook);
            System.out.println(olBook);
        }
        return olBooks;
    }
    
    /**
     * Fetches a JSON string using the {@link #restTemplate}.
     * @param url   Url query.
     * @return      The JSON string returned by the API.
     */
    private String fetchJson(String url){
        return restTemplate.getForObject(url, String.class);
    }
    
    /**
     * Retrieves a list of unique Open Library edition keys from a {@link OpenLibrarySearchResultList}
     * @param results   A {@link OpenLibrarySearchResultList}
     * @param limit     The maximum number of results to return.
     * @return          All unique edition keys found from the results.
     */
    private List<String> getEditionKeysFromResults(OpenLibrarySearchResultList results, int limit){
        int amount = 0;
        Set<String> keys = new TreeSet<String>();
        
        List<OpenLibrarySearchResult> docs = results.getDocs(); 
        for (OpenLibrarySearchResult result : docs){
            if (result != null 
                    && amount < limit 
                    && result.getEdition_key() != null){
                
                for (String key : result.getEdition_key()){
                    if (key != null 
                            && amount < limit 
                            && !keys.contains(key)){
                        keys.add(key);
                        amount++;
                    }
                }
            }
        }
        List<String> keysList = new ArrayList<String>();
        keysList.addAll(keys);
        return keysList;
    }
    
    /**
     * Parses a JSON string to a map containing {@link OpenLibraryBook}s.
     * @param json          The JSON string to parse.
     * @return              A map of {@link OpenLibraryBook}s.
     * @throws IOException  Possible IOException from the mapping.
     */
    private Map<String, OpenLibraryBook> parseToOpenLibraryBookMap(String json) throws IOException{
        return mapper.readValue(json, new TypeReference<Map<String, OpenLibraryBook>>(){});
    }
    
    /**
     * Parses a JSON string to an instance of {@link OpenLibrarySearchResultList}.
     * @param json          The JSON string to parse.
     * @return              An instance of {@link OpenLibrarySearchResultList}.
     * @throws IOException  Possible IOException from the mapping.
     */
    private OpenLibrarySearchResultList parseToOpenLibrarySearchResultList(String json) throws IOException{
        return mapper.readValue(json, OpenLibrarySearchResultList.class);
    }

    /**
     * Builds an url for fetching the details of multiple books, based on a given list of Open Library edition keys.
     * @param keys  A list of keys.
     * @return      An URL for fetching the listed books' details.
     */
    private String createUrlFromKeyList(List<String> keys) {
        StringBuilder url = new StringBuilder();
        url.append("http://openlibrary.org/api/books?bibkeys=");
        url.append("OLID:");
        if (keys.size() > 0){
            url.append(keys.get(0));
            for (int i = 1; i < keys.size(); i++) {
                url.append(",OLID:");
                url.append(keys.get(i));
            }
        }
        url.append("&jscmd=data&format=json");
        return url.toString();
    }

    /**
     * Trims the results to a maximum of "number" results.
     * @param results   Original results.
     * @param number    Maximum results to return.
     * @return          Trimmed results.
     */
    private List<OpenLibrarySearchResult> trimResults(OpenLibrarySearchResultList results, int number) {
        List<OpenLibrarySearchResult> docs = results.getDocs();
        if (docs.size() > number + 1){
            docs = docs.subList(0, number + 1);
        }
        return docs;
    }

    /**
     * Retrieves a book from the Open Library
     * @param id    The Open Library ID of the book that should be retrieved
     * @return      The book or null if no match.
     */
    @Override
    public Book retrieveByOpenLibraryId(String id) {
        if(id.equals("")){
            return null;
        }
        
        StringBuilder url = new StringBuilder();
        url.append("http://openlibrary.org/api/books?bibkeys=OLID:");
        url.append(id);
        url.append("&jscmd=data&format=json");
        
        return retrieveSingleBook(url.toString());
    }
    
    private Book retrieveSingleBook(String url){
        try {
            String json = fetchJson(url);
            Map<String, OpenLibraryBook> results = parseToOpenLibraryBookMap(json);
            OpenLibraryBook result = (OpenLibraryBook) results.values().toArray()[0];
            return BookConverter.olBookToBook(result);
            
        } catch (Exception ex) {
            return null;
        }
    }
}
