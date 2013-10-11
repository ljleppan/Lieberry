package wad.library.service;

import wad.library.domain.Book;


public interface OpenLibraryService {
    public Book retrieve(String isbn);
}
