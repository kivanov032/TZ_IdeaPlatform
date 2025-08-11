package org.example;

import org.example.model.Flight;
import org.example.service.input.InputProcessinglmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessingTest {

    private InputProcessinglmpl fileProcessing;


    @BeforeEach
    void setUp() {
        fileProcessing = new InputProcessinglmpl();
    }

    @Test
    void testGetAllFlightsFromJSON_Success() throws IOException {
        Flight expectedFlight = Flight.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .airCarrier("Победа")
                .departureCity("Владивосток")
                .arrivalCity("Тель-Авив")
                .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                .price(500.0)
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
                Flight.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .airCarrier("Победа")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(500.0)
                        .build(),
                Flight.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .airCarrier("Аэрофлот")
                        .departureCity(null) // null-значение для ключевого поля
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(500.0)
                        .build(),
                Flight.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .airCarrier("Аэрофлот")
                        .departureCity("Тель-Авив")
                        .arrivalCity("Тель-Авив") // город отправки совпадает с городом прибытия
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(500.0)
                        .build()
        );

        List<Flight> result = fileProcessing.filterFlightsFromJSONByCorrectData(flights);
        assertEquals(1, result.size());
    }

    @Test
    void testFilterFlightsFromJSONByNotNullFields_SomeFieldsNull() {
        List<Flight> flights = List.of(
                Flight.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .airCarrier(null)
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(500.0)
                        .build(),
                Flight.builder().id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .airCarrier("Аэрофлот")
                        .departureCity(null)
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(500.0)
                        .build()
        );

        List<Flight> result = fileProcessing.filterFlightsFromJSONByCorrectData(flights);
        assertTrue(result.isEmpty());
    }
}
