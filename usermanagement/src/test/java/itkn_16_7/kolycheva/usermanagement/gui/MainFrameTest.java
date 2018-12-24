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

    List<User> users;
    private Mock mockUserDAO;
    private MainFrame mainFrame;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            Properties properties = new Properties();
            properties.setProperty("dao.factory", MockDaoFactory.class.getName());
            DaoFactory.init(properties);
            mockUserDAO = ((MockDaoFactory) DaoFactory.getInstance()).getMockUserDao();
            User expectedUser = new User(new Long(1000), "George", "Bush", new Date());
            users = Collections.singletonList(expectedUser);
            mockUserDAO.expectAndReturn("findAll", users);
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
            mockUserDAO.verify();
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
        find(JPanel.class, "browsePanel");
        find(JButton.class, "addButton");
        find(JButton.class, "editButton");
        find(JButton.class, "deleteButton");
        find(JButton.class, "detailsButton");
        JTable table = (JTable) find(JTable.class, "userTable");
        assertEquals(3, table.getColumnCount());
        assertEquals(Messages.getString("UserTableModel.id"), table.getColumnName(0));
        assertEquals(Messages.getString("UserTableModel.first_name"), table.getColumnName(1));
        assertEquals(Messages.getString("UserTableModel.last_name"), table.getColumnName(2));
        assertEquals(1, table.getRowCount());
    }

    public void testAddUser() {
        try {
            String firstName = "John";
            String lastName = "Doe";
            Date now = new Date();
            User user = new User(firstName, lastName, now);
            User expectedUser = new User(new Long(1), firstName, lastName, now);
            mockUserDAO.expectAndReturn("create", user, expectedUser);
            ArrayList users = new ArrayList(this.users);
            users.add(expectedUser);
            mockUserDAO.expectAndReturn("findAll", users);
            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            JButton addButton = (JButton) find(JButton.class, "addButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, addButton));
            find(JPanel.class, "addPanel");
            fillFields(firstName, lastName, now);
            JButton okButton = (JButton) find(JButton.class, "okButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(2, table.getRowCount());
            mockUserDAO.verify();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void fillFields(String firstName, String lastName, Date now) {
        JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");
        getHelper().sendString(new StringEventData(this, firstNameField, firstName));
        getHelper().sendString(new StringEventData(this, lastNameField, lastName));
        DateFormat formatter = DateFormat.getDateInstance();
        String date = formatter.format(now);
        getHelper().sendString(new StringEventData(this, dateOfBirthField, date));
    }

    public void testCancelAddUser() {
        try {
            String firstName = "John";
            String lastName = "Doe";
            Date now = new Date();
            ArrayList users = new ArrayList(this.users);
            mockUserDAO.expectAndReturn("findAll", users);
            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            JButton addButton = (JButton) find(JButton.class, "addButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, addButton));
            find(JPanel.class, "addPanel");
            fillFields(firstName, lastName, now);
            JButton cancelButton = (JButton) find(JButton.class, "cancelButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, cancelButton));
            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            mockUserDAO.verify();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public void testDeleteUser() {
        try {
            User expectedUser = new User(new Long(1000), "George", "Bush", new Date());
            mockUserDAO.expect("delete", expectedUser);
            List users = new ArrayList();
            mockUserDAO.expectAndReturn("findAll", users);
            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            JButton deleteButton = (JButton) find(JButton.class, "deleteButton");
            getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
            getHelper().enterClickAndLeave(new MouseEventData(this, deleteButton));
            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(0, table.getRowCount());
            mockUserDAO.verify();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public void testEditUser() {
        try {
            User expectedUser = new User(new Long(1000), "George", "Bush", new Date());
            System.out.println(expectedUser);
            mockUserDAO.expect("update", expectedUser);
            List users = new ArrayList(this.users);
            mockUserDAO.expectAndReturn("findAll", users);
            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            JButton editButton = (JButton) find(JButton.class, "editButton");
            getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
            getHelper().enterClickAndLeave(new MouseEventData(this, editButton));
            find(JPanel.class, "editPanel");
            JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
            JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
            JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");
            getHelper().sendString(new StringEventData(this, firstNameField, "1"));
            getHelper().sendString(new StringEventData(this, lastNameField, "1"));
            JButton okButton = (JButton) find(JButton.class, "okButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            mockUserDAO.verify();
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void findDialog(String title) {
        JDialog dialog;
        DialogFinder dFinder = new DialogFinder(title);
        dialog = (JDialog) dFinder.find();
        assertNotNull("Could not find dialog '" + title + "'", dialog);
        getHelper().disposeWindow(dialog, this);
    }

    public void testCancelEditUser() {
        try {
            String firstName = "John";
            String lastName = "Doe";
            Date now = new Date();

            User expectedUser = new User(new Long(1), firstName, lastName, now);
            List users = new ArrayList(this.users);
            users.add(expectedUser);

            mockUserDAO.expectAndReturn("findAll", users);

            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());

            JButton editButton = (JButton) find(JButton.class, "editButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, editButton));

            String title = "Edit";
            findDialog(title);

            getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
            getHelper().enterClickAndLeave(new MouseEventData(this, editButton));

            find(JPanel.class, "editPanel");

            JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
            JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
            JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");

            assertEquals("George", firstNameField.getText());
            assertEquals("Bush", lastNameField.getText());

            getHelper().sendString(new StringEventData(this, firstNameField, firstName));
            getHelper().sendString(new StringEventData(this, lastNameField, lastName));
            DateFormat formatter = DateFormat.getDateInstance();
            String date = formatter.format(now);
            getHelper().sendString(new StringEventData(this, dateOfBirthField, date));

            JButton cancelButton = (JButton) find(JButton.class, "cancelButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, cancelButton));

            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(2, table.getRowCount());
            mockUserDAO.verify();

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public void testDetailsUser() {
        try {
            mockUserDAO.expectAndReturn("findAll", users);

            JTable table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());

            JButton detailsButton = (JButton) find(JButton.class, "detailsButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));

            String title = "Details";
            findDialog(title);

            getHelper().enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
            getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));

            find(JPanel.class, "detailsPanel");

            JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
            JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
            JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");

            assertEquals("George", firstNameField.getText());
            assertEquals("Bush", lastNameField.getText());

            JButton backButton = (JButton) find(JButton.class, "backButton");
            getHelper().enterClickAndLeave(new MouseEventData(this, backButton));

            find(JPanel.class, "browsePanel");
            table = (JTable) find(JTable.class, "userTable");
            assertEquals(1, table.getRowCount());
            mockUserDAO.verify();

        } catch (Exception e) {
            fail(e.toString());
        }
    }

}