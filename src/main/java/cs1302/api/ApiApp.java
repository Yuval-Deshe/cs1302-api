package cs1302.api;

import java.net.http.HttpClient;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    Button playButton;
    ComboBox<String> difficultySelect;
    ComboBox<String> categorySelect;
    TextField numField;


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
        createTitleScreen();
        createGameScreen();
        createEndScreen();
        playButton.setOnAction(e -> System.out.println(getTriviaQuestions()));
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        //endScreen = new Scene(endRoot, 800, 320);

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
            "Vehicles",
            "Comics");
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
        Label questionLabel = new Label("Question placeholder");
        Label aLabel = new Label("A)");
        Label bLabel = new Label("B)");
        Label cLabel = new Label("C)");
        Label dLabel = new Label("D)");

        Button aButton = new Button("A");
        aButton.setPrefSize(200, 80);
        aButton.setWrapText(true);
        Button bButton = new Button("B");
        bButton.setPrefSize(200, 80);
        bButton.setWrapText(true);
        Button cButton = new Button("C");
        cButton.setPrefSize(200, 80);
        cButton.setWrapText(true);
        Button dButton = new Button("D");
        dButton.setPrefSize(200, 80);
        dButton.setWrapText(true);

        Button nextButton = new Button("Next");
        nextButton.setAlignment(Pos.CENTER);
        Label defLabel = new Label("Definiton: ");

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
        gameScreen = new Scene(gameRoot, 800, 320);
    } // createGameScreen

        /** Initializes the end screen, to be shown when the game is over. */
    private void createEndScreen() {
        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setFont(new Font(30));
        Label scoreLabel = new Label("Correct answers: x/x\n" +
            "Score: x%");
        scoreLabel.setFont(new Font(30));
        Button playAgainButton = new Button("Play again?");
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
        String category = categorySelect.getValue();
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
        //TODO: Implement
        return null;
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
            // Make an HTTP request for the items matching the search terms and selected media type.
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            String body = response.body();
            if (response.statusCode() != 200) {
                throw new IOException("Status code: " + response.statusCode());
            } // if
            TriviaResponse triviaResponse = GSON
                .fromJson(body, TriviaResponse.class);
            // Check the API's status code to make sure that the questions were retrieved correctly
            if (!triviaResponse.responseCode.equals("0")) {
                throw new IOException("Trivia API status code: " + triviaResponse.responseCode);
            } // if
            return triviaResponse;
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
} // ApiApp
