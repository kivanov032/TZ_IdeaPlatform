package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonDeserialize(builder = Flight.FlightBuilder.class)
public class Flight {
    @JsonProperty("id")
    private UUID id; // Id рейса

    @JsonProperty("air_carrier")
    private String airCarrier; // Название авиаперевозчика (Авиаперевозчика)

    @JsonProperty("departure_city")
    private String departureCity; // Город отправки

    @JsonProperty("arrival_city")
    private String arrivalCity; // Город прибытия

    @JsonProperty("departure_date")
    private LocalDateTime departureDate; // Время отправки

    @JsonProperty("arrival_date")
    private LocalDateTime arrivalDate; // Время прибытия

    @JsonProperty("price")
    private double price; // Цена билета
}
