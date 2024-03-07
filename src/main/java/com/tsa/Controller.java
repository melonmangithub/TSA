package com.tsa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
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

    private GraphicsContext graphics;
    private double height;
    private double width;

    private boolean running;
    private Timeline timeline;
    private Duration interval;

    private double posx;
    private double posy;
    private double rotation;

    private ArrayList<Statement> program;
    private Parser parser;
    private int programIndex;
    private int animationStep;
    private final int STEP_COUNT = 50;

    @FXML
    private void initialize() {
        this.graphics = graphicsCanvas.getGraphicsContext2D();
        this.height = graphicsCanvas.getHeight();
        this.width = graphicsCanvas.getWidth();

        this.posx = 0;
        this.posy = 0;

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
    }

    @FXML
    private void toggleCode() {
        if (this.running) {
            this.timeline.stop();

            this.running = false;
            this.posx = 0;
            this.posy = 0;

            this.statusLabel.setText("Idle.");
            this.drawFrame();
        }

        else {
            this.running = true;
            this.statusLabel.setText("Running...");

            this.parser = new Parser(this.codeEditor.getText());
            this.program = this.parser.getProgram();

            this.timeline.play();
        }
    }

    private void drawFrame() {
        this.graphics.clearRect(0, 0, this.width, this.height);

        this.graphics.setFill(Color.RED);
        this.graphics.fillRect(this.posx, this.posy, 50, 35);
    }

    private void update() {
        if (!this.running) return;

        this.drawFrame();
        this.posx += 1;
        this.posy += 0;
    }
}