package ticket.booking;

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.service.TicketService;
import ticket.booking.service.TrainService;
import ticket.booking.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    // Class-level variables to share across functions
    private static TrainService trainService;
    private static TicketService ticketService;
    private static UserService userService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("Initializing Train Ticket Booking System...");

        try {
            // Initialize services
            trainService = new TrainService();
            ticketService = new TicketService();
            userService = new UserService(trainService, ticketService);
            scanner = new Scanner(System.in);

            System.out.println("System Ready!");
            runMenuLoop();

        } catch (IOException e) {
            System.err.println("Fatal Error: Could not initialize database files. " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close(); // Ensure scanner closes safely
            }
        }
    }

    private static void runMenuLoop() {
        boolean isRunning = true;
        
        while (isRunning) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleViewBookings();
                    break;
                case 3:
                    handleSearchTrains();
                    break;
                case 4:
                    handleBooking();
                    break;
                case 5:
                    handleSignUp();
                    break;
                case 6:
                    System.out.println("Exiting the system. Safe travels!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===========================");
        System.out.println("         MAIN MENU         ");
        System.out.println("===========================");
        System.out.println("1. Login");
        System.out.println("2. View My Bookings");
        System.out.println("3. Search Trains");
        System.out.println("4. Book a Ticket");
        System.out.println("5. SignUp");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Triggers the default switch case for invalid input
        }
    }

    private static void handleLogin() {
        System.out.print("Enter username: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userService.loginUser(name, password)) {
            System.out.println("Login successful! Welcome, " + name + ".");
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private static void handleViewBookings() {
        System.out.println("\n--- Your Bookings ---");
        userService.fetchMyBookings();
    }

    private static void handleSearchTrains() {
        System.out.print("Enter source station: ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination station: ");
        String dest = scanner.nextLine().trim();

        Train selectedTrain = searchAndSelectTrain(source, dest);

        // If a train is selected, we now ask for the date to show the dynamic seat map
        if (selectedTrain != null) {
            System.out.print("Enter date of travel (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();

            // Fetch dynamic availability based on existing tickets
            List<Ticket> bookedTickets = ticketService.getTicketsForTrainAndDate(selectedTrain.getTrainId(), date);
            trainService.displaySeatAvailability(selectedTrain, source, dest, bookedTickets);
        }
    }

    private static void handleBooking() {
        System.out.print("Enter source station: ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination station: ");
        String dest = scanner.nextLine().trim();

        Train selectedTrain = searchAndSelectTrain(source, dest);

        if (selectedTrain != null) {
            try {
                // 1. Get Date FIRST
                System.out.print("Enter date of travel (YYYY-MM-DD): ");
                String date = scanner.nextLine().trim();

                // 2. Display the Map for that specific Date
                List<Ticket> bookedTickets = ticketService.getTicketsForTrainAndDate(selectedTrain.getTrainId(), date);
                trainService.displaySeatAvailability(selectedTrain, source, dest, bookedTickets);

                // 3. Ask for Seat coordinates based on the new physical boundaries
                System.out.print("Enter seat row (0 to " + (selectedTrain.getTotalRows() - 1) + "): ");
                int row = Integer.parseInt(scanner.nextLine().trim());
                
                System.out.print("Enter seat column (0 to " + (selectedTrain.getSeatsPerRow() - 1) + "): ");
                int col = Integer.parseInt(scanner.nextLine().trim());
                
                // 4. Attempt Booking
                boolean isBooked = userService.bookTrainSeat(selectedTrain, row, col, source, dest, date);

                if (isBooked) {
                    System.out.println("\nSuccess! Ticket booked successfully.");
                } else {
                    System.out.println("\nBooking failed. Please ensure you are logged in and the seat is available (0).");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nBooking failed: Invalid seat input provided. Please enter numbers only.");
            }
        }
    }

    /**
     * Shared helper method for Cases 3 and 4.
     * Searches for trains, lists them, and handles user selection.
     */
    private static Train searchAndSelectTrain(String source, String dest) {
        List<Train> availableTrains = trainService.searchTrains(source, dest);

        if (availableTrains.isEmpty()) {
            System.out.println("No trains found for this route.");
            return null;
        }

        System.out.println("\n--- Available Trains ---");
        for (int i = 0; i < availableTrains.size(); i++) {
            System.out.println((i + 1) + ". " + availableTrains.get(i).getTrainInfo());
        }

        System.out.print("Select a train (enter list number): ");
        try {
            int trainChoice = Integer.parseInt(scanner.nextLine().trim());

            if (trainChoice >= 1 && trainChoice <= availableTrains.size()) {
                return availableTrains.get(trainChoice - 1);
            } else {
                System.out.println("Invalid train selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection. Please enter a number.");
        }
        return null; // Return null if selection fails
    }

    private static void handleSignUp() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter a new username: ");
        String signUpName = scanner.nextLine();
        System.out.print("Enter a password: ");
        String signUpPassword = scanner.nextLine();

        if (userService.signUp(signUpName, signUpPassword)) {
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Registration failed. Try using a different username.");
        }
    }
}