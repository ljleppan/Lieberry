package wad.library.domain.openlibrary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLibrarySearchResult {
    private List<String> isbn;
    private List<String> edition_key;
    
    public List<String> getIsbn() {
        return isbn;
    }

    public void setIsbn(List<String> isbn) {
        this.isbn = isbn;
    }

    public List<String> getEdition_key() {
        return edition_key;
    }

    public void setEdition_key(List<String> edition_key) {
        this.edition_key = edition_key;
    }
}
