package com.tsa;

import javafx.fxml.FXML;
import javafx.scene.control.*;  
import javafx.scene.paint.*;
import javafx.scene.transform.Rotate;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.*;
import java.util.*;
import com.tsa.lang.*;

public class Controller {
    @FXML
    private Canvas graphicsCanvas;
    @FXML
    private Label statusLabel;
    @FXML
    private Button runButton;
    @FXML
    private TextArea codeEditor;
    @FXML
    private Label levelDisplay;
    @FXML
    private AnchorPane winningPane;
    @FXML
    private Hyperlink nextLevelButton;
    @FXML
    private Hyperlink backToMenuHyperlink;
    @FXML
    private Hyperlink menuFromHeaderHyperlink;
    @FXML
    private Label usernameLabel;

    private GraphicsContext graphics;
    private double height;
    private double width;

    private boolean running;
    private Timeline timeline;
    private Duration interval;
    private boolean listenerInitalized;
    private boolean firstInitializedDone;

    private int posx;
    private int posy;
    private double rotation;
    private int[][] currentMap;
    private Image flagImage;

    private ArrayList<Statement> program;
    private Statement currentStatement;
    private boolean validMovement;
    private Parser parser;
    private int programIndex;
    private int animationStep;
    private double animationOffsetX;
    private double animationOffsetY;
    private double animationOffsetRot;

    private final int GRID_SIZE = 64;
    private final int STEP_COUNT = 40;
    private final int GRID_WIDTH = 8;
    private final int GRID_HEIGHT = 7;

