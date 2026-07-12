package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TrainService trainService;
    private final TicketService ticketService;
    private List<User> userList;
    private User currentUser;

    private static final String USER_FILE_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserService(TrainService trainService, TicketService ticketService) throws IOException {
        this.trainService = trainService;
        this.ticketService = ticketService;
        loadUserListFromFile();
    }

    private void loadUserListFromFile() throws IOException {
        File file = new File(USER_FILE_PATH);
        this.userList = file.exists() ? objectMapper.readValue(file, new TypeReference<List<User>>() {}) : new ArrayList<>();
    }

    public boolean loginUser(String name, String rawPassword) {
        Optional<User> foundUser = userList.stream()
                .filter(u -> u.getName().equals(name) && UserServiceUtil.checkPassword(rawPassword, u.getHashedPassword()))
                .findFirst();

        if (foundUser.isPresent()) {
            this.currentUser = foundUser.get();
            return true;
        }
        return false;
    }

    public boolean signUp(String name, String rawPassword) {
        if (name == null || name.trim().isEmpty() || rawPassword == null || rawPassword.isEmpty()) {
            return false;
        }

        // Check if username already exists to prevent duplicates
        boolean userExists = userList.stream().anyMatch(u -> u.getName().equalsIgnoreCase(name));
        if (userExists) {
            System.out.println("Username already exists!");
            return false;
        }

        // Generate secure credentials internally
        String userId = java.util.UUID.randomUUID().toString();
        String hashedPassword = ticket.booking.util.UserServiceUtil.hashPassword(rawPassword);

        // Instantiate the POJO and add it to the list
        User newUser = new User(userId, name, rawPassword, hashedPassword);
        userList.add(newUser);

        try {
            saveUserListToFile();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to write new user to database: " + e.getMessage());
            return false;
        }
    }

    public void fetchMyBookings() {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }
        List<Ticket> myTickets = ticketService.getTicketsForUser(currentUser.getUserId());
        if (myTickets.isEmpty()) {
            System.out.println("No active bookings found.");
        } else {
            for (Ticket ticket : myTickets) {
                System.out.println(ticket.getTicketInfo());
            }
        }
    }

    public boolean bookTrainSeat(Train train, int row, int seat, String source, String dest, String date) {
        if (currentUser == null) return false;

        List<List<Integer>> seats = train.getSeats();
        if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
            if (seats.get(row).get(seat) == 0) {
                // 1. Reserve Seat
                seats.get(row).set(seat, 1);
                trainService.updateTrain(train);

                // 2. Generate Ticket Record
                ticketService.createTicket(currentUser, train, source, dest, date);
                return true;
            }
        }
        return false;
    }

    private void saveUserListToFile() throws IOException {
        File file = new File(USER_FILE_PATH);
        // writerWithDefaultPrettyPrinter() ensures the JSON is nicely indented.
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, userList);
    }
}