package itkn_16_7.kolycheva.usermanagement.db;

import java.sql.Connection;

public interface ConnectionFactory {
Connection createConnection() throws DatabaseException;
}
