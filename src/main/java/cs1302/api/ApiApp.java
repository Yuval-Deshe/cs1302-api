package cs1302.api;

import java.net.http.HttpClient;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A trivia game using the OpenTriviaDB API and the Merriam-Webster Dictionary API.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object


    Stage stage;
    Scene titleScreen;
    Scene gameScreen;
    Scene endScreen;
    VBox titleRoot;
    VBox gameRoot;
    VBox endRoot;

    // Elements on the title screen
    Button playButton;
    ComboBox<String> difficultySelect;
    ComboBox<String> categorySelect;
    TextField numField;

    // Elements on the game screen
    Label questionLabel;
    Label aLabel;
    Label bLabel;
    Label cLabel;
    Label dLabel;
    Button aButton;
    Button bButton;
    Button cButton;
    Button dButton;
    Button nextButton;
    Label defLabel;

    // Elements on the end screen
    Label gameOverLabel;
    Label scoreLabel;
    Button playAgainButton;

    // Local variables
    String category;
    int questionNum;
    int numQuestions;
    int numCorrect;
    TriviaResponse triviaResponse;
    TriviaResult currentQuestion;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        titleRoot = new VBox(16);
        gameRoot = new VBox(16);
        endRoot = new VBox(64);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        numCorrect = 0;
        questionNum = -1;
        createTitleScreen();
        createGameScreen();
        createEndScreen();
        playButton.setOnAction(e -> {
            triviaResponse = getTriviaQuestions();
            currentQuestion = triviaResponse.results[0];
            numQuestions = triviaResponse.results.length;
            nextQuestion();
            changeScreen(gameScreen, "Trivia App: Quiz");
        });
        nextButton.setOnAction(e -> nextQuestion());
        playAgainButton.setOnAction(e -> {
            changeScreen(titleScreen, "Trivia App: Title Screen");
            numCorrect = 0;
            questionNum = -1;
        });
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // setup stage
        changeScreen(titleScreen, "Trivia App: Title Screen");
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /**
     * Changes which scene is shown and updates the title of the window.
     *
     * @param scene the scene to change to
     * @param title the new title of the window
     */
    private void changeScreen(Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
    } // changeScreen

    /** Initializes the titleScreen scene, to be shown when the app is launched.  */
    private void createTitleScreen() {
        // load the title image
        Image titleImage = new Image("file:resources/title.png");
        ImageView title = new ImageView(titleImage);
        title.setPreserveRatio(true);
        title.setFitWidth(640);

        // set some properties of the quiz including difficulty, category, and # of questions
        Label difficultyLabel = new Label("Select your difficulty:");
        Label categoryLabel = new Label("Select your category:");
        Label numLabel = new Label("Enter your desired number of questions (max 25):");
        difficultySelect = new ComboBox<String>();
        difficultySelect.getItems().addAll("Any Difficulty", "Easy", "Medium", "Hard");
        difficultySelect.getSelectionModel().selectFirst();
        categorySelect = new ComboBox<String>();
        categorySelect.getItems().addAll(
            "Science & Nature",
            "Computers",
            "Mythology",
            "Geography",
            "Art",
            "Animals",
            "Vehicles");
        categorySelect.getSelectionModel().selectFirst();
        numField = new TextField("10");

        // containers for the above elements
        HBox titleBox = new HBox(8, title);
        HBox difficultyBox = new HBox(8, difficultyLabel, difficultySelect);
        HBox categoryBox = new HBox(8, categoryLabel, categorySelect);
        HBox numBox = new HBox(8, numLabel, numField);

        playButton = new Button("Play");
        HBox playBox = new HBox(8, playButton);

        // setup scene
        titleRoot.getChildren().addAll(titleBox, new Pane(),
            difficultyBox, categoryBox, numBox, playBox);
        titleBox.setAlignment(Pos.CENTER);
        difficultyBox.setAlignment(Pos.CENTER);
        categoryBox.setAlignment(Pos.CENTER);
        numBox.setAlignment(Pos.CENTER);
        playBox.setAlignment(Pos.CENTER);
        titleScreen = new Scene(titleRoot, 800, 320);
    } // createTitleScreen

    /** Initializes the game screen, to be shown when the Play button is cilcked. */
    private void createGameScreen() {
        questionLabel = new Label("Question placeholder");
        questionLabel.setWrapText(true);
        aLabel = new Label("A)");
        bLabel = new Label("B)");
        cLabel = new Label("C)");
        dLabel = new Label("D)");

        aButton = new Button("A");
        aButton.setPrefSize(200, 80);
        aButton.setWrapText(true);
        bButton = new Button("B");
        bButton.setPrefSize(200, 80);
        bButton.setWrapText(true);
        cButton = new Button("C");
        cButton.setPrefSize(200, 80);
        cButton.setWrapText(true);
        dButton = new Button("D");
        dButton.setPrefSize(200, 80);
        dButton.setWrapText(true);

        nextButton = new Button("Next");
        nextButton.setAlignment(Pos.CENTER);
        defLabel = new Label("Definiton: ");
        defLabel.setWrapText(true);

        VBox aBox = new VBox(8, aLabel, aButton);
        aBox.setAlignment(Pos.TOP_LEFT);
        VBox bBox = new VBox(8, bLabel, bButton);
        bBox.setAlignment(Pos.TOP_RIGHT);
        VBox cBox = new VBox(8, cLabel, cButton);
        cBox.setAlignment(Pos.BOTTOM_LEFT);
        VBox dBox = new VBox(8, dLabel, dButton);
        dBox.setAlignment(Pos.BOTTOM_RIGHT);

        HBox questionBox = new HBox(16, questionLabel);
        HBox row1 = new HBox(16, aBox, bBox);
        HBox row2 = new HBox(16, cBox, dBox);
        HBox defBox = new HBox(16, defLabel);
        HBox nextBox = new HBox(16, nextButton);
        nextBox.setAlignment(Pos.CENTER);
        defBox.setAlignment(Pos.CENTER);

        gameRoot.getChildren().addAll(questionBox, row1, row2, nextBox, defBox);
        questionBox.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        gameScreen = new Scene(gameRoot, 800, 400);
    } // createGameScreen

        /** Initializes the end screen, to be shown when the game is over. */
    private void createEndScreen() {
        gameOverLabel = new Label("Game Over!");
        gameOverLabel.setFont(new Font(30));
        scoreLabel = new Label("Correct answers: x/x\n" +
            "Score: x%");
        scoreLabel.setFont(new Font(30));
        playAgainButton = new Button("Play again?");
        playAgainButton.setPrefSize(200, 80);

        HBox gameOverBox = new HBox(8, gameOverLabel);
        HBox scoreBox = new HBox(8, scoreLabel);
        HBox playAgainBox = new HBox(8, playAgainButton);
        gameOverBox.setAlignment(Pos.CENTER);
        scoreBox.setAlignment(Pos.CENTER);
        playAgainBox.setAlignment(Pos.CENTER);

        endRoot.getChildren().addAll(gameOverBox, scoreBox, playAgainBox);
        endScreen = new Scene(endRoot, 800, 320);
    } // createEndScreen

    /** Creates a full URI to be used by the HTTP request for trivia questions.
     *
     * @return the URI to be used by the HTTP request
     */
    private URI createTriviaSearchURI() {
        String baseString = "https://opentdb.com/api.php?type=multiple&";
        String amount = numField.getText().trim();
        category = categorySelect.getValue();
        String diff = difficultySelect.getValue().toLowerCase();

        int num = 10;
        try {
            num = Integer.valueOf(amount);
            if (num > 25) {
                num = 25;
            } // if
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                String message = "Invalid amount: " + amount +
                     "\n\nException: " + e.toString();
                Alert alert = new Alert(AlertType.ERROR,
                    message,
                    ButtonType.CLOSE);
                alert.showAndWait();
            });
        } // catch
        int categoryNum = 17;
        if (category.equals("Science & Nature")) {
            categoryNum = 17;
        } else if (category.equals("Computers")) {
            categoryNum = 18;
        } else if (category.equals("Mythology")) {
            categoryNum = 20;
        } else if (category.equals("Geography")) {
            categoryNum = 22;
        } else if (category.equals("Art")) {
            categoryNum = 25;
        } else if (category.equals("Animals")) {
            categoryNum = 27;
        } else if (category.equals("Vehicles")) {
            categoryNum = 28;
        } else if (category.equals("Comics")) {
            categoryNum = 29;
        } // if

        String query;
        if (diff.equals("any difficulty")) {
            query = String.format("amount=%s&category=%s", num, categoryNum);
        } else {
            query = String.format("amount=%s&category=%s&difficulty=%s", num, categoryNum, diff);
        } // if
        return URI.create(baseString + query);
    } // createTriviaSearchURI

    /** Creates a full URI to be used by the HTTP request for the dictionary
     * definition of the word that should be searched for.
     *
     * @return the URI to be used by the HTTP request
     */
    private URI createDictionarySearchURI () {
        String term = URLEncoder.encode(currentQuestion.correctAnswer.trim(),
            StandardCharsets.UTF_8);
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '+') {
                term = term.substring(0, i) + "%20" + term.substring(i + 1);
            } // if
        } // for
        return URI.create(String.format("https://dictionaryapi.com/api/v3/references/" +
        "collegiate/json/%s?key=8d40cdf8-661f-4048-ac22-8e3bf2667132", term));
    } // createDictionarySearchURI

        /** Creates a full URI to be used by the HTTP request for the dictionary
     * definition of the word that should be searched for.
     *
     * @param term the term to search for
     * @return the URI to be used by the HTTP request
     */
    private URI createDictionarySearchURI(String term) {
        String newTerm = URLEncoder.encode(term.trim(), StandardCharsets.UTF_8);
        for (int i = 0; i < newTerm.length(); i++) {
            if (newTerm.charAt(i) == '+') {
                newTerm = newTerm.substring(0, i) + "%20" + newTerm.substring(i + 1);
            } // if
        } // for
        return URI.create(String.format("https://dictionaryapi.com/api/v3/references/" +
        "collegiate/json/%s?key=8d40cdf8-661f-4048-ac22-8e3bf2667132", newTerm));
    } // createDictionarySearchURI

    /**
     * Generate trivia questions when the "Play" {@code Button} is clicked.
     *
     * @return the response from the Trivia API
     */
    private TriviaResponse getTriviaQuestions() {
        URI searchURI = createTriviaSearchURI();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(searchURI)
            .build();
        try {
            // Make an HTTP request for trivia questions matching the user input
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            String body = response.body();
            if (response.statusCode() != 200) {
                throw new IOException("Status code: " + response.statusCode());
            } // if
            TriviaResponse tempTriviaResponse = GSON
                .fromJson(body, TriviaResponse.class);
            // Check the API's status code to make sure that the questions were retrieved correctly
            if (!tempTriviaResponse.responseCode.equals("0")) {
                if (tempTriviaResponse.responseCode.equals("1")) {
                    throw new IOException("Trivia API status code: 1: No Results - " +
                        " Could not return results. The API doesn't have enough questions for " +
                        "your query. (Ex. Asking for 50 Questions in a Category that only has 20)");
                } else if (tempTriviaResponse.responseCode.equals("2")) {
                    throw new IOException("Trivia API status code: 2: Invalid Parameter - " +
                    "Contains an invalid parameter. Arguements passed in aren't valid.");
                } else if (tempTriviaResponse.responseCode.equals("3")) {
                    throw new IOException("Trivia API status code: 3: Token Not Found - Session " +
                    "Token does not exist.");
                } else if (tempTriviaResponse.responseCode.equals("4")) {
                    throw new IOException("Trivia API status code: 4: Token Empty - Session " +
                        "Token has returned all possible questions for the specified query." +
                        " Resetting the Token is necessary.\n\n");
                } // if
            } // if
            return tempTriviaResponse;
        } catch (IOException ioe) {
            Platform.runLater(() -> {
                String message = "URI: " + searchURI +
                    "\n\nException: " + ioe.toString();
                Alert alert = new Alert(AlertType.ERROR,
                    message,
                    ButtonType.CLOSE);
                alert.showAndWait();
            });
            return null;
        } catch (InterruptedException ie) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR,
                    ie.toString(),
                    ButtonType.CLOSE);
                alert.showAndWait();
            });
            return null;
        } // try
    } // getTriviaQuestions

    /**
     * Get the dictionary definition for the correct answer to the current trivia question.
     *
     * @return the dictionary definition for the correct answer to the current trivia question
     */
    private DictionaryResult[] getDictionaryDefinition() {
        URI searchURI = createDictionarySearchURI();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(searchURI)
            .build();
        HttpResponse<String> response = null;
        String body = null;
        try {
            // Make an HTTP request for the definition(s) of the search term
            response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            body = response.body();
            if (response.statusCode() != 200) {
                throw new IOException("Status code: " + response.statusCode());
            } // if
            DictionaryResult[] dictionaryResults = GSON
                .fromJson(body, DictionaryResult[].class);
            return dictionaryResults;
        } catch (IOException ioe) {
            Platform.runLater(() -> {
                String message = "URI: " + searchURI +
                    "\n\nException: " + ioe.toString();
                Alert alert = new Alert(AlertType.ERROR,
                    message,
                    ButtonType.CLOSE);
                alert.showAndWait();
            });
            return null;
        } catch (InterruptedException ie) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR,
                    ie.toString(),
                    ButtonType.CLOSE);
                alert.showAndWait();
            });
            return null;
        } catch (JsonSyntaxException jse) {
            return null;
        } // try
    } // getDictionaryDefinition

    /**
     * Change to the next question.
     */
    private void nextQuestion() {
        // update the question number and the question text
        if (questionNum < triviaResponse.results.length) {
            questionNum += 1;
        } // if
        if (questionNum == triviaResponse.results.length) {
            gameOver();
            return;
        } // if
        Platform.runLater(() -> {
            defLabel.setText("");
            String questionText = String.format("%s/%s: %s", questionNum + 1,
                triviaResponse.results.length, decodeHtml(currentQuestion.question));
            questionLabel.setText(decodeHtml(questionText));
            aButton.setStyle(null);
            bButton.setStyle(null);
            cButton.setStyle(null);
            dButton.setStyle(null);
            nextButton.setDisable(true);
            aButton.setDisable(false);
            bButton.setDisable(false);
            cButton.setDisable(false);
            dButton.setDisable(false);
        });
        currentQuestion = triviaResponse.results[questionNum];

        // Generate a random int value from 0 to 3, representing answer choices
        // a, b, c, and d respectively.
        int correctButton = (int) Math.floor(Math.random() * 4);
        Button[] buttonList = new Button[] {aButton, bButton, cButton, dButton};

        int count = 0;
        for (int i = 0; i < buttonList.length; i++) {
            if (i != correctButton) {
                Button currentButton = buttonList[i];
                String currentAnswer = decodeHtml(currentQuestion.incorrectAnswers[count]);
                currentButton.setOnAction(e -> answerIncorrectly(currentButton));
                // if the button DOES NOT represent the correct answer, change its text to represent
                // one of the incorrect answer choices. Set the button to an incorrect answer.
                Platform.runLater(() -> currentButton.setText(currentAnswer));
                count += 1;
            } else {
                Button currentButton = buttonList[i];
                // if the button represents the correct answer, change its text to represent
                // the correct answer choice. Set the chosen button the the correct answer.
                String correctAnswer = decodeHtml(currentQuestion.correctAnswer);
                currentButton.setOnAction(e -> answerCorrectly(currentButton));
                Platform.runLater(() -> currentButton.setText(correctAnswer));
            } // if
        } // for
    } // nextQuestion

    /**
     * Checks for a definition to the correct answer that is relevant to the category.
     * e.g.: a biographical definition for a Science & Nature question.
     *
     * @return the relevant definition of the correct answer, or the first definition if there is no
     * relevant definition
     */
    private String checkRelevantDef() {
        DictionaryResult[] def = getDictionaryDefinition();
        if (def == null || def.length == 0) {
            return "No definition found for \"" + decodeHtml(currentQuestion.correctAnswer) + "\".";
        } else {
            for (int i = 0; i < def.length; i++) {
                if (category.equals("Science & Nature") &&
                    def[i].meta.section.equals("biog")) {
                    // if it is a Science & Nature question and there is a biographical
                    // definition for the word, prioritize it.
                    return def[i].toString();
                } else if (category.equals("Geography") &&
                        def[i].meta.section.equals("geog")) {
                    // if it is a Geography question and there is a geographical
                    // definition for the word, prioritize it.
                    return def[i].toString();
                } // if
            } // for
        } // if
        return def[0].toString();
    } // checkRelevantDef

    /**
     * A method that is called when a question is answered correctly. Increments the correct
     * answer count, highlights the correct answer green, and displays its definition.
     *
     * @param b the button that was clicked
     */
    private void answerCorrectly(Button b) {
        numCorrect += 1; // increment the number of correct questions answered
        Platform.runLater(() -> {
            // make the button green
            b.setStyle("-fx-background-color: #00ff00;");
            // change defLabel to display the definition of the correct answer
            defLabel.setText(checkRelevantDef());
            nextButton.setDisable(false);
            aButton.setDisable(true);
            bButton.setDisable(true);
            cButton.setDisable(true);
            dButton.setDisable(true);
        });
    } // answerCorrectly

    /**
     * A method that is called when a question is answered incorrectly. Highlights the
     * chosen answer red, the correct answer green, and displays the correct answer's definition.
     *
     * @param b the button that was clicked
     */
    private void answerIncorrectly(Button b) {
        Button[] buttonList = new Button[] {aButton, bButton, cButton, dButton};
        Button temp = null;
        // find the button that represents the correct answer
        for (int i = 0; i < buttonList.length; i++) {
            if (buttonList[i].getText().equals(currentQuestion.correctAnswer)) {
                temp = buttonList[i];
            } // if
        } // for

        Button correct = temp;
        Platform.runLater(() -> {
            // make the chosen button red and the correct button green
            b.setStyle("-fx-background-color: #ff0000;");
            correct.setStyle("-fx-background-color: #00ff00;");
            // change defLabel to display the definition of the correct answer
            defLabel.setText(checkRelevantDef());
            nextButton.setDisable(false);
            aButton.setDisable(true);
            bButton.setDisable(true);
            cButton.setDisable(true);
            dButton.setDisable(true);
        });
    } // answerIncorrectly

    /** Ends the quiz and changes to the end screen. */
    private void gameOver() {
        double score = 100 * ((double) numCorrect / (double) numQuestions);
        Platform.runLater(() -> scoreLabel.setText("Correct answers: " + numCorrect +
            "/" + numQuestions + "\nScore: " + score + "%"));
        changeScreen(endScreen, "Trivia App: Game Over");
    } // gameOver

    /**
     * Decode HTML formatting.
     *
     * @param s the string to be decoded
     * @return the decoded string
     */
    private String decodeHtml(String s) {
        s = s.replace("&nbsp;", " ");
        s = s.replace("&quot;", "\"");
        s = s.replace("&rdquo;", "\"");
        s = s.replace("&ldquo;", "\"");
        s = s.replace("&apos;", "'");
        s = s.replace("&#039;", "'");
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", ">");
        s = s.replace("&amp;", "&");
        return s;
    } // decodeHtml
} // ApiApp
