import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ru.kafpin.wedding.controller.MainController;
import ru.kafpin.wedding.dao.ClientDao;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.dao.impl.*;
import ru.kafpin.wedding.model.Client;
import ru.kafpin.wedding.model.Guest;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.util.DBHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

public class MainApplicationTest extends ApplicationTest{
    private final String user = "manager";
    private final String password = "admin";
    ResourceBundle bundle;
    private List<Client> clientsToClean = new ArrayList<>();
    private List<Project> projectsToClean = new ArrayList<>();
    @AfterEach
    void cleanUp() {
        ClientDao clientDao = new ClientDaoImpl();
        ProjectDao projectDao = new ProjectDaoImpl();
        for (Client c : clientsToClean) {
            try {
                clientDao.delete(c);
            } catch (Exception e) {
                System.err.println("Не удалось удалить клиента: " + e.getMessage());
            }
        }
        clientsToClean.clear();
        for (Project p : projectsToClean) {
            try {
                projectDao.delete(p);
            } catch (Exception e) {
                System.err.println("Не удалось удалить проект: " + e.getMessage());
            }
        }
        projectsToClean.clear();
    }
    @Override
    public void start(Stage stage) throws Exception {
        bundle = ResourceBundle.getBundle("main", Locale.getDefault());
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplicationTest.class.getResource("main-view.fxml"),bundle);
        fxmlLoader.setControllerFactory(param -> {
            if (param == null || param == MainController.class) {
                return MainController.builder().clientDao(new ClientDaoImpl())
                        .contractorDao(new ContractorDaoImpl())
                        .contractorServiceDao(new ContractorServiceDaoImpl())
                        .guestDao(new GuestDaoImpl())
                        .portfolioDao(new PortfolioDaoImpl())
                        .projectDao(new ProjectDaoImpl())
                        .reportDao(new ReportDaoImpl())
                        .taskDao(new TaskDaoImpl())
                        .usedContractorDao(new UsedContractorDaoImpl())
                        .projectClientDao(new ProjectClientDaoImpl()).build();
            }
            throw new IllegalStateException(bundle.getString("mainApplication.ex.controllerFactory") + " " + param);
        });
        DBHelper.initConnection(user,password);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 820);
        stage.setTitle(bundle.getString("mainApplication.title"));
        stage.setScene(scene);
        stage.show();
    }
    @SneakyThrows
    @Test
    @DisplayName("Создание клиента")
    void addClient(){
        waitFor(10,TimeUnit.SECONDS,() -> lookup("#btnAdd").tryQuery().isPresent());
        clickOn("#btnAdd");
        waitFor(5,TimeUnit.SECONDS,() -> lookup("#tfName").tryQuery().isPresent());
        clickOn("#tfName");
        write("Иван");
        clickOn("#tfLastname");
        write("Катамаранов");
        clickOn("#tfMiddleName");
        write("Иванович");
        clickOn("#tfMail");
        write("ivan.katamaranov@gmail.com");
        clickOn("#tfPhone");
        write("79618887766");
        clickOn("#male");
        clickOn("#tfSeria");
        write("1234");
        clickOn("#tfNumber");
        write("567890");
        clickOn("#dpWhenIssued");
        write("15.05.2023");
        type(KeyCode.ENTER);
        clickOn("#dpDateOfRegistration");
        write("20.06.2023");
        clickOn("#dpDateOfRegistration");
        type(KeyCode.ENTER);
        clickOn("#tfWhoIssued");
        write("Отдел УФМС России по г. Москве, район Арбат");
        clickOn("#tfPlaceOfBirth");
        write("г. Москва Россия");
        clickOn("#tfAdressOfRegistration");
        write("г. Москва, ул. Тверская, д. 1, кв. 10");
        clickOn("#ok");
        waitFor(5,TimeUnit.SECONDS,() -> lookup("#clientsTable").tryQuery().isPresent());
        TableView<Client> clientTable = lookup("#clientsTable").query();
        boolean haveClient = false;
        for(Client client:clientTable.getItems()){
            if(client.getName().equals("Иван")
                    && client.getLastname().equals("Катамаранов")
                    && client.getMiddleName().equals("Иванович")
                    && client.getEmail().equals("ivan.katamaranov@gmail.com")
                    && client.getPhoneNumber().equals("79618887766")
                    && client.getGender().equals("М")
                    && client.getSeries().equals("1234")
                    && client.getNumber().equals("567890")
                    && client.getWhenIssued().equals(LocalDate.of(2023,5,15))
                    && client.getDateOfRegistration().equals(LocalDate.of(2023,6,20))
                    && client.getIssuedBy().equals("Отдел УФМС России по г. Москве, район Арбат")
                    && client.getPlaceOfBirth().equals("г. Москва Россия")
                    && client.getRegistrationAdress().equals("г. Москва, ул. Тверская, д. 1, кв. 10")){
                haveClient = true;
                break;
            }
        }
        assertThat(haveClient).isTrue();
        ClientDao clientDao = new ClientDaoImpl();
        List<Client> found = (List<Client>) clientDao.search("Иван", "Катамаранов", "Иванович");
        if (!found.isEmpty()) {
            clientsToClean.add(found.get(0));
        }
    }
    @SneakyThrows
    @Test
    @DisplayName("Создание проекта")
    void addProject(){
        waitFor(10,TimeUnit.SECONDS,() -> lookup("#tabPane").tryQuery().isPresent());
        TabPane tabPane = lookup("#tabPane").query();
        Tab projectTab = tabPane.getTabs().get(1);
        tabPane.getSelectionModel().select(projectTab);
        clickOn("#createProject");
        waitFor(3,TimeUnit.SECONDS,() -> lookup("#createProject").tryQuery().isPresent());
        clickOn("#createProject");
        waitFor(4,TimeUnit.SECONDS,() -> lookup("#dpDate").tryQuery().isPresent());
        clickOn("#dpDate");
        write("15.07.2025");
        type(KeyCode.ENTER);
        clickOn("#cmbTime");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#taWishes");
        write("Хотим свадьбу на природе, живой оркестр, фотографа, ведущего-тамаду");
        clickOn("#cmbStatus");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#cmbGroom");
        for (int i = 0; i < 4; i++) {
            type(KeyCode.DOWN);
        }
        type(KeyCode.ENTER);
        clickOn("#cmbBride");
        for (int i = 0; i < 6; i++) {
            type(KeyCode.DOWN);
        }
        type(KeyCode.ENTER);
        clickOn("#ok");
        Thread.sleep(2000);
        boolean haveProject = false;
        waitFor(5,TimeUnit.SECONDS,() -> lookup("#projectsTable").tryQuery().isPresent());
        TableView<Project> projectTable = lookup("#projectsTable").query();
        Project foundedProject = null;
        for(Project project:projectTable.getItems()){
            if(project.getWeddingDate().equals(LocalDateTime.of((LocalDate.of(2025,7,15)),(LocalTime.of(11,30)) ))
                && project.getWishesForWedding().equals("Хотим свадьбу на природе, живой оркестр, фотографа, ведущего-тамаду")
                && project.getStatus().equals("ожидает выполнения")){
                foundedProject = project;
                haveProject = true;
                break;
            }
        }
        boolean groomFound = foundedProject.getProjectClients().stream()
                .anyMatch(pc -> pc.getRole().equals("жених")
                        && pc.getClient().getName().equals("Михаил")
                        && pc.getClient().getLastname().equals("Громов")
                        && pc.getClient().getMiddleName().equals("Андреевич"));
        boolean brideFound = foundedProject.getProjectClients().stream()
                .anyMatch(pc -> pc.getRole().equals("невеста")
                        && pc.getClient().getName().equals("Елена")
                        && pc.getClient().getLastname().equals("Романова")
                        && pc.getClient().getMiddleName().equals("Дмитриевна"));
        assertThat(groomFound).isTrue();
        assertThat(brideFound).isTrue();
        assertThat(haveProject).isTrue();
        if (haveProject) {
            projectsToClean.add(foundedProject);
        }
    }
    @SneakyThrows
    @Test
    @DisplayName("Создание гостя без имени")
    void addGuestWithoutName() {
        waitFor(10,TimeUnit.SECONDS,() -> lookup("#tabPane").tryQuery().isPresent());
        TabPane tabPane = lookup("#tabPane").query();
        Tab projectTab = tabPane.getTabs().get(3);
        tabPane.getSelectionModel().select(projectTab);
        waitFor(3,TimeUnit.SECONDS,() -> lookup("#tfLastnameGuest").tryQuery().isPresent());
        clickOn("#tfLastnameGuest");
        write("Петрова");
        clickOn("#tfMiddleNameGuest");
        write("Ильинична");
        clickOn("#btnCreateGuest");
        Label info = lookup("#lbInfoGuest").query();
        boolean haveWarning = info.getText().equals(bundle.getString("mainController.guest.create.info.fillTextFields"));
        assertThat(haveWarning).isTrue();
    }
    @SneakyThrows
    @Test
    @DisplayName("Добавление гостя в проект, в котором он уже есть")
    void addAlreadyExistGuestToProject(){
        waitFor(10,TimeUnit.SECONDS,() -> lookup("#tabPane").tryQuery().isPresent());
        TabPane tabPane = lookup("#tabPane").query();
        Tab projectTab = tabPane.getTabs().get(3);
        tabPane.getSelectionModel().select(projectTab);
        waitFor(3,TimeUnit.SECONDS,() -> lookup("#guestTable").tryQuery().isPresent());
        interact(() -> {
            TableView<Guest> guestTable = lookup("#guestTable").query();
            guestTable.getSelectionModel().select(1);
            guestTable.getFocusModel().focus(1);
            guestTable.requestFocus();
        });
        Thread.sleep(1200);
        clickOn("#cmbProjectGuest");
        type(KeyCode.DOWN);
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        clickOn("#btnAddToProject");
        DialogPane dialogPane = lookup(".dialog-pane").query();
        String contentText = dialogPane.getContentText();
        String message = dialogPane.getContentText();
        boolean haveWarning = message.equals(bundle.getString("mainController.ex.guest.duplicateFionProject"));
        assertThat(haveWarning).isTrue();
    }

}
