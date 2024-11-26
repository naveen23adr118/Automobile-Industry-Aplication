import java.sql.*;
import java.util.Scanner;

public class AutomobileApp {
    
    // Database connection credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/automobile_db";
    private static final String USER = "root";  // Replace with your MySQL username
    private static final String PASSWORD = "12345";  // Replace with your MySQL password
    
    public static void main(String[] args) {
        // Try-with-resources statement to ensure connection and resources are closed
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            
            // Scanner to take input from the user
            Scanner scanner = new Scanner(System.in);
            
            // Main menu
            while (true) {
                System.out.println("\nAutomobile Sales Application");
                System.out.println("1. View All Cars");
                System.out.println("2. View All Customers");
                System.out.println("3. View All Sales");
                System.out.println("4. View Sales by Customer Email");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
                
                switch (choice) {
                    case 1:
                        viewAllCars(conn);
                        break;
                    case 2:
                        viewAllCustomers(conn);
                        break;
                    case 3:
                        viewAllSales(conn);
                        break;
                    case 4:
                        System.out.print("Enter customer email: ");
                        String email = scanner.nextLine();
                        viewSalesByCustomerEmail(conn, email);
                        break;
                    case 5:
                        System.out.println("Exiting the application...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Function to view all cars
    private static void viewAllCars(Connection conn) {
        String query = "SELECT * FROM Cars";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\nCars Available:");
            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String model = rs.getString("model");
                String manufacturer = rs.getString("manufacturer");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                
                System.out.println("ID: " + carId + ", Model: " + model + ", Manufacturer: " + manufacturer
                        + ", Year: " + year + ", Price: $" + price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Function to view all customers
    private static void viewAllCustomers(Connection conn) {
        String query = "SELECT * FROM Customers";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\nCustomers:");
            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                
                System.out.println("ID: " + customerId + ", Name: " + name + ", Email: " + email + ", Phone: " + phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Function to view all sales transactions
    private static void viewAllSales(Connection conn) {
        String query = "SELECT s.sale_id, c.model, cu.name, s.sale_price, s.sale_date " +
                       "FROM Sales s " +
                       "JOIN Cars c ON s.car_id = c.car_id " +
                       "JOIN Customers cu ON s.customer_id = cu.customer_id";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\nSales Transactions:");
            while (rs.next()) {
                int saleId = rs.getInt("sale_id");
                String carModel = rs.getString("model");
                String customerName = rs.getString("name");
                double salePrice = rs.getDouble("sale_price");
                Date saleDate = rs.getDate("sale_date");
                
                System.out.println("Sale ID: " + saleId + ", Car Model: " + carModel + ", Customer: " + customerName
                        + ", Sale Price: $" + salePrice + ", Sale Date: " + saleDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Function to view sales transactions by customer email
    private static void viewSalesByCustomerEmail(Connection conn, String email) {
        String query = "SELECT s.sale_id, c.model, cu.name, s.sale_price, s.sale_date " +
                       "FROM Sales s " +
                       "JOIN Cars c ON s.car_id = c.car_id " +
                       "JOIN Customers cu ON s.customer_id = cu.customer_id " +
                       "WHERE cu.email = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            System.out.println("\nSales Transactions for Customer: " + email);
            if (!rs.next()) {
                System.out.println("No sales found for the provided email.");
            } else {
                do {
                    int saleId = rs.getInt("sale_id");
                    String carModel = rs.getString("model");
                    String customerName = rs.getString("name");
                    double salePrice = rs.getDouble("sale_price");
                    Date saleDate = rs.getDate("sale_date");
                    
                    System.out.println("Sale ID: " + saleId + ", Car Model: " + carModel + ", Customer: " + customerName
                            + ", Sale Price: $" + salePrice + ", Sale Date: " + saleDate);
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
