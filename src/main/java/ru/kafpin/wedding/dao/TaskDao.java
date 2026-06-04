package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.Task;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с задачами проекта в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface TaskDao {
    /**
     * Возвращает задачу по ее идентификатору
     * @return объект Optional с найденным задачей, или пустой Optional, если задачи не существует
     */
    Optional<Task> getById(Long id);
    /**
     * Находит весь список задач
     * @return коллекция найденных задач (может вернуть пустую коллекцию)
     */
    Collection<Task> findAll();
    /**
     * Создает новую задачу в базе данных
     * @param task ссылка на экземпляр Task
     * @return сохраненный экземпляр задачи с присвоенным ID
     */
    Task save(Task task);
    /**
     * Обновляет данные существующей задачи
     * @param task ссылка на экземпляр Task
     * @return обновленный экземпляр задачи
     */
    Task update(Task task);
    /**
     *Удаляет задачу по ее ссылке на экземпляр
     * @param task ссылка на экземпляр Task
     */
    void delete(Task task);
    /**
     * Удаляет задачу по его ID
     * @param id уникальный идентификатор задачи
     */
    void deleteById(Long id);
    /**
     * Возвращает все связаные с данным клиентом проекты
     * @param description описание задачи
     * @param status статус задачи
     * @param project ссылка на экземпляр Project
     * @return коллекция найденных задач (может быть пустой)
     */
    Collection<Task> search(String description, String status, Project project);

    /**
     * Добавляет экземпляр задачи в соответствующий проект
     * @param project ссылка на экземпляр Project
     * @param task ссылка на экземпляр Task
     */
    void addTask(Project project, Task task);

    /**
     * Возвращает все связаные с данным проектом задачи
     * @param project ссылка на экземпляр Project
     * @return коллекция найденных задач (может быть пустой)
     */
    Collection<Task> findByProject(Project project);
}
