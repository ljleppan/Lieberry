package wad.library.service;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wad.library.domain.Book;
import wad.library.repository.BookRepository;


/**
 * A JPA-based impelementation of the {@link BookService} interface.
 * @author Loezi
 */
@Service
public class JpaBookService implements BookService {
    
    @Autowired 
    private BookRepository bookRepository;
    
    /**
     * A method that populates the database with example books for testing and demoing.
     */
    @PostConstruct
    private void init(){
        Book b = new Book();
        List<String> authors = new ArrayList<String>();
        authors.add("Cherryl A Rousel");
        b.setAuthors(authors);
        b.setIsbn("72579");
        b.setPublicationYear(1999);
        b.setTitle("Introduction to circuitry");
        b.setPublisher("Gaudeamus");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Abby Day");
        b.setAuthors(authors);
        b.setIsbn("978472579");
        b.setPublicationYear(2000);
        b.setTitle("A happy day, or a bidé day");
        b.setPublisher("Gaudeamus");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Vaadolf Kitler");
        b.setAuthors(authors);
        b.setIsbn("9780");
        b.setPublicationYear(2000);
        b.setTitle("Mein Kompfy Chair");
        b.setPublisher("Mofola");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Pelle H Ermanni");
        b.setAuthors(authors);
        b.setIsbn("75234212349");
        b.setPublicationYear(2005);
        b.setTitle("Kyllä se on oikeasti tämän värinen!");
        b.setPublisher("Fuuu");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Pelle H Ermanni");
        b.setAuthors(authors);
        b.setIsbn("242134");
        b.setPublicationYear(2005);
        b.setTitle("Parhaat plättyreseptini");
        b.setPublisher("Fuuu");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Michael Proven");
        b.setAuthors(authors);
        b.setIsbn("3452");
        b.setPublicationYear(1998);
        b.setTitle("Dropping emus: my studies on neuroscience");
        b.setPublisher("Pingviini");
        bookRepository.save(b);
        
        b = new Book();
        authors = new ArrayList<String>();
        authors.add("Vinski");
        authors.add("Moto");
        authors.add("Turbo");
        authors.add("Santtu");
        b.setAuthors(authors);
        b.setIsbn("98678");
        b.setPublicationYear(1995);
        b.setTitle("\"Eiköhän se riitä, että se jyrää tän Leipiksen uuden dildon!\" ja muita sutkautuksia");
        b.setPublisher("Fuuu");
        bookRepository.save(b);
    }


    /**
     * Return a single book matching the given ISBN.
     * 
     * @param isbn The ISBN to search for.
     * @return A single book matching the search.
     */
    @Override
    @Transactional(readOnly=true)
    public Book findByIsbn(String isbn) {
        return bookRepository.findOne(isbn);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByTitleLike(String name, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByTitleContaining(name, pageRequest);
    }
    
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
    @Override
    public Page<Book> findAllByAuthorLike(String author, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findAuthorsNamesContaining(author, pageRequest);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByPublisherLike(String publisher, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByPublisherContaining(publisher, pageRequest);
    }
    
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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByIsbnLike(String isbn, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByIsbnContaining(isbn, pageRequest);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return (Page<Book>) bookRepository.findAll(pageRequest);
    }

    /**
     * Saves a book.
     * 
     * @param book  The updated data.
     * @return      The saved book.
     */
    @Override
    @Transactional(readOnly=false)
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Creates a new book.
     * 
     * @param book  The book to be created.
     * @return      The created book.
     */
    @Override
    @Transactional(readOnly=false)
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Deletes an existing book.
     * 
     * @param book  The book to delete.
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    /**
     * Deletes a book based on its ISBN.
     * 
     * @param isbn  The ISBN of the book to delete.
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(String isbn) {
        bookRepository.delete(isbn);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByTitle(String name, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByTitle(name, pageRequest);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByAuthor(String author, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByAuthor(author, pageRequest);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByPublisher(String publisher, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByPublisher(publisher, pageRequest);
    }

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
    @Override
    @Transactional(readOnly=true)
    public Page<Book> findAllByPublicationYear(int year, int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "title");
        return bookRepository.findByPublicationYear(year, pageRequest);
    }
}
