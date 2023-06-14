package model.guis;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.bankAccounts.BankAccounts;
import model.clients.BankUsers;

public class AccountWindow extends JFrame {

    BankUsers user;
    JLabel alert;
    JLabel title;
    JLabel userName;
    JLabel userCpf;

    public AccountWindow(BankUsers user) {
        this.user = user;
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
            JButton createAccount = new JButton("Criar conta");
            createAccount.setBounds(400, 400, 150, 30);
            createAccount.addActionListener(this::addAccountBank);
            if (numAccounts == 0) {
                this.alert = new JLabel("Você ainda não possui contas!");
                this.alert.setBounds(390, 300, 500, 30);

                add(alert);
                add(createAccount);
            }
            if (numAccounts == 1) {
                add(createAccount);
            }
        }

        int labelX = 100;
        System.out.println(this.user.getAccounts());
        for (BankAccounts account : this.user.getAccounts()) {
            String formattedBalance = formatCurrency(account.getBalance());

            JLabel accountLabel = new JLabel("Número da conta: " + account.getNumber() + " - Tipo: " + account.getType()
                    + " - Saldo: R$" + formattedBalance);
            accountLabel.setBounds(labelX, 120, 700, 30);
            add(accountLabel);

            JButton depositButton = new JButton("Depósito");
            depositButton.setBounds(labelX, 150, 100, 30);
            depositButton.addActionListener(e -> performDeposit(account));
            add(depositButton);

            JButton transferButton = new JButton("Efetuar transferência");
            transferButton.setBounds(labelX + 110, 150, 160, 30);
            transferButton.addActionListener(e -> performTransfer(account));
            add(transferButton);

            JButton withdrawButton = new JButton("Realizar saque");
            withdrawButton.setBounds(labelX, 190, 120, 30);
            withdrawButton.addActionListener(e -> performWithdraw(account));
            add(withdrawButton);

            JButton removeAccountButton = new JButton("Remover conta");
            removeAccountButton.setBounds(labelX + 130, 190, 130, 30);
            removeAccountButton.addActionListener(e -> removeAccount(account));
            add(removeAccountButton);

            labelX += 400;
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
        new CreateAccountWindow(this.user);
    }

    
    public void removeAccount(BankAccounts account) {
        int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover essa conta?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.user.removeAccount(account, this.user);
            this.user.loadAccounts();
            dispose();
            new AccountWindow(this.user);
        }
    }
    
    public void performDeposit(BankAccounts account) {
        String amountString = JOptionPane.showInputDialog(this, "Digite o valor do depósito:");
        if (amountString != null) {
            try {
                BigDecimal amount = new BigDecimal(amountString);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    account.toDeposit(amount);
                    dispose();
                    new AccountWindow(this.user);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void performWithdraw(BankAccounts account) {
        String amountString = JOptionPane.showInputDialog(this, "Digite o valor do saque:");
        if (amountString != null) {
            try {
                BigDecimal amount = new BigDecimal(amountString);
                if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(account.getBalance()) > 0) {
                    JOptionPane.showMessageDialog(this, "Valor inválido ou saldo insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    account.toWithdraw(amount);
                    dispose();
                    new AccountWindow(this.user);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void performTransfer(BankAccounts account) {

    }

    public void logout(ActionEvent e) {
        dispose();
        this.user = null;
        new MainWindow();
    }

    private String formatCurrency(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
