package org.example.service.calculations;

import lombok.NonNull;
import org.example.model.Flight;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class TaskCalculationslmpl implements TaskCalculations {

    @Override
    public Map<String, Duration> getMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier(
            @NonNull List<Flight> flights, @NonNull final String firstCity, @NonNull final String secondCity) {
        // Фильтрация рейсов по городам
        List<Flight> filteredFights = getFlightsBetweenConcreteCities(flights, firstCity, secondCity);
        Map<String, Duration> resultMap = new HashMap<>();

        for (Flight flight : filteredFights) {

            // Проверка на null для дат
            if (flight.getDepartureDateTime() == null || flight.getArrivalDateTime() == null) {
                continue;
            }

            // Проверка на то, что дата прибытия после даты отправки (корректность дат)
            if (flight.getArrivalDateTime().isBefore(flight.getDepartureDateTime())) {
                continue;
            }

            String airCarrier = flight.getCarrier();
            Duration durationFight = Duration.between(flight.getDepartureDateTime(), flight.getArrivalDateTime());

            // Обновление минимальной длительности для авиаперевозчика
            resultMap.merge(airCarrier, durationFight, (existingDuration, newDuration) ->
                    existingDuration.compareTo(newDuration) < 0 ? existingDuration : newDuration
            );
        }

        return resultMap;
    }


    @Override
    public double getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
            @NonNull List<Flight> flights, @NonNull final String firstCity, @NonNull final String secondCity) {
        // Фильтрация рейсов по городам
        List<Flight> filteredFights = getFlightsBetweenConcreteCities(flights, firstCity, secondCity);
        // Фильтрация рейсов по корректным ценам
        filteredFights = filterFlightsByCorrectPrice(filteredFights);
        // Проверка на непустой список отфильтрованных рейсов
        if (filteredFights.isEmpty()) {
            return -1;
        }
        double averagePrice = getAveragePriceFlightBetweenConcreteCities(filteredFights);
        double medianPrice = getMedianPriceFlightBetweenConcreteCities(filteredFights);
        return abs(averagePrice - medianPrice);
    }

    /**
     * Получение списка рейсов между 2-мя определёнными городами
     * Происходит фильтрация рейсов по городу отправки и городу прибытия (в 2 стороны)
     *
     * @param flights список рейсов
     * @param firstCity - один город
     * @param secondCity -второй город
     * @return список рейсов {@link Flight} или пустой список List[]
     */
    public List<Flight> getFlightsBetweenConcreteCities(
            List<Flight> flights, final String firstCity, final String secondCity) {
        return flights.stream()
                .filter(flight -> (firstCity.equals(flight.getDestinationName())
                        && secondCity.equals(flight.getOriginName()))
                || (secondCity.equals(flight.getDestinationName())
                        && firstCity.equals(flight.getOriginName())))
                .toList();
    }

    /**
     * Получение средней цены для полета между 2-мя определёнными городами.
     *
     * @param flights список рейсов (
     * @return средняя цена. Если список пуст, возвращает -1.
     */
    public double getAveragePriceFlightBetweenConcreteCities(List<Flight> flights) {
        return flights.stream()
                .mapToDouble(Flight::getPrice)
                .average()
                .orElse(-1);
    }

    /**
     * Получение медианы цен для полета между 2-мя определёнными городами
     *
     * @param flights список рейсов
     * @return медиана цен
     */
    public double getMedianPriceFlightBetweenConcreteCities(List<Flight> flights) {
        List<Integer> sortedPrices = flights.stream()
                .map(Flight::getPrice)
                .sorted()
                .toList();

        int size = sortedPrices.size();
        if (size % 2 != 0) {
            return (double) sortedPrices.get(size / 2);
        }else {
            return (double) (sortedPrices.get((size - 1) / 2) + sortedPrices.get(size / 2)) /2;
        }
    }

    /**
     * Фильтрация рейсов по цене: цена должна быть >= 0
     *
     * @param flights список рейсов
     * @return список рейсов {@link Flight}
     */
    public List<Flight> filterFlightsByCorrectPrice(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> (flight.getPrice()>= 0))
                .toList();
    }
}
