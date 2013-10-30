/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wad.library.util;


public class IsbnConverter {
    
    public static String stringToIsbn(String input){
        String isbn = input.replaceAll("[^0-9]", "");
        if (input.toLowerCase().endsWith("x")){
            isbn = isbn.concat("X");
        }
        return isbn;
    }

}
