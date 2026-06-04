package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.UsedContractor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с подрядчиками проекта в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface UsedContractorDao {
    /**
     * Возвращает подрядчика проекта по его идентификатору
     * @return объект Optional с найденным подрядчиком, или пустой Optional, если подрядчика не существует
     */
    Optional<UsedContractor> getById(Long id);
    /**
     * Находит весь список подрядчиком проекта
     * @return коллекция найденных подрядчиком (может вернуть пустую коллекцию)
     */
    Collection<UsedContractor> findAll();
    /**
     * Создает нового подрядчика проекта в базе данных
     * @param usedContractor ссылка на экземпляр UsedContractor
     * @return сохраненный экземпляр подрядчика проекта с присвоенным ID
     */
    UsedContractor save(UsedContractor usedContractor);
    /**
     * Обновляет данные существующего подрядчика проекта
     * @param usedContractor ссылка на экземпляр UsedContractor
     * @return обновленный экземпляр подрядчика проекта
     */
    UsedContractor update(UsedContractor usedContractor);
    /**
     *Удаляет подрядчика проекта по ее ссылке на экземпляр
     * @param usedContractor ссылка на экземпляр UsedContractor
     */
    void delete(UsedContractor usedContractor);
    /**
     * Удаляет подрядчика проекта по его ID
     * @param id уникальный идентификатор подрядчика проекта
     */
    void deleteById(Long id);

    /**
     * Выполняет поиск использованных подрядчиков по id проекта и id подрядчика
     * @param project ссылка на экземпляр Project
     * @param contractor ссылка на экземпляр Contractor
     * */
    void deleteByProjectAndContractor(Project project, Contractor contractor);
}
