package itkn_16_7.kolycheva.usermanagement.db;

public class DaoFactoryImpl extends DaoFactory {

	@Override
	public UserDao getUserDao() {
		UserDao result = null;
        try {
            Class<?> clazz = Class.forName(properties.getProperty(USER_DAO));
            result = (UserDao) clazz.newInstance();
            result.setConnectionFactory(getConnectionFactory());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        	e.printStackTrace();
        }

        return result;
	}

}


