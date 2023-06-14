package model.guis;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import connMySQL.ConnBD;
import dao.ClientsListDAO;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClientsWindow extends JFrame {

    private JList<String> clientList;
    private DefaultListModel<String> model;
    private JTextField searchField;
    private JButton backButton;

    public ClientsWindow() {
        setTitle("Banco Jobs");
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new DefaultListModel<>();

        ClientsListDAO clientsListDAO = new ClientsListDAO(new ConnBD());
        List<String> clients = clientsListDAO.getAllClients();

        for (String client : clients) {
            model.addElement(client);
        }

        clientList = new JList<>(model);
        clientList.addListSelectionListener(e -> {
            String value = clientList.getSelectedValue().replaceAll("\\D", "");
            System.out.println(value);
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(clientList);

        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JLabel searchLabel = new JLabel("Número de CPF:");
        searchField = new JTextField(10);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                filterList();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                filterList();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                filterList();
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        getContentPane().add(searchPanel, BorderLayout.NORTH);

        backButton = new JButton("Voltar");
        backButton.addActionListener(this::backWindow);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void backWindow(ActionEvent e) {
        dispose();
        new MainWindow();
    }

    private void filterList() {
        String searchTerm = searchField.getText().toLowerCase();
        DefaultListModel<String> filteredModel = new DefaultListModel<>();

        for (int i = 0; i < model.size(); i++) {
            String item = model.getElementAt(i);
            String cpf = extractCPF(item);

            if (cpf.toLowerCase().contains(searchTerm)) {
                filteredModel.addElement(item);
            }
        }

        clientList.setModel(filteredModel);
    }

    private String extractCPF(String item) {
        int startIndex = item.indexOf("CPF: ") + 5;
        int endIndex = item.length() - 1;
        return item.substring(startIndex, endIndex);
    }
}
