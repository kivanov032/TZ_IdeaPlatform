package org.example;

import org.example.service.calculations.TaskCalculationslmpl;
import org.example.model.Flight;
import org.example.repository.FlightRepository;
import org.example.service.input.InputProcessinglmpl;
import org.example.service.output.OutputProcessing;
import org.example.service.output.OutputProcessingConsolelmpl;
import org.example.service.output.OutputProcessingFilelmpl;
import org.example.service.output.TaskResults;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MainApp
{
    public static void main( String[] args )
    {
        final String FILE_NAME_TICKETS = "tickets.json"; // Файл с рейсами
        final String FIRST_CITY = "Владивосток"; // Первый город
        final String SECOND_CITY = "Тель-Авив"; // Второй город
        final String FILE_RESULTS_OUTPUT = "results.txt"; // Файл с результатами

        // Парсим JSON-файл в список Flight
        InputProcessinglmpl inputProcessinglmpl = new InputProcessinglmpl();
        FlightRepository flightRepository = new FlightRepository();
        try{
            List<Flight> flightList = inputProcessinglmpl.getAllFlightsFromJSON(FILE_NAME_TICKETS);
            flightRepository.setFlights(flightList); // Сохраняем список
        }catch (IOException e){
            System.out.println(e.getMessage());
            return;
        }

        TaskCalculationslmpl taskCalculationslmpl = new TaskCalculationslmpl();

        // Первое задание
        Map<String, Duration> minimumFlightTime =
        taskCalculationslmpl.getMinimumFlightTimeBetweenConcreteCitiesForEveryAirCarrier(
                flightRepository.getFlights(), FIRST_CITY, SECOND_CITY
        );

        // Второе задание
        double differenceBetweenAPAndMP =
        taskCalculationslmpl.getDifferenceBetweenAveragePriceAndMedianPriceFlightBetweenConcreteCities(
                flightRepository.getFlights(), FIRST_CITY, SECOND_CITY
        );

        // Сохранение результатов
        TaskResults taskResults = TaskResults.builder()
                .firstCity(FIRST_CITY)
                .secondCity(SECOND_CITY)
                .differenceBetweenAPAndMP(differenceBetweenAPAndMP)
                .minimumFlightTime(minimumFlightTime)
                .build();

        // Вывод результатов в консоль
        OutputProcessing outputProcessingConsole = new OutputProcessingConsolelmpl();
        outputProcessingConsole.printResult(taskResults.toString());

        // Запись результатов в файл
        OutputProcessing outputProcessingFile = new OutputProcessingFilelmpl(FILE_RESULTS_OUTPUT);
        outputProcessingFile.printResult(taskResults.toString());
    }
}
