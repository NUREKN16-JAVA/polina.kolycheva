<pre><p>package com.nixsolutions.usermanagement.agent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.nixsolutions.usermanagement.gui.UserTableModel;
import com.nixsolutions.usermanagement.util.Messages;

/**
 * @author mak
 */
public class SearchGui extends JFrame {

    private SearchAgent agent;

    private JPanel contentPanel;

    private JPanel tablePanel;

    private JTable table;

    /**
     * @param agent
     */
    public SearchGui(SearchAgent agent) {
        this.agent = agent;
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setTitle(&quot;Searcher&quot;);
        this.setContentPane(getContentPanel());
    }

    /**
     * @return
     */
    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getSearchPanel(), BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(getTablePanel()), BorderLayout.CENTER);
        }
        return contentPanel;
    }

    /**
     * @return
     */
    private JPanel getTablePanel() {
        if (tablePanel == null) {
            tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(getTable(), BorderLayout.CENTER);
        }
        return tablePanel;
    }

    /**
     * @return
     */
    private JTable getTable() {
        if (table == null) {
            table = new JTable(new UserTableModel(new LinkedList()));
        }
        return table;
    }

    /**
     * @return
     */
    private JPanel getSearchPanel() {
        return new SearchPanel(agent);
    }

    public static void main(String[] args) {
        SearchGui gui = new SearchGui(null);
        gui.setVisible(true);
    }

    /**
     * @author mak
     */
    class SearchPanel extends JPanel implements ActionListener {
        //        protected JFrame parent;
        SearchAgent agent;

        private JPanel buttonPanel;

        private JPanel fieldPanel;

        private JButton cancelButton;

        private JButton searchButton;

        private JTextField firstNameField;

        private JTextField dateOfBirthField;

        private JTextField lastNameField;


        public SearchPanel(SearchAgent agent) {
            this.agent = agent;
            initialize();
        }

        private void initialize() {
            this.setName(&quot;addPanel&quot;); //$NON-NLS-1$
            this.setLayout(new BorderLayout());
            this.add(getFieldPanel(), BorderLayout.NORTH);

        }

        private JPanel getButtonPanel() {
            if (buttonPanel == null) {
                buttonPanel = new JPanel();
                buttonPanel.add(getSearchButton(), null);
                buttonPanel.add(getCancelButton(), null);
            }
            return buttonPanel;
        }

        private JButton getCancelButton() {
            if (cancelButton == null) {
                cancelButton = new JButton();
                cancelButton.setText(Messages.getString(&quot;AddPanel.cancel&quot;)); //$NON-NLS-1$
                cancelButton.setName(&quot;cancelButton&quot;); //$NON-NLS-1$
                cancelButton.setActionCommand(&quot;cancel&quot;); //$NON-NLS-1$
                cancelButton.addActionListener(this);
            }
            return cancelButton;
        }

        private JButton getSearchButton() {
            if (searchButton == null) {
                searchButton = new JButton();
                searchButton.setText(&quot;Search&quot;); //$NON-NLS-1$
                searchButton.setName(&quot;okButton&quot;); //$NON-NLS-1$
                searchButton.setActionCommand(&quot;ok&quot;); //$NON-NLS-1$
                searchButton.addActionListener(this);
            }
            return searchButton;
        }

        private JPanel getFieldPanel() {
            if (fieldPanel == null) {
                fieldPanel = new JPanel();
                fieldPanel.setLayout(new GridLayout(2, 3));
                addLabeledField(fieldPanel, &quot;FirstName&quot;, getFirstNameField()); //$NON-NLS-1$
                fieldPanel.add(getSearchButton());
                addLabeledField(fieldPanel, &quot;LastName&quot;, getLastNameField()); //$NON-NLS-1$
                fieldPanel.add(getCancelButton());
            }
            return fieldPanel;
        }

        protected JTextField getLastNameField() {
            if (lastNameField == null) {
                lastNameField = new JTextField();
                lastNameField.setName(&quot;lastNameField&quot;); //$NON-NLS-1$
            }
            return lastNameField;
        }

        private void addLabeledField(JPanel panel, String labelText,
                JTextField textField) {
            JLabel label = new JLabel(labelText);
            label.setLabelFor(textField);
            panel.add(label);
            panel.add(textField);
        }

        protected JTextField getFirstNameField() {
            if (firstNameField == null) {
                firstNameField = new JTextField();
                firstNameField.setName(&quot;firstNameField&quot;); //$NON-NLS-1$
            }
            return firstNameField;
        }

        protected void doAction(ActionEvent e) throws ParseException {
            if (&quot;ok&quot;.equalsIgnoreCase(e.getActionCommand())) {
                String firstName = getFirstNameField().getText();
                String lastName = getLastNameField().getText();
                try {
                    clearUsers();
                    agent.search(firstName, lastName);
                } catch (SearchException e1) {
                    throw new RuntimeException(e1);
                }
            }
            clearFields();
        }

        public void actionPerformed(ActionEvent e) {
            try {
                doAction(e);
            } catch (ParseException e1) {
                return;
            }
        }

        private void clearFields() {
            getFirstNameField().setText(&quot;&quot;);
            getLastNameField().setText(&quot;&quot;);
        }
    }

    /**
     * @param users
     */
    public void addUsers(Collection users) {
        System.out.println(&quot;addUsers : &quot; + users);
        UserTableModel model = (UserTableModel) getTable().getModel();
        model.addUsers(users);
        this.repaint();
    }

    private void clearUsers() {
        System.out.println(&quot;clearUsers : &quot;);
        UserTableModel model = (UserTableModel) getTable().getModel();
        model.clearUsers();
        this.repaint();
    }
}</p></pre>