package wad.library.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import wad.library.util.IsbnConverter;
import wad.library.util.IsbnConverter;
import static org.junit.Assert.*;

/**
 *
 * @author ljleppan
 */
public class IsbnConverterTest {
    
    public IsbnConverterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void IsbConverterTest(){
        Assert.assertEquals(
                "Expected '1' with input '1'",
                "1",
                IsbnConverter.stringToIsbn("1")
                );
        
        Assert.assertEquals(
                "Expected '' with input ''",
                "",
                IsbnConverter.stringToIsbn("")
                );
        Assert.assertEquals(
                "Expected '' with input 'a'",
                "",
                IsbnConverter.stringToIsbn("a")
                );
        Assert.assertEquals(
                "Expected '1X' with input '1X'",
                "1X",
                IsbnConverter.stringToIsbn("1X")
                );
        Assert.assertEquals(
                "Expected '1X' with input '1x'",
                "1X",
                IsbnConverter.stringToIsbn("1x")
                );
        Assert.assertEquals(
                "Expected '123123123' with input '123-123-123'",
                "123123123",
                IsbnConverter.stringToIsbn("123-123-123")
                );
        Assert.assertEquals(
                "Expected '123123123X' with input '123-123-123-X'",
                "123123123X",
                IsbnConverter.stringToIsbn("123-123-123-X")
                );
    }
}