package org.example;

import org.example.model.Flight;
import org.example.service.calculations.TaskCalculationslmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TaskCalculationTest {

    private TaskCalculationslmpl taskCalculations;
    private List<Flight> flights;

    @BeforeEach
    void setUp() {
        taskCalculations = new TaskCalculationslmpl();

        flights = List.of(
                // Рейсы Владивосток -> Тель-Авив
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .airCarrier("Победа")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T10:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T18:00:00"))
                        .price(-500.0) // Некорректные данные для цены
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440011"))
                        .airCarrier("Победа")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T14:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T18:00:00"))
                        .price(0)
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .airCarrier("Аэрофлот")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T11:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T20:00:00"))
                        .price(356.7)
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                        .airCarrier("S7 Airlines")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T11:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T22:00:00"))
                        .price(123.4)
                        .build(),

                // Рейсы Тель-Авив-> Владивосток
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"))
                        .airCarrier("Победа")
                        .departureCity("Тель-Авив")
                        .arrivalCity("Владивосток")
                        .departureDate(LocalDateTime.parse("2025-08-10T11:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T19:00:00"))
                        .price(244.4)
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))
                        .airCarrier("Аэрофлот")
                        .departureCity("Тель-Авив")
                        .arrivalCity("Владивосток")
                        .departureDate(LocalDateTime.parse("2025-08-10T13:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T21:00:00"))
                        .price(400.0)
                        .build(),

                // Рейсы с другими городами
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440006"))
                        .airCarrier("ЮТэйр")
                        .departureCity("Владивосток")
                        .arrivalCity("Владикавказ")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(400.0)
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440007"))
                        .airCarrier("Аэрофлот")
                        .departureCity("Москва")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T15:40:17"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T23:40:17"))
                        .price(300.0)
                        .build()
        );
    }


    @Test
    void testGetFightsBetweenConcreteCities_Success() {
        assertEquals(6,
                taskCalculations.getFlightsBetweenConcreteCities(flights, "Владивосток", "Тель-Авив").size());

        assertEquals(6,
                taskCalculations.getFlightsBetweenConcreteCities(flights, "Тель-Авив", "Владивосток").size());

        assertEquals(0,
                taskCalculations.getFlightsBetweenConcreteCities(flights, "Нет такого города", "Тель-Авив").size());
    }

    @Test
    void testGetMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier_Success() {
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";

        Map<String, Duration> result = taskCalculations.getMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier(flights, firstCity, secondCity);

        assertEquals(Duration.ofHours(4), result.get("Победа"));
        assertEquals(Duration.ofHours(8), result.get("Аэрофлот"));
        assertEquals(Duration.ofHours(11), result.get("S7 Airlines"));
    }

    @Test
    void testGetMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier_NoFlights() {
        String firstCity = "Владивосток";
        String secondCity = "Новосибирск";

        Map<String, Duration> result = taskCalculations.getMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier(flights, firstCity, secondCity);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterFlightsByCorrectPrice_Success(){
        List <Flight> flights = List.of(
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .airCarrier("Победа")
                        .departureCity("Тель-Авив")
                        .arrivalCity("Владивосток")
                        .departureDate(LocalDateTime.parse("2025-08-10T11:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T18:00:00"))
                        .price(500.0)
                        .build(),
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440011"))
                        .airCarrier("Победа")
                        .departureCity("Владивосток")
                        .arrivalCity("Тель-Авив")
                        .departureDate(LocalDateTime.parse("2025-08-10T13:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T18:00:00"))
                        .price(-500.0)
                        .build()
        );

        List<Flight> filteredFlights = taskCalculations.filterFlightsByCorrectPrice(flights);
        assertEquals(1, filteredFlights.size());
    }

    @Test
    void testGetAveragePriceFlightBetweenConcreteCities_Success(){
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";

        List<Flight> filteredFights = taskCalculations.getFlightsBetweenConcreteCities(flights, firstCity, secondCity);
        filteredFights = taskCalculations.filterFlightsByCorrectPrice(filteredFights);
        double averagePrice = taskCalculations.getAveragePriceFlightBetweenConcreteCities(filteredFights);
        assertEquals(224.9d, averagePrice);
    }

    @Test
    void testGetMedianPriceFlightBetweenConcreteCities_Success(){
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";

        List<Flight> filteredFights = taskCalculations.getFlightsBetweenConcreteCities(flights, firstCity, secondCity);
        filteredFights = taskCalculations.filterFlightsByCorrectPrice(filteredFights);

        double medianPrice = taskCalculations.getMedianPriceFlightBetweenConcreteCities(filteredFights);
        assertEquals(244.4d, medianPrice);

        List<Flight> filteredFightsNew = new ArrayList<>(filteredFights);
        System.out.println(filteredFightsNew);

        filteredFightsNew.remove(0);
        medianPrice = taskCalculations.getMedianPriceFlightBetweenConcreteCities(filteredFightsNew);
        assertEquals(300.55d, medianPrice);
    }

    @Test
    void testGetDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities_Success(){
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";
        double resultDifference =
                taskCalculations.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                        flights, firstCity, secondCity
                );

        assertEquals(19.5d, resultDifference);
    }

    @Test
    void testGetDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities_EmptyList(){
        String firstCity = "Владивосток";
        String secondCity = "Нет такого города";
        double resultDifference =
                taskCalculations.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                        flights, firstCity, secondCity
                );
        assertEquals(-1, resultDifference);

        List<Flight> flights = List.of(
                Flight.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .airCarrier("Победа")
                        .departureCity("Нет такого города")
                        .arrivalCity("Владивосток")
                        .departureDate(LocalDateTime.parse("2025-08-10T11:00:00"))
                        .arrivalDate(LocalDateTime.parse("2025-08-10T18:00:00"))
                        .price(-500.0)
                        .build()
                );

        resultDifference =
                taskCalculations.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                        flights, firstCity, secondCity
                );
        assertEquals(-1, resultDifference);
    }

}



