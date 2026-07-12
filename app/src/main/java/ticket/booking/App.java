package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.service.TicketService;
import ticket.booking.service.TrainService;
import ticket.booking.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Initializing Train Ticket Booking System...");

        try {
            TrainService trainService = new TrainService();
            TicketService ticketService = new TicketService();
            UserService userService = new UserService(trainService, ticketService);

            Scanner scanner = new Scanner(System.in);
            boolean isRunning = true;

            System.out.println("System Ready!");

            while (isRunning) {
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

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        if (userService.loginUser(name, password)) {
                            System.out.println("Login successful! Welcome, " + name + ".");
                        } else {
                            System.out.println("Invalid credentials. Try again.");
                        }
                        break;

                    case 2:
                        System.out.println("\n--- Your Bookings ---");
                        userService.fetchMyBookings();
                        break;

                    case 3:
                        System.out.print("Enter source station: ");
                        String source = scanner.nextLine();
                        System.out.print("Enter destination station: ");
                        String dest = scanner.nextLine();

                        List<Train> trains = trainService.searchTrains(source, dest);
                        if (trains.isEmpty()) {
                            System.out.println("No trains found for this route.");
                        } else {
                            System.out.println("\n--- Available Trains ---");
                            for (int i = 0; i < trains.size(); i++) {
                                System.out.println((i + 1) + ". " + trains.get(i).getTrainInfo());
                            }
                        }
                        break;

                    case 4:
                        System.out.print("Enter source station: ");
                        String bookSource = scanner.nextLine();
                        System.out.print("Enter destination station: ");
                        String bookDest = scanner.nextLine();

                        List<Train> availableTrains = trainService.searchTrains(bookSource, bookDest);

                        if (availableTrains.isEmpty()) {
                            System.out.println("No trains found for this route.");
                            break;
                        }

                        System.out.println("\n--- Available Trains ---");
                        for (int i = 0; i < availableTrains.size(); i++) {
                            System.out.println((i + 1) + ". " + availableTrains.get(i).getTrainInfo());
                        }

                        System.out.print("Select a train (enter list number): ");
                        int trainChoice;
                        try {
                            trainChoice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid selection.");
                            break;
                        }

                        if (trainChoice < 1 || trainChoice > availableTrains.size()) {
                            System.out.println("Invalid train selection.");
                            break;
                        }

                        Train selectedTrain = availableTrains.get(trainChoice - 1);

                        System.out.print("Enter seat row (0 to " + (selectedTrain.getSeats().size() - 1) + "): ");
                        int row = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter seat column: (0 to " + (selectedTrain.getSeats().get(row).size() - 1) + "): ");
                        int col = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter date of travel (YYYY-MM-DD): ");
                        String date = scanner.nextLine();

                        boolean isBooked = userService.bookTrainSeat(selectedTrain, row, col, bookSource, bookDest, date);

                        if (isBooked) {
                            System.out.println("\nSuccess! Ticket booked successfully.");
                        } else {
                            System.out.println("\nBooking failed. Please ensure you are logged in and the seat is available (0).");
                        }
                        break;

                    case 5:
                        System.out.println("\n--- User Registration ---");
                        System.out.print("Enter a new username: ");
                        String signUpName = scanner.nextLine();
                        System.out.print("Enter a password: ");
                        String signUpPassword = scanner.nextLine();

                        // Clean execution: logic is decoupled from CLI
                        if (userService.signUp(signUpName, signUpPassword)) {
                            System.out.println("Registration successful! You can now log in.");
                        } else {
                            System.out.println("Registration failed. Try using a different username.");
                        }
                        break;

                    case 6:
                        System.out.println("Exiting the system. Safe travels!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            }
            scanner.close();

        } catch (IOException e) {
            System.err.println("Fatal Error: Could not initialize database files. " + e.getMessage());
        }
    }
}