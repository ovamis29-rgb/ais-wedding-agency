package ru.kafpin.wedding.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.util.LanguageManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private String language;
    private ResourceBundle bundle;
    @Getter
    private LoginResult result;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private CheckBox chUSA;
    @FXML
    private CheckBox chRussia;
    @FXML
    private CheckBox chGermany;
    @Getter
    @AllArgsConstructor
    public static class LoginResult {
        private final String username;
        private final String password;
        private final boolean isLangChange;
    }
    @FXML
    void initialize(){
        bundle = ResourceBundle.getBundle("main",Locale.getDefault());
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            logger.error("Ошибка загрузки языка", e);
            return;
        }
        chRussia.setAllowIndeterminate(false);
        chUSA.setAllowIndeterminate(false);
        chGermany.setAllowIndeterminate(false);
        language = properties.getProperty("app.language", "en").trim();
        chUSA.setSelected("en".equals(language));
        chRussia.setSelected("ru_RU".equals(language));
        chGermany.setSelected("de_DE".equals(language));
        chRussia.setOnAction(actionEvent -> {
            if(chRussia.isSelected()){
                chGermany.setSelected(false);
                chUSA.setSelected(false);
                LanguageManager.saveLanguage(new Locale("ru","RU"));
                result = new LoginResult(null, null, true);
                ((Stage) chRussia.getScene().getWindow()).close();
            } else {
            chRussia.setSelected(true);
            }
        });
        chUSA.setOnAction(actionEvent -> {
            if(chUSA.isSelected()){
                chGermany.setSelected(false);
                chRussia.setSelected(false);
                LanguageManager.saveLanguage(Locale.ENGLISH);
                result = new LoginResult(null, null, true);
                ((Stage) chUSA.getScene().getWindow()).close();
            } else {
                chUSA.setSelected(true);
            }
        });
        chGermany.setOnAction(actionEvent -> {
            if(chGermany.isSelected()){
                chRussia.setSelected(false);
                chUSA.setSelected(false);
                LanguageManager.saveLanguage(Locale.GERMANY);
                result = new LoginResult(null, null, true);
                ((Stage) chGermany.getScene().getWindow()).close();
            }else {
                chGermany.setSelected(true);
            }
        });
        btnEnter.setOnAction(event -> {
            String u = tfLogin.getText().trim();
            String p = tfPassword.getText();
            if (u.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        bundle.getString("loginDialog.ex.login"),
                        ButtonType.OK).showAndWait();
                return;
            }
            if (p.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        bundle.getString("loginDialog.ex.password"),
                        ButtonType.OK).showAndWait();
                return;
            }
            result = new LoginResult(u, p, false);
            ((Stage) btnEnter.getScene().getWindow()).close();
        });
        btnCancel.setOnAction(event -> {
            result = null;
            ((Stage) btnCancel.getScene().getWindow()).close();
        });
    }
}
