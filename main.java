import java.util.*;

class Car {
    private String carId, brand, model;
    private double pricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double pricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }

    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }

    public double calculatePrice(int days) {
        return pricePerDay * days;
    }

    public String getInfo() {
        return carId + " - " + brand + " " + model + " ($" + pricePerDay + "/day) - " + (isAvailable ? "Available" : "Rented");
    }
}

class User {
    private String userId, name, username, password;

    public User(String userId, String name, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

class Rental {
    private Car car;
    private User user;
    private int rentalDays;

    public Rental(Car car, User user, int rentalDays) {
        this.car = car;
        this.user = user;
        this.rentalDays = rentalDays;
    }

    public Car getCar() { return car; }
    public User getUser() { return user; }

    public String getInfo() {
        return "User: " + user.getName() + ", Car: " + car.getBrand() + " " + car.getModel()
               + ", Days: " + rentalDays + ", Total: $" + car.calculatePrice(rentalDays);
    }
}

class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private User loggedInUser = null;

    // --- Car Methods ---
    public void addCar(Car car) {
        cars.add(car);
    }

    public void showAvailableCars() {
        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car.getInfo());
            }
        }
    }

    public void showAllCars() {
        System.out.println("\nAll Cars:");
        for (Car car : cars) {
            System.out.println(car.getInfo());
        }
    }

    // --- User Methods ---
    public void registerUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Choose username: ");
        String username = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("❌ Username already exists.");
                return;
            }
        }

        System.out.print("Choose password: ");
        String password = scanner.nextLine();

        String userId = "U" + (users.size() + 1);
        users.add(new User(userId, name, username, password));
        System.out.println("✅ Registration successful! Please log in.");
    }

    public boolean loginUser() {
        System.out.print("Username: ");
        String uname = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(uname) && user.getPassword().equals(pass)) {
                loggedInUser = user;
                System.out.println("✅ Login successful. Welcome " + user.getName() + "!");
                return true;
            }
        }

        System.out.println("❌ Invalid credentials.");
        return false;
    }

    // --- Rental Methods ---
    public void rentCar() {
        showAvailableCars();

        System.out.print("Enter Car ID: ");
        String carId = scanner.nextLine();
        System.out.print("Enter number of days: ");
        int days = Integer.parseInt(scanner.nextLine());

        for (Car car : cars) {
            if (car.getCarId().equals(carId) && car.isAvailable()) {
                car.rent();
                rentals.add(new Rental(car, loggedInUser, days));
                System.out.println("✅ Car rented successfully.");
                return;
            }
        }

        System.out.println("❌ Car not found or not available.");
    }

    public void returnCar() {
        System.out.print("Enter Car ID to return: ");
        String carId = scanner.nextLine();

        for (Rental rental : rentals) {
            if (rental.getUser().equals(loggedInUser) && rental.getCar().getCarId().equals(carId)) {
                rental.getCar().returnCar();
                rentals.remove(rental);
                System.out.println("✅ Car returned successfully.");
                return;
            }
        }

        System.out.println("❌ No matching rental found.");
    }

    public void showRentals() {
        System.out.println("\nCurrent Rentals:");
        for (Rental rental : rentals) {
            System.out.println(rental.getInfo());
        }
    }

    // --- Admin ---
    public void adminLogin() {
        System.out.print("Admin Username: ");
        String uname = scanner.nextLine();
        System.out.print("Admin Password: ");
        String pass = scanner.nextLine();

        if (uname.equals("admin") && pass.equals("admin123")) {
            adminMenu();
        } else {
            System.out.println("❌ Invalid admin credentials.");
        }
    }

    public void adminMenu() {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Car");
            System.out.println("2. View All Cars");
            System.out.println("3. View Rentals");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Car ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Brand: ");
                    String brand = scanner.nextLine();
                    System.out.print("Model: ");
                    String model = scanner.nextLine();
                    System.out.print("Price/day: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    addCar(new Car(id, brand, model, price));
                    System.out.println("✅ Car added.");
                    break;
                case 2:
                    showAllCars();
                    break;
                case 3:
                    showRentals();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    // --- Menus ---
    public void userMenu() {
        while (true) {
            System.out.println("\n===== User Menu =====");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. Logout");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    showAvailableCars();
                    break;
                case 2:
                    rentCar();
                    break;
                case 3:
                    returnCar();
                    break;
                case 4:
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    public void mainMenu() {
        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Register as User");
            System.out.println("3. User Login");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    if (loginUser()) {
                        userMenu();
                    }
                    break;
                case 4:
                    System.out.println("👋 Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();

        system.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        system.addCar(new Car("C002", "Honda", "Accord", 70.0));
        system.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
        system.addCar(new Car("C004", "Hyundai", "i20", 55.0));
        system.addCar(new Car("C005", "Ford", "Ecosport", 65.0));
        system.addCar(new Car("C006", "Tata", "Safari", 90.0));
        system.addCar(new Car("C007", "Maruti", "Baleno", 50.0));
        system.addCar(new Car("C008", "Nissan", "Magnite", 62.0));
        system.addCar(new Car("C009", "Kia", "Seltos", 75.0));
        system.addCar(new Car("C010", "Volkswagen", "Polo", 52.0));
        system.addCar(new Car("C011", "MG", "Hector", 80.0));
        system.addCar(new Car("C012", "Skoda", "Kushaq", 78.0));
        system.addCar(new Car("C013", "Renault", "Kiger", 58.0));
        system.addCar(new Car("C014", "Jeep", "Compass", 100.0));
        system.addCar(new Car("C015", "Hyundai", "Verna", 68.0));
        system.addCar(new Car("C016", "Tata", "Nexon", 72.0));
        system.addCar(new Car("C017", "Toyota", "Innova", 95.0));
        system.addCar(new Car("C018", "Maruti", "Ertiga", 60.0));
        system.addCar(new Car("C019", "Kia", "Carens", 78.0));
        system.addCar(new Car("C020", "Ford", "Figo", 48.0));
        system.mainMenu();
    }
}
