package ticket.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public TrainService() throws IOException {
        File file = new File(TRAIN_DB_PATH);
        this.trainList = file.exists() ? objectMapper.readValue(file, new TypeReference<List<Train>>() {}) : new ArrayList<>();
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> isValidRoute(train, source, destination))
                .collect(Collectors.toList());
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            System.err.println("Failed to save train data.");
        }
    }

    private boolean isValidRoute(Train train, String source, String destination) {
        if (train.getStations() == null) return false;
        int sourceIndex = train.getStations().indexOf(source.toLowerCase());
        int destIndex = train.getStations().indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }
}