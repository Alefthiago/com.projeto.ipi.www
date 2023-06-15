package model.guis;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import connMySQL.ConnBD;
import dao.AccountDAO;
import model.bankAccounts.BankAccounts;
import model.clients.BankUsers;
import model.customException.AccountHasBalanceE;
import model.customException.AccountWithBalanceE;
import model.customException.InvalidValueE;
import model.customException.SameAccountTransferE;

public class AccountWindow extends JFrame {

    BankUsers user;
    JLabel alert;
    JLabel title;
    JLabel userName;
    JLabel userCpf;
    JLabel totalBalanceLabel;

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

            JButton totalBalanceButton = new JButton("Mostrar total dos saldos");
            totalBalanceButton.setBounds(380, 350, 200, 30);
            totalBalanceButton.addActionListener(this::showTotalBalance);
            add(totalBalanceButton);
        }

        JButton logoutButton = new JButton("Sair");
        logoutButton.setBounds(20, 20, 100, 30);
        logoutButton.addActionListener(this::logout);
        add(logoutButton);

        JButton removeUserButton = new JButton("Excluir conta");
        removeUserButton.setBounds(20, 400, 150, 30);
        removeUserButton.addActionListener(this::removeUser);
        add(removeUserButton);

        setVisible(true);
        add(title);
        add(userCpf);
    }

    public void addAccountBank(ActionEvent e) {
        dispose();
        new CreateAccountWindow(this.user);
    }

    public void removeAccount(BankAccounts account) {
        try {
            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                throw new AccountWithBalanceE();
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover essa conta?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    this.user.removeAccount(account);
                    this.user.loadAccounts();
                    dispose();
                    new AccountWindow(this.user);
                }
            }
        } catch (AccountWithBalanceE exception) {
            exception.alert();
        }
    }

    public void removeUser(ActionEvent event) {
        AccountDAO DAO = new AccountDAO(new ConnBD());
        boolean hasBalance = DAO.checkAccountBalances(this.user.getCpf());

        try {
            if (hasBalance) {
                throw new AccountHasBalanceE();
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir sua conta?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    DAO.removeUser(this.user.getCpf());
                    logout(event);
                }
            }

        } catch (AccountHasBalanceE exception) {
            exception.alert();
        }
    }

    public void performDeposit(BankAccounts account) {
        String amountString = JOptionPane.showInputDialog(this, "Digite o valor do depósito:");
        if (amountString != null) {
            try {
                BigDecimal amount = new BigDecimal(amountString);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new InvalidValueE();
                } else {
                    account.toDeposit(amount);
                    this.user.loadAccounts();
                    dispose();
                    new AccountWindow(this.user);
                }
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidValueE exception) {
                exception.alert();
            }
        }
    }

    public void performWithdraw(BankAccounts account) {
        String amountString = JOptionPane.showInputDialog(this, "Digite o valor do saque:");
        if (amountString != null) {
            try {
                BigDecimal amount = new BigDecimal(amountString);
                if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(account.getBalance()) > 0) {
                    throw new InvalidValueE();
                } else {
                    account.toWithdraw(amount);
                    this.user.loadAccounts();
                    dispose();
                    new AccountWindow(this.user);
                }
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidValueE exception) {
                exception.alert();
            }
        }
    }

    public void performTransfer(BankAccounts account) {
        String accountNumberString = JOptionPane.showInputDialog(this, "Digite o número da conta de destino:");
        if (accountNumberString != null) {
            try {
                int accountNumber = Integer.parseInt(accountNumberString);
                AccountDAO DAO = new AccountDAO(new ConnBD());
                if (DAO.findAccountByNumber(accountNumber)) {
                    if (account.getNumber() == accountNumber) {
                        throw new SameAccountTransferE();
                    } else {
                        String amountString = JOptionPane.showInputDialog(this, "Digite o valor da transferência:");
                        if (amountString != null) {
                            try {
                                BigDecimal amount = new BigDecimal(amountString);
                                if (amount.compareTo(BigDecimal.ZERO) <= 0
                                        || amount.compareTo(account.getBalance()) > 0) {
                                    throw new InvalidValueE();
                                } else {
                                    account.toTransfer(amount, accountNumber);
                                    this.user.loadAccounts();
                                    dispose();
                                    new AccountWindow(this.user);
                                }
                            } catch (NumberFormatException exception) {
                                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (InvalidValueE exception) {
                                exception.alert();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "A conta de destino não foi encontrada!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Número de conta inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SameAccountTransferE exception) {
                exception.alert();
            }
        }
    }

    public void logout(ActionEvent event) {
        dispose();
        this.user = null;
        new MainWindow();
    }

    private String formatCurrency(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();

    }

    public void showTotalBalance(ActionEvent event) {
        JOptionPane.showMessageDialog(this, "Saldo de " + user.totalBalance());
    }

}
