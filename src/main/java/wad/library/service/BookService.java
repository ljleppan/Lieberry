package wad.library.service;

import org.springframework.data.domain.Page;
import wad.library.domain.Book;


/**
 * An interface for saving and retrieving books.
 * @author ljleppan
 */
public interface BookService {
    
    /**
     * Return a single book matching the given ISBN.
     * 
     * @param isbn The ISBN to search for.
     * @return A single book matching the search.
     */
    public Book findByIsbn(String isbn);
    
    /**
     * Return all books where the title contains the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param name The (partial) title
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByTitleLike(String name, int pageNumber, int pageSize);
    
    /**
     * Return all books where the publisher's name contains the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param publisher The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByPublisherLike(String publisher, int pageNumber, int pageSize);
    
    /**
     * Return all books where the ISBN contains the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param isbn The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByIsbnLike(String isbn, int pageNumber, int pageSize);
    
    /**
     * Return all books where the author's name contains the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param author The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByAuthorLike(String author, int pageNumber, int pageSize);
    
    /**
     * Return all books where the title equals to the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param name The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByTitle(String name, int pageNumber, int pageSize);
    
    /**
     * Return all books where the author's name equals to the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param author The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByAuthor(String author, int pageNumber, int pageSize);
    
    /**
     * Return all books where the publisher's name equals to the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param publisher The query string.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByPublisher(String publisher, int pageNumber, int pageSize);
    
    /**
     * Return all books where the publication year equals to the query.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param year The query.
     * @param pageNumber The number of the page that should be returned.
     * @param pageSize The number of results per page.
     * @return A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAllByPublicationYear(int year, int pageNumber, int pageSize);
    
    /**
     * Return all books.
     * 
     * <p>Results are paged and only the specified page is returned. The returned
     * list will be empty if there are not enough results to populate the requested 
     * page.</p>
     * 
     * @param pageNumber    The number of the page that should be returned.
     * @param pageSize      The number of results per page.
     * @return              A {@link org.springframework.data.domain.Page} of the search results.
     */
    public Page<Book> findAll(int pageNumber, int pageSize);
    
    /**
     * Saves a book.
     * 
     * @param book  The updated data.
     * @return      The saved book.
     */
    public Book update(Book book);
    
    /**
     * Creates a new book.
     * 
     * @param book  The book to be created.
     * @return      The created book.
     */
    public Book create(Book book);
    
    /**
     * Deletes an existing book.
     * 
     * @param book  The book to delete.
     */
    public void delete(Book book);
    
    /**
     * Deletes a book based on its ISBN.
     * 
     * @param isbn  The ISBN of the book to delete.
     */
    public void delete(String isbn);
}
