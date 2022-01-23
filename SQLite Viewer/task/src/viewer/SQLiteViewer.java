package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Objects;


public class SQLiteViewer extends JFrame {
    private final ModelDataBase dataBase = new ModelDataBase();
    private JButton executeButton;
    private JButton openFileButton;
    private JTextArea textQueryExecution;
    private JComboBox<String> tablesNameComboBox;
    private JTextField fileNameTextField;
    private JTable table;

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 600));
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");
        setLayout(new FlowLayout());
        initComponents();
        actionsComponents();
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JLabel openDBUrl = new JLabel("Url DB");

        fileNameTextField = new JTextField(50);
        fileNameTextField.setName("FileNameTextField");

        openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");

        tablesNameComboBox = new JComboBox<>();
        tablesNameComboBox.setName("TablesComboBox");
        tablesNameComboBox.addItem("Tables");

        textQueryExecution = new JTextArea();
        textQueryExecution.setName("QueryTextArea");
        textQueryExecution.setRows(5);
        textQueryExecution.setColumns(30);
        textQueryExecution.setEnabled(false);

        executeButton = new JButton("execute");
        executeButton.setName("ExecuteQueryButton");
        executeButton.setEnabled(false);

        table = new JTable();
        table.setName("Table");

        //Panels
        JPanel panelTables = new JPanel();
        panelTables.add(new JLabel("Tables: "));
        panelTables.add(tablesNameComboBox);

        JPanel panelExecution = new JPanel();
        panelExecution.add(textQueryExecution);
        panelExecution.add(executeButton);

        JPanel panelOpenDB = new JPanel();
        panelOpenDB.setBackground(Color.GRAY);
        panelOpenDB.add(openDBUrl);
        panelOpenDB.add(fileNameTextField, BorderLayout.NORTH);
        panelOpenDB.add(openFileButton, BorderLayout.NORTH);

        add(panelOpenDB, BorderLayout.NORTH);
        add(panelTables);
        add(panelExecution, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.EAST);
    }

    private void actionsComponents() {
        executeButton.addActionListener(e -> {
            dataBase.setColumnsName(Objects.requireNonNull(tablesNameComboBox.getSelectedItem()).toString());
            dataBase.makeQuery(textQueryExecution.getText());
            DefaultTableModel tabModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tabModel.setDataVector(dataBase.getDataInTable().toArray(new String[dataBase.getNumbersOfRows()][0]),
                    dataBase.getActualColumnsName().toArray(new String[0]));
            table.setSelectionBackground(Color.LIGHT_GRAY);
            table.setModel(tabModel);
        });

        openFileButton.addActionListener(e -> {
            if (new File(fileNameTextField.getText()).exists()) {
                dataBase.setInfoDB(fileNameTextField.getText());
                tablesNameComboBox.removeAllItems();
                dataBase.getTablesList().forEach(tablesNameComboBox::addItem);
                tablesNameComboBox.setEnabled(true);
                textQueryExecution.setEnabled(true);
                executeButton.setEnabled(true);

            } else {
                executeButton.setEnabled(false);
                tablesNameComboBox.setEnabled(false);
                textQueryExecution.setEnabled(false);
                JOptionPane.showMessageDialog(new Frame(), "Wrong file path or file doesn't exist!");
            }

        });

        tablesNameComboBox.addActionListener(e -> textQueryExecution.setText("SELECT * FROM "
                + tablesNameComboBox.getSelectedItem() + ';'));
    }

}