package wad.library.domain.openlibrary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLibrarySearchResultList {
    private String numFound;
    private List<OpenLibrarySearchResult> docs;

    public String getNumFound() {
        return numFound;
    }

    public void setNumFound(String numFound) {
        this.numFound = numFound;
    }

    public List<OpenLibrarySearchResult> getDocs() {
        return docs;
    }

    public void setDocs(List<OpenLibrarySearchResult> docs) {
        this.docs = docs;
    }

}
