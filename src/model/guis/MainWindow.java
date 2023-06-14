package model.guis;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import connMySQL.ConnBD;
import dao.UsersDAO;
import model.clients.BankUsers;

public class MainWindow extends JFrame{
    
    JLabel labelCpf;
    JTextField inputCpf;

    JLabel labelPass;
    JPasswordField inputPass;

    public MainWindow()
    {
        // configurações gerais da janela
        setTitle("Banco Jobs");
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        this.labelCpf = new JLabel("CPF: ");
        this.labelCpf.setBounds(400, 220, 120, 30);
        this.inputCpf = new JTextField();
        this.inputCpf.setBounds(430, 220, 120, 30);

        this.labelPass = new JLabel("Senha: ");
        this.labelPass.setBounds(385, 260, 120, 30);
        this.inputPass = new JPasswordField();
        this.inputPass.setBounds(430, 260, 120, 30);
        this.inputPass.setEchoChar('*');



        setLayout(null);
        JButton buttonLogin = new JButton("Entrar");
        buttonLogin.setBounds(450, 300, 80, 30);
        buttonLogin.addActionListener(this::login);

        JButton buttonRegister = new JButton("Criar");
        buttonRegister.setBounds(450, 340, 80, 30);
        buttonRegister.addActionListener(this::register);

        JButton buttonQueryClients = new JButton("Clientes");
        buttonQueryClients.setBounds(600, 340, 80, 30);
        buttonQueryClients.addActionListener(this::queryClients);

        add(inputCpf);
        add(labelCpf);
        add(inputPass);
        add(labelPass);
        add(buttonLogin);
        add(buttonRegister);
        add(buttonQueryClients);
        setVisible(true);
    }
    public void login(ActionEvent event)
    {
        String cpf = inputCpf.getText();
        char[] passData = inputPass.getPassword();
        String pass = new String(passData);
        if (cpf.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cpf.length() != 11) {
            JOptionPane.showMessageDialog(null, "CPF deve conter 11 digitos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UsersDAO data = new UsersDAO(new ConnBD());
        BankUsers user = data.loginUser(cpf, pass);
        if(user == null){
            return;
        }
        dispose();
        new AccountWindow(user);
    }
    public void register(ActionEvent event)
    {
        dispose();
        new RegisterWindow();
    }
    public void queryClients(ActionEvent event)
    {
        dispose();
        new ClientsWindow();
    }
}
