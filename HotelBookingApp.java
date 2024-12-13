import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// 主程式
public class HotelBookingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }
}

// 客戶類別
class Custom {
    private String name;
    private String email;
    private String phone;

    public Custom(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "姓名: " + name + ", Email: " + email + ", 電話: " + phone;
    }
}

// 訂單類別
class Order {
    private Custom customer;
    private List<Room> rooms;
    private String checkInDate;
    private String checkOutDate;

    public Order(Custom customer, List<Room> rooms, String checkInDate, String checkOutDate) {
        this.customer = customer;
        this.rooms = rooms;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(customer.toString())
          .append(", 入住日期: ").append(checkInDate)
          .append(", 退房日期: ").append(checkOutDate)
          .append(", 房間: ");
        for (Room room : rooms) {
            sb.append(room.getRoomNumber()).append(" ");
        }
        return sb.toString().trim();
    }
}

// 房間類別
class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double price;

    public Room(int id, String roomNumber, String type, double price) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return id + " - " + roomNumber + " (" + type + ", $" + price + ")";
    }
}

// 歡迎頁面
class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("SAD旅館");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("歡迎來到SAD旅館");
        welcomeLabel.setBounds(120, 30, 200, 30);
        add(welcomeLabel);

        JLabel checkInLabel = new JLabel("入住日期 (yyyy-MM-dd):");
        checkInLabel.setBounds(50, 70, 150, 30);
        add(checkInLabel);

        JTextField checkInField = new JTextField();
        checkInField.setBounds(200, 70, 100, 30);
        add(checkInField);

        JLabel checkOutLabel = new JLabel("退房日期 (yyyy-MM-dd):");
        checkOutLabel.setBounds(50, 110, 150, 30);
        add(checkOutLabel);

        JTextField checkOutField = new JTextField();
        checkOutField.setBounds(200, 110, 100, 30);
        add(checkOutField);

        JButton searchButton = new JButton("查詢房間");
        searchButton.setBounds(150, 150, 100, 30);
        add(searchButton);

        searchButton.addActionListener(e -> {
            String checkInDate = checkInField.getText();
            String checkOutDate = checkOutField.getText();
            new RoomSelectionPage(checkInDate, checkOutDate).setVisible(true);
            dispose();
        });

        JButton viewCustomersButton = new JButton("查看客戶資訊");
        viewCustomersButton.setBounds(250, 150, 120, 30);
        add(viewCustomersButton);

        viewCustomersButton.addActionListener(e -> {
            new CustomerViewPage().setVisible(true);
        });
    }
}

// 房間選擇頁面
class RoomSelectionPage extends JFrame {
    private List<Room> selectedRooms = new ArrayList<>();

    public RoomSelectionPage(String checkInDate, String checkOutDate) {
        setTitle("選擇房間");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("可用房間列表");
        titleLabel.setBounds(250, 20, 200, 30);
        add(titleLabel);

        DefaultListModel<Room> roomListModel = new DefaultListModel<>();
        JList<Room> roomList = new JList<>(roomListModel);
        JScrollPane scrollPane = new JScrollPane(roomList);
        scrollPane.setBounds(50, 60, 500, 200);
        add(scrollPane);

        loadAvailableRooms(roomListModel);

        JButton nextButton = new JButton("下一步");
        nextButton.setBounds(250, 300, 100, 30);
        add(nextButton);

        nextButton.addActionListener(e -> {
            selectedRooms.addAll(roomList.getSelectedValuesList());
            new CustomerInfoPage(checkInDate, checkOutDate, selectedRooms).setVisible(true);
            dispose();
        });
    }

    private void loadAvailableRooms(DefaultListModel<Room> model) {
        // 模擬房間資料
        model.addElement(new Room(1, "101", "單人房", 100));
        model.addElement(new Room(2, "102", "雙人房", 200));
        model.addElement(new Room(3, "103", "豪華套房", 300));
    }
}

// 顧客資訊頁面
class CustomerInfoPage extends JFrame {
    public CustomerInfoPage(String checkInDate, String checkOutDate, List<Room> selectedRooms) {
        setTitle("顧客資訊");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("姓名:");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 30);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 90, 100, 30);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(150, 90, 200, 30);
        add(emailField);

        JLabel phoneLabel = new JLabel("電話:");
        phoneLabel.setBounds(50, 130, 100, 30);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 130, 200, 30);
        add(phoneField);

        JButton confirmButton = new JButton("確認");
        confirmButton.setBounds(150, 200, 100, 30);
        add(confirmButton);

        confirmButton.addActionListener(e -> {
            Custom customer = new Custom(nameField.getText(), emailField.getText(), phoneField.getText());
            Order order = new Order(customer, selectedRooms, checkInDate, checkOutDate);
            saveOrderToFile(order);
            JOptionPane.showMessageDialog(this, "訂房成功!");
            new WelcomePage().setVisible(true);
            dispose();
        });
    }

    private void saveOrderToFile(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(order.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 查看客戶資訊頁面
class CustomerViewPage extends JFrame {
    public CustomerViewPage() {
        setTitle("客戶資訊");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(20, 20, 440, 300);
        add(scrollPane);

        JButton closeButton = new JButton("關閉");
        closeButton.setBounds(200, 330, 100, 30);
        add(closeButton);

        closeButton.addActionListener(e -> dispose());

        loadOrdersFromFile(textArea);
    }

    private void loadOrdersFromFile(JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            textArea.setText("目前無訂單資料。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}