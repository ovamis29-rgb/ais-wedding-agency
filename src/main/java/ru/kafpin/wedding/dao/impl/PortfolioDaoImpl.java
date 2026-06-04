package ru.kafpin.wedding.dao.impl;

import ru.kafpin.wedding.util.DBHelper;
import ru.kafpin.wedding.dao.ContractorDao;
import ru.kafpin.wedding.dao.PortfolioDao;
import ru.kafpin.wedding.model.Contractor;
import ru.kafpin.wedding.model.Portfolio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class PortfolioDaoImpl implements PortfolioDao {
    ResourceBundle bundle = ResourceBundle.getBundle("statments");
   @Override
    public Optional<Portfolio> getById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.findById"))){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<Portfolio> portfolios = mapper(rs);
            return portfolios.isEmpty() ? Optional.empty() : Optional.of(portfolios.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Portfolio> findAll() {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.findAll"))){
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        portfolio.setCreatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.save"),new String[] {"id"})){
            statement.setString(1, portfolio.getDescription());
            statement.setLong(2, portfolio.getContractor().getId());
            statement.setString(3, portfolio.getImgUrl());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()) {
                portfolio.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return portfolio;
    }

    @Override
    public Portfolio update(Portfolio portfolio) {
        portfolio.setUpdatedAt(LocalDateTime.now());
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.update"))){
            statement.setString(1, portfolio.getDescription());
            statement.setLong(2, portfolio.getContractor().getId());
            statement.setString(3, portfolio.getImgUrl());
            statement.setLong(4, portfolio.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return portfolio;
    }

    @Override
    public void delete(Portfolio portfolio) {
        if (portfolio != null) {
            deleteById(portfolio.getId());
            try {
                Contractor contractor = portfolio.getContractor();
                if (contractor.getPortfolio() != null && contractor!=null) {
                    contractor.getPortfolio().remove(portfolio);
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.delete"))){
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Portfolio> search(String description, Contractor contractor) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.search"))){
            statement.setString(1, (description == null ? "" : "%" + description + "%"));
            statement.setLong(2, contractor.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean portfolioExist(Contractor contractor, String urlImg) {
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.portfolioExists"))){
            statement.setLong(1,contractor.getId());
            statement.setString(2,urlImg);
            ResultSet rs = statement.executeQuery();
            return rs.getBoolean("portfolio_exists");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Portfolio> findByContractor(Contractor contractor) {
        if (contractor == null || contractor.getId() == null) {
            return new ArrayList<>();
        }
        try(PreparedStatement statement = DBHelper.getConnection().prepareStatement(bundle.getString("portfolio.findByContractor"))){
            statement.setLong(1,contractor.getId());
            ResultSet rs = statement.executeQuery();
            return mapper(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<Portfolio> mapper(ResultSet rs){
        List<Portfolio> list = new ArrayList<>();
        ContractorDao contractor = new ContractorDaoImpl();
        try{
            while(rs.next()){
                list.add(Portfolio.builder()
                        .id(rs.getLong("id"))
                        .description(rs.getString("description"))
                        .contractor(contractor.getById(rs.getLong("contractor_id")).orElse(null))
                        .imgUrl(rs.getString("image_url"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}