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
import dao.UsersDAO;
import model.clients.BankUsers;
import model.bankAccounts.BankAccounts;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClientsWindow extends JFrame {

    private JList<String> clientList;
    private DefaultListModel<String> model;
    private JTextField searchField;
    private JButton backButton;
    private JLabel userDataLabel;
    private JLabel accountDataLabel;

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
            if (!e.getValueIsAdjusting()) {
                handleSelection(clientList.getSelectedValue());
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(clientList);

        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Informações do Cliente");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        mainPanel.add(titleLabel, constraints);

        JLabel searchLabel = new JLabel("Número de CPF:");
        searchField = new JTextField(9);
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

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        mainPanel.add(searchLabel, constraints);

        constraints.gridx = 1;
        mainPanel.add(searchField, constraints);

        backButton = new JButton("Voltar");
        backButton.addActionListener(this::backWindow);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        mainPanel.add(backButton, constraints);

        userDataLabel = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        mainPanel.add(userDataLabel, constraints);

        accountDataLabel = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        mainPanel.add(accountDataLabel, constraints);

        getContentPane().add(mainPanel, BorderLayout.EAST);

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
        String cpf = item.replaceAll("\\D", "");
        return cpf;
    }

    private void handleSelection(String selectedValue) {
        String cpf = extractCPF(selectedValue);

        UsersDAO data = new UsersDAO(new ConnBD());
        BankUsers user = data.getUserByCPF(cpf);

        if (user != null) {
            StringBuilder userData = new StringBuilder();
            userData.append("<html>");
            userData.append("Nome: ").append(user.getFullName()).append("<br/>");
            userData.append("CPF: ").append(user.getCpf()).append("<br/>");
            userData.append("</html>");
            userDataLabel.setText(userData.toString());

            List<BankAccounts> accounts = user.getAccounts();

            if (accounts != null && !accounts.isEmpty()) {
                StringBuilder accountData = new StringBuilder();
                accountData.append("<html>Contas:<br/>");
                for (BankAccounts account : accounts) {
                    accountData.append("Número: ").append(account.getNumber()).append("<br/>");
                    accountData.append("Tipo: ").append(account.getType()).append("<br/>");
                    accountData.append("Saldo: ").append(account.getBalance()).append("<br/><br/>");
                }
                accountData.append("</html>");
                accountDataLabel.setText(accountData.toString());
            } else {
                accountDataLabel.setText("Não há contas associadas a este usuário.");
            }
        } else {
            userDataLabel.setText("");
            accountDataLabel.setText("");
        }
    }
}
