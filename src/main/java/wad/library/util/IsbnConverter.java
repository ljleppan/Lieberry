/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.util;


/**
 * Converts Strings to an ISBN-esque strings.
 * @author Loezi
 */
public class IsbnConverter {
    
    /**
     * Converts a String to an ISBN-esque String.
     * 
     * <p>The resulting String contains only numbers and a possible final X. String length is not checked.</p>
     * 
     * @param input Input String.
     * @return      ISBN-esque String.
     */
    public static String stringToIsbn(String input){
        String isbn = input.replaceAll("[^0-9]", "");
        if (input.toLowerCase().endsWith("x")){
            isbn = isbn.concat("X");
        }
        return isbn;
    }

}
