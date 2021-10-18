package czk.uhk.pro2.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextField txtInputName, txtInputMessage;
    JButton btnLogin, btnSend;
    JTextArea txtAreaChat;

    public MainFrame(int width, int height){
        super("PRO2 Client Chat");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelChat = new JPanel();
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLogin.add(new JLabel("Jméno"));
        txtInputName = new JTextField("",30);
        panelLogin.add(txtInputName);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("login clicked");
            }
        });
        panelLogin.add(btnLogin);


        panelChat.setLayout(new BoxLayout(panelChat,BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);
        txtAreaChat.setAutoscrolls(true);
        txtAreaChat.setEditable(false);
        for (int i = 0; i <50;i++){
            txtAreaChat.append("Hey"+i+"\n");
        }

        txtInputMessage = new JTextField("", 50);
        panelFooter.add(txtInputMessage);

        btnSend = new JButton("Odeslat");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("odeslano "+txtInputMessage.getText());
                txtInputMessage.setText("");
            }
        });
        panelFooter.add(btnSend);


        panelMain.add(panelLogin, BorderLayout.NORTH);
        panelMain.add(panelChat, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);
        add(panelMain);
    }
}
