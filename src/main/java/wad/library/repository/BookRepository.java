package wad.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wad.library.domain.Book;

/**
 *
 * @author Loezi
 */
public interface BookRepository extends JpaRepository<Book, String>{
    /**
     *
     * @param title
     * @param pageable
     * @return
     */
    public Page<Book> findByTitleContaining(String title, Pageable pageable);
    /**
     *
     * @param publisher
     * @param pageable
     * @return
     */
    public Page<Book> findByPublisherContaining(String publisher, Pageable pageable);
    /**
     *
     * @param isbn
     * @param pageable
     * @return
     */
    public Page<Book> findByIsbnContaining(String isbn, Pageable pageable);
    /**
     *
     * @param title
     * @param pageable
     * @return
     */
    public Page<Book> findByTitle(String title, Pageable pageable);
    /**
     *
     * @param publisher
     * @param pageable
     * @return
     */
    public Page<Book> findByPublisher(String publisher, Pageable pageable);
    /**
     *
     * @param year
     * @param pageable
     * @return
     */
    public Page<Book> findByPublicationYear(int year, Pageable pageable);
    
    /**
     *
     * @param author
     * @param pageable
     * @return
     */
    @Query("SELECT DISTINCT b from Book b, IN(b.authors) AS author WHERE author LIKE %?1%")
    public Page<Book> findAuthorsNamesContaining(String author, Pageable pageable);
    
    /**
     *
     * @param author
     * @param pageable
     * @return
     */
    @Query("SELECT DISTINCT b from Book b, IN(b.authors) AS author WHERE author = ?1")
    public Page<Book> findByAuthor(String author, Pageable pageable);
}
