package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ContractorDao;
import ru.kafpin.wedding.dao.ContractorServiceDao;
import ru.kafpin.wedding.dao.PortfolioDao;
import ru.kafpin.wedding.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ContractorDaoImpl implements ContractorDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
  @Override
    public Optional<Contractor> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Contractor> contractors = mapper(rs);
            return contractors.isEmpty() ? Optional.empty() : Optional.of(contractors.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Contractor> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.findAll"))){
            ResultSet rs = statement.executeQuery();
            List<Contractor> contractors = mapper(rs);
            ContractorServiceDao serviceDao = new ContractorServiceDaoImpl();
            PortfolioDao portfolioDao = new PortfolioDaoImpl();
            for (Contractor contractor : contractors) {
                Collection<ContractorService> services = serviceDao.findByContractor(contractor);
                contractor.setServices(new ArrayList<>(services));
                Collection<Portfolio> portfolios = portfolioDao.findByContractor(contractor);
                contractor.setPortfolio(new ArrayList<>(portfolios));
            }
            return contractors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Contractor save(Contractor contractor) {
        contractor.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.save"),new String[] {"id"})){
            statement.setString(1,contractor.getPhoneNumber());
            statement.setString(2,contractor.getEmail());
            statement.setString(3, contractor.getName());
            statement.setString(4,contractor.getLastname());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                contractor.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ContractorServiceDao serviceDao = new ContractorServiceDaoImpl();
        Collection<ContractorService> services = serviceDao.findByContractor(contractor);
        contractor.setServices(new ArrayList<>(services));
        return contractor;
    }

    @Override
    public Contractor update(Contractor contractor) {
        contractor.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.update"))){
            statement.setString(1,contractor.getPhoneNumber());
            statement.setString(2,contractor.getEmail());
            statement.setString(3, contractor.getName());
            statement.setString(4,contractor.getLastname());
            statement.setLong(5,contractor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contractor;
    }

    @Override
    public void delete(Contractor contractor) {
        if(contractor!=null){
            deleteById(contractor.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Contractor> search(String phoneNumber, String name, String lastname) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractor.search"))){
            statement.setString(1,(phoneNumber==null || phoneNumber.isEmpty()? "" : "%"+phoneNumber+"%"));
            statement.setString(2,(name==null || name.isEmpty()? "" : "%"+name+"%"));
            statement.setString(3,(lastname==null || lastname.isEmpty()? "" : "%"+lastname+"%"));
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean contrHaveAnyTasks(Project project, Contractor contractor) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.contrHaveAnyTasks"))){
            statement.setLong(1,project.getId());
            statement.setLong(2,contractor.getId());
            ResultSet rs = statement.executeQuery();
            return rs.getBoolean("conrhavetasksinproject");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Contractor> mapper(ResultSet rs) {
        List<Contractor> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(Contractor.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .lastname(rs.getString("lastname"))
                        .phoneNumber(rs.getString("phone_number"))
                        .email(rs.getString("email"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
