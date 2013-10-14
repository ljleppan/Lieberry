package wad.library.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wad.library.domain.Book;

@Service
public class JsonOpenLibraryService implements OpenLibraryService {
    
    private RestTemplate restTemplate;

    @Override
    public Book retrieve(String isbn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
