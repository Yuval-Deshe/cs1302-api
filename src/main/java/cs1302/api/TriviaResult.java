package cs1302.api;

import java.util.Arrays;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a result in a response from the OpenTriviaDB API. This is
 * used by Gson to create an object from the JSON response body.
*/
public class TriviaResult {
    String question;
    @SerializedName("correct_answer") String correctAnswer;
    @SerializedName("incorrect_answers") String[] incorrectAnswers;

    @Override
    public String toString() {
        return "Question: " + question +
            "\nCorrect answer: " + correctAnswer +
            "\nIncorrect answers: " + Arrays.toString(incorrectAnswers) + "\n\n";
    } // toString
} // TriviaResult
