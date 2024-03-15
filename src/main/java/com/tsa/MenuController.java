package com.tsa;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.*;

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
    @FXML
    private Hyperlink loginSignout;
    @FXML
    private Label usernameLabel;

    @FXML
    private TitledPane accountInputPane;
    @FXML
    private TextField accountUsername;
    @FXML
    private TextField accountPassword;
    @FXML
    private Hyperlink accountHyperlink;
    @FXML
    private Label accountFeedback;
    @FXML
    private Hyperlink accountCreateHyperlink;
    @FXML
    private Hyperlink exitHyperlink;


    private boolean listenerInitalized;
    private Image[] images;
    private boolean inAccountPanel;
    private boolean creating;

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

        this.loginSignout.setTextAlignment(TextAlignment.CENTER);
        this.images = new Image[App.LEVEL_COUNT];

        for (int i = 0; i < App.LEVEL_COUNT; i++) {
            images[i] = new Image("file:C:\\Users\\wanda\\Desktop\\project\\src\\main\\resources\\com\\tsa\\level_screenshot_" + (i + 1) + ".png");
        }

        this.accountInputPane.setOpacity(0);
        this.accountFeedback.setText("");
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

        if (App.loggedIn) {
            this.loginSignout.setText("Log Out");
        } else{ 
            this.loginSignout.setText("Login");
        }

        this.usernameLabel.setText(App.getFormattedUsername());

        this.accountHyperlink.setVisited(false);
        this.accountCreateHyperlink.setVisited(false);
        this.loginSignout.setVisited(false);
        this.exitHyperlink.setVisited(false);
    }

    @FXML
    private void startLevel() {
        if (!this.inAccountPanel) {
            App.switchMain();
        }
    }

    @FXML
    private void loginCallback() {
        if (!App.loggedIn) {
            this.openAccountPanel(false);
        } else {
            App.loggedIn = false;
            this.update();
        }
    }

    @FXML
    private void createAccountCallback() {
        if (!App.loggedIn) {
            this.openAccountPanel(true);
        }
    }

    private void scroll(int n) {
        if (this.inAccountPanel) {
            return;
        }

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
        if (!this.inAccountPanel) {
            this.scroll(-1);
        }
    }

    @FXML
    private void moveNext() {
        if (!this.inAccountPanel) {
            this.scroll(+1);
        }
    }

    // false: not creating account, true: are creating account
    @FXML
    private void openAccountPanel(boolean creating) {
        if (inAccountPanel) {
            return;
        }
        
        this.creating = creating;
        this.inAccountPanel = true;
        
        if (creating) {
            this.accountHyperlink.setText("Create Account");
            this.accountInputPane.setText("Create New Account");
        } else {
            this.accountHyperlink.setText("Login");
            this.accountInputPane.setText("Login");
        }

        this.accountFeedback.setText("");
        this.accountInputPane.setOpacity(1);
    }

    @FXML
    private void tryAccountLogin() {
        if (!inAccountPanel) {
            return;
        }

        if (!this.creating) {
            int id = App.database.getUser(this.accountUsername.getText(), this.accountPassword.getText());
            String username = this.accountUsername.getText();

            if (id < 1) {
                this.accountFeedback.setTextFill(Color.RED);
                this.accountFeedback.setText("Login Failed");
            } else {
                this.accountFeedback.setTextFill(Color.GREEN);
                this.accountFeedback.setText("Logged In!");
            }

            App.loggedIn = true;
            App.username = username;
            this.update();
        } else {
            int id = App.database.createUser(this.accountUsername.getText(), this.accountPassword.getText());

            if (id < 1) {
                this.accountFeedback.setTextFill(Color.RED);
                this.accountFeedback.setText("Creation Failed");
            } else {
                this.accountFeedback.setTextFill(Color.GREEN);
                this.accountFeedback.setText("Account Created!");
            }
        }
    }

    @FXML
    private void closeAccountPanel() {
        this.inAccountPanel = false;
        this.creating = false;
        this.accountInputPane.setOpacity(0);
        this.accountUsername.setText("");
        this.accountPassword.setText("");
        this.update();
    }
}