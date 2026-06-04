package ru.kafpin.wedding.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.dao.ContractorDao;
import ru.kafpin.wedding.dao.ContractorServiceDao;
import ru.kafpin.wedding.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Builder
public class TaskController {
    private Stage stage;
    private Project project;
    private ContractorDao contractorDao;
    private ContractorServiceDao contractorServiceDao;
    private Task task;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private ResourceBundle bundle;
    private Map<String, String> statusMapping;
    @FXML
    private Button ok;
    @FXML
    private Slider slPriority;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private ComboBox<Contractor> cmbContractor;
    @FXML
    private ComboBox<ContractorService> cmbService;
    @FXML
    private TextField tfPrice;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<String> cmbHours;
    @FXML
    private ComboBox<String> cmbMinutes;

    @FXML
    void initialize() {
        bundle = ResourceBundle.getBundle("main", Locale.getDefault());
        statusMapping = new HashMap<>();
        statusMapping.put(bundle.getString("mainApplication.cmb.status.wait"), "ожидает выполнения");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.inproccess"), "в процессе");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.complited"), "завершено");
        statusMapping.put(bundle.getString("mainApplication.cmb.status.cancelled"), "отменено");
        cmbContractor.valueProperty().addListener((obs,old,newVal) ->{
            if(cmbContractor.getValue()!=null){
                cmbService.setDisable(false);
                List<ContractorService> list = cmbContractor.getValue().getServices();
                if(list!=null){
                    ObservableList<ContractorService> services = FXCollections.observableArrayList(list);
                    if(services!=null) cmbService.setItems(services);
                } else{
                    cmbService.setItems(FXCollections.observableArrayList());
                }
            }else{
                cmbService.setDisable(true);
                cmbService.setItems(FXCollections.observableArrayList());
            }
        });
        cmbService.valueProperty().addListener((obs,old,current) -> {
            if(cmbService.getValue()!=null){
                tfPrice.setText(String.valueOf(cmbService.getValue().getPrice()));
            }
        });
        ObservableList<String> statuses = FXCollections.observableArrayList(
                bundle.getString("mainApplication.cmb.status.wait"),
                bundle.getString("mainApplication.cmb.status.inproccess"),
                bundle.getString("mainApplication.cmb.status.complited"),
                bundle.getString("mainApplication.cmb.status.cancelled")
        );
        cmbStatus.setItems(statuses);
        ok.disableProperty().bind(
                Bindings.createBooleanBinding(()->{
                            return cmbContractor.getValue()==null || cmbService.getValue()==null
                                    || cmbStatus.getValue()==null || tfPrice.getText()==null
                                    || tfPrice.getText().trim().isEmpty() || dpDate.getValue()==null
                                    || cmbHours.getValue()==null || cmbMinutes.getValue()==null;
                        },cmbContractor.valueProperty(),cmbService.valueProperty(),cmbStatus.valueProperty()
                        ,tfPrice.textProperty(),dpDate.valueProperty(),cmbHours.valueProperty(),cmbMinutes.valueProperty())
        );
        ObservableList<Contractor> contractors = FXCollections.observableArrayList(contractorDao.findAll());
        cmbContractor.setItems(contractors);
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        cmbHours.setItems(hours);

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 1) {
            minutes.add(String.format("%02d", i));
        }
        cmbMinutes.setItems(minutes);
        if(task.getContractorService()!=null){
            Contractor taskContractor = task.getContractorService().getContractor();
            cmbContractor.setValue(task.getContractorService().getContractor());
            cmbService.setValue(task.getContractorService());
            ObservableList<ContractorService> services = FXCollections.observableArrayList(contractorServiceDao
                    .findByContractor(taskContractor));
            cmbService.setItems(services);
        }
        String currentStatus = task.getStatus();
        if (currentStatus != null) {
            for (Map.Entry<String, String> entry : statusMapping.entrySet()) {
                if (entry.getValue().equals(currentStatus)) {
                    cmbStatus.setValue(entry.getKey());
                    break;
                }
            }
        }
        if(task.getPrice()!=null) tfPrice.setText(task.getPrice().toString());
        if(task.getPriority()!=null) slPriority.setValue(task.getPriority());
        if(task.getDeadline()!=null){
            dpDate.setValue(task.getDeadline().toLocalDate());
            LocalTime time = task.getDeadline().toLocalTime();
            cmbHours.setValue(String.format("%02d", time.getHour()));
            cmbMinutes.setValue(String.format("%02d", time.getMinute()));
        }
    }
    @FXML
    public void ButtonOk(){
        task.setContractorService(cmbService.getValue());
        task.setPrice(new BigDecimal(tfPrice.getText()));
        if (cmbStatus.getValue() != null && statusMapping.containsKey(cmbStatus.getValue())) {
            task.setStatus(statusMapping.get(cmbStatus.getValue()));
        }
        task.setDeadline(LocalDateTime.of(dpDate.getValue(),
                LocalTime.of(Integer.parseInt(cmbHours.getValue()),
                        Integer.parseInt(cmbMinutes.getValue()))));
        task.setPriority((int) slPriority.getValue());
        task.setProject(project);
        logger.info("Пользователь подтвердил создание/редактирование задачи");
        stage.close();
    }
    @FXML
    public void ButtonCancel(){
        logger.info("Пользователь отменил создание/редактирование задачи");
        stage.close();
    }
}