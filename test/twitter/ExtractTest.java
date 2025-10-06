/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

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
    private static final Tweet tweet5 = new Tweet(5, "femist2", "why are men", d4);
    private static final Tweet tweet6 = new Tweet(6, "tsfan11", "omg @taylorswift girliee congratss", d3);
    private static final Tweet tweet7 = new Tweet(7, "politician101", "After a long night of talks mediated by @realdonaldtrump, I am pleased to announce that @narendramodi and @CMShehbaz have agreed to a FULL AND IMMEDIATE CEASEFIRE. Congratulations to both countries on using @common_sense and @great-1ntelligence. Thank you for your attention to this matter!", d2);
    private static final Tweet tweet8 = new Tweet(8, "freelancer", "custom videos email at surybae@gmail.com", d1);


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanSameTime() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet5));

        assertEquals("expected start", d4, timespan.getStart());
        assertEquals("expected end", d4, timespan.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4));

        assertEquals("expected start", d4, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }


    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
    }

    // tests multiple mentions in 1 tweet, multiple in multiple, and other uses of @ like @gmail.com
    @Test
    public void testGetMentionedUsersMultiple() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6, tweet7, tweet8));
        Set<String> expectedMentions = new HashSet<>();
        expectedMentions.add("taylorswift");
        expectedMentions.add("realdonaldtrump");
        expectedMentions.add("narendramodi");
        expectedMentions.add("cmshehbaz");
        expectedMentions.add("common_sense");
        expectedMentions.add("great-1ntelligence");

        assertEquals("expected mentions", mentionedUsers, expectedMentions);
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
