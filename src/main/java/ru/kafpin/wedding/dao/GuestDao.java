package ru.kafpin.wedding.dao;

import ru.kafpin.wedding.model.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Предоставляет методы для работы с гостями в базе данных
 * Все методы могут выбросить {@link SQLException} при ошибках SQL
 */
public interface GuestDao {
    /**
     *  Возвращает услугу гостя по ее идентификатору
     * @param id уникальный идентификатор гостя
     * @return объект Optional с найденной гостем, или пустой Optional, если гость не существует
     */
    Optional<Guest> getById(Long id);
    /**
     * Находит весь список гостей
     * @return коллекция найденных гостей (может быть пустой)
     */
    Collection<Guest> findAll();
    /**
     * Создает нового гостя в базе данных
     * @param guest ссылка на экземпляр Guest
     * @return сохраненный экземпляр гостя с присвоенным ID
     */
    Guest save(Guest guest);
    /**
     * Обновляет данные существующего гостя
     * @param guest ссылка на экземпляр Guest
     * @return обновленный экземпляр гостя
     */
    Guest update(Guest guest);

    /**
     * Удаляет гостя по его ссылке на экземпляр
     * @param guest ссылка на экземпляр Guest
     */
    void delete(Guest guest);

    /**
     * Удаляет гостя по его ID
     * @param id уникальный идентификатор гостя
     */
    void deleteById(Long id);
    /**
     * Выполняет поиск услуги подрядчика по описанию, цене и предоплате
     * @param name фамилия
     * @param lastname имя
     * @param middleName отчетсво
     * @return коллекция найденных гостей (может быть пустой)
     */
    Collection<Guest> search(String name, String lastname, String middleName);

    /**
     * Добавляет экземпляр в список гостей проекта
     * @param project ссылка на экземпляр Project
     * @param guest ссылка на экземпляр Guest
     */
    void addGuestToProject(Project project, Guest guest);

    /**
     * Выполняет поиск гостей по id проекта
     * @param project ссылка на экземпляр Project
     * @return коллекция найденных гостей (может быть пустой)
     */
    Collection<Guest> findByProject(Project project);

    /**
     * Удаляет гостя по id проекта
     * @param project ссылка на экземпляр Project
     * @param guest ссылка на экземпляр Guest
     */
    void DeleteFromProject(Project project,Guest guest);

    /**
     * Выясняет входит-ли гость в список гостей проекта
     * @param project ссылка на экземпляр Project
     * @param guest guest ссылка на экземпляр Guest
     * @return Boolean значение в зависимрсти от того, есть ли гость в списке проекта
     */
    Boolean IsitProjectGuest(Project project,Guest guest);

}
