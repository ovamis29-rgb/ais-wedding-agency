package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ClientDao;
import ru.kafpin.wedding.model.Client;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class ClientDaoImpl implements ClientDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
  @Override
    public Optional<Client> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Client> clients = mapper(rs);
            return clients.isEmpty() ? Optional.empty() : Optional.of(clients.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Client> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<Client> search(String name, String lastname, String middleName){
        boolean isNameEmpty = (name == null || name.isEmpty());
        boolean isLastnameEmpty = (lastname == null || lastname.isEmpty());
        boolean isMiddleNameEmpty = (middleName == null || middleName.isEmpty());
        if (isNameEmpty && isLastnameEmpty && isMiddleNameEmpty) {
            return findAll();
        }
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.search"))){
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
    public Client save(Client client) {
        client.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.save"),new String[] {"id"})){
            statement.setString(1,client.getPhoneNumber());
            statement.setString(2,client.getEmail());
            statement.setString(3,client.getName());
            statement.setString(4,client.getLastname());
            statement.setString(5,client.getMiddleName());
            statement.setString(6,client.getGender());
            statement.setDate(7, Date.valueOf(client.getDateOfRegistration()));
            statement.setString(8,client.getPlaceOfBirth());
            statement.setString(9,client.getSeries());
            statement.setString(10,client.getNumber());
            statement.setString(11,client.getIssuedBy());
            statement.setDate(12,Date.valueOf(client.getWhenIssued()));
            statement.setString(13,client.getRegistrationAdress());
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
    public Client update(Client client) {
        client.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.update"))){
            statement.setString(1,client.getPhoneNumber());
            statement.setString(2,client.getEmail());
            statement.setString(3,client.getName());
            statement.setString(4,client.getLastname());
            statement.setString(5,client.getMiddleName());
            statement.setString(6,client.getGender());
            statement.setDate(7, Date.valueOf(client.getDateOfRegistration()));
            statement.setString(8,client.getPlaceOfBirth());
            statement.setString(9,client.getSeries());
            statement.setString(10,client.getNumber());
            statement.setString(11,client.getIssuedBy());
            statement.setDate(12,Date.valueOf(client.getWhenIssued()));
            statement.setString(13,client.getRegistrationAdress());
            statement.setLong(14,client.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public void delete(Client client) {
        if(client!=null){
            deleteById(client.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("client.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected List<Client> mapper(ResultSet rs) {
        List<Client> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(Client.builder()
                        .id(rs.getLong("id"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .lastname(rs.getString("lastname"))
                        .middleName(rs.getString("middle_name"))
                        .gender(rs.getString("gender"))
                        .dateOfRegistration(rs.getDate("date_of_registration").toLocalDate())
                        .placeOfBirth(rs.getString("place_of_birth"))
                        .series(rs.getString("series"))
                        .number(rs.getString("number"))
                        .issuedBy(rs.getString("issued_by"))
                        .whenIssued(rs.getDate("when_issued").toLocalDate())
                        .registrationAdress(rs.getString("registration_adress"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
