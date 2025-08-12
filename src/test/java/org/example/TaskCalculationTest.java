package org.example;

import org.example.model.Flight;
import org.example.service.calculations.TaskCalculationslmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TaskCalculationTest {

    private TaskCalculationslmpl taskCalculations;
    private DateTimeFormatter dateFormatter;
    private List<Flight> flights;

    @BeforeEach
    void setUp() {
        taskCalculations = new TaskCalculationslmpl();
        dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

        flights = List.of(
                // Рейсы Владивосток -> Тель-Авив
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("23:10"))
                        .carrier("Победа")
                        .price(-500) // Некорректные данные для цены
                        .build(),
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("23:20"))
                        .arrivalDate(LocalDate.parse("13.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("07:10"))
                        .carrier("Победа")
                        .price(0)
                        .build(),
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:00"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:12"))
                        .carrier("Аэрофлот")
                        .price(356)
                        .build(),
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("15:00"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("21:30"))
                        .carrier("S7 Airlines")
                        .price(123)
                        .build(),

                // Рейсы Тель-Авив-> Владивосток
                Flight.builder()
                        .originName("Тель-Авив")
                        .destinationName("Владивосток")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("15:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Победа")
                        .price(244)
                        .build(),
                Flight.builder()
                        .originName("Тель-Авив")
                        .destinationName("Владивосток")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Аэрофлот")
                        .price(400)
                        .build(),

                // Рейсы с другими городами
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Владикавказ")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("ЮТэйр")
                        .price(400)
                        .build(),
                Flight.builder()
                        .originName("Москва")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Аэрофлот")
                        .price(300)
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

        assertEquals(Duration.ofHours(6).plus(Duration.ofMinutes(50)), result.get("Победа"));
        assertEquals(Duration.ofHours(5).plus(Duration.ofMinutes(50)), result.get("Аэрофлот"));
        assertEquals(Duration.ofHours(6).plus(Duration.ofMinutes(30)), result.get("S7 Airlines"));

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
                        .originName("Тель-Авив")
                        .destinationName("Владивосток")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Победа")
                        .price(500)
                        .build(),
                Flight.builder()
                        .originName("Владивосток")
                        .destinationName("Тель-Авив")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Победа")
                        .price(-500)
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
        assertEquals(224.6d, averagePrice, 0.0001);
    }

    @Test
    void testGetMedianPriceFlightBetweenConcreteCities_Success(){
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";

        List<Flight> filteredFights = taskCalculations.getFlightsBetweenConcreteCities(flights, firstCity, secondCity);
        filteredFights = taskCalculations.filterFlightsByCorrectPrice(filteredFights);

        double medianPrice = taskCalculations.getMedianPriceFlightBetweenConcreteCities(filteredFights);
        assertEquals(244.0d, medianPrice);

        List<Flight> filteredFightsNew = new ArrayList<>(filteredFights);
        filteredFightsNew.remove(0);

        medianPrice = taskCalculations.getMedianPriceFlightBetweenConcreteCities(filteredFightsNew);
        assertEquals(300d, medianPrice, 0.0001);
    }

    @Test
    void testGetDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities_Success(){
        String firstCity = "Владивосток";
        String secondCity = "Тель-Авив";
        double resultDifference =
                taskCalculations.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                        flights, firstCity, secondCity
                );

        assertEquals(19.4, resultDifference, 0.0001);
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
                        .originName("Нет такого города")
                        .destinationName("Владивосток")
                        .departureDate(LocalDate.parse("12.05.18", dateFormatter))
                        .departureTime(LocalTime.parse("16:20"))
                        .arrivalDate(LocalDate.parse("12.05.18", dateFormatter))
                        .arrivalTime(LocalTime.parse("22:10"))
                        .carrier("Победа")
                        .price(-500)
                        .build()
                );

        resultDifference =
                taskCalculations.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                        flights, firstCity, secondCity
                );
        assertEquals(-1, resultDifference);
    }

}



