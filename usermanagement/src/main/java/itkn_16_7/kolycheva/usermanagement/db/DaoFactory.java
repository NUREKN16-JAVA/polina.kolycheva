package itkn_16_7.kolycheva.usermanagement.db;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {
	private static final String DAO_FACTORY = "dao.factory";
	protected static final String USER_DAO = "dao.itkn_16_7.kolycheva.usermanagement.db.UserDao";
	protected static Properties properties;
    private static DaoFactory instance;
	
    static {
    	properties = new Properties();
		try {
			properties.load(DaoFactory.class
					.getClass()
					.getClassLoader()
					.getResourceAsStream("settings.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	protected DaoFactory() {
		
	}
	
	public static DaoFactory getInstance() {
		if (instance == null) {
			try {
				Class<?> factoryClass = Class.forName(properties
						.getProperty(DAO_FACTORY));
				instance = (DaoFactory) factoryClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}
	
	protected ConnectionFactory getConnectionFactory(){
        return new ConnectionFactoryImpl(properties);
    }
	
	public abstract UserDao getUserDao();
	
	public static void init(Properties prop) {
		properties = prop;
		instance = null;
	}
}