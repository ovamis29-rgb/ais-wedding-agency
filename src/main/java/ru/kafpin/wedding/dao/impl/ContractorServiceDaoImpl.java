package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ContractorDao;
import ru.kafpin.wedding.dao.ContractorServiceDao;
import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.ContractorService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class ContractorServiceDaoImpl implements ContractorServiceDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
   @Override
    public Optional<ContractorService> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<ContractorService> services = mapper(rs);
            return services.isEmpty() ? Optional.empty() : Optional.of(services.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<ContractorService> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContractorService save(ContractorService service) {
        service.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.save"),new String[] {"id"})){
            statement.setLong(1,service.getContractor().getId());
            statement.setString(2,service.getService());
            statement.setBigDecimal(3, service.getPrice());
            statement.setBigDecimal(4,service.getPrepayment());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                service.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return service;
    }

    @Override
    public ContractorService update(ContractorService service) {
        service.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.update"))){
            statement.setLong(1,service.getContractor().getId());
            statement.setString(2,service.getService());
            statement.setBigDecimal(3, service.getPrice());
            statement.setBigDecimal(4,service.getPrepayment());
            statement.setLong(5,service.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return service;
    }

    @Override
    public void delete(ContractorService service) {
        if(service!=null){
            Contractor contractor = service.getContractor();
            contractor.getServices().remove(service);
            deleteById(service.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.delete"))){
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<ContractorService> search(ContractorService promt) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.search"))){
            statement.setString(1,promt.getService());
            statement.setBigDecimal(2,promt.getPrice());
            statement.setBigDecimal(3,promt.getPrepayment());
            statement.setLong(4,promt.getContractor().getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Collection<ContractorService> findByContractor(Contractor contractor) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.findByContractorId"))){
            statement.setLong(1, contractor.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean serviceExists(Contractor contractor, String serviceName) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("contractorService.serviceExists"))){
            statement.setLong(1,contractor.getId());
            statement.setString(2,serviceName);
            ResultSet rs = statement.executeQuery();
            return rs.getBoolean("service_exists");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<ContractorService> mapper(ResultSet rs){
        List<ContractorService> list = new ArrayList<>();
        ContractorDao contractorDao = new ContractorDaoImpl();
        try{
            while(rs.next()){
                list.add(ContractorService.builder()
                        .id(rs.getLong("id"))
                        .contractor(contractorDao.getById(rs.getLong("contractor_id")).orElse(null))
                        .service(rs.getString("service"))
                        .price(rs.getBigDecimal("price"))
                        .prepayment(rs.getBigDecimal("prepayment"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}