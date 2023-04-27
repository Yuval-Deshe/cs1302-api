package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A trivia game using the OpenTriviaDB API and the Merriam-Webster Dictionary API.
 */
public class ApiApp extends Application {
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
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        createTitleScreen();

        //gameScreen = new Scene(gameRoot, 800, 320);
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
        difficultySelect.getItems().addAll("Easy", "Medium", "Hard");
        ComboBox<String> categorySelect = new ComboBox<String>();
        categorySelect.getItems().addAll(
            "Any Category",
            "General Knowledge",
            "Books",
            "Film",
            "Music",
            "Musicals & Theatre",
            "Television",
            "Video Games",
            "Board Games",
            "Science & Nature",
            "Computers",
            "Mathematics",
            "Mythology",
            "Sports",
            "Geography",
            "History",
            "Politics",
            "Art",
            "Celebrities",
            "Animals",
            "Vehicles",
            "Comics",
            "Gadgets",
            "Anime & Manga",
            "Cartoons & Animations");

        TextField numField = new TextField();

        // containers for the above elements
        HBox titleBox = new HBox(8, title);
        HBox difficultyBox = new HBox(8, difficultyLabel, difficultySelect);
        HBox categoryBox = new HBox(8, categoryLabel, categorySelect);
        HBox numBox = new HBox(8, numLabel, numField);

        Button playButton = new Button("Play");
        HBox playBox = new HBox(8, playButton);
        // setup scene
        titleRoot.getChildren().addAll(titleBox, playBox, difficultyBox, categoryBox, numBox);
        titleBox.setAlignment(Pos.CENTER);
        difficultyBox.setAlignment(Pos.CENTER);
        categoryBox.setAlignment(Pos.CENTER);
        numBox.setAlignment(Pos.CENTER);
        playBox.setAlignment(Pos.CENTER);
        titleScreen = new Scene(titleRoot, 800, 320);
    } // createTitleScreen

} // ApiApp
