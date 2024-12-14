import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HotelBookingApp {
    private static List<Room> availableRooms = new ArrayList<>();
    public static List<BookedRoomRecord> bookedRoomRecords = new ArrayList<>();

    public static void main(String[] args) {
        loadRoomData();
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }

    private static void loadRoomData() {
        // 初始化房間數據
        availableRooms.add(new Room(1, "101", "單人房", 100));
        availableRooms.add(new Room(1, "102", "單人房", 100));
        availableRooms.add(new Room(1, "103", "單人房", 100));
        availableRooms.add(new Room(1, "104", "單人房", 100));
        availableRooms.add(new Room(2, "105", "雙人房", 200));
        availableRooms.add(new Room(2, "106", "雙人房", 200));
        availableRooms.add(new Room(2, "107", "雙人房", 200));
        availableRooms.add(new Room(3, "108", "豪華套房", 300));
        availableRooms.add(new Room(3, "109", "豪華套房", 300));
        availableRooms.add(new Room(4, "110", "總統套房", 500));

        // 從檔案讀取已被訂的房間紀錄
        try (BufferedReader reader = new BufferedReader(new FileReader("booked_rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int roomId = Integer.parseInt(parts[0]);
                String roomNumber = parts[1];
                String type = parts[2];
                double price = Double.parseDouble(parts[3]);
                String checkInDate = parts[4];
                String checkOutDate = parts[5];

                Room room = new Room(roomId, roomNumber, type, price);
                bookedRoomRecords.add(new BookedRoomRecord(room, checkInDate, checkOutDate));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No booked rooms file found. Starting fresh.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveBookedRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("booked_rooms.txt"))) {
            for (BookedRoomRecord record : bookedRoomRecords) {
                Room room = record.getRoom();
                writer.write(room.getId() + "," + room.getRoomNumber() + "," + room.getType() + "," +
                        room.getPrice() + "," + record.getCheckInDate() + "," + record.getCheckOutDate() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Room> getAvailableRooms(String checkInDate, String checkOutDate) {
        List<Room> result = new ArrayList<>(availableRooms);

        for (BookedRoomRecord record : bookedRoomRecords) {
            if (isDateOverlap(checkInDate, checkOutDate, record.getCheckInDate(), record.getCheckOutDate())) {
                result.remove(record.getRoom());
            }
        }
        return result;
    }

    public static void bookRooms(List<Room> rooms, String checkInDate, String checkOutDate) {
        for (Room room : rooms) {
            bookedRoomRecords.add(new BookedRoomRecord(room, checkInDate, checkOutDate));
        }
        saveBookedRooms();
    }

    private static boolean isDateOverlap(String start1, String end1, String start2, String end2) {
        return !(end1.compareTo(start2) <= 0 || start1.compareTo(end2) >= 0);
    }
}

class BookedRoomRecord {
    private Room room;
    private String checkInDate;
    private String checkOutDate;

    public BookedRoomRecord(Room room, String checkInDate, String checkOutDate) {
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Room getRoom() {
        return room;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}

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

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return id + " - " + roomNumber + " (" + type + ", $" + price + ")";
    }
}

class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("SAD旅館");
        setSize(800, 400);
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
        searchButton.setBounds(50, 150, 100, 30);
        add(searchButton);

        JButton viewCustomersButton = new JButton("查看訂單");
        viewCustomersButton.setBounds(200, 150, 150, 30);
        add(viewCustomersButton);

        searchButton.addActionListener(e -> {
            String checkInDate = checkInField.getText();
            String checkOutDate = checkOutField.getText();
            new RoomSelectionPage(checkInDate, checkOutDate).setVisible(true);
            dispose();
        });

        viewCustomersButton.addActionListener(e -> {
            new CustomerViewPage().setVisible(true);
        });
    }
}

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

        List<Room> availableRooms = HotelBookingApp.getAvailableRooms(checkInDate, checkOutDate);
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setBounds(50, 60, 500, 200);
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

        JCheckBox[] roomCheckboxes = new JCheckBox[availableRooms.size()];
        for (int i = 0; i < roomCheckboxes.length; i++) {
            Room room = availableRooms.get(i);
            roomCheckboxes[i] = new JCheckBox(room.toString());
            checkboxPanel.add(roomCheckboxes[i]);
        }
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setBounds(50, 60, 500, 200);
        add(scrollPane);

        JButton nextButton = new JButton("下一步");
        nextButton.setBounds(250, 300, 100, 30);
        add(nextButton);

        nextButton.addActionListener(e -> {
            selectedRooms.clear();
            for (int i = 0; i < roomCheckboxes.length; i++) {
                if (roomCheckboxes[i].isSelected()) {
                    selectedRooms.add(availableRooms.get(i));
                }
            }
            if (selectedRooms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "請選擇至少一間房間！");
            } else {
                new CustomerInfoPage(checkInDate, checkOutDate, selectedRooms).setVisible(true);
                dispose();
            }
        });
    }
}

class CustomerInfoPage extends JFrame {
    public CustomerInfoPage(String checkInDate, String checkOutDate, List<Room> selectedRooms) {
        setTitle("客戶資訊");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("客戶姓名:");
        nameLabel.setBounds(50, 30, 100, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 30, 200, 30);
        add(nameField);

        JLabel phoneLabel = new JLabel("聯絡電話:");
        phoneLabel.setBounds(50, 80, 100, 30);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 80, 200, 30);
        add(phoneField);

        JButton confirmButton = new JButton("確認預訂");
        confirmButton.setBounds(150, 150, 100, 30);
        add(confirmButton);

        confirmButton.addActionListener(e -> {
            String customerName = nameField.getText();
            String customerPhone = phoneField.getText();

            if (customerName.isEmpty() || customerPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "請填寫完整的客戶資訊！");
            } else {
                HotelBookingApp.bookRooms(selectedRooms, checkInDate, checkOutDate);
                JOptionPane.showMessageDialog(this, "預訂成功！\n客戶姓名: " + customerName + "\n聯絡電話: " + customerPhone);
                new WelcomePage().setVisible(true);
                dispose();
            }
        });
    }
}

class CustomerViewPage extends JFrame {
    public CustomerViewPage() {
        setTitle("Order");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("已預訂房間列表");
        titleLabel.setBounds(250, 20, 200, 30);
        add(titleLabel);

        JTextArea bookedRoomArea = new JTextArea();
        bookedRoomArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookedRoomArea);
        scrollPane.setBounds(50, 60, 500, 250);
        add(scrollPane);

        StringBuilder content = new StringBuilder();
        for (BookedRoomRecord record : HotelBookingApp.bookedRoomRecords) {
            content.append("房間: ").append(record.getRoom()).append("\n");
            content.append("入住日期: ").append(record.getCheckInDate()).append("\n");
            content.append("退房日期: ").append(record.getCheckOutDate()).append("\n\n");
        }
        bookedRoomArea.setText(content.toString());

        JButton backButton = new JButton("返回");
        backButton.setBounds(250, 330, 100, 30);
        add(backButton);

        backButton.addActionListener(e -> {
            new WelcomePage().setVisible(true);
            dispose();
        });
    }
}
