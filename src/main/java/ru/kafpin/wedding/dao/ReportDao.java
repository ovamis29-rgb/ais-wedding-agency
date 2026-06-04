package ru.kafpin.wedding.dao;
import ru.kafpin.wedding.model.Report;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с отчетами в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface ReportDao {
    /**
     * Возвращает отчет по его идентификатору
     * @return объект Optional с найденным отчетом, или пустой Optional, если отчета не существует
     */
    Optional<Report> getById(Long id);
    /**
     * Находит весь список отчетов
     * @return коллекция найденных отчетов (может вернуть пустую коллекцию)
     */
    Collection<Report> findAll();
    /**
     * Создает новый отчет в базе данных
     * @param id уникальный идентификатор проекта
     * @return сохраненный экземпляр отчета с присвоенным ID
     */
    Report save(Long id);
    /**
     * Обновляет данные существующего отчета
     * @param report ссылка на экземпляр Project
     * @return обновленный экземпляр отчета
     */
    Report update(Report report);
    /**
     *Удаляет отчет по его ссылке на экземпляр
     * @param report ссылка на экземпляр Project
     */
    void delete(Report report);
    /**
     * Удаляет отчет по его ID
     * @param id уникальный идентификатор отчета
     */
    void deleteById(Long id);
    /**
     * Выплняет поиск отчет по указанной дате
     * @param date дата свадьбы
     * @return объект Optional с найденной отчетом, или пустой Optional, если отчет не существует
     */
    Optional<Report> findByProjectDate(LocalDateTime date);
    /**
     * Собирает и возвращает статистические данные по проекту
     * (общий бюджет, количество выполненных задач и количество гостей).
     * Используется для генерации или обновления полей отчета.
     * @param project_id уникальный идентификатор проекта
     * @return экземпляр отчета, наполненный статистическими данными из базы,
     *         или экземпляр с дефолтными значениями, если данных нет
     */
    Report getTotalStats(Long project_id);
}
