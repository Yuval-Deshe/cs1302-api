package cs1302.api;

import java.util.Arrays;

/**
 * Represents a result in a response from the Merriam-Webster Dictionary API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class DictionaryResult {
    DictionaryMeta meta;
    String[] shortdef;

    @Override
    public String toString() {
        return "shortdef: " + Arrays.toString(shortdef) + "\n" + meta;
    } // toString
} // DictionaryResult
