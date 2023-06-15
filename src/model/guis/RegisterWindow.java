package model.guis;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import connMySQL.ConnBD;
import dao.UsersDAO;
import model.clients.BankUsers;
import model.customException.DifferentPasswordsE;
import model.customException.EmptyFieldsE;
import model.customException.ExistingCpfE;
import model.customException.InvalidCpfTypeE;
import model.customException.InvalidFullNameE;
import model.customException.ShortCpfE;

public class RegisterWindow extends JFrame {

    JLabel labelFirstName;
    JTextField inputFirstName;

    JLabel labelLastName;
    JTextField inputLastName;

    JLabel labelCpf;
    JTextField inputCpf;

    JLabel labelPass;
    JPasswordField inputPass;

    JLabel labelConfirmPass;
    JPasswordField inputConfirmPass;

    public RegisterWindow() {
        setTitle("Banco Jobs");
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.labelFirstName = new JLabel("Nome: ");
        this.labelFirstName.setBounds(390, 160, 120, 30);
        this.inputFirstName = new JTextField();
        this.inputFirstName.setBounds(430, 160, 120, 30);

        this.labelLastName = new JLabel("Sobrenome: ");
        this.labelLastName.setBounds(355, 200, 120, 30);
        this.inputLastName = new JTextField();
        this.inputLastName.setBounds(430, 200, 120, 30);

        this.labelCpf = new JLabel("CPF: ");
        this.labelCpf.setBounds(400, 240, 120, 30);
        this.inputCpf = new JTextField();
        this.inputCpf.setBounds(430, 240, 120, 30);

        this.labelPass = new JLabel("Senha: ");
        this.labelPass.setBounds(385, 280, 120, 30);
        this.inputPass = new JPasswordField();
        this.inputPass.setBounds(430, 280, 120, 30);
        this.inputPass.setEchoChar('*');

        this.labelConfirmPass = new JLabel("Confirmar Senha: ");
        this.labelConfirmPass.setBounds(325, 320, 120, 30);
        this.inputConfirmPass = new JPasswordField();
        this.inputConfirmPass.setBounds(430, 320, 120, 30);
        this.inputConfirmPass.setEchoChar('*');

        setLayout(null);
        JButton buttonRegister = new JButton("Criar");
        buttonRegister.setBounds(450, 360, 80, 30);
        buttonRegister.addActionListener(this::register);

        JButton buttonBack = new JButton("Voltar");
        buttonBack.setBounds(450, 400, 80, 30);
        buttonBack.addActionListener(this::back);

        add(labelFirstName);
        add(inputFirstName);
        add(labelLastName);
        add(inputLastName);
        add(labelCpf);
        add(inputCpf);
        add(labelPass);
        add(inputPass);
        add(labelConfirmPass);
        add(inputConfirmPass);
        add(buttonRegister);
        add(buttonBack);
        setVisible(true);
    }

    public void register(ActionEvent event) {

        char[] password1 = inputPass.getPassword();
        char[] password2 = inputConfirmPass.getPassword();
        String userCpf = inputCpf.getText();
        String userFirstName = inputFirstName.getText().toLowerCase().trim();
        String userLastName = inputLastName.getText().toLowerCase().trim();
        try {
            if (userFirstName.isEmpty() || userLastName.isEmpty() || userCpf.isEmpty() || password1.length == 0
                    || password2.length == 0) {
                throw new EmptyFieldsE();
            } else {
                boolean passwordsMatch = Arrays.equals(password1, password2);
                if (!passwordsMatch) {
                    throw new DifferentPasswordsE();
                } else {
                    if (!userFirstName.matches("[a-zA-Z ]+") || !userLastName.matches("[a-zA-Z ]+")) {
                        throw new InvalidFullNameE();
                    }
                    if (!userCpf.matches("\\d+")) {
                        throw new InvalidCpfTypeE();
                    }
                    if (userCpf.length() != 11) {
                        throw new ShortCpfE();
                    }
                    UsersDAO data = new UsersDAO(new ConnBD());
                    if (!data.checkCpf(userCpf)) {
                        return;
                    } else {
                        String userFullName = userFirstName + " " + userLastName;
                        String userPass = new String(password1);
                        BankUsers client = new BankUsers(userCpf, userFullName, userPass);
                        data.addUser(client);
                        dispose();
                        new AccountWindow(client);
                    }
                }
            }
        } catch (ExistingCpfE exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EmptyFieldsE exception) {
            exception.alert();
        } catch (ShortCpfE exception) {
            exception.alert();
        } catch (InvalidCpfTypeE exception) {
            exception.alert();
        } catch (InvalidFullNameE exception) {
            exception.alert();
        } catch (DifferentPasswordsE exception) {
            exception.alert();
        }
    }

    public void back(ActionEvent event) {
        dispose();
        new MainWindow();
    }
}
