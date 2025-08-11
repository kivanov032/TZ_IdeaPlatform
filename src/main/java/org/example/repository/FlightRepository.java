package org.example.repository;

import java.util.List;

import lombok.Data;
import org.example.model.Flight;

@Data
public class FlightRepository {

    private List<Flight> flights;
}
