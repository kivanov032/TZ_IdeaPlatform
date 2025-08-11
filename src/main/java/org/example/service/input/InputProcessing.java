package org.example.service.input;

import java.io.IOException;
import java.util.List;
import org.example.model.Flight;

public interface InputProcessing {

    /**
     * Получение данных о полётах из JSON-файла
     *
     * @param fileName имя файла
     * @return список рейсов {@link Flight}
     */
    List<Flight> getAllFlightsFromJSON(final String fileName) throws IOException;
}
