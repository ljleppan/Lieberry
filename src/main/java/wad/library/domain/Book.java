package wad.library.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="books")
public class Book implements Serializable {
    
    @Id
    @NotBlank
    @Column(name="isbn")
    private String isbn;
    
    @NotBlank
    @Column(name="title")
    private String title;
    
    @NotBlank
    @Column(name="author")
    private String author;
    
    @NotBlank
    @Column(name="publisher")
    private String publisher;
    
    @Min(0)
    @Column(name="publicationYear")
    private int publicationYear; 

    public void setIsbn(String isbn){
        this.isbn = isbn;
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
    
    public void setAuthor(String author){
        this.author = author;
    }
    
    public String getAuthor(){
        return this.author;
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
