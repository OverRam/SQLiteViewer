package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ModelDataBase {
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();
    private ArrayList<String> tablesList;
    private ArrayList<String> actualColumnsName;
    private ArrayList<String[]> dataInTable;
    private int numbersOfColumns;
    private int numbersOfRows;

    private static Connection connectionDB() {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public int getNumbersOfColumns() {
        return numbersOfColumns;
    }

    public int getNumbersOfRows() {
        return numbersOfRows;
    }

    public ArrayList<String[]> getDataInTable() {
        return dataInTable;
    }

    ArrayList<String> getActualColumnsName() {
        return actualColumnsName;
    }

    void setInfoDB(String url) {
//        String pathDataBase = "jdbc:sqlite:" + "sqlite_master.db";
        String pathDataBase = "jdbc:sqlite:" + url;
        dataSource.setUrl(pathDataBase);
        getTableList();
    }

    void setColumnsName(String table) {
        actualColumnsName = new ArrayList<>();
        numbersOfColumns = 0;
        try {
            ResultSet resultTable = connectionDB().createStatement().executeQuery("SELECT name FROM " +
                    "PRAGMA_TABLE_INFO('" + table + "')");
            while (resultTable.next()) {
                actualColumnsName.add(resultTable.getString("name"));
                numbersOfColumns++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getTableList() {
        tablesList = new ArrayList<>();
        try {
            ResultSet resultTable = connectionDB().createStatement().executeQuery("SELECT name FROM " +
                    "sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'");
            while (resultTable.next()) {
                tablesList.add(resultTable.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void makeQuery(String query) {
        try (Statement statement = connectionDB().createStatement()) {
            numbersOfRows = 0;
            ResultSet resultQuery = statement.executeQuery(query);
            dataInTable = new ArrayList<>();
            while (resultQuery.next()) {
                String[] temp = new String[actualColumnsName.size()];
                for (int i = 0; i < actualColumnsName.size(); i++) {
                    temp[i] = resultQuery.getString(i + 1);
                }
                numbersOfRows++;
                dataInTable.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> getTablesList() {
        return tablesList;
    }
}