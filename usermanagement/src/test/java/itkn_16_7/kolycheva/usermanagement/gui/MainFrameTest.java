package itkn_16_7.kolycheva.usermanagement.gui;

import java.awt.Component;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.mockobjects.dynamic.Mock;

import itkn_16_7.kolycheva.usermanagement.User;
import itkn_16_7.kolycheva.usermanagement.db.DaoFactory;
import itkn_16_7.kolycheva.usermanagement.db.DaoFactoryImpl;
import itkn_16_7.kolycheva.usermanagement.db.MockDaoFactory;
import itkn_16_7.kolycheva.usermanagement.db.MockUserDao;
import itkn_16_7.kolycheva.usermanagement.util.Messages;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.eventdata.JTableMouseEventData;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.DialogFinder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;

public class MainFrameTest extends JFCTestCase {

	private static final int NUM_ROW_ETALON = 1;
	private static final Long ID_ETALON = 5L;
	private static final String LASTNAME_ETALON = "Bush";
	private static final String FIRSTNAME_ETALON = "George";
	private static final Long ID_ADD_USER_ETALON = 1L;
	private static final String LASTNAME_ADD_USER_ETALON = "Doe";
	private static final String FIRSTNAME_ADD_USER_ETALON = "John";
	private static final int NUM_ROW_AFTER_ADD_USER_ETALON = 2;
	private static final int NUM_ROW_BEFORE_ADD_USER_ETALON = 1;
	private static final int NUM_COLUMN_ETALON = 3;
	List<User> users;
	private Mock mockUserDao;
	private MainFrame mainFrame;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			Properties properties = new Properties();
			properties.setProperty("dao.factory", MockDaoFactory.class.getName());
			DaoFactory.init(properties);
			mockUserDao = ((MockDaoFactory) DaoFactory.getInstance()).getMockUserDao();
			User expectedUser = new User(ID_ETALON, FIRSTNAME_ETALON, LASTNAME_ETALON, new Date());
			users = Collections.singletonList(expectedUser);
			mockUserDao.expectAndReturn("findAll", users);
			setHelper(new JFCTestHelper());
			mainFrame = new MainFrame();

		} catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.setVisible(true);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		try {
			mockUserDao.verify();
			mainFrame.setVisible(false);
			getHelper().cleanUp(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component find(Class componentClass, String name) {
		Component component;
		NamedComponentFinder finder = new NamedComponentFinder(componentClass, name);
		finder.setWait(0);
		component = finder.find(mainFrame, 0);
		assertNotNull("Could not find component '" + name + "'", component);
		return component;
	}

	public void testBrowsePanel() {
		try {
			find(JPanel.class, "browsePanel");
			find(JButton.class, "addButton");
			find(JButton.class, "editButton");
			find(JButton.class, "deleteButton");
			find(JButton.class, "detailsButton");
			JTable table = (JTable) find(JTable.class, "userTable");
			assertEquals(NUM_COLUMN_ETALON, table.getColumnCount());
			assertEquals(Messages.getString("UserTableModel.ID"), table.getColumnName(0));
			assertEquals(Messages.getString("UserTableModel.first_name"), table.getColumnName(1));
			assertEquals(Messages.getString("UserTableModel.last_name"), table.getColumnName(2));
			assertEquals(NUM_ROW_ETALON, table.getRowCount());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testAddUser() {
		try {
			Date now = new Date();
			User user = new User(FIRSTNAME_ADD_USER_ETALON, LASTNAME_ADD_USER_ETALON, now);
			User expectedUser = new User(ID_ADD_USER_ETALON, FIRSTNAME_ADD_USER_ETALON, LASTNAME_ADD_USER_ETALON, now);
			mockUserDao.expectAndReturn("create", user, expectedUser);
			ArrayList users = new ArrayList(this.users);
			users.add(expectedUser);
			mockUserDao.expectAndReturn("findAll", users);
			JTable table = (JTable) find(JTable.class, "userTable");
			assertEquals(NUM_ROW_BEFORE_ADD_USER_ETALON, table.getRowCount());
			JButton addButton = (JButton) find(JButton.class, "addButton");
			getHelper().enterClickAndLeave(new MouseEventData(this, addButton));
			find(JPanel.class, "addPanel");
			JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
			JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
			JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");
			getHelper().sendString(new StringEventData(this, firstNameField, FIRSTNAME_ADD_USER_ETALON));
			getHelper().sendString(new StringEventData(this, lastNameField, LASTNAME_ADD_USER_ETALON));
			DateFormat formatter = DateFormat.getDateInstance();
			String date = formatter.format(now);
			getHelper().sendString(new StringEventData(this, dateOfBirthField, date));
			JButton okButton = (JButton) find(JButton.class, "okButton");
			getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
			find(JPanel.class, "browsePanel");
			table = (JTable) find(JTable.class, "userTable");
			assertEquals(NUM_ROW_AFTER_ADD_USER_ETALON, table.getRowCount());
			mockUserDao.verify();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testDeleteUser() {
		try {
			User expectedUser = new User(ID_ETALON, FIRSTNAME_ETALON, LASTNAME_ETALON, new Date());
			mockUserDao.expectAndReturn("delete", expectedUser);
			List users = new ArrayList();
			mockUserDao.expectAndReturn("findAll", users);
			JTable table = (JTable) find(JTable.class, "userTable");
			assertEquals(1, table.getRowCount());
			JButton deleteButton = (JButton) find(JButton.class, "deleteButton");
			getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
			getHelper().enterClickAndLeave(new MouseEventData(this, deleteButton));
			String title = "Delete";
			DialogFinder dFinder = new DialogFinder(title);
			JDialog dialog = (JDialog) dFinder.find();
			assertNotNull("Could not find dialog '" + title + "'", dialog);
			getHelper().disposeWindow(dialog, this);
			find(JPanel.class, "browsePanel");
			table = (JTable) find(JTable.class, "userTable");
			assertEquals(0, table.getRowCount());
			mockUserDao.verify();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testEditUser() {
		try {
			User expectedUser = new User(ID_ETALON, FIRSTNAME_ETALON, LASTNAME_ETALON, new Date());
			System.out.println(expectedUser);
			List users = new ArrayList(this.users);
			mockUserDao.expectAndReturn("findAll", users);
			JTable table = (JTable) find(JTable.class, "userTable");
			assertEquals(1, table.getRowCount());
			JButton editButton = (JButton) find(JButton.class, "editButton");
			getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
			getHelper().enterClickAndLeave(new MouseEventData(this, editButton));
			find(JPanel.class, "editPanel");
			JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
			JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
			getHelper().sendString(new StringEventData(this, firstNameField, "1"));
			getHelper().sendString(new StringEventData(this, lastNameField, "1"));
			JButton okButton = (JButton) find(JButton.class, "okButton");
			getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
			find(JPanel.class, "browsePanel");
			table = (JTable) find(JTable.class, "userTable");
			assertEquals(1, table.getRowCount());
			mockUserDao.verify();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	public void testDetailsUser() {
		try {
			JTable table = (JTable) find(JTable.class, "userTable");
			assertEquals(1, table.getRowCount());
			JButton detailsButton = (JButton) find(JButton.class, "detailsButton");
			getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));
			getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
			getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));
			find(JPanel.class, "detailsPanel");
			JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
			JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
			assertEquals(FIRSTNAME_ETALON, firstNameField.getText());
			assertEquals(LASTNAME_ETALON, lastNameField.getText());
			JButton backButton = (JButton) find(JButton.class, "backButton");
			getHelper().enterClickAndLeave(new MouseEventData(this, backButton));
			find(JPanel.class, "browsePanel");
			table = (JTable) find(JTable.class, "userTable");
			assertEquals(1, table.getRowCount());
			mockUserDao.verify();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

}