    @FXML
    private void initialize() {
        this.graphics = graphicsCanvas.getGraphicsContext2D();
        this.height = graphicsCanvas.getHeight();
        this.width = graphicsCanvas.getWidth();
        this.flagImage = new Image("file:C:\\Users\\wanda\\Desktop\\project\\src\\main\\resources\\com\\tsa\\checkeredflag.png", GRID_SIZE, GRID_SIZE, false, false);
        this.currentMap = App.getCurrentLevel();
        this.winningPane.setOpacity(0);
        this.codeEditor.setText("");
        this.nextLevelButton.setVisited(false);
        this.backToMenuHyperlink.setVisited(false);
        this.menuFromHeaderHyperlink.setVisited(false);
        App.stageRefrence.setTitle("TSA Project - " + MenuController.TITLES[App.currentLevel] + " (Level " + (App.currentLevel + 1) + ")");
        
        if (this.firstInitializedDone) {
            if (App.levelProgresses[App.currentLevel] == 0) {
                App.levelProgresses[App.currentLevel] = 1;
            }
        }

        this.firstInitializedDone = true;
        this.updateLevelCaption();
        this.usernameLabel.setText(App.getFormattedUsername());

        this.resetEverything();

        this.interval = Duration.millis(5);
        this.running = false;

        this.timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    update();
                }
            }),
            new KeyFrame(interval)
        );

        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.drawFrame(); // prime graphics

        if (!this.listenerInitalized) {
            App.stageRefrence.sceneProperty().addListener((observable, oldScene, newScene) -> {
                // i know its really janky
                // but this is how the controller recognizes that its being switched to
                // actually insane solution lmao but it works
                if (newScene.getWidth() == 800) {
                    initialize();
                }
            });

            this.listenerInitalized = true;
        }
    }

    @FXML
    private void setMenu() {
        App.switchMenu();
    }

    @FXML
    private void nextLevel() {
        App.currentLevel++;
        this.codeEditor.setText("");
        this.initialize();
    }

    private void setWinScreen() {
        if (App.currentLevel >= App.LEVEL_COUNT - 1) {
            this.nextLevelButton.setDisable(true);
        } else {
            this.nextLevelButton.setDisable(false);
        }

        this.winningPane.setOpacity(1);
        this.updateLevelCaption();
        this.usernameLabel.setText(App.getFormattedUsername());
    }

    private void updateLevelCaption() {
        String title = MenuController.TITLES[App.currentLevel];
        
        switch (App.levelProgresses[App.currentLevel]) {
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

        this.levelDisplay.setText(title);
    }   

    private void resetEverything() {
        this.posx = 0;
        this.posy = 0;
        this.animationStep = 0;
        this.animationOffsetX = 0;
        this.animationOffsetY = 0;
        this.rotation = 0;
        this.animationOffsetRot = 0;
    }

    @FXML
    private void toggleCode() {
        if (this.running) {
            this.timeline.stop();

            this.running = false;
            this.resetEverything();

            this.statusLabel.setText("Idle.");
            this.drawFrame();
        }

        else {
            this.parser = new Parser(this.codeEditor.getText());
            this.program = this.parser.getProgram();
            this.program = this.expandProgram();

            if (this.program.size() < 1) {
                return;
            }

            this.running = true;
            this.statusLabel.setText("Running...");

            this.resetEverything();
            this.programIndex = -1;
            this.nextInstruction();

            this.timeline.play();
        }
    }

    private ArrayList<Statement> expandProgram() {
        ArrayList<Statement> out = new ArrayList<Statement>();

        for (Statement statement : this.program) {
            if (statement.getType() == StatementType.STATEMENT_FUNCTION) {
                out.add(statement);
            } else if (statement.getType() == StatementType.STATEMENT_REPEAT) {
                for (Statement nestedStatement : this.expandLoop((RepeatStatement) statement)) {
                    out.add(nestedStatement);
                }
            }
        }

        return out;
    }

    private ArrayList<Statement> expandLoop(RepeatStatement rstatement) {
        ArrayList<Statement> out = new ArrayList<Statement>();

        for (int i = 0; i < rstatement.repetitions; i++) {
            for (Statement statement : rstatement.subStatements) {
                if (statement.getType() == StatementType.STATEMENT_FUNCTION) {
                    out.add(statement);
                } else if (statement.getType() == StatementType.STATEMENT_REPEAT) {
                    for (Statement nestedStatement : this.expandLoop((RepeatStatement) statement)) {
                        out.add(nestedStatement);
                    }
                }
            }
        }

        return out;
    }

    private void rotate(double angle, double x, double y) {
        Rotate r = new Rotate(angle, x, y);
        graphics.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void stopRun() {
        this.programIndex = -1;
        this.currentStatement = null;
        this.timeline.stop();
        this.running = false;
        this.statusLabel.setText("Idle.");
        this.drawFrame();
    }

    private void drawFrame() {
        this.graphics.clearRect(0, 0, this.width, this.height);

        this.graphics.setStroke(Color.BLACK);
        this.graphics.setLineWidth(.5);

        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                int type = this.currentMap[y][x];

                switch (type) {
                    case 0: break;

                    case 1: {
                        this.graphics.setFill(Color.BLACK);
                        this.graphics.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                        break;
                    }

                    case 2: {
                        this.graphics.drawImage(flagImage, x * GRID_SIZE, y * GRID_SIZE);
                        break;
                    }
                }

                this.graphics.setStroke(Color.LIGHTGRAY);
                this.graphics.strokeRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }

        this.graphics.setStroke(Color.GRAY);
        this.graphics.setLineWidth(3);
        this.graphics.strokeRect(0, 0, this.width, this.height);

        double rx = (this.posx * GRID_SIZE) + this.animationOffsetX;
        double ry = (this.posy * GRID_SIZE) + this.animationOffsetY;  

        double cx = rx + (GRID_SIZE / 2);
        double cy = ry + (GRID_SIZE / 2);

        // save stack before rotation
        this.graphics.save();
        this.rotate(this.rotation + this.animationOffsetRot, cx, cy);

        this.graphics.setFill(Color.RED);

        this.graphics.fillPolygon(
            new double[] {cx - 20, cx - 10, cx - 20, cx + 20},
            new double[] {cy + 20, cy,      cy - 20, cy},
            4
        );

        this.graphics.setStroke(Color.BLACK);
        this.graphics.setLineWidth(1);

        this.graphics.strokePolygon(
            new double[] {cx - 20, cx - 10, cx - 20, cx + 20},
            new double[] {cy + 20, cy,      cy - 20, cy},
            4
        );

        this.graphics.restore();
    }

    // true: left -- false: right
    private void updateTurnFunction(boolean lr) {
        if (this.animationStep < STEP_COUNT) {
            if (lr) {
                this.animationOffsetRot -= ((double) 90 / STEP_COUNT);
            } else {
                this.animationOffsetRot += ((double) 90 / STEP_COUNT);
            }
        }

        else if (this.animationStep >= STEP_COUNT) {
            if (lr) {
                this.rotation -= 90;
            } else {
                this.rotation += 90;
            }

            this.animationOffsetRot = 0;
        }
    }

    private void updateMoveFunction() {
        if (this.animationStep < STEP_COUNT) {
            double step = ((double) GRID_SIZE / STEP_COUNT);
            double rad = (this.rotation += this.animationOffsetRot) * (Math.PI / 180.0);

            if (!this.validMovement) {
                step /= 4;
            }

            if (this.validMovement) {
                this.animationOffsetX += step * Math.cos(rad);
                this.animationOffsetY += step * Math.sin(rad);
            } else {
                if (this.animationStep >= STEP_COUNT / 2) {
                    this.animationOffsetX -= step * Math.cos(rad);
                    this.animationOffsetY -= step * Math.sin(rad);
                } else {
                    this.animationOffsetX += step * Math.cos(rad);
                    this.animationOffsetY += step * Math.sin(rad);
                }
            }
        }

        else if (this.animationStep >= STEP_COUNT) {
            double rad = (this.rotation += this.animationOffsetRot) * (Math.PI / 180.0);

            int dx = (int) Math.round(GRID_SIZE * Math.cos(rad));
            int dy = (int) Math.round(GRID_SIZE * Math.sin(rad));
            
            if (validMovement) {
                if (dx > 0) this.posx++;
                else if (dx < 0) this.posx--;
                if (dy > 0) this.posy++;
                else if (dy < 0) this.posy--;
            }

            this.animationOffsetX = 0;
            this.animationOffsetY = 0;
        }
    }

    private void nextInstruction() {
        this.programIndex++;

        if (this.programIndex >= this.program.size()) {
            this.stopRun();
        } else {
            this.currentStatement = this.program.get(programIndex);

            if (this.currentStatement.getType() == StatementType.STATEMENT_FUNCTION) {
                FunctionStatement fn = (FunctionStatement) this.currentStatement;
                switch (fn.functionName) {
                    case "left":
                    case "right": 
                        break;
                    case "forward":
                        this.determineValidMovement();
                        break;

                    default: {
                        this.stopRun();
                    }
                }
            }
        }
    }

    private void determineValidMovement() {
        this.validMovement = true;

        int dx = (int) (GRID_SIZE * Math.cos(this.rotation * (Math.PI / 180)));
        int dy = (int) (GRID_SIZE * Math.sin(this.rotation * (Math.PI / 180)));

        int nx = (dx == 0 ? 0 : (dx > 1 ? 1 : -1)) + this.posx;
        int ny = (dy == 0 ? 0 : (dy > 1 ? 1 : -1)) + this.posy;

        if ((nx < 0 || nx >= GRID_WIDTH) || (ny < 0 || ny >= GRID_HEIGHT)) {
            this.validMovement = false;
        } else if (this.currentMap[ny][nx] == 1) {
            this.validMovement = false;
        }
    }

    private void update() {
        if ((!this.running) || (this.programIndex == -1)) {
            return;
        }

        if (this.currentStatement.getType() == StatementType.STATEMENT_FUNCTION) {
            FunctionStatement function = (FunctionStatement) this.currentStatement;

            switch (function.functionName) {
                case "forward":
                    this.updateMoveFunction();
                    break;
                case "left":
                    this.updateTurnFunction(true);
                    break;
                case "right":
                    this.updateTurnFunction(false);
                    break;
            }
        }

        if (this.animationStep >= STEP_COUNT) {
            this.animationStep = 0;

            if (this.currentMap[this.posy][this.posx] == 2) {
                App.levelProgresses[App.currentLevel] = 2;
                this.setWinScreen();
                this.stopRun();
            }

            this.nextInstruction();
        } else {
            this.animationStep++;
        }

        this.drawFrame();
    }
}