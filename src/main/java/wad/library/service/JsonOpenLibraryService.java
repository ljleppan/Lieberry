package wad.library.service;

import org.springframework.stereotype.Service;
import wad.library.domain.Book;

@Service
public class JsonOpenLibraryService implements OpenLibraryService {

    @Override
    public Book retrieve(String isbn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
