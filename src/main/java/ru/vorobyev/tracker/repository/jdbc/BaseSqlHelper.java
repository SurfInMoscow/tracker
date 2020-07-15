package ru.vorobyev.tracker.repository.jdbc;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

@Getter
@Setter
public abstract class BaseSqlHelper<T> {
    
    private final ConnectionFactory connectionFactory;
    
    private T entity;
    
    private Set<T> entities;
    
    private int size;

    private boolean result;

    public BaseSqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        transactionalExecute();
    }

    private void transactionalExecute() {
        try(Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    public abstract void processing(Connection connection) throws SQLException;
}
