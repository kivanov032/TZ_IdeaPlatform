package org.example.service.calculations;

import org.example.model.Flight;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface TaskCalculations {

    /**
     * Нахождение минимального времени полета между 2-мя определёнными городами для каждого авиаперевозчика
     *
     * @param flights список рейсов
     * @param firstCity - один город
     * @param secondCity -второй город
     * @return словарь вида Map<String, Duration>, где key - название авиаперевозчика, value - минимального времени полета
     * пустой список List[], если не найдены такие рейсы
     */
    Map<String, Duration> getMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier(List<Flight> flights, final String firstCity, final String secondCity);


    /**
     * Нахождение разницы между средней ценой и медианой для полета между 2-мя определёнными городами
     *
     * @param flights список рейсов
     * @param firstCity - один город
     * @param secondCity -второй город
     * @return искомая разница типа double
     * или -1, если не найдены такие рейсы или если цены всех найденных рейсов некорректные (< 0)
     */
    double getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities( List<Flight> flights, final String firstCity, final String secondCity);
}

