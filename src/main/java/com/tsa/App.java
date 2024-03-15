package com.tsa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static final int LEVEL_COUNT = 4;

    // 0 for not started, 1 for in progress, 2 for done
    public static int[] levelProgresses = new int[] {
        0, 0, 0, 0
    };

    // 0 - air, 1 - wall, 2 - coin
    static int[][] levelOne = new int[][] {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 2, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };

    static int[][] levelTwo = new int[][] {
        {0, 0, 0, 0, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 0, 1, 1},
        {1, 1, 1, 1, 1, 0, 1, 1},
        {1, 1, 1, 1, 1, 2, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1}
    };

    static int[][] levelThree = new int[][] {
        {0, 0, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 1, 1, 1, 1, 1},
        {1, 1, 0, 0, 1, 1, 1, 1},
        {1, 1, 1, 0, 0, 1, 1, 1},
        {1, 1, 1, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 2, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1}
    };

    static int[][] levelFour = new int[][] {
        {0, 1, 1, 1, 1, 1, 1, 1},
        {0, 1, 0, 0, 0, 1, 2, 1},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {0, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static int currentLevel = 0;

    static Scene mainScene;
    static Scene menuScene;
    static Stage stageRefrence;
    static Database database;
    static boolean loggedIn;
    static String username = "pkonnoth";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderMain = new FXMLLoader(App.class.getResource("main.fxml"));
        FXMLLoader fxmlLoaderMenu = new FXMLLoader(App.class.getResource("menu.fxml"));
        stageRefrence = stage;

        mainScene = new Scene(fxmlLoaderMain.load(), 800, 540);
        menuScene = new Scene(fxmlLoaderMenu.load(), 440, 410);
        database = new Database();
        
        App.switchMenu();
        stage.show();
    }

    public static String getFormattedUsername() {
        if (App.loggedIn) {
            int tmp = 0;

            for (int i : App.levelProgresses) {
                if (i == 2) {
                    tmp++;
                }
            }

            double progress = 100 * (double) tmp / App.LEVEL_COUNT;
            return String.format("%s (%d%% Complete)", App.username, (int) Math.round(progress));
        } else {
            return "Not Logged In";
        }
    }

    public static void switchMenu() {
        stageRefrence.setScene(menuScene);
        App.centerStage();
    }

    public static void switchMain() {
        stageRefrence.setScene(mainScene);
        App.centerStage();
    }

    public static int[][] getCurrentLevel() {
        switch (App.currentLevel) {
            case 0: return levelOne;
            case 1: return levelTwo;
            case 2: return levelThree;
            case 3: return levelFour;

            default:
                return null;
        }
    }

    public static void centerStage() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene currentScene = stageRefrence.getScene();

        stageRefrence.setX((screenBounds.getWidth() - currentScene.getWidth()) / 2); 
        stageRefrence.setY((screenBounds.getHeight() - currentScene.getHeight()) / 2);  
    }

    public static void main(String[] args) {
        launch();
    }
}