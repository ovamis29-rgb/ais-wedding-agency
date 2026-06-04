package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.GuestDao;
import ru.kafpin.wedding.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class GuestDaoImpl implements GuestDao{
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
  @Override
    public Optional<Guest> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Guest> guests = mapper(rs);
            return guests.isEmpty() ? Optional.empty() : Optional.of(guests.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Guest> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Guest save(Guest guest) {
        guest.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.save"),new String[] {"id"})){
            statement.setString(1,guest.getName());
            statement.setString(2,guest.getLastname());
            statement.setString(3,guest.getMiddleName());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                guest.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guest;
    }

    @Override
    public Guest update(Guest guest) {
        guest.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.update"))){
            statement.setString(1,guest.getName());
            statement.setString(2,guest.getLastname());
            statement.setString(3,guest.getMiddleName());
            statement.setLong(4,guest.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guest;
    }

    @Override
    public void delete(Guest guest) {
        if(guest!=null){
            deleteById(guest.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Guest> search(String name, String lastname, String middleName) {
        boolean isNameEmpty = (name == null || name.isEmpty());
        boolean isLastnameEmpty = (lastname == null || lastname.isEmpty());
        boolean isMiddleNameEmpty = (middleName == null || middleName.isEmpty());
        if (isNameEmpty && isLastnameEmpty && isMiddleNameEmpty) {
            return findAll();
        }
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.search"))){
            statement.setString(1, (name == null || name.isEmpty() ? "" : "%" + name.trim() + "%"));
            statement.setString(2, (lastname == null || lastname.isEmpty() ? "" : "%" + lastname.trim() + "%"));
            statement.setString(3, (middleName == null || middleName.isEmpty() ? "" : "%" + middleName.trim() + "%"));
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Guest> findByProject(Project project){
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.findByProject"))) {
            statement.setLong(1,project.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void addGuestToProject(Project project, Guest guest) {
        if(project == null || guest == null){
            return;
        }
        if(project.getGuests() == null){
            project.setGuests(new ArrayList<>());
        }
        project.getGuests().add(guest);
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.addGuest"))){
            statement.setLong(1,guest.getId());
            statement.setLong(2,project.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void DeleteFromProject(Project project,Guest guest) {
        if(project == null || guest == null){
            return;
        }else{
            try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.deleteFromProject"))){
                statement.setLong(1,project.getId());
                statement.setLong(2,guest.getId());
                statement.executeUpdate();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (project.getGuests() != null) {
                project.getGuests().remove(guest);
            }
        }
    }

    @Override
    public Boolean IsitProjectGuest(Project project, Guest guest) {
        if(project==null && guest==null) return null;
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("guest.isItProjectGuest"))){
            statement.setLong(1,project.getId());
            statement.setLong(2,guest.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_it_project_guest");
            }
            return null;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    protected List<Guest> mapper(ResultSet rs){
        List<Guest> list = new ArrayList<>();
        try{
            while(rs.next()){
                list.add(Guest.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .lastname(rs.getString("lastname"))
                        .middleName(rs.getString("middle_name"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}