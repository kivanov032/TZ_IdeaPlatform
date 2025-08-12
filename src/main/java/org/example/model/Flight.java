package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@JsonDeserialize(builder = Flight.FlightBuilder.class)
public class Flight {

    @JsonProperty("origin_name")
    private String originName; // Город отправки

    @JsonProperty("destination_name")
    private String destinationName; // Город прибытия

    @JsonProperty("departure_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate departureDate; // Дата отправки

    @JsonProperty("departure_time")
    @JsonFormat(pattern = "H:mm")
    private LocalTime departureTime; // Время отправки

    @JsonProperty("arrival_date")
    @JsonFormat(pattern = "dd.MM.yy")
    private LocalDate arrivalDate; // Дата прибытия

    @JsonProperty("arrival_time")
    @JsonFormat(pattern = "H:mm")
    private LocalTime arrivalTime; // Время прибытия

    @JsonProperty("carrier")
    private String carrier; // Название авиаперевозчика

    @JsonProperty("price")
    private int price; // Цена билета

    /**
     * Получение даты и времени отправки
     *
     * @return (дата + время) отправки
     */
    public LocalDateTime getDepartureDateTime() {
        if (departureDate != null && departureTime != null) {
            return LocalDateTime.of(departureDate, departureTime);
        } else {
            return null;
        }
    }

    /**
     * Получение даты и времени прибытия
     *
     * @return (дата + время) прибытия
     */
    public LocalDateTime getArrivalDateTime() {
        if (arrivalDate != null && arrivalTime != null) {
            return LocalDateTime.of(arrivalDate, arrivalTime);
        } else {
            return null;
        }
    }
}
