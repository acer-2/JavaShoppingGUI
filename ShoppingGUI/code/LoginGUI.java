package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JButton userButton;
    private JButton adminButton;

    public LoginGUI() {
        setTitle("登录界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        userButton = new JButton("用户登录");
        adminButton = new JButton("管理员登录");

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前登录界面
                UserLoginGUI userLoginGUI = new UserLoginGUI();
                userLoginGUI.setVisible(true);
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前登录界面
                AdminLoginGUI adminLoginGUI = new AdminLoginGUI();
                adminLoginGUI.setVisible(true);
            }
        });

        add(userButton);
        add(adminButton);

        pack();
        setLocationRelativeTo(null); // 将窗口居中显示
    }

    public static void main(String[] args) {
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
    }
}










