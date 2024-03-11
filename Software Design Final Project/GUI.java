import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text; // Import Text class
import javafx.stage.Stage;

public class GUI extends Application {

    private Text resultText; // Text control to display the result

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Book Cipher");

        Button decryptButton = new Button("Decrypt");
        Button encryptButton = new Button("Encrypt");
        

        decryptButton.setOnAction(e -> displayDecryptionResult());
        encryptButton.setOnAction(e -> displayEncryptionResult());


        resultText = new Text(); // Create a Text control

        StackPane root = new StackPane();
        StackPane.setAlignment(decryptButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(encryptButton, Pos.CENTER_RIGHT);

        root.getChildren().addAll(decryptButton, encryptButton, resultText);
        primaryStage.setScene(new Scene(root, 400, 250));

        primaryStage.show();
    }

    private void displayDecryptionResult() {
        // Perform decryption in a separate thread or background task
        new Thread(() -> {
            String decryptionResult = Main.decrypt("encrypted.txt", "book.txt");

            // Update the GUI with the result using Platform.runLater
            javafx.application.Platform.runLater(() -> {
                resultText.setText(decryptionResult);
            });
        }).start();
    }

    private void displayEncryptionResult() {
        // Perform encryption in a separate thread or background task
        new Thread(() -> {
            String encryptionResult = Main.encrypt("plain.txt", "book.txt");

            // Update the GUI with the result using Platform.runLater
            javafx.application.Platform.runLater(() -> {
                resultText.setText(encryptionResult);
            });
        }).start();
    }
}
