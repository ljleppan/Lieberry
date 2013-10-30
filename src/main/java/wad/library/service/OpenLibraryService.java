package wad.library.service;

import java.util.List;
import wad.library.domain.Book;


public interface OpenLibraryService {
    public Book retrieveByIsbn(String query);
    public List<Book> retrieveByAuthor(String query, int pageNumber, int perPage);
    public List<Book> retrieveByTitle(String query, int pageNumber, int perPage);
}
