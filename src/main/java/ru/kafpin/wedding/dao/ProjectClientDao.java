package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Portfolio;
import ru.kafpin.wedding.model.ProjectClient;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с клиентами - молодоженами подрядчика в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ProjectClientDao {
    /**
     * Возвращает клиента по его идентификатору
     * @param id уникальный идентификатор клиента
     * @return объект Optional с найденным клиентом, или пустой Optional, если клиента не существует
     */
    Optional<ProjectClient> getById(Long id);
    /**
     * Находит весь список клиентов (в этом списке может быть только 2 клиента)
     * @return коллекция найденных клиентов (может быть пустой)
     */
    Collection<ProjectClient> findAll();
    /**
     * Создает нового клиента в базе данных
     * @param client ссылка на экземпляр ProjectClient
     * @return сохраненный экземпляр клиента с присвоенным ID
     */
    ProjectClient save(ProjectClient client);
    /**
     * Обновляет данные существующего клиента
     * @param client ссылка на экземпляр ProjectClient
     * @return обновленный экземпляр клиента
     */
    ProjectClient update(ProjectClient client);
    /**
     * Удаляет клиента по его ссылке на экземпляр
     * @param client ссылка на экземпляр ProjectClient
     */
    void delete(ProjectClient client);
    /**
     * Удаляет клиента по его ID
     * @param id уникальный идентификатор клиента
     */
    void deleteById(Long id);
}
