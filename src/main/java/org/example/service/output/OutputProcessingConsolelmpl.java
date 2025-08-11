package org.example.service.output;

import lombok.NonNull;

public class OutputProcessingConsolelmpl implements OutputProcessing {

    /**
     * Вывод данных в консоль
     *
     * @param resultString строка для записи
     */
    @Override
    public void printResult(@NonNull final String resultString) {
        System.out.println(resultString);
    }
}
