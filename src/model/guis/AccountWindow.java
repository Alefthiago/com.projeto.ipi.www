package model.guis;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.clients.BankUsers;

public class AccountWindow extends JFrame {

    BankUsers user;
    JLabel alert;
    JLabel title;
    JLabel userName;
    JLabel userCpf;

    public AccountWindow(BankUsers user) {
        this.user = user;
        System.out.println(this.user.getAccounts());
        setTitle("Banco Jobs");
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        this.title = new JLabel("Bem-Vindo " + this.user.getFullName());
        this.title.setBounds(400, 50, 500, 30);

        this.userCpf = new JLabel("Número do cpf: " + this.user.getCpf());
        this.userCpf.setBounds(400, 80, 500, 30);

        int numAccounts = this.user.getAccounts().size();
        if (numAccounts == 0 || numAccounts == 1) {
            this.alert = new JLabel("Você ainda não possui contas!");
            this.alert.setBounds(390, 300, 500, 30);

            JButton createAccount = new JButton("Criar conta");
            createAccount.setBounds(400, 340, 150, 30);
            createAccount.addActionListener(this::addAccountBank);
            add(alert);
            add(createAccount);
        }

        JButton logoutButton = new JButton("Sair");
        logoutButton.setBounds(20, 20, 100, 30);
        logoutButton.addActionListener(this::logout);
        add(logoutButton);

        setVisible(true);
        add(title);
        add(userCpf);
    }

    public void addAccountBank(ActionEvent e) {
        dispose();
        new CreateAccountWindow(user);
    }

    public void logout(ActionEvent e) {
        dispose();
        this.user = null;
        new MainWindow();
    }
}
