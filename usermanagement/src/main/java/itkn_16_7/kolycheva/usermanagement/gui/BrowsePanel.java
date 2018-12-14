package itkn_16_7.kolycheva.usermanagement.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import itkn_16_7.kolycheva.usermanagement.User;
import itkn_16_7.kolycheva.usermanagement.db.DatabaseException;
import itkn_16_7.kolycheva.usermanagement.util.Messages;

public class BrowsePanel extends JPanel implements ActionListener {

	private MainFrame parent;
	private JPanel buttonPanel;
	private JButton addButton;
	private JButton detailsButton;
	private JButton deleteButton;
	private JButton editButton;
	private JScrollPane tablePanel;
	private JTable userTable;
	

	public BrowsePanel(MainFrame frame) {
		parent = frame;
		initialize();
	}


	private void initialize() {
		this.setName("browsePanel"); //$NON-NLS-1$
		this.setLayout(new BorderLayout());
		this.add(getTablePanel(),BorderLayout.CENTER);
		this.add(getButtonsPanel(), BorderLayout.SOUTH);
		
	}


	private JPanel getButtonsPanel() {
		if (buttonPanel==null) {
			buttonPanel= new JPanel();
			buttonPanel.add(getAddButton(),null);
			buttonPanel.add(getEditButton(),null);
			buttonPanel.add(getDeleteButton(),null);
			buttonPanel.add(getDetailsButton(),null);
		
		}
		return buttonPanel;
	}


	private JButton getDetailsButton() {
		if (detailsButton==null) {
			detailsButton=new JButton();
			detailsButton.setText(Messages.getString("BrowsePanel.details")); //$NON-NLS-1$
			detailsButton.setName("detailsButton"); //$NON-NLS-1$
			detailsButton.setActionCommand("details"); //$NON-NLS-1$
			detailsButton.addActionListener(this);
		}
		
		return detailsButton;
	}


	private JButton getDeleteButton() {
		if (deleteButton==null) {
			deleteButton=new JButton();
			deleteButton.setText(Messages.getString("BrowsePanel.delete")); //$NON-NLS-1$
			deleteButton.setName("deleteButton"); //$NON-NLS-1$
			deleteButton.setActionCommand("delete"); //$NON-NLS-1$
			deleteButton.addActionListener(this);
		}
		return deleteButton;
	}


	private JButton getEditButton() {
		if (editButton==null) {
			editButton=new JButton();
			editButton.setText(Messages.getString("BrowsePanel.edit")); //$NON-NLS-1$
			editButton.setName("editButton"); //$NON-NLS-1$
			editButton.setActionCommand("edit"); //$NON-NLS-1$
			editButton.addActionListener(this);
		}
		return editButton;
	}


	private JButton getAddButton() {
		if (addButton==null) {
			addButton=new JButton();
			addButton.setText(Messages.getString("BrowsePanel.add")); //$NON-NLS-1$
			addButton.setName("addButton"); //$NON-NLS-1$
			addButton.setActionCommand("add"); //$NON-NLS-1$
			addButton.addActionListener(this);
		}
		return addButton;
	}


	private JScrollPane getTablePanel() {
		if (tablePanel==null) {
			tablePanel=new JScrollPane(getUserTable());
		}
		return tablePanel;
	}


	private JTable getUserTable() {
		if (userTable==null) {
			userTable=new JTable();
			userTable.setName("userTable"); //$NON-NLS-1$
		}
	
		return userTable;
	}


	public void initTable() {
		UserTableModel model;
		try {
			model = new UserTableModel(parent.getDao().findAll());
		} catch (DatabaseException e) {
			model = new UserTableModel(new ArrayList());
		JOptionPane.showMessageDialog(this,e.getMessage(),"Error",
				JOptionPane.ERROR_MESSAGE);	
		}
		getUserTable().setModel(model);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if ("add".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
			this.setVisible(false);
			parent.showAddPanel();
		} else if ("edit".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, Messages.getString("BrowsePanel.сhoose_user_edit"), "Edit", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			User user = ((UserTableModel) userTable.getModel()).getUser(selectedRow);
			this.setVisible(false);
			parent.showEditPanel(user);
		} else if ("delete".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, Messages.getString("BrowsePanel.сhoose_user_delete"), "Delete", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			int answ = JOptionPane.showConfirmDialog(null, Messages.getString("BrowsePanel.confirm_delete"), //$NON-NLS-1$
					"Delete", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

			if (answ == JOptionPane.YES_OPTION) {
				try {
					parent.getDao().delete(((UserTableModel) userTable.getModel()).getUser(selectedRow));
				} catch (DatabaseException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
			}

			initTable();
			return;
		} else if ("details".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, Messages.getString("BrowsePanel.сhoose_user_show_info"), "Details", //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			User user = ((UserTableModel) userTable.getModel()).getUser(selectedRow);
			this.setVisible(false);
			parent.showDetailsPanel(user);
		}
	}


}
