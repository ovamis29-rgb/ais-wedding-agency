module ru.kafpin.wedding {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires org.apache.commons.io;
    requires java.desktop;
    requires org.slf4j;

    opens ru.kafpin.wedding to javafx.fxml;
    exports ru.kafpin.wedding;

    exports ru.kafpin.wedding.model;
    opens ru.kafpin.wedding.model to javafx.fxml;

    exports ru.kafpin.wedding.controller;
    opens ru.kafpin.wedding.controller to javafx.fxml;

    opens ru.kafpin.wedding.util to javafx.fxml;
    exports ru.kafpin.wedding.util;

    exports ru.kafpin.wedding.dao.impl;
    exports ru.kafpin.wedding.dao;
    opens ru.kafpin.wedding.dao;
    opens ru.kafpin.wedding.dao.impl;
}