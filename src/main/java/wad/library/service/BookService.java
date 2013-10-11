package wad.library.service;

import java.util.List;
import wad.library.domain.Book;


public interface BookService {
    public Book findByIsbn(String isbn);
    public List<Book> findAllByTitle(String name);
    public List<Book> findAllByAuthor(String author);
    public List<Book> findAllByPublisher(String publisher);
    public List<Book> findAllByPublicationYear(int year);
    public List<Book> findAllByIsbn(String isbn);
    public List<Book> findAll();
    public Book update(Book book);
    public Book create(Book book);
    public void delete(Book book);
    public void delete(String isbn);
}
