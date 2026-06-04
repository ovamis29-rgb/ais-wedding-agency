package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Client;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с клиентами в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ClientDao {

    /**
     * Возвращает клиента по его идентификатору
     * @param id уникальный идентификатор клиента
     * @return объект Optional с найденным клиентом, или пустой Optional, если клиент не существует
     */
    Optional<Client> getById(Long id);

    /**
     * Находит весь список клиентов
     * @return коллекция найденных клиентов (может быть пустой)
     */
    Collection<Client> findAll();

    /**
     * Создает нового клиента в базе данных
     * @param client ссылка на экземпляр Client
     * @return сохраненный экземпляр клиента с присвоенным ID
     */
    Client save(Client client);

    /**
     * Обновляет данные существующего клиента
     * @param client ссылка на экземпляр Client
     * @return обновленный экземпляр клиента
     */
    Client update(Client client);

    /**
     * Удаляет клиента по его ссылке на экземпляр
     * @param client ссылка на экземпляр Client
     */
    void delete(Client client);

    /**
     * Удаляет клиента по его ID
     * @param id уникальный ID клиента
     */
    void deleteById(Long id);

    /**
     * Выполняет поиск клиентов по ФИО
     * @param name имя клиента
     * @param lastname фамилия клиента
     * @param middleName отчество клиента
     * @return коллекция найденных клиентов (может быть пустой)
     */
    Collection<Client> search(String name, String lastname, String middleName);
}
