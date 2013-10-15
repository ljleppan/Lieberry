package wad.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLibraryBook {
    
    private String ISBN;
    private String url;
    private String title;
    private String subtitle;
    private List<OpenLibraryListElement> authors;
    private List<OpenLibraryListElement> publishers;
    private String publish_date;
    
    @Override
    public String toString(){
        String s = "ISBN: "+this.ISBN+"\n"+
                "url: "+this.url+"\n"+
                "title: "+this.title+"\n"+
                "subtitle: "+this.subtitle+"\n"+
                "authors: "+this.authors+"\n"+
                "publishers: "+this.getPublishers()+"\n"+
                "publish_date: "+this.publish_date;
        return s;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String isbn) {
        this.ISBN = isbn;
    }

    public List<OpenLibraryListElement> getAuthors() {
        return authors;
    }

    public void setAuthors(List<OpenLibraryListElement> authors) {
        this.authors = authors;
    }

    public List<OpenLibraryListElement> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<OpenLibraryListElement> publishers) {
        this.publishers = publishers;
    }
}
