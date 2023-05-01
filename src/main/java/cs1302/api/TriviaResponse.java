package cs1302.api;

/**
 * Represents a result in a response from the OpenTriviaDB API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class TriviaResponse {
    @SerializedName("response_code") int responseCode;
    TriviaResult[] results;
} // TriviaResponse
