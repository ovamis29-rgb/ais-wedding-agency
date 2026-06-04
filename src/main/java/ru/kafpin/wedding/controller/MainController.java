package ru.kafpin.wedding.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Builder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.MainApplication;
import ru.kafpin.wedding.dao.*;
import ru.kafpin.wedding.model.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.*;

@Builder
public class MainController {
    private ResourceBundle bundle;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private TextField tfNewFileNamePortfolio;
    @FXML
    private ComboBox<Contractor> cmbContractorPortfolio;
    @FXML
    private TextArea taDescriptionPortfolio;
    @FXML
    private TableView<Portfolio> portfolioTable;
    @FXML
    private TableColumn<Portfolio, String> columnPortfolioDescription;
    @FXML
    private ImageView portfolioImageView;
    @FXML
    private Label lbInfoPortfolio;
    @FXML
    private TableColumn<Task,String> columnPriceTask;
    @FXML
    private TableView<ContractorService> servicesTable;
    @FXML
    private TableColumn<ContractorService,String> columnDescriptionService;
    @FXML
    private TableColumn<ContractorService,BigDecimal> columnPriceService;
    @FXML
    private TableColumn<ContractorService,BigDecimal> columnPrepaymentService;
    @FXML
    private TextField tfLinkPortfolio;
    @FXML
    private TextField tfDescriptionService;
    @FXML
    private TextField tfPriceService;
    @FXML
    private TextField tfPrepaymentService;
    @FXML
    private Label lbInfoContractor;
    @FXML
    private Label lbInfoGuest;
    @FXML
    private ComboBox<Project> cmbProjectGuest;
    @FXML
    private TextField tfContractorName;
    @FXML
    private TextField tfContractorLastname;
    @FXML
    private TextField tfContractorPhone;
    @FXML
    private TextField tfContractorMail;
    @FXML
    private TableView<Contractor>  contractorsTable;
    @FXML
    private TableColumn<Contractor,String> columnContractorName;
    @FXML
    private TableColumn<Contractor,String> columnContractorLastname;
    @FXML
    private TableColumn<Contractor,String> columnContractorMail;
    @FXML
    private TableColumn<Contractor,String> columnContractorPhone;
    @FXML
    private TextField tfNameGuest;
    @FXML
    private TextField tfLastnameGuest;
    @FXML
    private TextField tfMiddleNameGuest;
    @FXML
    private TableView<Guest> guestTable;
    @FXML
    private TableColumn<Guest,String> columnNameGuest;
    @FXML
    private TableColumn<Guest,String> columnLastnameGuest;
    @FXML
    private TableColumn<Guest,String> columnMiddleNameGuest;
    @FXML
    private Label lbInfoReports;
    @FXML
    private TableView<Report> reportsTable;
    @FXML
    private TableColumn<Report, BigDecimal> columnTotalBudgetReport;
    @FXML
    private TableColumn<Report,Long> columnAmountOfGuestsReport;
    @FXML
    private TableColumn<Report,Long> columnCompletedTasks;
    @FXML
    private ComboBox<Project> cmbProjectReport;
    @FXML
    private TableColumn<Task,Integer> columnPriorityTask;
    @FXML
    private TableColumn<Task,String> columnGoalTask;
    @FXML
    private TableColumn<Task,String> columnStatusTask;
    @FXML
    private TableColumn<Task,LocalDateTime> columnCompleteDateTask;
    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TextField tfTaskGoal;
    @FXML
    private ComboBox<String> cmbTaskStatus;
    @FXML
    private Label lbInfoClients;
    @FXML
    private Label lbInfoProjects;
    @FXML
    private ComboBox<LocalTime> cmbTimeProject;
    @FXML
    private DatePicker dpProject;
    @FXML
    private TableColumn<Project,String> columnStatusProject;
    @FXML
    private TableColumn<Project,String> columnRequirementsProject;
    @FXML
    private TableColumn<Project, LocalDateTime> columnDateProject;
    @FXML
    private TableView<Project> projectsTable;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField tfNameClient;
    @FXML
    private TextField tfLastnameClient;
    @FXML
    private TextField tfMiddleNameClient;
    @FXML
    private Label lbInfoClient;
    @FXML
    private TableColumn<Client,String> columnNameClient;
    @FXML
    private TableColumn<Client,String> columnLastnameClient;
    @FXML
    private TableColumn<Client,String> columnMiddleNameClient;
    @FXML
    private TableView<Client> clientsTable;
    ClientDao clientDao;
    ContractorDao contractorDao;
    ContractorServiceDao contractorServiceDao;
    GuestDao guestDao;
    PortfolioDao portfolioDao;
    ProjectDao projectDao;
    ReportDao reportDao;
    TaskDao taskDao;
    UsedContractorDao usedContractorDao;
    ProjectClientDao projectClientDao;

