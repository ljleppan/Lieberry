package wad.library.service;

import org.springframework.data.domain.Page;
import wad.library.domain.Book;


public interface BookService {
    public Book findByIsbn(String isbn);
    public Page<Book> findAllByTitleLike(String name, int pageNumber, int pageSize);
    public Page<Book> findAllByPublisherLike(String publisher, int pageNumber, int pageSize);
    public Page<Book> findAllByIsbnLike(String isbn, int pageNumber, int pageSize);
    public Page<Book> findAllByAuthorLike(String author, int pageNumber, int pageSize);
    public Page<Book> findAllByTitle(String name, int pageNumber, int pageSize);
    public Page<Book> findAllByAuthor(String author, int pageNumber, int pageSize);
    public Page<Book> findAllByPublisher(String publisher, int pageNumber, int pageSize);
    public Page<Book> findAllByPublicationYear(int year, int pageNumber, int pageSize);
    public Page<Book> findAll(int pageNumber, int pageSize);
    public Book update(Book book);
    public Book create(Book book);
    public void delete(Book book);
    public void delete(String isbn);
}
