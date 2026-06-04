package ru.kafpin.wedding.util;

import javafx.scene.control.Alert;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@NoArgsConstructor
public class LanguageManager {
    private static final Logger logger = LoggerFactory.getLogger(LanguageManager.class);

    public static void saveLanguage(Locale locale) {
        Properties properties = new Properties();
        String lang = locale.getLanguage().equals("ru") ? "ru_RU" :
                locale.getLanguage().equals("de") ? "de_DE" : "en";
        try {
            properties.load(
                    new FileInputStream("src/main/resources/config.properties")
            );
            properties.setProperty("app.language", lang);
            properties.store(
                    new FileOutputStream("src/main/resources/config.properties"),
                    null
            );
            Locale.setDefault(locale);
            logger.info("Язык изменён на {}", locale);
        } catch (IOException e) {
            logger.error("Ошибка сохранения языка", e);
        }
    }
    public static void loadLanguage(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            String language = properties.getProperty("app.language");
            logger.info("Загружены настройки языка {}", language);
            switch (language) {
                case "en" -> Locale.setDefault(Locale.ENGLISH);
                case "ru_RU" -> Locale.setDefault(new Locale("ru", "RU"));
                case "de_DE" -> Locale.setDefault(Locale.GERMANY);
            }
        } catch (IOException e) {
            logger.error("Ошибка загрузки языка", e);
        }
    }
}