    private final ObservableList<Client> clients = FXCollections.observableArrayList();
    private final ObservableList<Project> projects = FXCollections.observableArrayList();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final ObservableList<Report> reports = FXCollections.observableArrayList();
    private final ObservableList<Guest> guests = FXCollections.observableArrayList();
    private final ObservableList<Contractor> contractors = FXCollections.observableArrayList();
    private final ObservableList<ContractorService> services = FXCollections.observableArrayList();
    private final ObservableList<Portfolio> portfolios = FXCollections.observableArrayList();
    private final ObservableList<Project> projectsAll = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        logger.info("Инициализация MainController");
        bundle = ResourceBundle.getBundle("main", Locale.getDefault());
        clients.addAll(clientDao.findAll());
        logger.info("Загружено {} клиентов",clients.size());
        columnNameClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnLastnameClient.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnMiddleNameClient.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        clientsTable.setItems(clients);
        clientsTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldclient, current) -> {
            if(current!=null){
                tfNameClient.setText(current.getName());
                tfLastnameClient.setText(current.getLastname());
                tfMiddleNameClient.setText(current.getMiddleName());
            }
        }));
        projects.addAll(projectDao.findAll());
        logger.info("Загружено {} проектов",projects.size());
        columnStatusProject.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnRequirementsProject.setCellValueFactory(new PropertyValueFactory<>("wishesForWedding"));
        columnDateProject.setCellValueFactory(new PropertyValueFactory<>("weddingDate"));
        projectsTable.setItems(projects);
        projectsTable.getSelectionModel().selectedItemProperty().addListener(((observableValue,oldproject,current) -> {
            if(current!=null){
                dpProject.setValue(current.getWeddingDate().toLocalDate());
                cmbTimeProject.setValue(current.getWeddingDate().toLocalTime());
                tasks.clear();
                tasks.addAll(taskDao.findByProject(projectsTable.getSelectionModel().getSelectedItem()));
                logger.info("Найдено {} задач проекта",tasks.size());
                tasksTable.setItems(tasks);
            }
        }));
        columnGoalTask.setCellValueFactory(new PropertyValueFactory<>("goal"));
        columnPriorityTask.setCellValueFactory(new PropertyValueFactory<>("priority"));
        columnCompleteDateTask.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        columnStatusTask.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnPriceTask.setCellValueFactory(new PropertyValueFactory<>("price"));
        tasksTable.setPlaceholder(new Label(bundle.getString("mainController.taskTable.placeHolder")));
        tasksTable.getSelectionModel().selectedItemProperty().addListener(((observableValue,oldtask,current) -> {
            if(current!=null){
                tfTaskGoal.setText(current.getContractorService().getService());
                cmbTaskStatus.setValue(current.getStatus());
            }
        }));
        ObservableList<String> statuses = FXCollections.observableArrayList(
                bundle.getString("mainApplication.cmb.status.wait"),
                bundle.getString("mainApplication.cmb.status.inproccess"),
                bundle.getString("mainApplication.cmb.status.complited"),
                bundle.getString("mainApplication.cmb.status.cancelled")
        );
        cmbTaskStatus.setItems(statuses);
        columnDescriptionService.setCellValueFactory(new PropertyValueFactory<>("service"));
        columnPriceService.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnPrepaymentService.setCellValueFactory(new PropertyValueFactory<>("prepayment"));
        servicesTable.setPlaceholder(new Label(bundle.getString("mainController.servicesTable.placeHolder")));
        servicesTable.getSelectionModel().selectedItemProperty().addListener(((observableValue,oldservice,current)  -> {
            if(current!=null){
                tfDescriptionService.setText(current.getService());
                tfPriceService.setText(String.valueOf(current.getPrice()));
                tfPrepaymentService.setText(String.valueOf(current.getPrepayment()));
            }
        }));
        reports.addAll(reportDao.findAll());
        logger.info("Найдено {} отчетов",reports.size());
        columnTotalBudgetReport.setCellValueFactory(new PropertyValueFactory<>("totalBudget"));
        columnAmountOfGuestsReport.setCellValueFactory(new PropertyValueFactory<>("amountOfGuests"));
        columnCompletedTasks.setCellValueFactory(new PropertyValueFactory<>("completedTasksCount"));
        reportsTable.setItems(reports);
        reportsTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldReport, current) -> {
            if(current!=null){
                cmbProjectReport.setValue(current.getProject());
            }
        }));
        guests.addAll(guestDao.findAll());
        logger.info("Найдено {} гостей",guests.size());
        columnNameGuest.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnLastnameGuest.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnMiddleNameGuest.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        guestTable.setItems(guests);
        guestTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldguest, current) -> {
            if(current!=null){
                tfNameGuest.setText(current.getName());
                tfLastnameGuest.setText(current.getLastname());
                tfMiddleNameGuest.setText(current.getMiddleName());
            }
        }));
        contractors.addAll(contractorDao.findAll());
        logger.info("Найдено {} подрядчиков",guests.size());
        columnContractorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnContractorLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        columnContractorMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnContractorPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        contractorsTable.setItems(contractors);
        cmbContractorPortfolio.setItems(contractors);
        contractorsTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldContractor, current) -> {
            if(current!=null){
                tfContractorName.setText(current.getName());
                tfContractorLastname.setText(current.getLastname());
                tfContractorMail.setText(current.getEmail());
                tfContractorPhone.setText(current.getPhoneNumber());
                services.clear();
                services.addAll(contractorServiceDao.findByContractor(current));
                logger.info("Найдено {} услуг подрядчика",services.size());
                servicesTable.setItems(services);
            }
        }));
        portfolios.addAll(portfolioDao.findAll());
        logger.info("Найдено {} портфолио",portfolios.size());
        columnPortfolioDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        portfolioTable.setPlaceholder(new Label(bundle.getString("mainController.portfolioTable.placeHolder")));
        portfolioTable.setItems(portfolios);
        portfolioTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldPortfolio, currentPortfolio) -> {
            if (currentPortfolio != null) {
                taDescriptionPortfolio.setText(currentPortfolio.getDescription());
                tfLinkPortfolio.setText(currentPortfolio.getImgUrl());
                cmbContractorPortfolio.setValue(currentPortfolio.getContractor());
                if (currentPortfolio.getImgUrl() != null && !currentPortfolio.getImgUrl().isEmpty()) {
                    String basePath = System.getProperty("user.dir");
                    String imgPath = basePath + "/" + currentPortfolio.getImgUrl();
                    File imageFile = new File(imgPath);
                    if(imageFile.exists()){
                        String name = imageFile.getName();
                        String nameWithoutExt = name.replaceFirst("\\.[^.]*$", "");
                        tfNewFileNamePortfolio.setText(nameWithoutExt);
                        try{
                            Image img = new Image(imageFile.toURI().toString());
                            portfolioImageView.setImage(img);
                        } catch (Exception e) {
                            logger.error("Ошибка загрузки изображения");
                            portfolioImageView.setImage(null);
                            lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.info.nonImg"));
                        }
                    }
                }
            }else {
                taDescriptionPortfolio.clear();
                tfLinkPortfolio.clear();
                portfolioImageView.setImage(null);
                tfNewFileNamePortfolio.clear();
            }
        }));
        clientsTable.setPlaceholder(new Label(bundle.getString("mainController.clientTable.placeHolder")));
        projectsTable.setPlaceholder(new Label(bundle.getString("mainController.projectTable.placeHolder")));
        reportsTable.setPlaceholder(new Label(bundle.getString("mainController.reportTable.placeHolder")));
        guestTable.setPlaceholder(new Label(bundle.getString("mainController.guestTable.placeHolder")));
        contractorsTable.setPlaceholder(new Label(bundle.getString("mainController.contractorTable.placeHolder")));
        ObservableList<LocalTime> times = FXCollections.observableArrayList(
                LocalTime.of(10,0),
                LocalTime.of(11,30),
                LocalTime.of(13,0),
                LocalTime.of(14,30),
                LocalTime.of(16,0),
                LocalTime.of(17,30)
        );
        cmbTimeProject.setItems(times);
        cmbProjectReport.setItems(projectsAll);
        projectsAll.addAll(projectDao.findAll());
        cmbProjectGuest.setItems(projectsAll);
        tfNameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfNameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfLastnameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfLastnameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfMiddleNameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfMiddleNameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfNameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfNameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfLastnameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfLastnameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfMiddleNameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfMiddleNameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfContractorName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\d\\s\\-'\\.,\"«»()]*$")) {
                tfContractorName.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\d\\s\\-'\\.,\"«»()]", ""));
            }
        });
        tfContractorLastname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfContractorLastname.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfContractorMail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("[^\\w.%+-@]")) {
                tfContractorMail.setText(newValue.replaceAll("[^\\w.%+-@]", "").replaceAll("\\s", ""));
            }
        });
        tfContractorPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[0-9\\+\\s\\-\\(\\)]*$")) {
                tfContractorPhone.setText(newValue.replaceAll("[^0-9\\+\\s\\-\\(\\)]", ""));
            }
        });

        tfNameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfNameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfLastnameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfLastnameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfMiddleNameClient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfMiddleNameClient.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfNameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfNameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfLastnameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfLastnameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfMiddleNameGuest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfMiddleNameGuest.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfDescriptionService.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\d\\s\\-.,;:!?'\"]*$")) {
                tfDescriptionService.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\d\\s\\-.,;:!?'\"]", ""));
            }
        });
        tfPriceService.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[0-9.]*$")) {
                tfPriceService.setText(newValue.replaceAll("[^0-9.]", ""));
            } else if (newValue != null) {
                String result = newValue.replaceAll("\\.(?=.*\\.)", "");
                if (!result.equals(newValue)) {
                    tfPriceService.setText(result);
                }
            }
        });
        tfPrepaymentService.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[0-9.]*$")) {
                tfPrepaymentService.setText(newValue.replaceAll("[^0-9.]", ""));
            } else if (newValue != null) {
                String result = newValue.replaceAll("\\.(?=.*\\.)", "");
                if (!result.equals(newValue)) {
                    tfPrepaymentService.setText(result);
                }
            }
        });
        taDescriptionPortfolio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\d\\s\\-.,;:!?'\"]*$")) {
                taDescriptionPortfolio.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\d\\s\\-.,;:!?'\"]", ""));
            }
        });
        tfNewFileNamePortfolio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\d\\-_]*$")) {
                tfNewFileNamePortfolio.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\d\\-_]", ""));
            }
        });
    }
    @FXML
    public void clientSearch() {
        logger.info("Поиск клиента");
        lbInfoClient.setText("");
        String name = tfNameClient.getText().trim();
        String lastname = tfLastnameClient.getText().trim();
        String middleName = tfMiddleNameClient.getText().trim();
        clients.clear();
        try {
            clients.addAll(clientDao.search(name, lastname, middleName));
            logger.info("Успешно найдено {} клиентов",clients.size());
        }catch (RuntimeException e) {
            logger.error("Ошибка поиска клиентов",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.client.search"), ButtonType.OK);
            alert.showAndWait();
        }
        if (clients.isEmpty()) {
            lbInfoClient.setText(bundle.getString("mainController.client.info.nonSearch"));
            logger.info("Клиенты по заданным критериям не найдены");
        }
        tfNameClient.clear();
        tfLastnameClient.clear();
        tfMiddleNameClient.clear();
    }

    @FXML
    public void relatedProjects() {
        logger.info("Поиск связанных с клиентом проектом");
        lbInfoClient.setText("");
        Client client = clientsTable.getSelectionModel().getSelectedItem();
        if(client!=null){
            List<Project> list = null;
            try {
                list = (List<Project>) projectDao.relatedProjects(client);
            }catch (RuntimeException e) {
                logger.error("Ошибка поиска связанных проектов с клиентом",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.lient.relatedProjects"), ButtonType.OK);
                alert.showAndWait();
            }
            if (list!= null && !list.isEmpty()){
                tabPane.getSelectionModel().select(1);
                projects.clear();
                projects.addAll(list);
                logger.info("Успешно найдено {} связанных проектов",projects.size());
                dpProject.setValue(null);
                cmbTimeProject.setValue(null);
                lbInfoClient.setText("");
            }else{
                lbInfoClient.setText(bundle.getString("mainController.client.info.noProjects"));
            }
        }else{
            lbInfoClient.setText(bundle.getString("mainController.client.info.chooseClient"));
        }
    }
    @FXML
    public void findProjectByDate() {
        logger.info("Поиск проекта по дате");
        lbInfoProjects.setText("");
        LocalDate date = dpProject.getValue();
        LocalTime time = cmbTimeProject.getValue();
        if(date==null || time==null){
            projects.clear();
            projects.addAll(projectDao.findAll());
            dpProject.setValue(null);
            cmbTimeProject.setValue(null);
            return;
        }
        try{
            LocalDateTime dateTime = LocalDateTime.of(date,time);
            Optional<Project> optional = projectDao.findByDate(dateTime);
            Project project = optional.orElse(null);
            if(project==null){
                lbInfoProjects.setText(bundle.getString("mainController.project.info.noSuchProjectDate"));
                logger.info("Не найдено проектов с датой {}",dateTime);
                return;
            }
            projects.clear();
            projects.add(project);
            logger.info("Найден проект с датой {} и id = {}",dateTime,project.getId());
        } catch (RuntimeException e) {
            logger.error("Ошибка поиска проекта по дате: date={}, time={}", date, time, e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.project.searchByDate"), ButtonType.OK);
            alert.showAndWait();
        }
        dpProject.setValue(null);
        cmbTimeProject.setValue(null);
    }
    @FXML
    public void deleteClient() {
        logger.info("Удаление клиента");
        lbInfoClient.setText("");
        Client client = clientsTable.getSelectionModel().getSelectedItem();
        if (client == null) {
            lbInfoClient.setText(bundle.getString("mainController.client.info.chooseClient"));
            return;
        }
        try {
            clientDao.delete(client);
            logger.info("Успешно удален клиент с id = {}",client.getId());
            clients.clear();
            tfNameClient.clear();
            tfLastnameClient.clear();
            tfMiddleNameClient.clear();
            clients.addAll(clientDao.findAll());
        }catch (RuntimeException e) {
            logger.error("Ошибка удаления клиента",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.client.delete"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    private void showDialog(String fxmlPath, String title, Object data){
        logger.info("Открытие диалогового окна: fxml={}, title={}", fxmlPath, title);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlPath),bundle);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tabPane.getScene().getWindow());
        stage.setTitle(title);
        try {
            if("client-view.fxml".equals(fxmlPath) && data instanceof Client){
                ClientController controller = ClientController.builder().client((Client) data).stage(stage).bundle(bundle).build();
                fxmlLoader.setController(controller);
                logger.info("Открыто диалоговое окно '{}': clientId = {}",title,((Client) data).getId());
            }
            else if("project-view.fxml".equals(fxmlPath) && data instanceof Project){
                ProjectController controller = ProjectController.builder().project((Project) data).clientDao(clientDao).bundle(bundle).stage(stage).build();
                fxmlLoader.setController(controller);
                logger.info("Открыто диалоговое окно '{}': projectId = {}",title,((Project) data).getId());
            } else if ("task-view.fxml".equals(fxmlPath) && data instanceof List) {
                List<?> list = (List<?>) data;
                Project project = (Project) list.getFirst();
                Task task = (Task) list.getLast();
                TaskController controller = TaskController.builder().project(project).task(task)
                        .contractorDao(contractorDao).contractorServiceDao(contractorServiceDao).bundle(bundle).stage(stage).build();
                fxmlLoader.setController(controller);
                logger.info("Открыто диалоговое окно '{}': projectId = {}",title,project.getId());
            }
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Ошибка загрузки FXML диалога: fxml={}", fxmlPath, e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.showDialog"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void clientAdd() {
        logger.info("Добавление клиента");
        Client client = new Client();
        showDialog("client-view.fxml", bundle.getString("mainController.client.info.add"), client);
        if (client.getName() != null && !client.getName().trim().isEmpty() &&
                client.getLastname() != null && !client.getLastname().trim().isEmpty() &&
                client.getDateOfRegistration() != null &&
                client.getPlaceOfBirth() != null && !client.getPlaceOfBirth().trim().isEmpty() &&
                client.getSeries() != null && !client.getSeries().trim().isEmpty() &&
                client.getNumber() != null && !client.getNumber().trim().isEmpty() &&
                client.getIssuedBy() != null && !client.getIssuedBy().trim().isEmpty() &&
                client.getWhenIssued() != null &&
                client.getRegistrationAdress() != null && !client.getRegistrationAdress().trim().isEmpty()) {
            try {
                clientDao.save(client);
                logger.info("Успешно добавлен клиент: id={} {} {}",client.getId(),client.getName(),client.getLastname());
                clients.add(client);
            } catch (RuntimeException e) {
                logger.error("Ошибка добавления клиента",e);
                String message = bundle.getString("mainController.ex.client.save");
                if(e.getMessage().contains("client_phone_number_key")) message = bundle.getString("mainController.ex.client.save.alreadyExistphone");
                if(e.getMessage().contains("client_email_key")) message = bundle.getString("mainController.ex.client.save.alreadyExistemail");
                if(e.getMessage().contains("fio_client")) message = bundle.getString("mainController.ex.client.save.alreadyExistfio");
                Alert alert = new Alert(Alert.AlertType.ERROR,message, ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void editClient() {
        logger.info("Редактирование клиента");
        lbInfoClient.setText("");
        Client client = clientsTable.getSelectionModel().getSelectedItem();
        if(client==null){
            lbInfoClient.setText(bundle.getString("mainController.client.info.chooseClient"));
            return;
        }
        showDialog("client-view.fxml", bundle.getString("mainController.client.add"), client);
        if (client.getName() != null && !client.getName().trim().isEmpty() &&
                client.getLastname() != null && !client.getLastname().trim().isEmpty() &&
                client.getDateOfRegistration() != null &&
                client.getPlaceOfBirth() != null && !client.getPlaceOfBirth().trim().isEmpty() &&
                client.getSeries() != null && !client.getSeries().trim().isEmpty() &&
                client.getNumber() != null && !client.getNumber().trim().isEmpty() &&
                client.getIssuedBy() != null && !client.getIssuedBy().trim().isEmpty() &&
                client.getWhenIssued() != null &&
                client.getRegistrationAdress() != null && !client.getRegistrationAdress().trim().isEmpty()) {
            try{
                clientDao.update(client);
                logger.info("Успешно отредактирован клиент: id={} {} {}",client.getId(),client.getName(),client.getLastname());
                clientsTable.refresh();
            }catch (RuntimeException e) {
                logger.error("Ошибка редактирования клиента",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.client.edit"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void searchReport() {
        logger.info("Поиск отчета");
        lbInfoReports.setText("");
        Project project = cmbProjectReport.getValue();
        if(project==null) {
            reports.clear();
            reports.addAll(reportDao.findAll());
        }else{
            try{
                Optional<Report> tmp = reportDao.findByProjectDate(project.getWeddingDate());
                Report report = tmp.orElse(null);
                reports.clear();
                reports.add(report);
                logger.info("Успешно найден отчет по проекту: id = {} date = {}",project.getId(),project.getWeddingDate());
            }catch (RuntimeException e) {
                logger.error("Ошибка поиска отчета",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.report.search"), ButtonType.OK);
                alert.showAndWait();
            }
        }
        cmbProjectReport.setValue(null);
    }
    @FXML
    public void deleteReport() {
        logger.info("Удаление отчета");
        lbInfoReports.setText("");
        Report report = reportsTable.getSelectionModel().getSelectedItem();
        if(report==null) {
            lbInfoReports.setText(bundle.getString("mainController.report.info.chooseReport"));
        }else{
            try {
                reportDao.delete(report);
                logger.info("Успешно удален отчет: {}",report.getId());
                reports.clear();
                reports.addAll(reportDao.findAll());
            }catch (RuntimeException e) {
                logger.error("Ошибка удаления отчета {}",report.getId(),e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.report.delete"), ButtonType.OK);
                alert.showAndWait();
            }
        }
        cmbProjectReport.setValue(null);
    }
    @FXML
    public void inDetailsReport() {
        logger.info("Показ отчета по проекту");
        lbInfoProjects.setText("");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        Optional<Report> tmp = reportDao.findByProjectDate(project.getWeddingDate());
        Report report = tmp.orElse(null);
        if(report!=null){
            tabPane.getSelectionModel().select(2);
            try{
                LocalDate date = project.getWeddingDate().toLocalDate();
                LocalTime time = project.getWeddingDate().toLocalTime();
                Optional<Report> finded = reportDao.findByProjectDate(LocalDateTime.of(date,time));
                Report findedReport = finded.orElse(null);
                reports.clear();
                reports.add(report);
            }catch (RuntimeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.report.search"), ButtonType.OK);
                alert.showAndWait();
            }
        }else{
            lbInfoProjects.setText(bundle.getString("mainController.project.info.noReport"));
        }
    }
    @FXML
    public void createReport() {
        logger.info("Создание отчета");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        Report createdReport = reportDao.findByProjectDate(project.getWeddingDate()).orElse(null);
        if (project != null && createdReport==null) {
            try{
                Report report = reportDao.save(project.getId());
                logger.info("Успешно создан отчет id = {} по проекту id = {}",report.getId(),project.getId());
                reports.add(report);
                tabPane.getSelectionModel().select(2);
                reportsTable.getSelectionModel().select(0);
                reportsTable.getFocusModel().focus(0);
                reportsTable.requestFocus();
            }catch (RuntimeException e) {
                logger.error("Ошибка создания отчета",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.report.save"), ButtonType.OK);
                alert.showAndWait();
            }
        }else if (createdReport!=null){
            lbInfoProjects.setText(bundle.getString("mainController.project.info.alreadyHave"));
        }
    }
    @FXML
    public void regenerateReport() {
        logger.info("Перегенерация отчета клиента");
        lbInfoReports.setText("");
        Report report = reportsTable.getSelectionModel().getSelectedItem();
        if(report!=null){
            try{
                reportDao.update(report);
                logger.info("Успешно перегенерирован отчет по проекту: id = {}",report.getId());
                reportsTable.refresh();
                lbInfoReports.setText(bundle.getString("mainController.report.info.regenerate"));
            }catch (RuntimeException e) {
                logger.error("Ошибка обновления отчета",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.report.update"), ButtonType.OK);
                alert.showAndWait();
            }
        }else{
            lbInfoReports.setText(bundle.getString("mainController.report.info.chooseReport"));
        }
    }
    @FXML
    public void searchGuest() {
        logger.info("Поиск гостя");
        lbInfoGuest.setText("");
        String name = tfNameGuest.getText().trim();
        String lastname = tfLastnameGuest.getText().trim();
        String middleName = tfMiddleNameGuest.getText().trim();
        try{
            List<Guest> list = (List<Guest>) guestDao.search(name,lastname,middleName);
            if(list==null || list.isEmpty()){
                lbInfoGuest.setText(bundle.getString("mainController.guest.search.noSuchGuest"));
                guests.clear();
            }
            logger.info("Найдено {} гостей",list.size());
            guests.clear();
            guests.addAll(list);
        }catch (RuntimeException e) {
            logger.error("Ошибка поиска гостя",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.guest.search"), ButtonType.OK);
            alert.showAndWait();
        }
        tfNameGuest.clear();
        tfLastnameGuest.clear();
        tfMiddleNameGuest.clear();
        cmbProjectGuest.setValue(null);
    }

    @FXML
    public void projectContractors() {
        logger.info("Поиск подрядчиков проекта");
        lbInfoProjects.setText("");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if (project == null) {
            lbInfoProjects.setText(bundle.getString("mainController.project.info.chooseProject.toSeeContractors"));
            return;
        }
        List<Contractor> list = project.getContractors();
        if (list == null || list.isEmpty()) {
            lbInfoProjects.setText(bundle.getString("mainController.project.info.noConractorsYet"));
            contractors.clear();
            return;
        }
        logger.info("Найдено {} подрядчиков для проекта: id = {}",list.size(),project.getId());
        contractors.clear();
        contractors.addAll(list);
        tabPane.getSelectionModel().select(4);
    }
    @FXML
    public void deleteProject() {
        logger.info("Удаление проекта");
        lbInfoProjects.setText("");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if(project==null){
            lbInfoProjects.setText(bundle.getString("mainController.project.info.chooseProject"));
            return;
        }
        try {
            projectDao.delete(project);
            logger.info("Успешно удален проект: id = {}",project.getId());
            projects.clear();
            dpProject.setValue(null);
            cmbTimeProject.setValue(null);
            projects.addAll(projectDao.findAll());
        }catch (RuntimeException e) {
            logger.error("Ошибка удаления проекта",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.project.delete"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void deleteGuestFromProject() {
        logger.info("Удаление гостя из проекта");
        lbInfoGuest.setText("");
        Guest guest = guestTable.getSelectionModel().getSelectedItem();
        Project project = cmbProjectGuest.getValue();
        if (guest == null || project == null) {
            String missingFields = "";
            if (guest == null) missingFields += bundle.getString("mainController.project.missingField.guest") + " ";
            if (project == null) missingFields += bundle.getString("mainController.project.missingField.project") + " ";
            lbInfoGuest.setText(bundle.getString("mainController.project.missingField.choose") + " " + missingFields);
            return;
        } else {
            if(!guestDao.IsitProjectGuest(project,guest)){
                lbInfoGuest.setText(bundle.getString("mainController.project.guestsList"));
                return;
            }
            try {
                guestDao.DeleteFromProject(project,guest);
                logger.info("Успешно удален гость id = {} из проекта id = {}",guest.getId(),project.getId());
                lbInfoGuest.setText(bundle.getString("mainController.project.guestsList.delete.success"));
            } catch (RuntimeException e) {
                logger.error("Ошибка удаления гостя из проекта",e);
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        bundle.getString("mainController.ex.guest.deleteFromProject"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void projectCreate() {
        logger.info("Создание проекта");
        Project project = new Project();
        showDialog("project-view.fxml", bundle.getString("mainController.project.create"), project);
        if ( project != null && project.getWeddingDate() != null && project.getProjectClients()!=null &&
                !project.getProjectClients().isEmpty()) {
            try{
                project = projectDao.save(project);
                List<ProjectClient> clients = project.getProjectClients();
                clients.getFirst().setProject(project);
                clients.getLast().setProject(project);
                projectClientDao.save(clients.getFirst());
                projectClientDao.save(clients.getLast());
                logger.info("Успешно создан проект id = {}",project.getId());
                projects.add(project);
            }catch (RuntimeException e) {
                logger.error("Ошибка создания проекта",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.project.save"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void projectEdit() {
        logger.info("Редактирование проекта");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if (project == null) {
            lbInfoProjects.setText(bundle.getString("mainController.project.info.chooseProject"));
            return;
        }
        showDialog("project-view.fxml", bundle.getString("mainController.project.edit"), project);
        if (project != null && project.getWeddingDate() != null &&
                project.getProjectClients() != null && !project.getProjectClients().isEmpty()) {
            try {
                project = projectDao.update(project);
                List<ProjectClient> clients = project.getProjectClients();
                projectClientDao.update(clients.getFirst());
                projectClientDao.update(clients.getLast());
                logger.info("Успешно отредактирован проект: id = {}",project.getId());
                projectsTable.refresh();
            } catch (RuntimeException e) {
                logger.error("Ошибка редактирования проекта",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.project.edit"), ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            lbInfoProjects.setText(bundle.getString("mainController.project.info.incorrectData"));
        }
    }
    @FXML
    public void taskAdd() {
        logger.info("Добавление задачи в проект");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        Task task = new Task();
        if(project==null) {
            lbInfoProjects.setText(bundle.getString("mainController.project.info.chooseProject.toAddTask"));
            return;
        }
        List<Object> list = List.of(project, task);
        try {
            showDialog("task-view.fxml",bundle.getString("mainController.task.add"),list);
            if (task.getContractorService() == null) {
                logger.info("Пользователь отменил добавление задачи");
                return;
            }
            taskDao.save(task);
            taskDao.addTask(project,task);
            tasks.add(task);
            logger.info("Успешно создана задача id = {} для проекта id = {}",task.getId(),project.getId());
            project.getContractors().add(task.getContractorService().getContractor());
            UsedContractor usedContractor = new UsedContractor();
            usedContractor.setContractor(task.getContractorService().getContractor());
            usedContractor.setProject(project);
            usedContractorDao.save(usedContractor);
        }catch (RuntimeException e) {
            logger.error("Ошибка добавления задачи",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.addTask"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void taskEdit() {
        logger.info("Редактирование задачи");
        Task task = tasksTable.getSelectionModel().getSelectedItem();
        if(task==null){
            lbInfoProjects.setText(bundle.getString("mainController.task.info.chooseTask"));
            return;
        }else {
            try{
            Project project = task.getProject();
            List<Object> list = List.of(project,task);
            showDialog("task-view.fxml",bundle.getString("mainController.task.edit"),list);
            taskDao.update(task);
            logger.info("Успешно отредактирована задача: id = {}",task.getId());
            tasks.clear();
            tasks.addAll(taskDao.findByProject(project));
        }catch (RuntimeException e) {
                logger.error("Ошибка редактирования задачи",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.task.edit"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void taskDelete() {
        logger.info("Удаление задачи");
        lbInfoProjects.setText("");
        Task task = tasksTable.getSelectionModel().getSelectedItem();
        if (task == null) {
            lbInfoProjects.setText(bundle.getString("mainController.task.info.chooseTask"));
            return;
        }
        Project project = task.getProject();
        Contractor contractor = task.getContractorService().getContractor();
        try {
            taskDao.delete(task);
            logger.info("Успешно удалена задача: id = {}",task.getId());
            tasks.clear();
            tasks.addAll(taskDao.findAll());
        }catch (RuntimeException e) {
            logger.error("Ошибка удаления задача",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.task.delete"), ButtonType.OK);
            alert.showAndWait();
        }
        boolean contrHaveAnyTasks = contractorDao.contrHaveAnyTasks(project,contractor);
        try{
            if (!contrHaveAnyTasks && contractor != null){
                project.getContractors().remove(contractor);
                usedContractorDao.deleteByProjectAndContractor(project, contractor);
                logger.info("Подрядчик id = {} удален из проекта id = {}, за неимением в нем задач связанных с ним",contractor.getId(),project.getId());
            }
        }catch (RuntimeException e) {
            logger.error("Ошибка подрядчика из проекта",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.contractor.deleteFromProject"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void taskSearch() {
        logger.info("Поиск задачи");
        String goal = tfTaskGoal.getText().trim();
        String selectedStatus = cmbTaskStatus.getValue();
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if(project == null){
            lbInfoProjects.setText(bundle.getString("mainController.project.info.chooseProject"));
            return;
        }
        Map<String, String> statusMapping = new HashMap<>();
        statusMapping.put(bundle.getString("mainApplication.cmb.status.wait"), "ожидает выполнения");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.inproccess"), "в процессе");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.complited"), "завершено");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.cancelled"), "отменено");
        String statusForDb = null;
        if (selectedStatus != null && statusMapping.containsKey(selectedStatus)) {
            statusForDb = statusMapping.get(selectedStatus);
        }
        try {
            List<Task> list;
            if (!goal.isEmpty() || statusForDb != null) {
                list = (List<Task>) taskDao.search(goal, statusForDb, project);
            } else {
                list = (List<Task>) taskDao.findByProject(project);
            }
            tasks.clear();
            if (list != null && !list.isEmpty()) {
                tasks.addAll(list);
                logger.info("Найдено {} задач", list.size());
            } else {
                lbInfoProjects.setText(bundle.getString("mainController.task.search.nonFound"));
                logger.info("Задачи не найдены");
            }
        } catch (RuntimeException e) {
            logger.error("Ошибка поиска задачи", e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.task.search"), ButtonType.OK);
            alert.showAndWait();
        }
        tfTaskGoal.clear();
        cmbTaskStatus.setValue(null);
    }
    @FXML
    public void addGuestToProject() {
        logger.info("Добавление гостя в проект");
        lbInfoGuest.setText("");
        Guest guest = guestTable.getSelectionModel().getSelectedItem();
        Project project = cmbProjectGuest.getValue();
        if (guest == null) {
            lbInfoGuest.setText(bundle.getString("mainController.project.info.guestList.choose"));
            return;
        }
        if (project == null) {
            lbInfoGuest.setText(bundle.getString("mainController.project.info.chooseProject"));
            return;
        }
        try {
            guestDao.addGuestToProject(project, guest);
            logger.info("Гость успешно добавлен в проект: id = {}",project.getId());
            lbInfoGuest.setText(bundle.getString("mainController.project.guestsList.add"));
        } catch (RuntimeException e) {
            logger.error("Ошибка добавления гостя в проект", e);
            String message = bundle.getString("mainController.ex.guest.save");
            if(e.getMessage().contains("unique_project_guest")) {
                message = bundle.getString("mainController.ex.guest.duplicateFionProject");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        }
        tfNameGuest.clear();
        tfLastnameGuest.clear();
        tfMiddleNameGuest.clear();
        cmbProjectGuest.setValue(null);
    }
    @FXML
    public void createGuest() {
        logger.info("Создание гостя");
        lbInfoGuest.setText("");
        String name = tfNameGuest.getText().trim();
        String lastname = tfLastnameGuest.getText().trim();
        String middleName = tfMiddleNameGuest.getText().trim();
        Guest guest = new Guest();
        if(name.isEmpty() || lastname.isEmpty()) {
            lbInfoGuest.setText(bundle.getString("mainController.guest.create.info.fillTextFields"));
            return;
        }else{
            try{
                guest.setName(name);
                guest.setLastname(lastname);
                guest.setMiddleName(middleName);
                guestDao.save(guest);
                guests.add(guest);
                logger.info("Успешно создан гость id = {}",guest.getId());
            } catch (RuntimeException e) {
                logger.error("Ошибка создания гостя",e);
                String message = bundle.getString("mainController.ex.guest.save");
                if(e.getMessage().contains("fio_guest")) {
                    message = bundle.getString("mainController.ex.guest.duplicateFio");
                }
                Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
                alert.showAndWait();
            }
        }
        tfNameGuest.clear();
        tfLastnameGuest.clear();
        tfMiddleNameGuest.clear();
    }
    @FXML
    public void editGuest() {
        logger.info("Редактирование гостя");
        lbInfoGuest.setText("");
        String name = tfNameGuest.getText().trim();
        String lastname = tfLastnameGuest.getText().trim();
        String middleName = tfMiddleNameGuest.getText().trim();
        Guest guest = guestTable.getSelectionModel().getSelectedItem();
        if(guest == null){
            lbInfoGuest.setText(bundle.getString("mainController.guest.edit.chooseInTable"));
            return;
        }
        else if(name.isEmpty() || lastname.isEmpty()){
            lbInfoGuest.setText(bundle.getString("mainController.guest.create.info.fillTextFields"));
            return;
        }
        else {
            guest.setName(name);
            guest.setLastname(lastname);
            if(middleName != null) guest.setMiddleName(middleName);
            try{
                guestDao.update(guest);
                logger.info("Успешно отредактирован гость id = {}", guest.getId());
            } catch (RuntimeException e) {
                logger.error("Ошибка редактирования гостя", e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.guest.edit"), ButtonType.OK);
                alert.showAndWait();
            }
            guestTable.refresh();
            tfNameGuest.clear();
            tfLastnameGuest.clear();
            tfMiddleNameGuest.clear();
        }
    }
    @FXML
    public void deleteGuest() {
        logger.info("Удаление гостя");
        lbInfoGuest.setText("");
        Guest guest = guestTable.getSelectionModel().getSelectedItem();
        if(guest==null){
            lbInfoGuest.setText(bundle.getString("mainController.guest.chooseGuest"));
            return;
        }else{
            try{
                guestDao.delete(guest);
                logger.info("Успешно удален гость id = {}",guest.getId());
                guests.clear();
                guests.addAll(guestDao.findAll());
            } catch (RuntimeException e) {
                logger.error("Ошибка удаления гостя",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.guest.delete"), ButtonType.OK);
                alert.showAndWait();
            }
        }
        tfNameGuest.clear();
        tfLastnameGuest.clear();
        tfMiddleNameGuest.clear();
    }
    @FXML
    public void relatedGuests() {
        logger.info("Поиск гостей проекта");
        Project project = projectsTable.getSelectionModel().getSelectedItem();
        if(project==null) {
            lbInfoReports.setText(bundle.getString("mainController.project.info.searchGuest.chooseProject"));
            return;
        }
        try{
            List<Guest> list = (List<Guest>) guestDao.findByProject(project);
            if(list==null){
                lbInfoReports.setText(bundle.getString("mainController.project.info.searchGuest.noGuests"));
                logger.info("Не найдено гостей по данному проекту");
                return;
            }
            logger.info("Найдено {} гостей проекта id = {}",list.size(),project.getId());
            guests.clear();
            guests.addAll(list);
            tabPane.getSelectionModel().select(3);
            cmbProjectGuest.setValue(project);
        } catch (RuntimeException e) {
                logger.error("Ошибка поиска гостей проекта",e);
                Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.project.searchGuest"), ButtonType.OK);
                alert.showAndWait();
        }
    }
    @FXML
    public void searchContractor() {
        logger.info("Поиск подрядчика");
        lbInfoContractor.setText("");
        String phone = tfContractorPhone.getText().trim();
        String name = tfContractorName.getText().trim();
        String lastname = tfContractorLastname.getText().trim();
        if (phone.isEmpty() && name.isEmpty() && lastname.isEmpty()) {
            contractors.clear();
            contractors.addAll(contractorDao.findAll());
            tfContractorPhone.clear();
            tfContractorName.clear();
            tfContractorLastname.clear();
            tfContractorMail.clear();
            return;
        }
        try {
            List<Contractor> contractorList = (List<Contractor>) contractorDao.search(phone, name, lastname);
            if (contractorList == null || contractorList.isEmpty()) {
                lbInfoContractor.setText(bundle.getString("mainController.contractor.info.notFound"));
                contractors.clear();
            } else {
                contractors.clear();
                contractors.addAll(contractorList);
                logger.info("Найдено {} подрядчиков", contractorList.size());
                lbInfoContractor.setText("");
            }
        } catch (RuntimeException e) {
            logger.error("Ошибка поиска подрядчика", e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.contractor.search"), ButtonType.OK);
            alert.showAndWait();
        }
        tfContractorPhone.clear();
        tfContractorName.clear();
        tfContractorLastname.clear();
        tfContractorMail.clear();
    }
    @FXML
    public void addContractor() {
        logger.info("Создание подрядчика");
        lbInfoContractor.setText("");
        String name = tfContractorName.getText().trim();
        String lastname = tfContractorLastname.getText().trim();
        String phone = tfContractorPhone.getText().trim();
        String email = tfContractorMail.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            logger.warn("Попытка создать подрядчика без имени или телефона: name='{}', phone='{}'", name, phone);
            lbInfoContractor.setText(bundle.getString("mainController.contractor.add.fillTextFields"));
            return;
        }
        Contractor contractor = new Contractor();
        contractor.setName(name);
        contractor.setLastname(lastname);
        contractor.setPhoneNumber(phone);
        contractor.setEmail(email);
        try {
            contractorDao.save(contractor);
            contractors.add(contractor);
            lbInfoContractor.setText(bundle.getString("mainController.contractor.add.success"));
            logger.info("Успешно создан подрядчик id = {}",contractor.getId());
            tfContractorName.clear();
            tfContractorLastname.clear();
            tfContractorPhone.clear();
            tfContractorMail.clear();
        } catch (RuntimeException e) {
            logger.error("Ошибка создания подрядчика",e);
            String message = bundle.getString("mainController.ex.contractor.save");
            if(e.getMessage().contains("contractor_phone_number_key"))
                message = bundle.getString("mainController.ex.contractor.duplicatePhone");
            if(e.getMessage().contains("contractor_email_key"))
                message = bundle.getString("mainController.ex.contractor.duplicateEmail");
            if(e.getMessage().contains("fio_contractor"))
                message = bundle.getString("mainController.ex.contractor.duplicateFio");
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void editContractor() {
        logger.info("Редактирование подрядчика");
        lbInfoContractor.setText("");
        String name = tfContractorName.getText().trim();
        String lastname = tfContractorLastname.getText().trim();
        String phone = tfContractorPhone.getText().trim();
        String email = tfContractorMail.getText().trim();
        Contractor contractor = contractorsTable.getSelectionModel().getSelectedItem();
        if(contractor==null){
            lbInfoContractor.setText(bundle.getString("mainController.contractor.info.chooseContractor"));
            return;
        } else if(name.isEmpty() || phone.isEmpty()){
            lbInfoContractor.setText(bundle.getString("mainController.contractor.add.fillTextFields"));
            return;
        }
        contractor.setName(name);
        contractor.setLastname(lastname);
        contractor.setEmail(email);
        contractor.setPhoneNumber(phone);
        try{
            contractorDao.update(contractor);
            logger.info("Успешно отредактирован подрядчик id = {}",contractor.getId());
            contractorsTable.refresh();
        }catch (RuntimeException e) {
            logger.error("Ошибка редактирования подрядчика",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.contractor.edit"), ButtonType.OK);
            alert.showAndWait();
        }
        tfContractorName.clear();
        tfContractorLastname.clear();
        tfContractorMail.clear();
        tfContractorPhone.clear();
    }
    @FXML
    public void deleteContractor() {
        logger.info("Удаление подрядчика");
        lbInfoContractor.setText("");
        Contractor contractor = contractorsTable.getSelectionModel().getSelectedItem();
        if(contractor==null){
            lbInfoContractor.setText(bundle.getString("mainController.contractor.info.choose"));
            return;
        }
        try{
            Alert question = new Alert(Alert.AlertType.CONFIRMATION,
                    bundle.getString("mainController.alert.delete.contractor.confirmation"),
                    ButtonType.YES,ButtonType.NO);
            if(question.showAndWait().orElse(ButtonType.NO)!=ButtonType.YES){
                logger.info("Пользователь отказался от удаления подрядчика id = {}",contractor.getId());
                return;
            }
            for (Project project : projects) {
                if (project.getContractors() != null && project.getContractors().contains(contractor)) {
                    project.getContractors().remove(contractor);
                    usedContractorDao.deleteByProjectAndContractor(project, contractor);
                }
            }
            String basePath = System.getProperty("user.dir");
            String contractorFolderPath = basePath + "/uploads/portfolio/" + contractor.getId();
            File contractorFolder = new File(contractorFolderPath);
            if (contractorFolder.exists() && contractorFolder.isDirectory()) {
                FileUtils.deleteDirectory(contractorFolder);
                logger.info("Удалена папка портфолио подрядчика {}",contractorFolderPath);
            }
            contractorDao.delete(contractor);
            logger.info("Успешно удален подрядчик id = {}",contractor.getId());
            contractors.clear();
            contractors.addAll(contractorDao.findAll());
        }catch (RuntimeException e) {
            logger.error("Ошибка удаления подрядчика",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.contractor.delete"), ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            logger.error("Ошибка удаления папки подрядчика",e);
            Alert alert = new Alert(Alert.AlertType.ERROR, bundle.getString("mainController.ex.deleteFolder"), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void searchService() {
        logger.info("Поиск услуги подрядчика");
        lbInfoContractor.setText("");
        String description = tfDescriptionService.getText().trim();
        String price = tfPriceService.getText().trim();
        String prepayment = tfPrepaymentService.getText().trim();
        Contractor contractor = contractorsTable.getSelectionModel().getSelectedItem();
        if (contractor == null) {
            logger.warn("Попытка поиска услуг без выбранного подрядчика");
            lbInfoContractor.setText(bundle.getString("mainController.service.info.chooseContractor"));
            services.clear();
            tfDescriptionService.clear();
            tfPriceService.clear();
            tfPrepaymentService.clear();
            return;
        }
        if (description.isEmpty() && price.isEmpty() && prepayment.isEmpty()) {
            services.clear();
            services.addAll(contractorServiceDao.findByContractor(contractor));
            logger.info("Показаны все услуги подрядчика id={}", contractor.getId());
            lbInfoContractor.setText("");
            tfDescriptionService.clear();
            tfPriceService.clear();
            tfPrepaymentService.clear();
            return;
        }
        try {
            ContractorService service = ContractorService.builder()
                    .service(description.isEmpty() ? null : description)
                    .price(price.isEmpty() ? null : new BigDecimal(price))
                    .prepayment(prepayment.isEmpty() ? null : new BigDecimal(prepayment))
                    .contractor(contractor)
                    .build();
            List<ContractorService> list = (List<ContractorService>) contractorServiceDao.search(service);
            if (list == null || list.isEmpty()) {
                services.clear();
                lbInfoContractor.setText(bundle.getString("mainController.service.info.notFound"));
            } else {
                services.clear();
                services.addAll(list);
                logger.info("Найдено {} услуг", list.size());
                lbInfoContractor.setText("");
            }
        } catch (RuntimeException e) {
            logger.error("Ошибка поиска услуги", e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.service.search"), ButtonType.OK);
            alert.showAndWait();
        }
        tfDescriptionService.clear();
        tfPriceService.clear();
        tfPrepaymentService.clear();
    }
    @FXML
    public void addService() {
        logger.info("Добавление услуги подрядчику");
        lbInfoContractor.setText("");
        String description = tfDescriptionService.getText().trim();
        String price = tfPriceService.getText().trim();
        String prepayment = tfPrepaymentService.getText().trim();
        Contractor contractor = contractorsTable.getSelectionModel().getSelectedItem();
        if (contractor == null) {
            logger.warn("Попытка добавить услугу без выбранного подрядчика");
            lbInfoContractor.setText(bundle.getString("mainController.service.info.chooseContractor.toAddService"));
            return;
        }
        if (description.isEmpty() || price.isEmpty()) {
            logger.warn("Попытка добавить услугу с пустыми полями: description='{}', price='{}'", description, price);
            lbInfoContractor.setText(bundle.getString("mainController.service.add.fillFields"));
            return;
        }
        try {
            boolean serviceExists = contractorServiceDao.serviceExists(contractor,description);
            if (serviceExists) {
                lbInfoContractor.setText(bundle.getString("mainController.contractor.addService.already"));
                return;
            }
            ContractorService service = new ContractorService();
            service.setService(description);
            service.setContractor(contractor);
            service.setPrice(new BigDecimal(price));
            service.setPrepayment(new BigDecimal(prepayment));
            service = contractorServiceDao.save(service);
            if (contractor.getServices() == null) {
                contractor.setServices(new ArrayList<>());
            }
            contractor.getServices().add(service);
            services.add(service);
            logger.info("Успешно добавлена услуга id = {} подрядчику id = {}",service.getId(),contractor.getId());
        } catch (RuntimeException e) {
            logger.error("Ошибка добавления услуги",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.service.save"), ButtonType.OK);
            alert.showAndWait();
        }
        tfDescriptionService.clear();
        tfPriceService.clear();
        tfPrepaymentService.clear();
    }
    @FXML
    public void editService() {
        logger.info("Редактирование услуги");
        lbInfoContractor.setText("");
        String description = tfDescriptionService.getText().trim();
        String price = tfPriceService.getText().trim();
        String prepayment = tfPrepaymentService.getText().trim();
        ContractorService service = servicesTable.getSelectionModel().getSelectedItem();
        if(service==null){
            lbInfoContractor.setText(bundle.getString("mainController.service.info.chooseService"));
            return;
        }
        try{
            service.setService(description);
            service.setPrice(price.isEmpty()? BigDecimal.ZERO : new BigDecimal(price));
            service.setPrepayment(prepayment.isEmpty()? BigDecimal.ZERO : new BigDecimal(prepayment));
            contractorServiceDao.update(service);
            servicesTable.refresh();
            logger.info("Успешно отредактирована услуга id = {}",service.getId());
        }catch (RuntimeException e) {
            logger.error("Ошибка редактирования услуги",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.service.edit"), ButtonType.OK);
            alert.showAndWait();
        }
        tfDescriptionService.clear();
        tfPriceService.clear();
        tfPrepaymentService.clear();
    }
    @FXML
    public void deleteService() {
        logger.info("Удаление услуги");
        lbInfoContractor.setText("");
        ContractorService service = servicesTable.getSelectionModel().getSelectedItem();
        if(service==null){
            lbInfoContractor.setText(bundle.getString("mainController.service.info.chooseService"));
            return;
        }
        try{
            Alert question = new Alert(Alert.AlertType.CONFIRMATION,
                    bundle.getString("mainController.alert.delete.service.confirmation"),
                        ButtonType.YES,ButtonType.NO);
            if(question.showAndWait().orElse(ButtonType.NO)!=ButtonType.YES){
                logger.info("Пользователь отказался удалять услугу id = {}",service.getId());
                return;
            }
            List<ContractorService> list = (List<ContractorService>)
                    contractorServiceDao.findByContractor(service.getContractor());
            contractorServiceDao.delete(service);
            logger.info("Успешно удалена услуга id = {}",service.getId());
            services.clear();
            services.addAll(list);
        } catch (RuntimeException e) {
            logger.error("Ошибка удаления услуги",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.service.delete"), ButtonType.OK);
            alert.showAndWait();
        }
        tfDescriptionService.clear();
        tfPriceService.clear();
        tfPrepaymentService.clear();
    }
    @FXML
    public void clickOnChooseFile() {
        logger.info("Выбор изображения для портфолио");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("mainController.filechooser.title"));
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.allFilter"),"*.jpg",
                "*.jpeg", "*.png","*.gif","*.bmp");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.jpgFilter"),"*.jpg");
        FileChooser.ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.jpegFilterter"),"*.jpeg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.pngFilter"),"*.png");
        FileChooser.ExtensionFilter gifFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.gifFilter"),"*.gif");
        FileChooser.ExtensionFilter bmpFilter = new FileChooser.ExtensionFilter(bundle.getString("mainController.filechooser.bmpFilter"),"*.bmp");
        fileChooser.getExtensionFilters().addAll(allFilter,jpgFilter,jpegFilter,pngFilter,gifFilter,bmpFilter);
        File file = fileChooser.showOpenDialog(MainApplication.getStage());
        if(file!=null){
            String filePath = file.getAbsolutePath();
            tfLinkPortfolio.setText(filePath);
            logger.info("Выбрано изображение {}",filePath);
        }
    }
    @FXML
    public void searchPortfolio() {
        logger.info("Поиск портфолио");
        lbInfoPortfolio.setText("");
        String description = taDescriptionPortfolio.getText().trim();
        Contractor contractor = cmbContractorPortfolio.getValue();
        if(contractor == null && description.isEmpty()){
            portfolios.clear();
            portfolios.addAll(portfolioDao.findAll());
            taDescriptionPortfolio.clear();
            cmbContractorPortfolio.setValue(null);
            return;
        }
        if(contractor == null){
            lbInfoPortfolio.setText(bundle.getString("mainController.contractor.info.choose"));
            taDescriptionPortfolio.clear();
            cmbContractorPortfolio.setValue(null);
            return;
        }
        try{
            List<Portfolio> list = (List<Portfolio>) portfolioDao.search(description, contractor);
            if(list == null || list.isEmpty()){
                lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.info.noSuchPortfolio"));
                taDescriptionPortfolio.clear();
                cmbContractorPortfolio.setValue(null);
                portfolios.clear();
                return;
            }
            portfolios.clear();
            portfolios.addAll(list);
            logger.info("Найдено {} портфолио", list.size());
        } catch (RuntimeException e) {
            logger.error("Ошибка поиска портфолио", e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.portfolio.search"), ButtonType.OK);
            alert.showAndWait();
        }
        taDescriptionPortfolio.clear();
        cmbContractorPortfolio.setValue(null);
    }
    @FXML
    public void addPortfolio() {
        logger.info("Добавление портфолио");
        lbInfoPortfolio.setText("");
        String description = taDescriptionPortfolio.getText().trim();
        Contractor contractor = cmbContractorPortfolio.getValue();
        String imgPath = tfLinkPortfolio.getText();
        String newName = tfNewFileNamePortfolio.getText();
        if (description.isEmpty() || contractor == null || imgPath.isEmpty() || newName.isEmpty()) {
            logger.warn("Попытка добавить портфолио с недостающими данными: description empty={}, contractor={}, imgPath empty={}, newName empty={}",
                    description.isEmpty(), contractor == null, imgPath.isEmpty(), newName.isEmpty());
            String missingFields = "";
            if (description.isEmpty()) missingFields += bundle.getString("mainController.portfolio.add.missingFields.description") + " ";
            if (contractor == null) missingFields += bundle.getString("mainController.portfolio.add.missingFields.contractor") + " ";
            if (imgPath.isEmpty()) missingFields += bundle.getString("mainController.portfolio.add.missingFields.image") + " ";
            if (newName.isEmpty()) missingFields += bundle.getString("mainController.portfolio.add.missingFields.newFileName") + " ";
            lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.add.missingFields.choose") + " " + missingFields.trim());
            return;
        }
        try{
            String basePath = System.getProperty("user.dir");
            String contractorFolderPath = basePath + "/uploads/portfolio/" + contractor.getId();
            File inFile = new File(imgPath);
            if(inFile.exists()){
                File directory = new File(contractorFolderPath);
                if(!directory.exists()) directory.mkdirs();
                String extension = inFile.getName().substring(inFile.getName().lastIndexOf("."));
                String newFileName =  newName + extension;
                File outFile = new File(directory,newFileName);
                String pathForBd = "uploads/portfolio/" + contractor.getId() + "/" + newFileName;
                Portfolio portfolio = new Portfolio();
                portfolio.setContractor(contractor);
                portfolio.setDescription(description);
                portfolio.setImgUrl(pathForBd);
                boolean alreadyExists = portfolioDao.portfolioExist(contractor,pathForBd);
                if(alreadyExists){
                    lbInfoPortfolio.setText(bundle.getString("mainController.contractor.add.portfolio.already"));
                    return;
                }
                Files.copy(inFile.toPath(),outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                portfolioDao.save(portfolio);
                contractor.getPortfolio().add(portfolio);
                portfolios.add(portfolio);
                logger.info("Успешно добавлено портфолио id = {} подрядчику {}",portfolio.getId(),contractor.getId());
            }
        } catch (Exception e) {
            logger.error("Ошибка создания портфолио",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.portfolio.save"), ButtonType.OK);
            alert.showAndWait();
        }
        taDescriptionPortfolio.clear();
        cmbContractorPortfolio.setValue(null);
        tfLinkPortfolio.clear();
        tfNewFileNamePortfolio.clear();
    }
    @FXML
    public void deletePortfolio() {
        logger.info("Удаление портфолио");
        lbInfoPortfolio.setText("");
        Portfolio portfolio = portfolioTable.getSelectionModel().getSelectedItem();
        if(portfolio==null){
            lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.info.choosePortfolio"));
            return;
        }
        File fileToDelete = new File(portfolio.getImgUrl()).getAbsoluteFile();
        try{
            Contractor contractor = portfolio.getContractor();
            String imgPath = portfolio.getImgUrl();
            File folder = new File("uploads/portfolio/" + contractor.getId()).getAbsoluteFile();
            if(folder.exists() && folder.isDirectory()){
                File[] files = folder.listFiles();
                if (files!=null){
                List<File> list = List.of(files);
                   if(list.contains(fileToDelete)){
                       fileToDelete.delete();
                       logger.info("Удален файл портфолио");
                   }
                    File[] currentFiles = folder.listFiles();
                   if(currentFiles!= null && currentFiles.length==0){
                       folder.delete();
                       logger.info("Удалена папка портфолио");
                   }
                }
            }
            portfolioDao.delete(portfolio);
            logger.info("Успешно удалено портфолио");
            portfolios.clear();
            portfolios.addAll(portfolioDao.findByContractor(cmbContractorPortfolio.getValue()));
        }catch (Exception e) {
            logger.error("Ошибка удаления портфолио",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.portfolio.delete"), ButtonType.OK);
            alert.showAndWait();
        }
        taDescriptionPortfolio.clear();
        cmbContractorPortfolio.setValue(null);
        tfLinkPortfolio.clear();
        tfNewFileNamePortfolio.clear();
    }
    @FXML
    public void editPortfolio() {
        logger.info("Редактирование портфолио");
        Portfolio portfolio = portfolioTable.getSelectionModel().getSelectedItem();
        if (portfolio == null) {
            lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.info.choosePortfolio"));
            return;
        }
        String description = taDescriptionPortfolio.getText().trim();
        Contractor contractor = cmbContractorPortfolio.getValue();
        String imgPath = tfLinkPortfolio.getText();
        String newName = tfNewFileNamePortfolio.getText();
        if (contractor == null) {
            lbInfoPortfolio.setText(bundle.getString("mainController.contractor.info.choose"));
            return;
        }
        try {
            File inFile = new File(portfolio.getImgUrl()).getAbsoluteFile();
            String extension = inFile.getName().substring(inFile.getName().lastIndexOf("."));
            String oldNameWithoutExt = inFile.getName().substring(0, inFile.getName().lastIndexOf("."));
            String newFileName = newName.isEmpty() ? oldNameWithoutExt : newName + extension;
            String basePath = System.getProperty("user.dir");
            File newContractorFolder = new File(basePath + "/uploads/portfolio/" + contractor.getId()).getAbsoluteFile();
            File outFile = new File(newContractorFolder, newFileName);
            File oldContractorFolder = new File(basePath + "/uploads/portfolio/" + portfolio.getContractor().getId());
            boolean isNewContractor = !contractor.equals(portfolio.getContractor());
            boolean isNewName = !newFileName.equals(inFile.getName());
            boolean isNewImage = !imgPath.equals(portfolio.getImgUrl());
            if(isNewContractor || isNewName || isNewImage){
                if (!newContractorFolder.exists()) {
                    newContractorFolder.mkdirs();
                    logger.info("Создана папка для портфолио нового подрядчика");
                }
                File sourceFile = isNewImage ? new File(imgPath).getAbsoluteFile() : inFile;
                if (sourceFile.exists()) {
                    Files.copy(sourceFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logger.info("Изображение портфолио скопировано в папку новго подрядчика");
                    if (isNewImage || isNewContractor || isNewName) {
                        if (inFile.exists() && !inFile.equals(outFile)) {
                            inFile.delete();
                            logger.info("Прошлый файл портфолио удален");
                        }
                    }
                }
                if(isNewContractor) {
                    File[] currentFiles = oldContractorFolder.listFiles();
                    if (currentFiles != null && currentFiles.length == 0) {
                        oldContractorFolder.delete();
                        logger.info("Удалена папка старого подрядчика - в нем нет портфолио");
                    }
                }
                portfolio.setImgUrl("uploads/portfolio/" + contractor.getId() + "/" + newFileName);
            }
            portfolio.setDescription(description);
            portfolio.setContractor(contractor);
            portfolioDao.update(portfolio);
            logger.info("Успешно обновлено портфолио id = {}",portfolio.getId());
            String newImgPath = basePath + "/" + portfolio.getImgUrl();
            File newImageFile = new File(newImgPath);
            if (newImageFile.exists()) {
                Image img = new Image(newImageFile.toURI().toString());
                portfolioImageView.setImage(img);
            } else {
                portfolioImageView.setImage(null);
                lbInfoPortfolio.setText(bundle.getString("mainController.portfolio.noImage"));
            }
        } catch (Exception e) {
            logger.error("Ошибка редактирования портфолио",e);
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    bundle.getString("mainController.ex.portfolio.edit"), ButtonType.OK);
            alert.showAndWait();
        }
        taDescriptionPortfolio.clear();
        cmbContractorPortfolio.setValue(null);
        tfLinkPortfolio.clear();
        tfNewFileNamePortfolio.clear();
        portfolioTable.refresh();
    }
}