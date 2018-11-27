package itkn_16_7.kolycheva.usermanagement.db;

import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.Date;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;

import itkn_16_7.kolycheva.usermanagement.User;

public class HsqldbUserDaoTest extends DatabaseTestCase{

    private static final Long TEST_ID = 1000L;
   
    
	private HsqldbUserDao dao;
	private ConnectionFactory connectionFactory;
	
	protected void setUp() throws Exception {
		super.setUp();
		dao = new HsqldbUserDao(connectionFactory);
	}

	public void testCreate() {
		try {
			User user = new User();
			user.setFirstName("Polina");
			user.setLastName("Kolycheva");
			user.setDateOfBirth(new Date());
			assertNull(user.getId());
			user = dao.create(user);
			assertNotNull(user);
			assertNotNull(user.getId());
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testFindAll() {
		try {
			Collection collection = dao.findAll();
			assertNotNull("Collection is null",collection);
			assertEquals("Collection size",2, collection.size());
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testUpdateUser() throws DatabaseException {
	        User user = dao.find(TEST_ID);
	        assertNotNull(user);
	        user.setFirstName("Polina");
	        dao.update(user);
	        User updatedUser = dao.find(TEST_ID);
	        assertEquals(user.getFirstName(), updatedUser.getFirstName());
	}
	
	public void testDelete() {
        User deletedUser = new User();
        deletedUser.setId(TEST_ID);
        try {
            dao.delete(deletedUser);
            dao.find(TEST_ID);
            fail();
        } catch (DatabaseException e) {
        	String left = e.getMessage();
        	String right = TEST_ID.toString();
        	left = left.substring(left.length()-right.length());
        	assertTrue(left.equals(right));
        }
	}
	
    public void testFind() throws DatabaseException {
        User userToCheck = dao.find(TEST_ID);
        assertNotNull(userToCheck);
        assertEquals("Bill", userToCheck.getFirstName());
        assertEquals("Gates", userToCheck.getLastName());
}
	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		connectionFactory = new ConnectionFactoryImpl();
		return new DatabaseConnection(connectionFactory.createConnection());
	}


	@Override
	protected IDataSet getDataSet() throws Exception {
		IDataSet dataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSet.xml"));
		return dataSet;
	}

}