package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminLoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public AdminLoginGUI() {
        setTitle("管理员登录");
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
                boolean isAuthenticated = authenticateAdmin(username, password);

                if (isAuthenticated) {
                    dispose(); // 关闭当前登录界面
                    AdminInterface adminInterface = new AdminInterface();
                    adminInterface.setVisible(true);
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

    private boolean authenticateAdmin(String username, String password) {
        File file = new File("resources/administrator.txt");

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

class AdminInterface extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> list;

    public AdminInterface() {
        setTitle("商城管理员界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("添加商品");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductDialog();
            }
        });

        JButton deleteButton = new JButton("删除商品");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex != -1) {
                    listModel.remove(selectedIndex);
                    removeProductFromTxt(selectedIndex);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
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

    private void removeProductFromTxt(int index) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("resources/adminshop.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            lines.remove(index);

            try (PrintWriter writer = new PrintWriter(new FileWriter("resources/adminshop.txt"))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class AddProductDialog extends JDialog {
        private JTextField idField;
        private JTextField nameField;
        private JTextField priceField;

        public AddProductDialog() {
            setTitle("添加商品");
            setModal(true);
            setLayout(new GridLayout(4, 2));

            idField = new JTextField();
            nameField = new JTextField();
            priceField = new JTextField();

            add(new JLabel("ID:"));
            add(idField);
            add(new JLabel("商品名:"));
            add(nameField);
            add(new JLabel("价格:"));
            add(priceField);

            JButton okButton = new JButton("确定");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String price = priceField.getText();

                    if (id.isEmpty() || name.isEmpty() || price.isEmpty()) {
                        JOptionPane.showMessageDialog(AddProductDialog.this, "请输入商品完整信息");
                    } else {
                        try {
                            Integer.parseInt(id);
                            Double.parseDouble(price);
                            listModel.addElement(id + " " + name + " " + price);
                            addProductToTxt(id, name, price);
                            dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(AddProductDialog.this, "请输入商品完整信息");
                        }
                    }
                }
            });

            JButton cancelButton = new JButton("取消");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            add(okButton);
            add(cancelButton);

            pack();
            setLocationRelativeTo(null); // 将窗口居中显示
            setVisible(true);
        }

        private void addProductToTxt(String id, String name, String price) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("resources/adminshop.txt", true))) {
                writer.println(id + " " + name + " " + price);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}