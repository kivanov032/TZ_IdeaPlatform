package org.example;

import org.example.model.Flight;
import org.example.service.input.InputProcessinglmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessingTest {

    private InputProcessinglmpl fileProcessing;
    private DateTimeFormatter dateFormatter;


    @BeforeEach
    void setUp() {
        fileProcessing = new InputProcessinglmpl();
        dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
    }

    @Test
    void testGetAllFlightsFromJSON_Success() throws IOException {
        Flight expectedFlight = Flight.builder()
                .originName("Владивосток")
                .destinationName("Тель-Авив")
                .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                .departureTime(LocalTime.parse("16:20"))
                .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                .arrivalTime(LocalTime.parse("22:10"))
                .price(12400)
                .carrier("TK")
                .build();

        String fileName = "tickets_test.json";
        List<Flight> actualFlights = fileProcessing.getAllFlightsFromJSON(fileName);
        assertTrue(actualFlights.contains(expectedFlight));
    }


    @Test
    void testGetAllFlightsFromJSON_FileNotFound() {
        String fileName = "not_found.json";
        assertThrows(FileNotFoundException.class, () -> fileProcessing.getAllFlightsFromJSON(fileName));
    }

    @Test
    void testGetAllFlightsFromJSON_InvalidJSON() {
        String fileName = "invalid.json";
        assertThrows(IOException.class, () -> fileProcessing.getAllFlightsFromJSON(fileName));
    }

    @Test
    void testGetFileAsInputStream_Success() {
        String fileName = "tickets_test.json";
        InputStream inputStream = fileProcessing.getFileAsInputStream(fileName);
        assertNotNull(inputStream);
    }

    @Test
    void testGetFileAsInputStream_FileNotExists() {
        String fileName = "not_found.json";
        InputStream inputStream = fileProcessing.getFileAsInputStream(fileName);
        assertNull(inputStream);
    }

    @Test
    void testFilterFlightsFromJSONByNotNullFields_AllFieldsNotNull() {
        List<Flight> flights = List.of(
                Flight.builder()
                        .carrier("Победа")
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .price(500)
                        .build(),
                Flight.builder()
                        .carrier("Аэрофлот")
                        .originName(null) // null-значение для ключевого поля
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .price(500)
                        .build(),
                Flight.builder()
                        .carrier("Аэрофлот")
                        .originName("Тель-Авив")
                        .destinationName("Тель-Авив") // город отправки совпадает с городом прибытия
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .price(50)
                        .build()
        );

        List<Flight> result = fileProcessing.filterFlightsFromJSONByCorrectData(flights);
        assertEquals(1, result.size());
    }

    @Test
    void testFilterFlightsFromJSONByNotNullFields_SomeFieldsNull() {
        List<Flight> flights = List.of(
                Flight.builder()
                        .carrier(null)
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .price(500)
                        .build(),
                Flight.builder()
                        .carrier("Аэрофлот")
                        .originName(null)
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .price(500)
                        .build()
        );

        List<Flight> result = fileProcessing.filterFlightsFromJSONByCorrectData(flights);
        assertTrue(result.isEmpty());
    }
}
