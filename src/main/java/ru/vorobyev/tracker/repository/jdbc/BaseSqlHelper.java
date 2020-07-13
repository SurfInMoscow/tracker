package ru.vorobyev.tracker.repository.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public abstract class BaseSqlHelper<T> {
    
    private final ConnectionFactory connectionFactory;
    
    private T entity;
    
    private Set<T> entities;
    
    private int size;

    public BaseSqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private void transactionalExecute() {
        try(Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                processing(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getSQLState() + e.getMessage());
        }
    }


    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Set<T> getEntities() {
        return entities;
    }

    public void setEntities(Set<T> entities) {
        this.entities = entities;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public abstract void processing(Connection connection) throws SQLException;
}
