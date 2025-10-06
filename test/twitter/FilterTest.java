/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-03-01T11:00:00Z");
    private static final Instant d4 = Instant.parse("2016-01-29T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "xmed3", "til MAGA is stipud", d3);
    private static final Tweet tweet4 = new Tweet(4, "femist1", "why are men", d4);
    private static final Tweet tweet5 = new Tweet(5, "femist1", "why really are men", d4);
    private static final Tweet tweet6 = new Tweet(6, "tsfan11", "omg @taylorswift girliee congratss", d3);
    private static final Tweet tweet7 = new Tweet(7, "politician101", "After a long night of talks mediated by @realdonaldtrump, I am pleased to announce that @narendramodi and @CMShehbaz have agreed to a FULL AND IMMEDIATE CEASEFIRE. Congratulations to both countries on using @common_sense and @great-1ntelligence. Thank you for your attention to this matter!", d2);
    private static final Tweet tweet8 = new Tweet(8, "freelancer", "custom videos email at surybae@gmail.com", d1);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet4, tweet5, tweet8), "femist1");

        assertEquals("expected list", Arrays.asList(tweet4, tweet5), writtenBy);
    }

    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet4, tweet7, tweet8), "monsterman");

        assertEquals("expected empty list", 0, writtenBy.size());
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    @Test
    public void testInTimespanIncludesBoundaries() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        Timespan timespan = new Timespan(d1, d3);

        List<Tweet> result = Filter.inTimespan(tweets, timespan);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2, tweet3);

        assertEquals("expected boundary tweets included", expected, result);
    }

    @Test
    public void testInTimespanNoneInRange() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        Timespan timespan = new Timespan(Instant.parse("2015-12-01T00:00:00Z"), Instant.parse("2015-12-31T23:59:59Z"));

        List<Tweet> result = Filter.inTimespan(tweets, timespan);
        assertTrue("expected empty", result.isEmpty());
    }

    @Test
    public void testInTimespanPreservesOrder() {
        List<Tweet> tweets = Arrays.asList(tweet3, tweet2, tweet1, tweet7, tweet4);
        Timespan timespan = new Timespan(d1, d2);

        List<Tweet> result = Filter.inTimespan(tweets, timespan);
        List<Tweet> expected = Arrays.asList(tweet2, tweet1, tweet7);

        assertEquals("expected preserved input order", expected, result);
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingSingleWord() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        List<String> words = Arrays.asList("rivest");

        List<Tweet> result = Filter.containing(tweets, words);
        List<Tweet> expected = Arrays.asList(tweet1, tweet2);

        assertEquals("expected rivest tweets", expected, result);
    }

    @Test
    public void testContainingMultipleWords() {
        List<Tweet> tweets = Arrays.asList(tweet3, tweet4, tweet5);
        List<String> words = Arrays.asList("men", "maga");

        List<Tweet> result = Filter.containing(tweets, words);
        List<Tweet> expected = Arrays.asList(tweet3, tweet4, tweet5);

        assertEquals("expected", expected, result);
    }

    @Test
    public void testContainingNoMatches() {
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        List<String> words = Arrays.asList("banana", "kiwi");

        List<Tweet> result = Filter.containing(tweets, words);
        assertTrue("expected empty", result.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
