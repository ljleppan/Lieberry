package wad.library.domain;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.AutoPopulatingList;
import wad.library.util.IsbnConverter;

/**
 * Represents a single book.
 * @author ljleppan
 */
@Entity
@Table(name="books")
public class Book implements Serializable {
    
    @Id
    @NotBlank
    @Column(name="ISBN")
    private String isbn;
    
    @NotBlank
    @Column(name="TITLE")
    private String title;
    
    @NotEmpty
    @ElementCollection 
    @CollectionTable(
        name="AUTHOR",
        joinColumns=@JoinColumn(name="BOOK_ISBN")
    )
    @Column(name="AUTHOR")
    private List<String> authors = new AutoPopulatingList(String.class);
    
    @NotBlank
    @Column(name="PUBLISHER")
    private String publisher;
    
    @Min(0)
    @Column(name="PUBLICATIONYEAR")
    private int publicationYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
    
    @Transient
    private String olId;
    
    @Override
    public String toString(){
        return "ISBN: "+this.isbn+"\n"+
                "title: "+this.title+"\n"+
                "authors: "+this.authors.get(0)+"\n"+
                "publisher: "+this.publisher+"\n"+
                "publicationYear: "+this.publicationYear+"\n"+
                "olID: "+this.olId;
    }

    /**
     * Replaces the book's current ISBN with the input. 
     * 
     * <p>The input is formatted and sanitized by {@link IsbnConverter.stringToIsbn}</p>
     * 
     * @param isbn The new ISBN.
     */
    public void setIsbn(String isbn){
        this.isbn = IsbnConverter.stringToIsbn(isbn);
    }
   
    /**
     * Return the book's current ISBN.
     * @return Book's current ISBN as a String.
     */
    public String getIsbn() {
        return this.isbn;
    }
    
    /**
     * Replaces the book's current title with the input.
     * @param title The new title.
     */
    public void setTitle(String title){
        this.title = title;
    }
    
    /**
     * Return the book's current title.
     * @return Book's current title as a string.
     */
    public String getTitle(){
        return this.title;
    }
    
    /**
     * Replaces the book's current author list with the input.
     * @param authors The new {@link java.util.List} of author names as Strings.
     */
    public void setAuthors(List<String> authors){
        this.authors = authors;
    }
    
    /**
     * Return the current list of authors.
     * @return The book's current list of authors as a {@link java.util.List}
     */
    public List<String> getAuthors(){
        return this.authors;
    }
    
    /**
     * Replaces the book's current publisher with the input.
     * @param publisher The new publisher's name.
     */
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    
    /**
     * Return the book's current publisher's name.
     * @return The curren't publisher's name as a string.
     */
    public String getPublisher(){
        return this.publisher;
    }
    
    /**
     * Replace the current publication year with the input.
     * @param publicationYear The new publication year.
     */
    public void setPublicationYear(int publicationYear){
        this.publicationYear = publicationYear;
    }
    
    /**
     * Return the book's publication year.
     * @return The book's publication year as an Integer.
     */
    public int getPublicationYear(){
        return this.publicationYear;
    }

    /**
     * Return the book's Open Library ID
     * @return the oldID
     */
    public String getOlId() {
        return olId;
    }

    /**
     * Set the book's Open Library ID
     * @param olId Open Library ID
     */
    public void setOlId(String olId) {
        this.olId = olId;
    }
}