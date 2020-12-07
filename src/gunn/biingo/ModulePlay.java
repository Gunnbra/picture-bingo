package gunn.biingo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.Arrays;

public class ModulePlay {
    private File projectDirectory;
    private ModuleDatabase moduleDatabase;

    private boolean[] trackedNumbers = new boolean[76];

    public ModulePlay(File projDir, ModuleDatabase modData) {
        projectDirectory = projDir;
        moduleDatabase = modData;
    }

    public void tabPlayGame(Tab tab) {
        // Scene Creations
        BorderPane mainLayout = new BorderPane();

        // CENTER
        VBox callBox = new VBox();
        callBox.setAlignment(Pos.CENTER);
        callBox.setSpacing(10);
        callBox.setPadding(new Insets(0, 0, 0, 0));
        // B I N G O
        HBox bingoBox = new HBox();
        bingoBox.setAlignment(Pos.CENTER);
        bingoBox.setSpacing(90);
        bingoBox.setPadding(new Insets(0, 0, 0, 0));
        Text textB = new Text("B");
        textB.setStyle("-fx-font-size: 50px; -fx-font-weight: bold");
        textB.setTextAlignment(TextAlignment.CENTER);
        Text textI = new Text("I");
        textI.setStyle("-fx-font-size: 50px; -fx-font-weight: bold");
        textI.setTextAlignment(TextAlignment.CENTER);
        Text textN = new Text("N");
        textN.setStyle("-fx-font-size: 50px; -fx-font-weight: bold");
        textN.setTextAlignment(TextAlignment.CENTER);
        Text textG = new Text("G");
        textG.setStyle("-fx-font-size: 50px; -fx-font-weight: bold");
        textG.setTextAlignment(TextAlignment.CENTER);
        Text textO = new Text("O");
        textO.setStyle("-fx-font-size: 50px; -fx-font-weight: bold");
        textO.setTextAlignment(TextAlignment.CENTER);
        bingoBox.getChildren().add(textB);
        bingoBox.getChildren().add(textI);
        bingoBox.getChildren().add(textN);
        bingoBox.getChildren().add(textG);
        bingoBox.getChildren().add(textO);
        callBox.getChildren().add(bingoBox);
        // Called Numbers
        HBox calledNumbers = renderPlayTracker();
        callBox.getChildren().add(calledNumbers);
        mainLayout.setCenter(callBox);

        // BOTTOM
        HBox optionBox = new HBox();
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(100);
        optionBox.setPadding(new Insets(10, 5, 10, 5));
        // Elements
        Button buttonReset = new Button("Reset Board");
        Button buttonVerify = new Button("Verification");
        Button buttonLastCalled = new Button("Last Called");
        optionBox.getChildren().add(buttonReset);
        optionBox.getChildren().add(buttonVerify);
        optionBox.getChildren().add(buttonLastCalled);
        mainLayout.setBottom(optionBox);

        tab.setContent(mainLayout);

        // Listeners

        //Button View Cards
        buttonVerify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    moduleDatabase.databasePopup();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Renders all possible numbers with a checkbox, to keep track of what has been called
     */
    public HBox renderPlayTracker() {
        // Main PlayBox
        HBox playBox = new HBox();
        playBox.setAlignment(Pos.CENTER);
        playBox.setSpacing(10);
        playBox.setPadding(new Insets(0, 10, 10, 10));

        // File Locations
        File iconDir = new File(projectDirectory + "/" + "icons");
        String[] fileList = iconDir.list();

        // Loops through B I N G O
        for (int i = 0; i < 5; i++) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(0, 10, 0, 10));

            // Looks through 1-15 of each letter
            for (int j = 1; j < 16; j++) {
                HBox numBox = new HBox();
                numBox.setAlignment(Pos.CENTER);
                numBox.setPadding(new Insets(0, 10, 0, 10));

                final File fileNum = new File(iconDir.getAbsolutePath() + "/" + "01.png");
                numBox.setStyle("-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: black;");
                numBox.setPadding(new Insets(5, 5, 5, 5));
                numBox.setSpacing(5);
                numBox.setAlignment(Pos.CENTER);

                // Adds number
                Text textNumber = new Text(Integer.toString(j + (i * 15)));
                numBox.getChildren().add(textNumber);

                String numberName = Integer.toString(j + (i * 15));
                if (Integer.parseInt(numberName) < 10) {
                    numberName = "0" + numberName;
                }

                File numFile = new File(iconDir.getAbsolutePath() + "/" + numberName + ".png");

                // If icon file of number exists, use element, if not use a large text version of number
                if (numFile.exists()) {
                    Image image = new Image("file:" + numFile);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    numBox.getChildren().add(imageView);
                } else {
                    Text t = new Text(numberName);
                    t.setStyle("-fx-font-size: 22; -fx-font-weight: bold");
                    numBox.getChildren().add(t);
                }

                // Add checkbox
                CheckBox checkNum = new CheckBox();
                String finalNumberName = numberName;
                checkNum.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        int num = Integer.parseInt(finalNumberName);

                        trackedNumbers[num] = t1;
                        moduleDatabase.setTracked(trackedNumbers);
                        moduleDatabase.rerenderDatabaseCard();
                    }
                });

                numBox.getChildren().add(checkNum);
                vBox.getChildren().add(numBox);
            }
            playBox.getChildren().add(vBox);
        }

        // Put into scrollpane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(playBox);

        HBox calledBox = new HBox();
        calledBox.setAlignment(Pos.CENTER);
        calledBox.setSpacing(10);
        calledBox.setPadding(new Insets(0, 10, 0, 10));
        calledBox.getChildren().add(scrollPane);
        return calledBox;
    }
}
