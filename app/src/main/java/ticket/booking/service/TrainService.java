package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private final List<Train> trainList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    private boolean isValidRoute(Train train, String source, String destination) {
        if (train.getStations() == null) return false;
        int sourceIndex = train.getStations().indexOf(source.toLowerCase());
        int destIndex = train.getStations().indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }

    public TrainService() throws IOException {
        File file = new File(TRAIN_DB_PATH);
        this.trainList = file.exists() ? objectMapper.readValue(file, new TypeReference<List<Train>>() {}) : new ArrayList<>();
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> isValidRoute(train, source, destination))
                .collect(Collectors.toList());
    }

    public boolean isSeatAvailable(Train train, int row, int col, String source, String dest, List<Ticket> bookedTickets) {
        List<String> stations = train.getStations();
        int reqStart = stations.indexOf(source.toLowerCase());
        int reqEnd = stations.indexOf(dest.toLowerCase());

        if (reqStart == -1 || reqEnd == -1 || reqStart >= reqEnd) return false;

        for (Ticket ticket : bookedTickets) {
            // Only check tickets that are for the exact same seat
            if (ticket.getRow() == row && ticket.getCol() == col) {
                int bookedStart = stations.indexOf(ticket.getSource().toLowerCase());
                int bookedEnd = stations.indexOf(ticket.getDestination().toLowerCase());

                // The Overlap Formula
                if (reqStart < bookedEnd && reqEnd > bookedStart) {
                    return false; // Seat is taken for at least part of this journey
                }
            }
        }
        return true; // Seat is free!
    }

    // Rewrite the visual display map
    public void displaySeatAvailability(Train train, String source, String dest, List<Ticket> bookedTickets) {
        System.out.println("\n--- Seat Map (" + source + " -> " + dest + ") ---");
        System.out.println("[O] = Available  |  [X] = Booked\n");

        System.out.print("       ");
        for (int col = 0; col < train.getSeatsPerRow(); col++) {
            System.out.print(String.format("Col %-2d ", col));
        }
        System.out.println();

        for (int row = 0; row < train.getTotalRows(); row++) {
            System.out.print(String.format("Row %-2d ", row));
            for (int col = 0; col < train.getSeatsPerRow(); col++) {
                
                if (isSeatAvailable(train, row, col, source, dest, bookedTickets)) {
                    System.out.print(" [O]   ");
                } else {
                    System.out.print(" [X]   ");
                }
            }
            System.out.println();
        }
    }
}