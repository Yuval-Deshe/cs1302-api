package cs1302.api;

import java.util.Arrays;

/**
 * Represents a result in a response from the Merriam-Webster Dictinoary API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class DictionaryResponse {
    DictionaryResult[] results;

    @Override
    public String toString() {
        return Arrays.toString(results);
    } // toString
} // DictionaryResult
