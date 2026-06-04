package ru.kafpin.wedding.util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kafpin.wedding.controller.LoginController;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginDialog {
    private LoginController controller;

    public Optional<LoginController.LoginResult> showAndWait() {
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("main");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kafpin/wedding/logging-view.fxml"),bundle);
            controller = new LoginController();
            loader.setController(controller);
            Scene scene = new Scene(loader.load(),580,229);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            return Optional.ofNullable(controller.getResult());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
