package com.tsa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.*;
import javafx.scene.transform.Rotate;
import javafx.scene.canvas.*;
import javafx.scene.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.*;
import javafx.util.*;
import com.tsa.lang.*;

public class MenuController {
    static final String[] TITLES = new String[] {
        "Basic Movement",
        "Advanced Movement",
        "Basic Looping",
        "Harder Looping"
    };

    static final String[] DESCRIPTIONS = new String[] {
        "Get Started with Basic Movement Functions",
        "Start Using Looping for Movement",
        "Begin Using Looping for Moving in Patterns",
        "Write a More Complex Program"
    };

    @FXML
    private Label titleText;
    @FXML
    private Label descriptionText;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private ImageView previewDisplay;

    private boolean listenerInitalized;
    private Image[] images;

    @FXML
    private void initialize() {
        if (!this.listenerInitalized) {
            App.stageRefrence.sceneProperty().addListener((observable, oldScene, newScene) -> {
                // jank again. this is how the menu controller realizes its being observed lmao
                if (newScene.getWidth() == 440) {
                    this.update();
                }
            });

            this.listenerInitalized = true;
        }

        this.images = new Image[App.LEVEL_COUNT];

        for (int i = 0; i < App.LEVEL_COUNT; i++) {
            images[i] = new Image("file:C:\\Users\\wanda\\Desktop\\project\\src\\main\\resources\\com\\tsa\\level_screenshot_" + (i + 1) + ".png");
        }

        this.update();
    }

    private void update() {
        int level = App.currentLevel;
        int progress = App.levelProgresses[level];
        String title = TITLES[level];

        switch (progress) {
            case 0:
                title += " (Not Started)";
                break;
            case 1:
                title += " (In Progress)";
                break;
            case 2:
                title += " (Completed!)";
                break;
        }

        this.titleText.setText(title);
        this.descriptionText.setText(DESCRIPTIONS[level]);

        this.previewDisplay.setImage(images[App.currentLevel]);

        if (App.currentLevel == 0) {
            this.previousButton.setDisable(true);
        } else {
            this.previousButton.setDisable(false);
        }

        if (App.currentLevel == App.LEVEL_COUNT - 1) {
            this.nextButton.setDisable(true);
        } else {
            this.nextButton.setDisable(false);
        }

        App.stageRefrence.setTitle("TSA Project - Main Menu");
    }

    @FXML
    private void startLevel() {
        App.switchMain();
    }

    private void scroll(int n) {
        App.currentLevel += n;

        if (App.currentLevel > App.LEVEL_COUNT - 1) {
            App.currentLevel = App.LEVEL_COUNT - 1;
        }

        else if (App.currentLevel < 0) {
            App.currentLevel = 0;
        }

        this.update();
    }

    @FXML
    private void movePrevious() {
        this.scroll(-1);
    }

    @FXML
    private void moveNext() {
        this.scroll(+1);
    }
}