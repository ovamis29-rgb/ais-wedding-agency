package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.dao.ReportDao;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.Report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;


public class ReportDaoImpl implements ReportDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
    @Override
    public Optional<Report> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Report> reports = mapper(rs);
            return reports.isEmpty() ? Optional.empty() : Optional.of(reports.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Report> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Report save(Long id) {
        Report report = new Report();
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.save"), new String[]{"id"})) {
            report = getTotalStats(id);
            report.setCreatedAt(LocalDateTime.now());
            statement.setLong(1, id);
            statement.setBigDecimal(2, report.getTotalBudget());
            statement.setLong(3, report.getCompletedTasksCount());
            statement.setLong(4, report.getAmountOfGuests());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                report.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    @Override
    public Report update(Report report) {
        report.setUpdatedAt(LocalDateTime.now());
        try (PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.update"))) {
            Report stats = getTotalStats(report.getProject().getId());
            report.setTotalBudget(stats.getTotalBudget());
            report.setCompletedTasksCount(stats.getCompletedTasksCount());
            report.setAmountOfGuests(stats.getAmountOfGuests());
            statement.setBigDecimal(1, report.getTotalBudget());
            statement.setLong(2, report.getCompletedTasksCount());
            statement.setLong(3, report.getAmountOfGuests());
            statement.setLong(4, report.getProject().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return report;
    }

    @Override
    public void delete(Report report) {
        if(report!=null){
            deleteById(report.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Report> findByProjectDate(LocalDateTime date) {
        ProjectDao projectDao = new ProjectDaoImpl();
        Long projectId = projectDao.findByDate(date)
                .map(Project::getId)
                .orElse(null);
        if(projectId == null){
            return Optional.empty();
        }
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.search"))){
            statement.setLong(1, projectId);
            ResultSet rs = statement.executeQuery();
            List<Report> reports = mapper(rs);
            return reports.isEmpty() ? Optional.empty() : Optional.of(reports.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Report getTotalStats(Long project_id){
        Report report = new Report();
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("report.reportStats"))){
            statement.setLong(1, project_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                report.setTotalBudget(rs.getBigDecimal("total_budget"));
                report.setCompletedTasksCount(rs.getLong("completed_tasks_count"));
                report.setAmountOfGuests(rs.getLong("amount_of_guests"));
                return report;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Report> mapper(ResultSet rs){
        List<Report> list = new ArrayList<>();
        ProjectDao project = new ProjectDaoImpl();
        try{
            while(rs.next()){
                list.add(Report.builder()
                        .id(rs.getLong("id"))
                        .project(project.getById(rs.getLong("project_id")).orElse(null))
                        .totalBudget(rs.getBigDecimal("total_budget"))
                        .completedTasksCount(rs.getLong("completed_tasks_count"))
                        .amountOfGuests(rs.getLong("amount_of_guests"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}