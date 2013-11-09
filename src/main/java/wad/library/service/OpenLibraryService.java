package wad.library.service;

import java.util.List;
import wad.library.domain.Book;


/**
 * An Interface for retrieving book data from the Open Library
 * @author ljleppan
 */
public interface OpenLibraryService {
    
    
    /**
     * Retrieves a single book based on the Open Library ID.
     * @param id The ID.
     * @return      The first matching result.
     */
    public Book retrieveByOpenLibraryId(String id);
    
    /**
     * Retrieves a single book based on the ISBN.
     * @param query The query string.
     * @return      The first matching result.
     */
    public Book retrieveByIsbn(String query);
    
    /**
     * Retrieves a {@link java.util.List} of "number" books whose author's name matches at least partially with the query.
     * 
     * <p></p>
     * 
     * @param query         The query string.
     * @param number        The number of the results that should be returned.
     * @return              A {@link java.util.List} of books matching the requested page of the results.
     */
    public List<Book> retrieveByAuthor(String query, int number);

    /**
     * Retrieves a {@link java.util.List} of "number" books whose title matches at least partially with the query.
     * 
     * @param query         The query string.
     * @param number        The number of the results that should be returned.
     * @return              A {@link java.util.List} of books matching the requested page of the results.
     */
    public List<Book> retrieveByTitle(String query, int number);
}
