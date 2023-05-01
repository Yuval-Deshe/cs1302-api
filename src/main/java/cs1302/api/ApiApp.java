package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
    public void start(Stage stage) {

        this.stage = stage;

        createTitleScreen();
        createGameScreen();
        createEndScreen();

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
        ComboBox<String> difficultySelect = new ComboBox<String>();
        difficultySelect.getItems().addAll("Any Difficulty", "Easy", "Medium", "Hard");
        ComboBox<String> categorySelect = new ComboBox<String>();
        categorySelect.getItems().addAll(
            "Science & Nature",
            "Computers",
            "Mythology",
            "Geography",
            "Art",
            "Animals",
            "Vehicles",
            "Comics");
        TextField numField = new TextField();

        // containers for the above elements
        HBox titleBox = new HBox(8, title);
        HBox difficultyBox = new HBox(8, difficultyLabel, difficultySelect);
        HBox categoryBox = new HBox(8, categoryLabel, categorySelect);
        HBox numBox = new HBox(8, numLabel, numField);

        Button playButton = new Button("Play");
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

    /** Creates a full URI to be used by the HTTP request.
     *
     * @return the URI to be used by the HTTP request
     */
    private URI createSearchURI () {
        String term = searchField.getText().trim();
        String media = selectMedia.getValue();

        String searchTerm = URLEncoder.encode(term, StandardCharsets.UTF_8);
        String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
        String searchMedia = URLEncoder.encode(media, StandardCharsets.UTF_8);
        String query = String.format("term=%s&limit=%s&media=%s", searchTerm, limit, searchMedia);
        return URI.create(BASE_URI + query);
    } // createSearchURI


} // ApiApp
