package itkn_16_7.kolycheva.usermanagement.gui;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JPanel;

import itkn_16_7.kolycheva.usermanagement.User;
import itkn_16_7.kolycheva.usermanagement.util.Messages;



public class DetailsPanel extends AddPanel {
	
	private User user;
    private JButton backButton;

    public DetailsPanel(MainFrame mainFrame) {
        super(mainFrame);
        setName("detailsPanel"); //$NON-NLS-1$
    }

    protected void doAction(ActionEvent e) throws ParseException {
        if ("ok".equalsIgnoreCase(e.getActionCommand())) { //$NON-NLS-1$
            user.setFirstName(getFirstNameField().getText());
            user.setLastName(getLastNameField().getText());
            DateFormat format = DateFormat.getDateInstance();
        }
    }

    public void setUser(User user) {
        DateFormat format = DateFormat.getDateInstance();
        this.user = user;
        getFirstNameField().setText(user.getFirstName());
        getFirstNameField().setEditable(false);
        getLastNameField().setText(user.getLastName());
        getLastNameField().setEditable(false);
        getDateOfBirthField().setText(format.format(user.getDateOfBirth()));
        getDateOfBirthField().setEditable(false);
    }

    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getBackButton());
        }
        return buttonPanel;
    }

    private JButton getBackButton() {
        if (backButton == null) {
            backButton = new JButton();
            backButton.setText(Messages.getString(Messages.getString("DetailsPanel.back"))); //$NON-NLS-1$
            backButton.setName("backButton"); //$NON-NLS-1$
            backButton.setActionCommand("back"); //$NON-NLS-1$
            backButton.addActionListener(this);
        }
        return backButton;
    }
}
