package cs1302.api;

import java.util.Arrays;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a result in a response from the OpenTriviaDB API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class TriviaResponse {
    @SerializedName("response_code") String responseCode;
    TriviaResult[] results;

    @Override
    public String toString() {
        return "responseCode: " + responseCode + "\n" + Arrays.toString(results);
    } // toString
} // TriviaResponse
