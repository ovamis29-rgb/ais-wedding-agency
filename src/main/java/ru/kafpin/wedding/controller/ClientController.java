package ru.kafpin.wedding.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kafpin.wedding.model.Client;
import java.util.Locale;
import java.util.ResourceBundle;

@Setter
@Getter
@Builder
public class ClientController {
    private Stage stage;
    private Client client;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    @FXML
    private Button ok;
    @FXML
    private CheckBox male;
    @FXML
    private CheckBox female;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfLastname;
    @FXML
    private TextField tfMiddleName;
    @FXML
    private TextField tfMail;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfSeria;
    @FXML
    private TextField tfNumber;
    @FXML
    private DatePicker dpWhenIssued;
    @FXML
    private DatePicker dpDateOfRegistration;
    @FXML
    private TextField tfWhoIssued;
    @FXML
    private TextField tfPlaceOfBirth;
    @FXML
    private TextField tfAdressOfRegistration;
    private ResourceBundle bundle = ResourceBundle.getBundle("main", Locale.getDefault());
    @FXML
    void initialize() {
        tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfName.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfLastname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfLastname.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfMiddleName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']*$")) {
                tfMiddleName.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-']", ""));
            }
        });
        tfPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[0-9\\+\\s\\-\\(\\)]*$")) {
                tfPhone.setText(newValue.replaceAll("[^0-9\\+\\s\\-\\(\\)]", ""));
            }
        });
        tfSeria.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 4) {
                tfSeria.setText(newValue.replaceAll("[^\\d]", "").length() > 4 ?
                        newValue.replaceAll("[^\\d]", "").substring(0, 4) :
                        newValue.replaceAll("[^\\d]", ""));
            }
        });
        tfNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 6) {
                tfNumber.setText(newValue.replaceAll("[^\\d]", "").length() > 6 ?
                        newValue.replaceAll("[^\\d]", "").substring(0, 6) :
                        newValue.replaceAll("[^\\d]", ""));
            }
        });
        tfPlaceOfBirth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-.]*$")) {
                tfPlaceOfBirth.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F\\s\\-.]", ""));
            }
        });
        tfWhoIssued.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F0-9\\s\\-.,\"'№#/()]*$")) {
                tfWhoIssued.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F0-9\\s\\-.,\"'№#/()]", ""));
            }
        });
        tfAdressOfRegistration.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F0-9\\s\\-.,/]*$")) {
                tfAdressOfRegistration.setText(newValue.replaceAll("[^a-zA-Zа-яА-ЯёЁ\\u00C0-\\u024F0-9\\s\\-.,/]", ""));
            }
        });
        tfMail.textProperty().addListener((observable, oldValue, newValue) ->
                tfMail.setText(newValue.replaceAll("[^\\w.%+-@]", "").replaceAll("\\s", ""))
        );
        male.setOnAction(events ->{
            if(female.isSelected()){
                female.setSelected(false);
                male.setSelected(true);
            }
        });
        female.setOnAction(events ->{
            if(male.isSelected()){
                male.setSelected(false);
                female.setSelected(true);
            }
        });
        ok.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                tfName.getText().trim().isEmpty() ||
                                        tfLastname.getText().trim().isEmpty() ||
                                        tfPhone.getText().trim().isEmpty() ||
                                        tfSeria.getText().trim().isEmpty() ||
                                        tfNumber.getText().trim().isEmpty() ||
                                        dpWhenIssued.getValue() == null ||
                                        dpDateOfRegistration.getValue() == null ||
                                        tfWhoIssued.getText().trim().isEmpty() ||
                                        tfPlaceOfBirth.getText().trim().isEmpty() ||
                                        tfAdressOfRegistration.getText().trim().isEmpty() ||
                                        (!male.isSelected() && !female.isSelected()) ||
                                        (!tfMail.getText().trim().isEmpty() && !tfMail.getText().trim().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")),
                        tfName.textProperty(), tfLastname.textProperty(), tfPhone.textProperty(),
                        tfSeria.textProperty(), tfNumber.textProperty(),
                        dpWhenIssued.valueProperty(), dpDateOfRegistration.valueProperty(),
                        tfWhoIssued.textProperty(), tfPlaceOfBirth.textProperty(),
                        tfAdressOfRegistration.textProperty(), tfMail.textProperty(),
                        male.selectedProperty(),
                        female.selectedProperty()
                )
        );
        if (client.getName() != null) tfName.setText(client.getName());
        if (client.getLastname() != null) tfLastname.setText(client.getLastname());
        if (client.getMiddleName() != null) tfMiddleName.setText(client.getMiddleName());
        if(client.getGender() != null){
            String gender = client.getGender();
            if (gender.equals(bundle.getString("clientController.gender.male"))) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
        }
        if (client.getEmail() != null) tfMail.setText(client.getEmail());
        if (client.getPhoneNumber() != null) tfPhone.setText(client.getPhoneNumber());
        if (client.getSeries() != null) tfSeria.setText(client.getSeries());
        if (client.getNumber() != null) tfNumber.setText(client.getNumber());
        if (client.getWhenIssued() != null) dpWhenIssued.setValue(client.getWhenIssued());
        if (client.getDateOfRegistration() != null) dpDateOfRegistration.setValue(client.getDateOfRegistration());
        if (client.getIssuedBy() != null) tfWhoIssued.setText(client.getIssuedBy());
        if (client.getPlaceOfBirth() != null) tfPlaceOfBirth.setText(client.getPlaceOfBirth());
        if (client.getRegistrationAdress() != null) tfAdressOfRegistration.setText(client.getRegistrationAdress());
    }

    @FXML
    public void ClientOk() {
        client.setName(tfName.getText().trim());
        client.setLastname(tfLastname.getText().trim());
        client.setMiddleName(tfMiddleName.getText().trim());
        client.setGender(male.isSelected()? "М":"Ж");
        client.setEmail(tfMail.getText().trim());
        client.setPhoneNumber(tfPhone.getText().trim());
        client.setSeries(tfSeria.getText().trim());
        client.setNumber(tfNumber.getText().trim());
        client.setWhenIssued(dpWhenIssued.getValue());
        client.setDateOfRegistration(dpDateOfRegistration.getValue());
        client.setIssuedBy(tfWhoIssued.getText().trim());
        client.setPlaceOfBirth(tfPlaceOfBirth.getText().trim());
        client.setRegistrationAdress(tfAdressOfRegistration.getText().trim());
        logger.info("Пользователь подтвердил создание/редактирование клиента");
        stage.close();
    }
    @FXML
    public void CancelClient(){
        logger.info("Пользователь отменил создание/редактирование клиента");
        stage.close();
    }
}
