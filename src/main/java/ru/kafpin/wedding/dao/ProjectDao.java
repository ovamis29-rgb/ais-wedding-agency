package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы со свадебными проектами в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ProjectDao{
    /**
     * Возвращает проект по его идентификатору
     * @return объект Optional с найденным проектом, или пустой Optional, если проекта не существует
     */
    Optional<Project> getById(Long id);
    /**
     * Находит весь список проектов
     * @return коллекция найденных проектов (может вернуть пустую коллекцию)
     */
    Collection<Project> findAll();
    /**
     * Создает новый проект в базе данных
     * @param project ссылка на экземпляр Project
     * @return сохраненный экземпляр проекта с присвоенным ID
     */
    Project save(Project project);
    /**
     * Обновляет данные существующего проекта
     * @param project ссылка на экземпляр Project
     * @return обновленный экземпляр проекта
     */
    Project update(Project project);

    /**
     *Удаляет проект по его ссылке на экземпляр
     * @param project ссылка на экземпляр Project
     */
    void delete(Project project);
    /**
     * Удаляет проект по его ID
     * @param id уникальный идентификатор проекта
     */
    void deleteById(Long id);
    /**
     * Выплняет поиск проекта по указанной дате свадьбы
     * @param date дата свадьбы
     * @return объект Optional с найденной проектом, или пустой Optional, если проект не существует
     */
    Optional<Project> findByDate(LocalDateTime date);
    /**
     * Добавляет молодоженов в список клиентов проекта
     * @param bride клиент - невеста
     * @param groom клиент - жених
     * @param project ссылка на экземпляр Project
     */
    Collection<ProjectClient> addSpouses(Project project, Client bride, Client groom);
    /**
     * Выплняет поиск всче связаныч с данным клиентом проектов
     * @param client клиент
     * @return коллекция найденных проектов (может быть пустой)
     */
    Collection<Project> relatedProjects(Client client);
}
