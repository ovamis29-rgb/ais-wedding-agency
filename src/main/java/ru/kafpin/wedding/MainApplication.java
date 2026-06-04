package ru.kafpin.wedding;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.controller.LoginController;
import ru.kafpin.wedding.util.LanguageManager;
import ru.kafpin.wedding.util.LoginDialog;
import ru.kafpin.wedding.controller.MainController;
import ru.kafpin.wedding.dao.*;
import ru.kafpin.wedding.dao.impl.*;
import ru.kafpin.wedding.util.DBHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    ResourceBundle bundle;
    @Getter
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        MainApplication.stage = primaryStage;
        logger.info("Приложение запущено");
        ClientDao clientDao = new ClientDaoImpl();
        ContractorDao contractorDao = new ContractorDaoImpl();
        ContractorServiceDao contractorServiceDao = new ContractorServiceDaoImpl();
        GuestDao guestDao = new GuestDaoImpl();
        PortfolioDao portfolioDao = new PortfolioDaoImpl();
        ProjectDao projectDao = new ProjectDaoImpl();
        ReportDao reportDao = new ReportDaoImpl();
        TaskDao taskDao = new TaskDaoImpl();
        UsedContractorDao usedContractorDao = new UsedContractorDaoImpl();
        ProjectClientDao projectClientDao = new ProjectClientDaoImpl();
        while(true){
            LanguageManager.loadLanguage();
            LoginDialog loginDialog = new LoginDialog();
            Optional<LoginController.LoginResult> result = loginDialog.showAndWait();
            if(result.isEmpty()){
                logger.info("Пользователь отменил вход, выход");
                Platform.exit();
                return;
            }
            if (result.get().isLangChange()) {
                continue;
            }
            bundle = ResourceBundle.getBundle("main", Locale.getDefault());
            logger.info("Загружены ресурсы для локали {}", Locale.getDefault());
            String username = result.get().getUsername();
            String password = result.get().getPassword();
            try{
                DBHelper.initConnection(username,password);
                logger.info("Пользователь {} успешно подключился",username);
                break;
            }catch (SQLException e){
                logger.error("Ошибка подключения для пользователя {}",username,e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("mainApplication.alert.title"));
                alert.setHeaderText(bundle.getString("mainApplication.alert.headerText"));
                alert.setContentText(bundle.getString("mainApplication.alert.contentText"));
                alert.showAndWait();
            }
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"),bundle);
            fxmlLoader.setControllerFactory(param -> {
                if (param == null || param == MainController.class) {
                    return MainController.builder().clientDao(clientDao)
                            .contractorDao(contractorDao)
                            .contractorServiceDao(contractorServiceDao)
                            .guestDao(guestDao)
                            .portfolioDao(portfolioDao)
                            .projectDao(projectDao)
                            .reportDao(reportDao)
                            .taskDao(taskDao)
                            .usedContractorDao(usedContractorDao)
                            .projectClientDao(projectClientDao).build();
                }
                throw new IllegalStateException(bundle.getString("mainApplication.ex.controllerFactory") + " " + param);
            });
            Scene scene = new Scene(fxmlLoader.load(), 1200, 820);
            primaryStage.setTitle(bundle.getString("mainApplication.title"));
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {
                logger.error("Ошибка загрузки FXML ", e);
                Platform.exit();
            }
    }
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void stop(){
        DBHelper.closeConnection();
        logger.info("Приложение остановлено");
    }
}