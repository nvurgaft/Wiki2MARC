package com.protowiki.core;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Nick
 */
public class Matching {
    
    /**
     *  Tests if the Jaro Winkler distance between two characters sequence is higher or equals to a provided minimal value 
     * 
     * @param a Character sequence
     * @param b Character sequence
     * @param minimalAcceptableDistance The minimum acceptable Jaro Winkler distance that must be accepted for this
     * method to succeed (a double value between 0 and 1)
     * @return True is and only if the Jaro Winkler distance is higher or equal to the minimalAcceptableDistance
     */
    public static boolean jaroWinkler(CharSequence a, CharSequence b, double minimalAcceptableDistance) {
        return StringUtils.getJaroWinklerDistance(a, b)>=minimalAcceptableDistance;
    }
    
    /**
     *  Tests if the Levenshteins distance between two characters sequence is higher or equals to a provided minimal value
     * 
     * @param a Character sequence
     * @param b Character sequence
     * @param minimalAcceptableDistance The minimum acceptable Levenshtein distance that must be accepted for this
     * method to succeed (the distance is the amount of characters that share the same index location in both strings)
     * @return True is and only if the Levenshtein distance is higher or equal to the minimalAcceptableDistance
     */
    public static boolean levenshtein(CharSequence a, CharSequence b, double minimalAcceptableDistance) {
        return StringUtils.getLevenshteinDistance(a, b)>=minimalAcceptableDistance;
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return 
     */
    public static int levenshteinDifference(CharSequence a, CharSequence b) {
        return (a.length()>=b.length() ? a.length() : b.length()) - StringUtils.getLevenshteinDistance(a, b);
    }
    
    
}
