import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Class for Customer
class Customer {
    private String cusId;
    private String name;
    private String email;
    private String phoneNumber;

    public Customer(String cusId, String name, String email, String phoneNumber) {
        this.cusId = cusId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getCusId() {
        return cusId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

// Class for RoomType
class RoomType {
    private String id;
    private String name;
    private double price;
    private int availableRooms;

    public RoomType(String id, String name, double price, int availableRooms) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availableRooms = availableRooms;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public boolean isAvailable() {
        return availableRooms > 0;
    }

    public void bookRoom() {
        if (isAvailable()) {
            availableRooms--;
        }
    }
}

// Class for Reservation
class Reservation {
    private String reservationId;
    private Customer customer;
    private RoomType roomType;
    private String checkInDate;
    private String checkOutDate;

    public Reservation(String reservationId, Customer customer, RoomType roomType, String checkInDate, String checkOutDate) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}

// Main Application with JFrame
public class HotelBookingSystem {
    private static ArrayList<RoomType> roomTypes = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        // Initialize Room Types
        roomTypes.add(new RoomType("R001", "Single Room", 100.0, 5));
        roomTypes.add(new RoomType("R002", "Double Room", 150.0, 3));
        roomTypes.add(new RoomType("R003", "Suite", 300.0, 2));

        // Create JFrame
        JFrame frame = new JFrame("Hotel Booking System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Hotel Booking System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Center Panel for Room Types
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(roomTypes.size() + 1, 1));
        centerPanel.add(new JLabel("Available Rooms:", JLabel.CENTER));

        for (RoomType roomType : roomTypes) {
            JButton roomButton = new JButton(roomType.getName() + " - $" + roomType.getPrice() + " (Available: " + roomType.getAvailableRooms() + ")");
            roomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (roomType.isAvailable()) {
                        String customerId = JOptionPane.showInputDialog("Enter Customer ID:");
                        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
                        String customerEmail = JOptionPane.showInputDialog("Enter Customer Email:");
                        String customerPhone = JOptionPane.showInputDialog("Enter Customer Phone:");
                        Customer customer = new Customer(customerId, customerName, customerEmail, customerPhone);

                        String checkInDate = JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):");
                        String checkOutDate = JOptionPane.showInputDialog("Enter Check-Out Date (YYYY-MM-DD):");

                        String reservationId = "RES" + (reservations.size() + 1);
                        Reservation reservation = new Reservation(reservationId, customer, roomType, checkInDate, checkOutDate);
                        reservations.add(reservation);

                        roomType.bookRoom();
                        JOptionPane.showMessageDialog(frame, "Reservation Confirmed!\nReservation ID: " + reservationId);

                        frame.dispose(); // Refresh UI
                        main(args);
                    } else {
                        JOptionPane.showMessageDialog(frame, "No rooms available!");
                    }
                }
            });
            centerPanel.add(roomButton);
        }

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
