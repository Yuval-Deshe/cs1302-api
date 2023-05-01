package cs1302.api;

/**
 * Represents a result in a response from the OpenTriviaDB API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class DictionaryResult {
    String id;
    String section;
    String[] shortdef;
} // DictionaryResult
