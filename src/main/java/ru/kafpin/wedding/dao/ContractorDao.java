package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.UsedContractor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с подрядчиками в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ContractorDao {
    /**
     * Возвращает подрядчика по его идентификатору
     * @param id уникальный идентификатор подрядчика
     * @return объект Optional с найденным подрядчиком, или пустой Optional, если подрядчик не существует
     */
    Optional<Contractor> getById(Long id);

    /**
     * Находит весь список подрядчиков
     * @return коллекция найденных подрядчиков (может быть пустой)
     */
    Collection<Contractor> findAll();

    /**
     * Создает нового подрядчика в базе данных
     * @param contractor ссылка на экземпляр Contractor
     * @return сохраненный экземпляр подрядчика с присвоенным ID
     */
    Contractor save(Contractor contractor);

    /**
     * Обновляет данные существующего подрядчика
     * @param contractor ссылка на экземпляр Contractor
     * @return обновленный экземпляр подрядчика
     */
    Contractor update(Contractor contractor);

    /**
     * Удаляет подрядчика по его ссылке на экземпляр
     * @param contractor ссылка на экземпляр Contractor
     */
    void delete(Contractor contractor);

    /**
     * Удаляет подрядчика по его ID
     * @param id уникальный идентификатор подрядчика
     */
    void deleteById(Long id);

    /**
     * Выполняет поиск подрядчика по фамилии, имени и номеру телефона
     * @param phoneNumber номер подрядчика
     * @param name имя подрядчика
     * @param lastname фамилия подрядчика
     * @return коллекция найденных подрядчиков (может быть пустой)
     */
    Collection<Contractor> search(String phoneNumber, String name, String lastname);

    /**
     * Выполняет проверку есть ли данного подрячдика еще задачи в проекте
     * @param project ссылка на экземпляр Project
     * @param contractor ссылка на экземпляр Contractor
     * @return true если у подрядчика еще есть задачи, иначе false
     */
    Boolean contrHaveAnyTasks(Project project,Contractor contractor);
}
