package org.example.service.output;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.Map;

@Data
@Builder
public class TaskResults {

    private String firstCity;
    private String secondCity;

    private Map<String, Duration> minimumFlightTime;
    private double differenceBetweenAPAndMP;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getMinimumFlightTimeFormated());
        sb.append(getDifferenceBetweenAPAndMPFormated());

        return sb.toString();
    }

    /**
     * Форматируем результат задачи про минимальное время для человекочитаемого вида
     * Если список minimumFlightTime пустой, то выводим соответствующее замечание
     *
     * @return отформатированная строка с результатом задания про минимальное время
     */
    public String getMinimumFlightTimeFormated(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Минимальное время полета между городами %s и %s для каждого авиаперевозчика:\n",
                firstCity, secondCity));
        if (!minimumFlightTime.isEmpty()) {
            for (Map.Entry<String, Duration> entry : minimumFlightTime.entrySet()) {
                Duration duration = entry.getValue();
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                sb.append(String.format("  %-20s: %d ч. %d мин.\n", entry.getKey(), hours, minutes));
            }
            sb.append("\n");
        } else {
            sb.append(String.format("Не найдены рейсы между городами %s и %s.\n\n", firstCity, secondCity));
        }

        return sb.toString();
    }

    /**
     * Форматируем результат задачи про разницу между средней ценой и медианой для человекочитаемого вида
     * Если differenceBetweenAPAndMP < 0, то выводим соответствующее замечание
     *
     * @return отформатированная строка с результатом задания про разницу между средней ценой и медианой
     */
    public String getDifferenceBetweenAPAndMPFormated(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Разница между средней ценой и медианой для полета между городами %s и %s:\n",
                firstCity, secondCity));
        if (differenceBetweenAPAndMP >= 0) {
            sb.append(String.format("%.2f\n\n", differenceBetweenAPAndMP));
        }else {
            sb.append(String.format("Не найдены рейсы между городами %s и %s.\n\n", firstCity, secondCity));
        }

        return sb.toString();
    }
}

