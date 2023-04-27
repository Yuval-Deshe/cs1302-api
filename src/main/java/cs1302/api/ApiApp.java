package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    VBox root;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox(16);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

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
        ComboBox<String> categorySelect = new ComboBox<String>();
        TextField numField = new TextField();

        // containers for the above elements
        HBox difficultyBox = new HBox(8, difficultyLabel, difficultySelect);
        HBox categoryBox = new HBox(8, categoryLabel, categorySelect);
        HBox numBox = new HBox(8, numLabel, numField);

        Button playButton = new Button("Play");

        // setup scene
        root.getChildren().addAll(title, playButton, difficultyBox, categoryBox, numBox);
        playButton.setAlignment(Pos.CENTER);
        difficultyBox.setAlignment(Pos.CENTER);
        categoryBox.setAlignment(Pos.CENTER);
        numBox.setAlignment(Pos.CENTER);
        titleScreen = new Scene(root);

        // setup stage
        stage.setTitle("Trivia App: Title Screen");
        stage.setScene(titleScreen);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
