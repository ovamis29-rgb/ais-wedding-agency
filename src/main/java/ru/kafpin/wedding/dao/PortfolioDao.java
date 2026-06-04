package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Guest;
import ru.kafpin.wedding.model.Portfolio;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с портфолио подрядчика в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface PortfolioDao {
    /**
     * Возвращает портфолио подрядчика по ее идентификатору
     * @param id уникальный идентификатор портфолио
     * @return объект Optional с найденным портфолио, или пустой Optional, если портфолио не существует
     */
    Optional<Portfolio> getById(Long id);
    /**
     * Находит весь список портфолио
     * @return коллекция найденных портфолио (может быть пустой)
     */
    Collection<Portfolio> findAll();
    /**
     * Создает новое портфолио в базе данных
     * @param portfolio ссылка на экземпляр Portfolio
     * @return сохраненный экземпляр портфолио с присвоенным ID
     */
    Portfolio save(Portfolio portfolio);
    /**
     * Обновляет данные существующего портфолио
     * @param portfolio ссылка на экземпляр Portfolio
     * @return обновленный экземпляр портфолио
     */
    Portfolio update(Portfolio portfolio);
    /**
     * Удаляет портфолио по его ссылке на экземпляр
     * @param portfolio ссылка на экземпляр Portfolio
     */
    void delete(Portfolio portfolio);
    /**
     * Удаляет портфолио по его ID
     * @param id уникальный идентификатор портфолио
     */
    void deleteById(Long id);
    /**
     * Выполняет поиск портфолио по описанию, цене и подрядчику
     * @param description описание
     * @param contractor ссылка на экземпляр Contractor
     * @return коллекция найденных портфолио (может быть пустой)
     */
    Collection<Portfolio> search(String description, Contractor contractor);

    /**
     * Выполняет поиск портфолио по подрядчику
     * @param contractor ссылка на экземпляр Contractor
     * @return коллекция найденных портфолио (может быть пустой)
     */
    Collection<Portfolio> findByContractor(Contractor contractor);

    /**
     * Выполняет проверку на то существует-ли у подрядчика такое портфолио
     * @param contractor ссылка на экземпляр Contractor
     * @param urlImg путь к изображению
     * @return true если портфолио существует, иначе false
     */
    Boolean portfolioExist(Contractor contractor,String urlImg);
}
