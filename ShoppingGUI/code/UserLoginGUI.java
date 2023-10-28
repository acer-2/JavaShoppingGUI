package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserLoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public UserLoginGUI() {
        setTitle("用户登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean isAuthenticated = authenticateUser(username, password);

                if (isAuthenticated) {
                    dispose(); // 关闭当前登录界面
                    UserInterface userInterface = new UserInterface();
                    userInterface.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "账户密码不正确，请重新输入。");
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

        add(new JLabel("用户名:"));
        add(usernameField);
        add(new JLabel("密码:"));
        add(passwordField);
        add(loginButton);

        pack();
        setLocationRelativeTo(null); // 将窗口居中显示
    }

    private boolean authenticateUser(String username, String password) {
        File file = new File("resources/users.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

class UserInterface extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private ShoppingCart cart = new ShoppingCart();

    public UserInterface() {
        setTitle("商城用户界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("加入购物车");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex != -1) {
                    String[] parts = listModel.getElementAt(selectedIndex).split(" ");
                    cart.add(parts[1], 1, Double.parseDouble(parts[2]));
                }
            }
        });

        JButton viewButton = new JButton("查看购物车");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CartDialog();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadProductsFromTxt();

        pack();
        setLocationRelativeTo(null); // 将窗口居中显示
    }

    private void loadProductsFromTxt() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/adminshop.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CartDialog extends JDialog {
        private DefaultListModel<String> listModel;
        private JList<String> list;

        public CartDialog() {
            setTitle("购物车");
            setModal(true);
            setLayout(new BorderLayout());

            listModel = new DefaultListModel<>();
            list = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(list);
            add(scrollPane, BorderLayout.CENTER);

            for (String item : cart.getItems()) {
                listModel.addElement(item);
            }

            JButton removeButton = new JButton("移出购物车");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        cart.remove(selectedIndex);
                        listModel.remove(selectedIndex);
                    }
                }
            });

            JButton clearButton = new JButton("清空购物车");
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart.clear();
                    listModel.clear();
                }
            });

            JButton sumButton = new JButton("计算总价");
            sumButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(CartDialog.this, "总价: " + cart.sum());
                }
            });

            JButton cancelButton = new JButton("取消");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(removeButton);
            buttonPanel.add(clearButton);
            buttonPanel.add(sumButton);
            buttonPanel.add(cancelButton);
            add(buttonPanel, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(null); // 将窗口居中显示
            setVisible(true);
        }
    }
}
