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

    private double positionx;

    @FXML
    private void initialize() {
        this.graphics = graphicsCanvas.getGraphicsContext2D();
        this.height = graphicsCanvas.getHeight();
        this.width = graphicsCanvas.getWidth();

        this.positionx = 20;

        this.interval = Duration.millis(50);
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
        this.drawFrame();
    }

    @FXML
    private void toggleCode() {
        if (this.running) {
            this.timeline.stop();

            this.running = false;
            this.positionx = 20;
            this.drawFrame();

            this.statusLabel.setText("Idle.");
        }

        else {
            this.running = true;
            this.statusLabel.setText("Running...");

            this.timeline.play();
        }
    }

    private void drawFrame() {
        this.graphics.clearRect(0, 0, this.width, this.height);

        this.graphics.setFill(Color.BLUE);
        this.graphics.fillOval(this.positionx, 50, 25, 25);
    }

    private void update() {
        if (!this.running) return;

        this.drawFrame();
        this.positionx += 1.0;
    }
}