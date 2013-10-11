package wad.library.service;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.library.domain.Book;
import wad.library.repository.BookRepository;


@Service
public class JpaBookService implements BookService {
    
    @Autowired 
    private BookRepository bookRepository;
    
    @PostConstruct
    private void init(){
        Book b = new Book();
        b.setAuthor("Author");
        b.setIsbn("0385472579");
        b.setPublicationYear(1999);
        b.setTitle("Kirja");
        b.setPublisher("Gaudeamus");
        bookRepository.save(b);
        
        b = new Book();
        b.setAuthor("Author2");
        b.setIsbn("9780385472579");
        b.setPublicationYear(2000);
        b.setTitle("Teos");
        b.setPublisher("Mofola");
        bookRepository.save(b);
    }

    @Override
    @Transactional(readOnly=true)
    public Book findByIsbn(String isbn) {
        return bookRepository.findOne(isbn);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Book> findAllByTitle(String name) {
        return bookRepository.findByTitleContaining(name);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Book> findAllByAuthor(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Book> findAllByPublisher(String publisher) {
        return bookRepository.findByPublisherContaining(publisher);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Book> findAllByPublicationYear(int year) {
        return bookRepository.findByPublicationYear(year);
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<Book> findAllByIsbn(String isbn) {
        return bookRepository.findByIsbnContaining(isbn);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly=false)
    public Book update(Book book) {
        delete(book);
        create(book);
        return book;
    }

    @Override
    @Transactional(readOnly=false)
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly=false)
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly=false)
    public void delete(String isbn) {
        bookRepository.delete(isbn);
    }
    
}
