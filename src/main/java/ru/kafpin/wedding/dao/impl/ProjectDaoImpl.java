package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ProjectDao;
import ru.kafpin.wedding.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ProjectDaoImpl implements ProjectDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
        @Override
    public Optional<Project> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Project> projects = mapper(rs);
            return projects.isEmpty() ? Optional.empty() : Optional.of(projects.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Project> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project save(Project project) {
        project.setCreatedAt(LocalDateTime.now());
        if (project.getStatus() == null || project.getStatus().isBlank()) {
            project.setStatus("ожидает выполнения");
        }
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.save"), new String[] {"id"})){
            statement.setTimestamp(1, Timestamp.valueOf(project.getWeddingDate()));
            statement.setString(2, project.getStatus());
            statement.setString(3, project.getWishesForWedding());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                project.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return project;
    }

    @Override
    public Project update(Project project) {
        project.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.update"))){
            statement.setTimestamp(1,Timestamp.valueOf(project.getWeddingDate()));
            statement.setString(2, project.getStatus());
            statement.setString(3,project.getWishesForWedding());
            statement.setLong(4,project.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return project;
    }

    @Override
    public void delete(Project project) {
        if(project!=null){
            deleteById(project.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Project> findByDate(LocalDateTime date) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.searchByDate"))){
            statement.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = statement.executeQuery();
            List<Project> projects = mapper(rs);
            return projects.isEmpty() ? Optional.empty() : Optional.of(projects.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ProjectClient> addSpouses(Project project, Client bride, Client groom) {
        if (project.getProjectClients() == null) {
            project.setProjectClients(new ArrayList<>());
        }
        List<ProjectClient> list = new ArrayList<>();
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.addSpouses"), new String[] {"id"})){
            for(Client spouse : List.of(bride, groom)){
                statement.setLong(1, project.getId());
                statement.setLong(2, spouse.getId());
                statement.setString(3, spouse == bride ? "невеста" : "жених");
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()){
                    ProjectClient pc = ProjectClient.builder()
                            .id(rs.getLong("id"))
                            .project(project)
                            .client(spouse)
                            .role(spouse == bride ? "невеста" : "жених")
                            .build();
                    list.add(pc);
                    project.getProjectClients().add(pc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Collection<Project> relatedProjects(Client client) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.relatedProjects"))){
            statement.setLong(1, client.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addContractorToProject(Contractor contractor,Project project){
        if(contractor==null)return;
        project.getContractors().add(contractor);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("project.addContractorToProject"))){
            statement.setLong(1,project.getId());
            statement.setLong(2,contractor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected List<Project> mapper(ResultSet rs){
        List<Project> list = new ArrayList<>();
        ClientDaoImpl clientDao = new ClientDaoImpl();
        try{
            while(rs.next()){
                Long projectId = rs.getLong("id");
                List<Contractor> contractors = new ArrayList<>();
                try(PreparedStatement st = DBHelper.getConnection().prepareStatement(bundle.getString("project.findContractorsByProject"))){
                    st.setLong(1, projectId);
                    ResultSet rsC = st.executeQuery();
                    while(rsC.next()){
                        contractors.add(Contractor.builder()
                                .id(rsC.getLong("id"))
                                .name(rsC.getString("name"))
                                .lastname(rsC.getString("lastname"))
                                .email(rsC.getString("email"))
                                .phoneNumber(rsC.getString("phone_number"))
                                .build());
                    }
                }
                String status = rs.getString("status");
                if (status == null) {
                    status = "ожидает выполнения";
                }
                Project project = Project.builder()
                        .id(projectId)
                        .weddingDate(rs.getTimestamp("wedding_date").toLocalDateTime())
                        .status(status)
                        .wishesForWedding(rs.getString("wishes_for_wedding"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .contractors(contractors)
                        .projectClients(new ArrayList<>())
                        .build();
                try(PreparedStatement st = DBHelper.getConnection().prepareStatement(bundle.getString("project.findClientsByProject"))){
                    st.setLong(1, projectId);
                    ResultSet rsCl = st.executeQuery();
                    while(rsCl.next()){
                        Client client = clientDao.getById(rsCl.getLong("client_id")).orElse(null);
                        project.getProjectClients().add(ProjectClient.builder()
                                .id(rsCl.getLong("pc_id"))
                                .role(rsCl.getString("role"))
                                .client(client)
                                .project(project)
                                .build());
                    }
                }
                list.add(project);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}