package Model;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Vector;

public class RantalsTableModel extends AbstractTableModel {
    private String[] titles = {"Car's Num", "Client CIN", "Rental Date", "Return Date"};
    private Vector<String[]> data ;
    List<Ranting> rentals;
    public RantalsTableModel() {
        rentals = DAOFactory.getRantingDAO().all();
        this.loadData();
    }

    public void loadData() {
        this.data = new Vector<String[]>();//[clients.size()][3];
        for(Ranting c:rentals) {
            String[] str = {c.getCar().getRegistrationNumber(), c.getClient().getCIN(), c.getRentalDate().toString(), c.getReturnDate().toString()};
            data.add (str);
        }
        fireTableChanged(null);
    }

    public int getRowCount() {
        return this.data.size();
    }

    public int getColumnCount() {
        return this.titles.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data.get(rowIndex)[columnIndex];
    }

    public String getColumnName(int col) {
        return this.titles[col];
    }

    public void removeRow(int i) {
        this.data.remove(i);
        fireTableChanged(null);
    }

    public void setRow(int i, String[] str) {
        this.data.set(i, str);
        fireTableChanged(null);
    }

    public void addRow(String[] str) {
        this.data.add(str);
        fireTableChanged(null);
    }
}
