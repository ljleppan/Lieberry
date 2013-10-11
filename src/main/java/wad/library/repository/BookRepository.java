package wad.library.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.library.domain.Book;

public interface BookRepository extends JpaRepository<Book, String>{
    public List<Book> findByTitleContaining(String title);
    public List<Book> findByAuthorContaining(String author);
    public List<Book> findByPublisherContaining(String publisher);
    public List<Book> findByPublicationYear(int year);
    public List<Book> findByIsbnContaining(String isbn);
}
