package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Train {
    private String trainId;
    private String trainNo;
    private int totalRows;
    private int seatsPerRow;
    private Map<String, String> stationTimes;
    private List<String> stations;

    public Train() {}

    public Train(String trainId, String trainNo, List<List<List<Integer>>> seats, int totalRows, int seatsPerRow, Map<String, String> stationTimes, List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }

    public Map<String, String> getStationTimes() { return stationTimes; }
    public void setStationTimes(Map<String, String> stationTimes) { this.stationTimes = stationTimes; }

    public List<String> getStations() { return stations; }
    public void setStations(List<String> stations) { this.stations = stations; }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

    public int getSeatsPerRow() { return seatsPerRow; }
    public void setSeatsPerRow(int seatsPerRow) { this.seatsPerRow = seatsPerRow; }

    public String getTrainInfo() {
        return String.format("Train ID: %s | Train No: %s", trainId, trainNo);
    }
}