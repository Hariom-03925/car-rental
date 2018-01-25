package Controller;

import Model.Client;
import Model.DAO;
import Model.DAOFactory;
import View.ClientsPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientController implements ActionListener {
	private ClientsPanel clientsPanel;
	private DAO<Client> clientDAO;

	public ClientController(ClientsPanel clientsPanel){
		this.clientDAO = DAOFactory.getClientDAO();
		this.clientsPanel = clientsPanel;
		this.clientsPanel.getAddButton().addActionListener(this);
		this.clientsPanel.getResetButton().addActionListener(this);
		this.clientsPanel.getExportButton().addActionListener(this);
		this.clientsPanel.getDeleteButton().addActionListener(this);
		this.clientsPanel.getUpdateButton().addActionListener(this);

		/*final ListSelectionModel lsm = this.clientsPanel.getTable().getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(! lsm.isSelectionEmpty()) {
					int selectedRow = lsm.getMinSelectionIndex();
					//System.out.println(selectedRow);
				}
			}
		});*/
	}

	public void finalize() throws Throwable {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.clientsPanel.getAddButton()){
			if (!this.clientsPanel.getCinField().getText().equals("")
					&& !this.clientsPanel.getFnameField().getText().equals("")
					&& !this.clientsPanel.getLnameField().getText().equals(""))
			{
				//create a new client from field with dao
				Client client = new Client( this.clientsPanel.getCinField().getText(),
						this.clientsPanel.getFnameField().getText(),
						this.clientsPanel.getLnameField().getText());
				String[] str = {this.clientsPanel.getCinField().getText(),
						this.clientsPanel.getFnameField().getText(),
						this.clientsPanel.getLnameField().getText()};
				if(this.clientsPanel.getAddButton().getText().equals("Modify")) {
					this.clientDAO.update(client);
					int i = this.clientsPanel.getTable().getSelectedRow();

					this.clientsPanel.getModel().setRow(i, str);

					this.clientsPanel.getAddButton().setText("Add");
					this.clientsPanel.getCinField().setEnabled(true);
				} else {
					this.clientDAO.create(client);
					this.clientsPanel.getModel().addRow(str);
				}
				this.clientsPanel.getLnameField().setText("");
				this.clientsPanel.getFnameField().setText("");
				this.clientsPanel.getCinField().setText("");
			}
			else {
				int dialogResult = JOptionPane.showConfirmDialog (null, "There is an empty field!","Warning", JOptionPane.DEFAULT_OPTION);
			}
		} else if (e.getSource() == this.clientsPanel.getResetButton()) {
			this.clientsPanel.getLnameField().setText("");
			this.clientsPanel.getFnameField().setText("");
			this.clientsPanel.getCinField().setText("");
			this.clientsPanel.getCinField().setEnabled(true);
		} else if (e.getSource() == this.clientsPanel.getExportButton()){
			ExcelExporter.export(this.clientsPanel.getModel().getTitles(), this.clientsPanel.getModel().getData(), "Clients.xls");
		} else if (e.getSource() == this.clientsPanel.getDeleteButton()) {
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete selected elements?","Warning", JOptionPane.DEFAULT_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				int[] selection = this.clientsPanel.getTable().getSelectedRows();
				for(int i = 0; i < selection.length; i++) {
					clientDAO.delete(
							new Client(
									this.clientsPanel.getTable().getModel().getValueAt(selection[i], 0).toString(),
									this.clientsPanel.getTable().getModel().getValueAt(selection[i], 1).toString(),
									this.clientsPanel.getTable().getModel().getValueAt(selection[i], 2).toString()));
					this.clientsPanel.getModel().removeRow(selection[i]);
				}
			}
		} else if (e.getSource() == this.clientsPanel.getUpdateButton()) {
			int[] selection = this.clientsPanel.getTable().getSelectedRows();
			if (selection.length == 1) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete selected elements?","Warning", JOptionPane.DEFAULT_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					this.clientsPanel.getCinField().setText(this.clientsPanel.getTable().getModel().getValueAt(selection[0], 0).toString());
					this.clientsPanel.getCinField().setEnabled(false);
					this.clientsPanel.getFnameField().setText(this.clientsPanel.getTable().getModel().getValueAt(selection[0], 1).toString());
					this.clientsPanel.getLnameField().setText(this.clientsPanel.getTable().getModel().getValueAt(selection[0], 2).toString());
					this.clientsPanel.getAddButton().setText("Modify");
				}
			} else {
				int dialogResult = JOptionPane.showConfirmDialog (null, "You have to choose just one row!","Warning", JOptionPane.DEFAULT_OPTION);
			}
		}
	}
}