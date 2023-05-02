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
        String tempId = meta.id;
        if (tempId.contains(":")) {
            tempId = tempId.substring(0, tempId.indexOf(":"));
        } // if
        String ret = tempId + ":";
        for (int i = 0; i < shortdef.length; i++) {
            ret = ret + "\n" + (i + 1) + ". " + shortdef[i];
        } // for
        return ret;
    } // toString
} // DictionaryResult
