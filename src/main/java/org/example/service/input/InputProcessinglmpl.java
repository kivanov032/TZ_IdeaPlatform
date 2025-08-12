package org.example.service.input;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import org.example.model.Flight;

public class InputProcessinglmpl implements InputProcessing {

    /**
     * Парсинг входящего JSON-файла в список объектов типа {@link Flight}.
     * Возвращает отфильтрованный список, исключая объекты с null в ключевых полях.
     *
     * @param fileName имя файла
     * @return отфильтрованный список объектов {@link Flight}
     * @throws FileNotFoundException если файл не найден
     * @throws IOException если произошла ошибка ввода-вывода или парсинга JSON
     */

    @Override
    public List<Flight> getAllFlightsFromJSON(@NonNull final String fileName) throws IOException {
        InputStream inputStream = getFileAsInputStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Файл не найден: " + fileName);
        }

        try (inputStream) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode ticketsNode = rootNode.get("tickets");
            if (ticketsNode == null) {
                throw new IOException("Список 'tickets' отсутствует в JSON.");
            }

            List<Flight> flights = objectMapper.readValue(ticketsNode.traverse(), new TypeReference<>() {});

            // Фильтрация списка, исключая объекты с null в ключевых полях
            return filterFlightsFromJSONByCorrectData(flights);

        } catch (IOException ex) {
            throw new IOException("Ошибка парсинга: " + ex.getMessage());
        }
    }

//    /**
//     * Преобразует файл в поток {@link InputStream}
//     * Файл ищется в директории \src\main\resources
//     *
//     * @param fileName имя файла
//     * @return поток {@link InputStream}, или {@code null}, если файл не найден
//     */
//    public InputStream getFileAsInputStream(@NonNull final String fileName) {
//        return this.getClass()
//                .getClassLoader()
//                .getResourceAsStream(fileName);
//    }

    /**
     * Преобразует файл в поток {@link InputStream}.
     * Файл ищется в той же директории, что и jar-файл.
     *
     * @param fileName имя файла
     * @return поток {@link InputStream}, или {@code null}, если файл не найден
     */
    public InputStream getFileAsInputStream(@NonNull final String fileName) {
        try {
            String currentDir = System.getProperty("user.dir");
            File file = new File(currentDir, fileName);

            return file.exists() ? new FileInputStream(file) : null;

        } catch (FileNotFoundException e) {
            return null;
        }
    }



    /**
     * Фильтрует список рейсов, оставляя только те, у которых все ключевые поля не равны {@code null}.
     * Ключевые поля включают: авиаперевозчика ({@link Flight#getCarrier}), город отправления
     * ({@link Flight#getOriginName()}) и город прибытия ({@link Flight#getDestinationName()}).
     *
     * @param flights список рейсов для фильтрации
     * @return отфильтрованный список рейсов
     */
    public List<Flight> filterFlightsFromJSONByCorrectData(final List<Flight> flights) {
        if (flights == null) {
            return Collections.emptyList();
        }
        return flights.stream()
                .filter(fight -> fight.getCarrier() != null &&
                                fight.getOriginName() != null &&
                                fight.getDestinationName() != null &&
                                !fight.getOriginName().equals(fight.getDestinationName())
                        )
                .collect(Collectors.toList());
    }

}
