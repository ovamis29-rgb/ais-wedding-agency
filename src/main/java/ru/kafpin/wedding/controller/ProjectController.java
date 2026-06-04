package ru.kafpin.wedding.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Builder;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.dao.ClientDao;
import ru.kafpin.wedding.model.Client;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.ProjectClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Setter
public class ProjectController {
    private Stage stage;
    private Project project;
    private ClientDao clientDao;
    private Client groom;
    private Client bride;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private ResourceBundle bundle;
    private Map<String, String> statusMapping;
    @FXML
    private Button ok;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<LocalTime> cmbTime;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private TextArea taWishes;
    @FXML
    private TextField tfGroomName;
    @FXML
    private TextField tfGroomLastname;
    @FXML
    private TextField tfGroomSurname;
    @FXML
    private TextField tfGroomMail;
    @FXML
    private TextField tfGroomPhone;
    @FXML
    private TextField tfBrideName;
    @FXML
    private TextField tfBrideLastname;
    @FXML
    private TextField tfBrideSurname;
    @FXML
    private TextField tfBrideMail;
    @FXML
    private TextField tfBridePhone;
    @FXML
    private ComboBox<Client> cmbGroom;
    @FXML
    private ComboBox<Client> cmbBride;

    @FXML
    void initialize() {
        bundle = ResourceBundle.getBundle("main", Locale.getDefault());

        statusMapping = new HashMap<>();
        statusMapping.put(bundle.getString("mainApplication.cmb.status.wait"), "ожидает выполнения");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.inproccess"), "в процессе");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.complited"), "завершено");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.cancelled"), "отменено");

        taWishes.textProperty().addListener((obs, old, newVal) -> {
            if (!newVal.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-.,'()0-9]*$")) {
                taWishes.setText(newVal.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-.,'()0-9]", ""));
            }
        });

        ObservableList<String> statuses = FXCollections.observableArrayList(
                bundle.getString("mainApplication.cmb.status.wait"),
                bundle.getString("mainApplication.cmb.status.inproccess"),
                bundle.getString("mainApplication.cmb.status.complited"),
                bundle.getString("mainApplication.cmb.status.cancelled")
        );
        cmbStatus.setItems(statuses);

        cmbStatus.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && statusMapping.containsKey(newVal)) {
                project.setStatus(statusMapping.get(newVal));
            }
        });

        ObservableList<LocalTime> projectTimes = FXCollections.observableArrayList(
                LocalTime.of(10,0),
                LocalTime.of(11,30),
                LocalTime.of(13,0),
                LocalTime.of(14,30),
                LocalTime.of(16,0),
                LocalTime.of(17,30)
        );

        ObservableList<Client> maleClients = FXCollections.observableArrayList(clientDao.findAll().stream()
                .filter(client -> client.getGender().equals("М"))
                .collect(Collectors.toList()));
        ObservableList<Client> femaleClients = FXCollections.observableArrayList(clientDao.findAll().stream()
                .filter(client -> client.getGender().equals("Ж"))
                .collect(Collectors.toList()));

        cmbGroom.setItems(maleClients);
        cmbBride.setItems(femaleClients);

        cmbGroom.setOnAction(event -> {
            Client selectedClient = cmbGroom.getValue();
            if (selectedClient != null) {
                tfGroomName.setText(selectedClient.getName());
                tfGroomLastname.setText(selectedClient.getLastname());
                tfGroomSurname.setText(selectedClient.getMiddleName() != null ? selectedClient.getMiddleName() : "");
                tfGroomMail.setText(selectedClient.getEmail() != null ? selectedClient.getEmail() : "");
                tfGroomPhone.setText(selectedClient.getPhoneNumber());
            }
        });

        cmbBride.setOnAction(event -> {
            Client selectedClient = cmbBride.getValue();
            if (selectedClient != null) {
                tfBrideName.setText(selectedClient.getName());
                tfBrideLastname.setText(selectedClient.getLastname());
                tfBrideSurname.setText(selectedClient.getMiddleName() != null ? selectedClient.getMiddleName() : "");
                tfBrideMail.setText(selectedClient.getEmail() != null ? selectedClient.getEmail() : "");
                tfBridePhone.setText(selectedClient.getPhoneNumber());
            }
        });

        cmbTime.setItems(projectTimes);

        ok.disableProperty().bind(
                Bindings.createBooleanBinding(() -> {
                            return tfGroomName.getText().trim().isEmpty() ||
                                    tfGroomLastname.getText().trim().isEmpty() ||
                                    tfBrideName.getText().trim().isEmpty() ||
                                    tfBrideLastname.getText().trim().isEmpty() ||
                                    dpDate.getValue() == null;
                        },
                        tfGroomName.textProperty(), tfGroomLastname.textProperty(),
                        tfBrideName.textProperty(), tfBrideLastname.textProperty(),
                        dpDate.valueProperty(), cmbTime.valueProperty()
                ));

        if (project.getWeddingDate() != null) {
            dpDate.setValue(LocalDate.from(project.getWeddingDate()));
            cmbTime.setValue(LocalTime.from(project.getWeddingDate()));
        }
        if (project.getWishesForWedding() != null) taWishes.setText(project.getWishesForWedding());

        String selectedStatusKey = cmbStatus.getValue();
        if (selectedStatusKey != null && statusMapping.containsKey(selectedStatusKey)) {
            String statusForDb = statusMapping.get(selectedStatusKey);
            project.setStatus(statusForDb);
        }

        if (project.getProjectClients() != null) {
            for (ProjectClient pc : project.getProjectClients()) {
                if (pc != null && pc.getRole() != null && pc.getClient() != null) {
                    if ("жених".equalsIgnoreCase(pc.getRole())) {
                        Client groomClient = pc.getClient();
                        cmbGroom.setValue(groomClient);
                        if (groomClient.getName() != null) tfGroomName.setText(groomClient.getName());
                        if (groomClient.getLastname() != null) tfGroomLastname.setText(groomClient.getLastname());
                        if (groomClient.getMiddleName() != null) tfGroomSurname.setText(groomClient.getMiddleName());
                        if (groomClient.getEmail() != null) tfGroomMail.setText(groomClient.getEmail());
                        if (groomClient.getPhoneNumber() != null) tfGroomPhone.setText(groomClient.getPhoneNumber());
                    } else if ("невеста".equalsIgnoreCase(pc.getRole())) {
                        Client brideClient = pc.getClient();
                        cmbBride.setValue(brideClient);
                        if (brideClient.getName() != null) tfBrideName.setText(brideClient.getName());
                        if (brideClient.getLastname() != null) tfBrideLastname.setText(brideClient.getLastname());
                        if (brideClient.getMiddleName() != null) tfBrideSurname.setText(brideClient.getMiddleName());
                        if (brideClient.getEmail() != null) tfBrideMail.setText(brideClient.getEmail());
                        if (brideClient.getPhoneNumber() != null) tfBridePhone.setText(brideClient.getPhoneNumber());
                    }
                }
            }
        }
    }
    @FXML
    public void ButtonOk() {
        Client finalGroom = (cmbGroom.getValue() != null) ? cmbGroom.getValue() : this.groom;
        Client finalBride = (cmbBride.getValue() != null) ? cmbBride.getValue() : this.bride;
        ProjectClient groomSelected = null;
        ProjectClient brideSelected = null;
        if (finalGroom == null || finalBride == null) {
            return;
        }
        if (project.getProjectClients() == null) {
            project.setProjectClients(new ArrayList<>());
        } else {
            List<ProjectClient> list = project.getProjectClients();
            for (ProjectClient pc : list) {
                if ("жених".equalsIgnoreCase(pc.getRole())) {
                    groomSelected = ProjectClient.builder().id(pc.getId()).client(finalGroom).project(project).role("жених").build();
                } else if ("невеста".equalsIgnoreCase(pc.getRole())) {
                    brideSelected = ProjectClient.builder().id(pc.getId()).client(finalBride).project(project).role("невеста").build();
                }
            }
            project.getProjectClients().clear();
        }
        if (groomSelected == null) {
            groomSelected = ProjectClient.builder().client(finalGroom).project(project).role("жених").build();
        }
        if (brideSelected == null) {
            brideSelected = ProjectClient.builder().client(finalBride).project(project).role("невеста").build();
        }

        project.getProjectClients().addAll(List.of(groomSelected, brideSelected));

        if (!project.getProjectClients().isEmpty() && dpDate.getValue() != null && cmbTime.getValue() != null) {
            project.setWeddingDate(LocalDateTime.of(dpDate.getValue(), cmbTime.getValue()));
            if (taWishes.getText() != null) project.setWishesForWedding(taWishes.getText());
            logger.info("Пользователь подтвердил создание/редактирование проекта");
            stage.close();
        }
    }

    @FXML
    public void ButtonCancel() {
        logger.info("Пользователь отменил создание/редактирование проекта");
        stage.close();
    }
}