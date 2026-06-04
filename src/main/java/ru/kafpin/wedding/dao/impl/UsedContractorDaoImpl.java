package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ContractorDao;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.dao.UsedContractorDao;
import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Project;
import ru.kafpin.wedding.model.UsedContractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UsedContractorDaoImpl implements UsedContractorDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
   @Override
    public Optional<UsedContractor> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<UsedContractor> usedContractors = mapper(rs);
            return usedContractors.isEmpty() ? Optional.empty() : Optional.of(usedContractors.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<UsedContractor> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UsedContractor save(UsedContractor usedContractor) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.save"),new String[] {"id"})){
            statement.setLong(1, usedContractor.getProject().getId());
            statement.setLong(2, usedContractor.getContractor().getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                usedContractor.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usedContractor;
    }

    @Override
    public UsedContractor update(UsedContractor usedContractor) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.update"))){
            statement.setLong(1, usedContractor.getProject().getId());
            statement.setLong(2, usedContractor.getContractor().getId());
            statement.setLong(3, usedContractor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usedContractor;
    }

    @Override
    public void delete(UsedContractor usedContractor) {
        if(usedContractor != null){
            deleteById(usedContractor.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.delete"))){
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByProjectAndContractor(Project project, Contractor contractor) {
        if(project==null && contractor==null) return;
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("usedContractor.findByProjectAndContractor"))){
            statement.setLong(1,project.getId());
            statement.setLong(2,contractor.getId());
            ResultSet rs = statement.executeQuery();
            deleteById(rs.getLong("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<UsedContractor> mapper(ResultSet rs){
        List<UsedContractor> list = new ArrayList<>();
        ContractorDao contractor = new ContractorDaoImpl();
        ProjectDao project = new ProjectDaoImpl();
        try{
            while(rs.next()){
                list.add(UsedContractor.builder()
                        .id(rs.getLong("id"))
                        .project(project.getById(rs.getLong("project_id")).orElse(null))
                        .contractor(contractor.getById(rs.getLong("contractor_id")).orElse(null))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}