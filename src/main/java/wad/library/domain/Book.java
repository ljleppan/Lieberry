package wad.library.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

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
    private List<String> authors = new ArrayList<String>();
    
    @NotBlank
    @Column(name="PUBLISHER")
    private String publisher;
    
    @Min(0)
    @Column(name="PUBLICATIONYEAR")
    private int publicationYear; 

    public void setIsbn(String isbn){
        String newIsbn = isbn.replaceAll("[^0-9]", "");
        if (isbn.toLowerCase().endsWith("x")){
            newIsbn = newIsbn.concat("X");
        }
        this.isbn = newIsbn;
    }
   
    public String getIsbn() {
        return this.isbn;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setAuthors(List<String> authors){
        this.authors = authors;
    }
    
    public List<String> getAuthors(){
        return this.authors;
    }
    
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    
    public String getPublisher(){
        return this.publisher;
    }
    
    public void setPublicationYear(int publicationYear){
        this.publicationYear = publicationYear;
    }
    
    public int getPublicationYear(){
        return this.publicationYear;
    }
}