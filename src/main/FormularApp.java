import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FormularApp extends JFrame {

    private JTextField textField;
    private JCheckBox checkBox;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private ButtonGroup radioGroup;
    private JComboBox<String> comboBox;
    private JSpinner spinner;
    private DefaultTableModel tableModel; // Model pentru tabel

    public FormularApp() {
        setTitle("Site de haine ");
        setSize(600, 400); // Măresc dimensiunea pentru a încăpea tabelul
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();

        // Inițializez modelul tabelului
        String[] columnNames = {"Nume", "Anotimp", "Marime", "Sex", "Varsta"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Adaug tabelul la fereastra
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        loadDataFromJson(); // La pornirea programului, încarc datele din JSON


    }

    private void initializeUI() {
        // Layout manager
        setLayout(new GridLayout(7, 2, 10, 10));

        // Text field
        add(new JLabel("Nume:"));
        textField = new JTextField();
        add(textField);

        // Spinner
        add(new JLabel("Varsta:"));
        spinner = new JSpinner();
        add(spinner);

        // Combo box
        add(new JLabel("Sex:"));
        String[] values = {"Feminin", "Masculin"};
        comboBox = new JComboBox<>(values);
        add(comboBox);

        // Radio buttons
        add(new JLabel("Marime:"));
        radioButton1 = new JRadioButton("S");
        radioButton2 = new JRadioButton("M");
        radioGroup = new ButtonGroup();
        radioGroup.add(radioButton1);
        radioGroup.add(radioButton2);
        JPanel radioPanel = new JPanel();
        radioPanel.add(radioButton1);
        radioPanel.add(radioButton2);
        add(radioPanel);

        // Check box
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Anotimp:"));
        checkBox = new JCheckBox("Iarna");
        checkBoxPanel.add(checkBox);

        // A doua opțiune pentru primul checkbox
        JCheckBox checkBoxOption1 = new JCheckBox("Vara");
        checkBoxPanel.add(checkBoxOption1);
        add(checkBoxPanel);

        // Butoane
        JButton saveButton = new JButton("Salvare");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToJson();
                loadDataFromJson(); // Reîmprospătez tabelul după salvare
            }
        });
        add(saveButton);

        JButton cancelButton = new JButton("Anulare");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(cancelButton, BorderLayout.SOUTH); // Mut butonul în partea de jos a ferestrei
    }

    private void saveDataToJson() {
        // Creare obiect JSON
        JSONObject jsonData = new JSONObject();
        jsonData.put("Nume", textField.getText());
        jsonData.put("Anotimp", checkBox.isSelected() ? "Iarna" : "Vara");
        jsonData.put("Marime", radioButton1.isSelected() ? "S" : "M");
        jsonData.put("Sex", comboBox.getSelectedItem());
        jsonData.put("Varsta", spinner.getValue());

        // Scrierea în fișier JSON
        try (FileWriter file = new FileWriter("date.json", true)) {
            file.write(jsonData.toJSONString() + "\n");
            file.flush();
            JOptionPane.showMessageDialog(this, "Date salvate cu succes!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la salvarea datelor!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromJson() {
        // Încarc datele din JSON și le afisez în tabel
        tableModel.setRowCount(0); // Șterg toate rândurile din tabel pentru a le reîncărca

        try (BufferedReader reader = new BufferedReader(new FileReader("date.json"))) {
            JSONParser jsonParser = new JSONParser();
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject jsonData = (JSONObject) jsonParser.parse(line);
                Object[] rowData = {
                        jsonData.get("Nume"),
                        jsonData.get("Anotimp"),
                        jsonData.get("Marime"),
                        jsonData.get("Sex"),
                        jsonData.get("Varsta")
                };
                tableModel.addRow(rowData);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la încărcarea datelor din JSON!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormularApp();
            }
        });
    }
}

