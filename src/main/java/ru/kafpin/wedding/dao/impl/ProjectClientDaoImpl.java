package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ClientDao;
import ru.kafpin.wedding.dao.ProjectClientDao;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.model.ProjectClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProjectClientDaoImpl implements ProjectClientDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
    @Override
    public Optional<ProjectClient> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("projectClient.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<ProjectClient> projectClients = mapper(rs);
            return projectClients.isEmpty() ? Optional.empty() : Optional.of(projectClients.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ProjectClient> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("projectClient.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProjectClient save(ProjectClient client) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("projectClient.save"),new String[] {"id"})){
            statement.setLong(1,client.getProject().getId());
            statement.setLong(2,client.getClient().getId());
            statement.setString(3,client.getRole());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                client.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public ProjectClient update(ProjectClient client) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("projectClient.update"))){
            statement.setLong(1,client.getProject().getId());
            statement.setLong(2,client.getClient().getId());
            statement.setString(3,client.getRole());
            statement.setLong(4,client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public void delete(ProjectClient client) {
        if(client!=null){
            deleteById(client.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("projectClient.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<ProjectClient> mapper(ResultSet rs){
        List<ProjectClient> list = new ArrayList<>();
        ClientDao client = new ClientDaoImpl();
        ProjectDao project = new ProjectDaoImpl();
        try{
            while(rs.next()){
                list.add(ProjectClient.builder()
                        .id(rs.getLong("id"))
                        .project(project.getById(rs.getLong("project_id")).orElse(null))
                        .client(client.getById(rs.getLong("client_id")).orElse(null))
                        .role(rs.getString("role"))
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}