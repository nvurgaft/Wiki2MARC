package com.protowiki.utils;

import static com.protowiki.utils.Validators.isBlank;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Nick
 */
public class RDFUtils {

    /**
     * Splices the '^^' association tag from a string literal for instance this
     * text: "113230702^^http://www.w3.org/2001/XMLSchema#integer" would become
     * this text: "113230702" Notice that this method also trims the output
     * string!
     *
     * @param integerUri the uri text to splice
     * @return the uri text without the Literal type
     */
    public static String spliceLiteralType(String integerUri) {
        if (isBlank(integerUri) || !integerUri.contains("^^")) {
            return integerUri;
        }
        return integerUri.substring(0, integerUri.indexOf("^^"));
    }

    /**
     * Splices the language tag suffix from a text literal for instance this
     * text: "This is an author description.@jp" would become this text: "This
     * is an author description." Notice that this method also trims the output
     * string!
     *
     * @param text the text to splice
     * @return a language tag suffix spliced out version of the text
     */
    public static String spliceLiteralLaguageTag(String text) {
        if (isBlank(text) || !text.contains("@")) {
            return text;
        }
        return text.substring(0, text.indexOf("@"));
    }

    /**
     * Escapes a string
     *
     * @param text string to escape
     * @return escaped string, null if text string is null
     */
    public static String escapeString(String text) {
        return StringEscapeUtils.escapeJava(text);
    }

    /**
     * Unescapes a string
     *
     * @param text string to escape
     * @return escaped string, null if text string is null
     */
    public static String unescapeString(String text) {
        return StringEscapeUtils.unescapeJava(text);
    }

    /**
     * "Normalizes a full name (as found in MARC 100 fields) into a name that
     * would be easily read and queried in wikipedia/dbPedia Example: a name
     * string such as "Doe, John" would become "John Doe"
     *
     * @param marcName the name string
     * @return the "normalized" name
     */
    public static String normalizeMARCName(String marcName) {
        if (isBlank(marcName) || !marcName.contains(",")) {
            return marcName;
        }

        String[] fname = marcName.split(",");
        return (fname[1].trim() + " " + fname[0].trim());
    }

    /**
     * Slices an article name from a given wikipedia URL
     *
     * Example: for input "http://www.wikipedia.com/Stan_Lee", the result would
     * be "Stan_Lee"
     *
     * @param url Wikipedia article url string
     * @return sliced article name
     */
    public static String sliceNameFromUrl(String url) {
        if (isBlank(url)) {
            return url;
        }
        return url.substring(url.lastIndexOf("/") + 1, url.length()).trim();
    }
}
