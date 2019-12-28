package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ExitQuizGenerator extends Application {

	@Override
	public void start(Stage primaryStage) {
		
	
		try {
			Button saveButton = new Button("Save to File");
			Button exitWO = new Button("Exit Without Saving");
			BorderPane root = new BorderPane();
			root.setLeft(saveButton);
			root.setRight(exitWO);
			Scene scene = new Scene(root,1000,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Exit Quiz Generator");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args) {
		System.out.println("Exit Quiz Generator");
		launch(args);
	}
}
