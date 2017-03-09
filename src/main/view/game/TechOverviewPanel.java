package view.game;

import java.awt.Point;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import view.GameModelAdapter;
import view.ViewEnum;
import view.assets.AssetManager;

public class TechOverviewPanel extends OverviewPanel{
	ScrollPane scrollPane = new ScrollPane();
	GraphicsContext techGraphics;
	Canvas canvas;
	Group root;
	public TechOverviewPanel(GameModelAdapter gameModelAdapter, AssetManager assets, ViewEnum view, Group root) {
		super(gameModelAdapter, assets, view);
		this.root = root;
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		canvas = new Canvas();
		techGraphics = canvas.getGraphicsContext2D();
		scrollPane.setContent(canvas);
		scrollPane.getStyleClass().setAll("scroll");
	}

	public void draw(GraphicsContext g, Point screenDimensions, long currentPulse) {
		techGraphics.clearRect(0, 0, screenDimensions.x, screenDimensions.y);
		scrollPane.setMaxWidth(screenDimensions.x - 259);
		scrollPane.setMaxHeight(screenDimensions.y - 147);
		scrollPane.setTranslateX(185);
		scrollPane.setTranslateY(50);
		canvas.setWidth(3000);
		canvas.setHeight(screenDimensions.y - 147);
        drawPanelBox(g, screenDimensions);
	}

	public void showGUIElements() {
		root.getChildren().add(scrollPane);
	}

	public void hideGUIElements() {
		root.getChildren().remove(scrollPane);
	}
}
