import Domeniu.Utilizator;
import Service.AppException;
import Service.IService;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginMain
{
    private IService service;

    private JavaFXApplication app ;

        public LoginMain(IService service) {
            this.service = service;
        }
        public void start(Stage primaryStage)
        {

            BorderPane root = new BorderPane();

            AnchorPane leftAnchorPane = new AnchorPane();
            leftAnchorPane.setPrefSize(200, 200);
            root.setLeft(leftAnchorPane);

            AnchorPane rightAnchorPane = new AnchorPane();
            rightAnchorPane.setPrefSize(200, 200);
            root.setRight(rightAnchorPane);

            AnchorPane centerAnchorPane = new AnchorPane();
            centerAnchorPane.setPrefSize(200, 200);
            root.setCenter(centerAnchorPane);

            Label usernameLabel = new Label("Username");
            usernameLabel.setLayoutX(14);
            usernameLabel.setLayoutY(153);
            usernameLabel.setFont(Font.font("Arial Narrow Bold", 16));
            centerAnchorPane.getChildren().add(usernameLabel);

            Label passwordLabel = new Label("Password");
            passwordLabel.setLayoutX(14);
            passwordLabel.setLayoutY(190);
            passwordLabel.setFont(Font.font("Arial Narrow Bold", 16));
            centerAnchorPane.getChildren().add(passwordLabel);

            TextField usernameField = new TextField();
            usernameField.setLayoutX(91);
            usernameField.setLayoutY(150);
            usernameField.setPrefSize(173, 26);
            centerAnchorPane.getChildren().add(usernameField);

            TextField passwordField = new TextField();
            passwordField.setLayoutX(91);
            passwordField.setLayoutY(186);
            passwordField.setPrefSize(173, 26);
            centerAnchorPane.getChildren().add(passwordField);

            Button loginButton = new Button("Login");
            loginButton.setLayoutX(16);
            loginButton.setLayoutY(295);
            loginButton.setPrefSize(231, 26);
            loginButton.setStyle("-fx-background-color: #fab025;");
            centerAnchorPane.getChildren().add(loginButton);

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.show();

            loginButton.setOnAction(event ->{
                    if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Please fill in all fields");
                        alert.showAndWait();
                    }
                    else{
                        Utilizator utilizator = new Utilizator(usernameField.getText(), passwordField.getText());
                        try {
                            this.app = new JavaFXApplication(service, utilizator);
                            service.login(utilizator, this.app);
                            primaryStage.close();
                            this.app.start(new Stage());
                        } catch (AppException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Invalid username or password");
                            alert.showAndWait();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
            });
        }
}
