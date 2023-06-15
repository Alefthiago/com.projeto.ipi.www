package model.guis;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.bankAccounts.BankCheckingAccount;
import model.bankAccounts.BankSavingsAccount;
import model.clients.BankUsers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountWindow extends JFrame {

    BankUsers user;

    public CreateAccountWindow(BankUsers user) {
        this.user = user;
        setTitle("Criar Conta");
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel label = new JLabel("Selecione o tipo de conta:");
        label.setBounds(120, 20, 200, 30);

        JButton SavingsButton = new JButton("Poupança");
        SavingsButton.setBounds(120, 60, 150, 30);
        SavingsButton.addActionListener(this::addAccountSavings);

        JButton checkingButton = new JButton("Corrente");
        checkingButton.setBounds(120, 100, 150, 30);
        checkingButton.addActionListener(this::addAccountChecking);

        JButton backButton = new JButton("Voltar");
        backButton.setBounds(120, 140, 150, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountWindow(user);
                dispose();
            }
        });

        add(label);
        add(SavingsButton);
        add(checkingButton);
        add(backButton);

        setVisible(true);
    }

    public void back(ActionEvent event) {
        dispose();
        new AccountWindow(this.user);
    }

    public void addAccountSavings(ActionEvent event) {
        BankSavingsAccount newAccount = new BankSavingsAccount(this.user.getCpf(), "poupança");
        this.user.addAccount(newAccount);
        this.user.loadAccounts();
        new AccountWindow(this.user);
        dispose();
    }

    public void addAccountChecking(ActionEvent event) {
        BankCheckingAccount newAccount = new BankCheckingAccount(this.user.getCpf(), "corrente");
        this.user.addAccount(newAccount);
        this.user.loadAccounts();
        new AccountWindow(this.user);
        dispose();
    }
}
