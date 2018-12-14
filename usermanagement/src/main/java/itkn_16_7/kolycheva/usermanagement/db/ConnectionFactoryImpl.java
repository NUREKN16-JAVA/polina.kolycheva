package itkn_16_7.kolycheva.usermanagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryImpl implements ConnectionFactory {
	private String driver = "org.hsqldb.jdbcDriver";
	private String url = "jdbc:hsqldb:file:db/usermanagement";
	private String user ="sa";
	private String password = "";
	public ConnectionFactoryImpl() {

	}

	public ConnectionFactoryImpl(String driver, String url, String user, String password) {
     this.driver = driver;
     this.url = url;
     this.user = user;
     this.password = password;
	}

	public ConnectionFactoryImpl(Properties properties) {
		 user =properties.getProperty("connection.user");
		 password =properties.getProperty("connection.password");
		url =properties.getProperty("connection.url");
		driver =properties.getProperty("connection.driver");
	}

	@Override
	public Connection createConnection() throws DatabaseException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e);
		}
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new DatabaseException(e);
			
		}
	}

}
