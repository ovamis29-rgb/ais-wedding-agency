package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ContractorServiceDao;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.dao.TaskDao;
import ru.kafpin.wedding.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class TaskDaoImpl implements TaskDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
   @Override
    public Optional<Task> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Task> tasks = mapper(rs);
            return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Task> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task save(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.save"), new String[] {"id"})){
            statement.setLong(1, task.getContractorService().getId());
            statement.setBigDecimal(2, task.getPrice());
            statement.setString(3, task.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(task.getDeadline()));
            statement.setInt(5, task.getPriority());
            statement.setLong(6, task.getProject().getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                task.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public Task update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.update"))){
            statement.setLong(1, task.getContractorService().getId());
            statement.setBigDecimal(2, task.getPrice());
            statement.setString(3, task.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(task.getDeadline()));
            statement.setInt(5, task.getPriority());
            statement.setLong(6, task.getProject().getId());
            statement.setLong(7, task.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return task;
    }

    @Override
    public void delete(Task task) {
        if(task != null){
            Project project = task.getProject();
            project.getTasks().remove(getById(task.getId()));
            deleteById(task.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.delete"))){
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Task> search(String description, String status,Project project) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.search"))){
            statement.setString(1, (description == null || description.isEmpty() ? "%" : "%" + description + "%"));
            statement.setString(2, (status == null || status.isEmpty() ? "%" : "%" + status + "%"));
            statement.setLong(3,project.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Task> findByProject(Project project) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("task.findByProject"))){
            statement.setLong(1,project.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(Project project, Task task) {
        if(project.getTasks() == null){
            project.setTasks(new ArrayList<>());
        }
        project.getTasks().add(task);
        task.setProject(project);
    }

    protected List<Task> mapper(ResultSet rs){
        List<Task> list = new ArrayList<>();
        ProjectDao projectDao = new ProjectDaoImpl();
        ContractorServiceDao contractorServiceDao = new ContractorServiceDaoImpl();
        try{
            while(rs.next()){
                list.add(Task.builder()
                        .id(rs.getLong("id"))
                        .contractorService(contractorServiceDao.getById(rs.getLong("contractor_service_id")).orElse(null))
                        .price(rs.getBigDecimal("price"))
                        .status(rs.getString("status"))
                        .deadline(rs.getTimestamp("deadline").toLocalDateTime())
                        .priority(rs.getInt("priority"))
                        .project(projectDao.getById(rs.getLong("project_id")).orElse(null))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}