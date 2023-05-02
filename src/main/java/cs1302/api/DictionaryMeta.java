package cs1302.api;

/**
 * Represents a result in a response from the Merriam-Webster Dictionary API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class DictionaryMeta {
    String id;
    String section;

    @Override
    public String toString() {
        return String.format("id: %s | section: %s\n", id, section);
    } // toString
} // DictionaryMeta
