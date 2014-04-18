import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4; 

import java.util.*;


@RunWith(JUnit4.class)
public class StringFrequencyTest {

    private StringFrequency stringFrequency;

    @Before
    public void setUp() throws Exception { 
        stringFrequency = new StringFrequency();
    }

    @Test
    public void testNullString() {
        try {
            List<String> list = stringFrequency.getSortedWordsByApperance(null, 4);
            assertTrue("Failure - Passing null should have thrown an exception", false);
        } catch (IllegalArgumentException e) {
            assertTrue("This will always be true", true);
        }
    }

    @Test
    public void testEmptyString() {
        List<String> list = stringFrequency.getSortedWordsByApperance("", 4);
        assertEquals("Passing \"\" should return an empty list", 0, list.size());
    }

    @Test
    public void testStringNoCharsString() {
        List<String> list = stringFrequency.getSortedWordsByApperance("   ", 4);
        assertEquals("Passing \"   \" should return an empty list", 0, list.size());
    }

    @Test
    public void testOnlyNumbers() {
        List<String> list = stringFrequency.getSortedWordsByApperance("837 8 -1 10,000", 4);
        assertEquals("Passing numbers returns return an empty list", 0, list.size());
    }

    @Test
    public void testOnlySymbols() {
        List<String> list = stringFrequency.getSortedWordsByApperance("&(@&( @) !| \\ ^", 4);
        assertEquals("Passing symbols should return an empty list", 0, list.size());
    }

    @Test
    public void testNumbersAndSymbols() {
        List<String> list = stringFrequency.getSortedWordsByApperance("&(5 @53&( @) 2!| 4\\ ^6", 4);
        assertEquals("Passing numbers and symbols should return an empty list", 0, list.size());
    }

    @Test
    public void testNumbersAndChars() {
        List<String> list = stringFrequency.getSortedWordsByApperance("b5 5g3 25 5g3 988hsh hshq99", 4);
        assertEquals("Passing numbers and chars", 4, list.size());
        assertEquals("g3 should be in the list", "g3", list.get(0));
        assertEquals("b5 should be in the list", "b5", list.get(1));
        assertEquals("hsh should be in the list", "hsh", list.get(2));
        assertEquals("hshq99 should be in the list", "hshq99", list.get(3));

        list = stringFrequency.getSortedWordsByApperance("i18n i985 18ln", 4);
        assertEquals("Passing numbers and chars", 3, list.size());
        assertEquals("i18n should be in the list", "i18n", list.get(0));
        assertEquals("i985 should be in the list", "i985", list.get(1));
        assertEquals("ln should be in the list", "ln", list.get(2));

        list = stringFrequency.getSortedWordsByApperance("9lsnsh9 91jn92mj 92f89k9", 4);
        assertEquals("Passing numbers and chars", 3, list.size());
        assertEquals("f89k9 should be in the list", "f89k9", list.get(0));
        assertEquals("jn92mj should be in the list", "jn92mj", list.get(1));
        assertEquals("lsnsh9 should be in the list", "lsnsh9", list.get(2));
    }

    @Test
    public void testSymbolsAndChars() {
        List<String> list = stringFrequency.getSortedWordsByApperance("$jifji! $j$ifji! fkji/  %ej-he** ie_l he8 he8he", 7);
        assertEquals("Passing numbers and chars", 7, list.size());
        assertEquals("ej-he should be in the list", "ej-he", list.get(0));
        assertEquals("fkji should be in the list", "fkji", list.get(1));
        assertEquals("he8 should be in the list", "he8", list.get(2));
        assertEquals("he8he should be in the list", "he8he", list.get(3));
        assertEquals("ie_l should be in the list", "ie_l", list.get(4));
        assertEquals("j$ifji should be in the list", "j$ifji", list.get(5));
        assertEquals("jifji should be in the list", "jifji", list.get(6));
    }

    @Test
    public void testOneWord() {
        List<String> list = stringFrequency.getSortedWordsByApperance("hello", 4);
        assertEquals("Should return a list of 1", 1, list.size());
        assertEquals("hello should be in the list", "hello", list.get(0));
    }

    @Test
    public void testAllWordsAppearOnce() {
        List<String> list = stringFrequency.getSortedWordsByApperance("I love my dog.", 4);
        assertEquals("Should return a list of 4", 4, list.size());

        // they should be ordered alphabetically
        assertEquals("dog should be first the list", "dog", list.get(0));
        assertEquals("I should be second the list", "I", list.get(1));
        assertEquals("love should be third the list", "love", list.get(2));
        assertEquals("my should be last the list", "my", list.get(3));

        // lets get 3 words to make sure "my" was the one dropped
        list = stringFrequency.getSortedWordsByApperance("I love my dog.", 3);
        assertEquals("Should return a list of 3", 3, list.size());

        // they should be ordered alphabetically
        assertEquals("dog should be first the list", "dog", list.get(0));
        assertEquals("I should be second the list", "I", list.get(1));
        assertEquals("love should be third the list", "love", list.get(2));
    }

    @Test
    public void testMixedCase() {
        List<String> list = stringFrequency.getSortedWordsByApperance("I loVe My dog. Dog is my Best FrIEnd.", 4);
        assertEquals("Should return a list of 4", 4, list.size());

        // they should be ordered by number or apperance and alphabetically
        // "dog" should be first since it appears twice and is alphabetically ahead of "my"
        assertEquals("dog should be first the list", "dog", list.get(0));
        // my should be second
        assertEquals("my should be second the list", "my", list.get(1));
        assertEquals("best should be third the list", "best", list.get(2));
        assertEquals("friend should be last the list", "friend", list.get(3));
    }

    @Test
    public void testSingleChars() {
        List<String> list = stringFrequency.getSortedWordsByApperance("l o V e M y d o g. D o g i s m y B e s t F r i E n d.", 4);
        assertEquals("Should return a list of 0", 0, list.size());
    }

    @Test
    public void testOneWordManyTimes() {
        List<String> list = stringFrequency.getSortedWordsByApperance("ma ma ma ma ma", 4);
        assertEquals("Should return a list of 1", 1, list.size());
        assertEquals("Should have \"ma\" as only word", "ma", list.get(0));
    }

    @After
    public void tearDown() throws Exception { 
    }
}