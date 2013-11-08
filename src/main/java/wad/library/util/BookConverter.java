package wad.library.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import wad.library.domain.Book;
import wad.library.domain.openlibrary.OpenLibraryBook;
import wad.library.domain.openlibrary.OpenLibraryListElement;


/**
 * Converts instances of {@link OpenLibraryBook} to instances of {@link Book}.
 * @author Loezi
 */
public class BookConverter {
    /**
     * Converts an instance of {@link OpenLibraryBook} to an instance of {@link Book}.
     * @param olBook    An instance of {@link OpenLibraryBook}.
     * @return          An instance of {@link Book}.
     */
    public static Book olBookToBook(OpenLibraryBook olBook) {
        Book book = new Book();
        
        String olId = "";
        if (olBook.getKey() != null){
            olId = olBook.getKey().substring(olBook.getKey().lastIndexOf("/")+1);
        }
        book.setOlId(olId);
        
        String isbn = "";
        if (olBook.getIdentifiers().containsKey("isbn_10")){
            isbn = olBook.getIdentifiers().get("isbn_10").get(0);
        }
        if (olBook.getIdentifiers().containsKey("isbn_13")){
            isbn = olBook.getIdentifiers().get("isbn_13").get(0);
        }
        book.setIsbn(isbn);
        
        String title = "";
        if (olBook.getTitle() != null){
            title = olBook.getTitle();
        }
        if (olBook.getSubtitle() != null){
            title = title+olBook.getSubtitle();
        }
        book.setTitle(title);
        
        if (olBook.getAuthors() != null){
            List<String> authors = new ArrayList<String>();
            for (OpenLibraryListElement author : olBook.getAuthors()){
                authors.add(author.getName());
            }
            book.setAuthors(authors);
        }
        
        if (olBook.getPublishers() != null && !olBook.getPublishers().isEmpty()){
            String publisher = olBook.getPublishers().get(0).getName();
            book.setPublisher(publisher);
        }
        
        if (olBook.getPublish_date() != null){
            String pubDate = olBook.getPublish_date();
            pubDate = pubDate.substring(pubDate.length() - 4);
            System.out.println("pubdate = "+pubDate);
            try{
                book.setPublicationYear(Integer.parseInt(pubDate));
            } catch (Exception ex){
                System.out.println("Exception: "+ex);
                book.setPublicationYear(new GregorianCalendar().get(GregorianCalendar.YEAR));
            }
        }

        System.out.println("Transformed given olBook to following book: "+book);
        return book;
    }
}
