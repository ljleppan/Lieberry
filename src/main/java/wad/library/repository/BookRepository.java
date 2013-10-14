package wad.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wad.library.domain.Book;

public interface BookRepository extends JpaRepository<Book, String>{
    public Page<Book> findByTitleContaining(String title, Pageable pageable);
    public Page<Book> findByPublisherContaining(String publisher, Pageable pageable);
    public Page<Book> findByIsbnContaining(String isbn, Pageable pageable);
    public Page<Book> findByTitle(String title, Pageable pageable);
    public Page<Book> findByPublisher(String publisher, Pageable pageable);
    public Page<Book> findByPublicationYear(int year, Pageable pageable);
    
    @Query("SELECT DISTINCT b from Book b, IN(b.authors) AS author WHERE author LIKE %?1%")
    public Page<Book> findAuthorsNamesContaining(String author, Pageable pageable);
    
    @Query("SELECT DISTINCT b from Book b, IN(b.authors) AS author WHERE author = ?1")
    public Page<Book> findByAuthor(String author, Pageable pageable);
}
