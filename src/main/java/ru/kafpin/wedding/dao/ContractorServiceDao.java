package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.ContractorService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с услугами подрядчика в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ContractorServiceDao {
    /**
     *  Возвращает услугу подрядчика по ее идентификатору
     * @param id уникальный идентификатор услуги подрядчика
     * @return объект Optional с найденной услугой подрядчика, или пустой Optional, если услуга не существует
     */
    Optional<ContractorService> getById(Long id);

    /**
     * Находит весь список услуг подрядчика
     * @return коллекция найденных услуг подрядчика (может быть пустой)
     */
    Collection<ContractorService> findAll();

    /**
     * Создает новую услугу в базе данных
     * @param service ссылка на экземпляр ContractorService
     * @return сохраненный экземпляр услуги с присвоенным ID
     */
    ContractorService save(ContractorService service);

    /**
     * Обновляет данные существующей услуги
     * @param service ссылка на экземпляр ContractorService
     * @return обновленный экземпляр услуги
     */
    ContractorService update(ContractorService service);

    /**
     * Удаляет услугу подрядчика по его ссылке на экземпляр
     * @param service ссылка на экземпляр ContractorService
     */
    void delete(ContractorService service);

    /**
     * Удаляет услугу по ее ID
     * @param id уникальный идентификатор услуги
     */
    void deleteById(Long id);

    /**
     * Выполняет поиск услуги подрядчика по описанию, цене и предоплате
     * @param promt экземпляр с заполненными полями для поиска по ним
     * @return коллекция найденных услуг (может быть пустой)
     */
    Collection<ContractorService> search(ContractorService promt);
    /**
     * Выполняет поиск услуг по id подрядчика
     * @param contractor ссылка на экземпляр Contractor
     * @return коллекция найденных услуг (может быть пустой)
     */
    Collection<ContractorService> findByContractor(Contractor contractor);

    /**
     * Выполняет проверку существует-ли данная услуга у подрядчика
     * @param contractor ссылка на экземпляр Contractor
     * @param serviceName описание услуги
     * @return true если услуга существует, иначе false
     */
    Boolean serviceExists(Contractor contractor, String serviceName);
}
