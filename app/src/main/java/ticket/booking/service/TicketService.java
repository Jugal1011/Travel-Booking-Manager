package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TicketService {
    private final List<Ticket> ticketList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TICKET_DB_PATH = "app/src/main/java/ticket/booking/localDb/tickets.json";

    private void saveTicketsToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TICKET_DB_PATH), ticketList);
        } catch (IOException e) {
            System.err.println("Failed to save ticket data.");
        }
    }

    public TicketService() throws IOException {
        File file = new File(TICKET_DB_PATH);
        this.ticketList = file.exists() ? objectMapper.readValue(file, new TypeReference<List<Ticket>>() {}) : new ArrayList<>();
    }

    public List<Ticket> getTicketsForUser(String userId) {
        return ticketList.stream()
                .filter(ticket -> ticket.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Ticket createTicket(User user, Train train, String source, String destination, String date, int row, int col) {
        // Using standard constructor instead of builder
        Ticket newTicket = new Ticket(
                UUID.randomUUID().toString(),
                user.getUserId(),
                train.getTrainId(),
                source,
                destination,
                date,
                row,
                col
        );

        ticketList.add(newTicket);
        saveTicketsToFile();
        return newTicket;
    }

    public boolean cancelTicket(String userId, String ticketId) {
        boolean removed = ticketList.removeIf(ticket -> ticket.getTicketId().equals(ticketId) && ticket.getUserId().equals(userId));
        if (removed) {
            saveTicketsToFile();
        }
        return removed;
    }

    public List<Ticket> getTicketsForTrainAndDate(String trainId, String date) {
        return ticketList.stream()
                .filter(ticket -> ticket.getTrainId().equalsIgnoreCase(trainId) && 
                                  ticket.getDateOfTravel().equals(date))
                .collect(Collectors.toList());
    }
}