package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Ticket {
    private String ticketId;
    private String userId;
    private String trainId;
    private String source;
    private String destination;
    private String dateOfTravel;

    public Ticket() {}

    public Ticket(String ticketId, String userId, String trainId, String source, String destination, String dateOfTravel) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.trainId = trainId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDateOfTravel() { return dateOfTravel; }
    public void setDateOfTravel(String dateOfTravel) { this.dateOfTravel = dateOfTravel; }

    public String getTicketInfo() {
        return String.format("Ticket ID: %s | User: %s | Route: %s to %s | Date: %s",
                ticketId, userId, source, destination, dateOfTravel);
    }
}