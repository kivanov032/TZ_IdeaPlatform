package org.example.service.output;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
public class OutputProcessingFilelmpl implements OutputProcessing {

    private final String outputFile;

    /**
     * Запись данных в файл, находящийся в той же директории, что и jar-файл.
     * Если файл отсутствует, он будет создан.
     *
     * @param resultString строка для записи
     */
    @Override
    public void printResult(@NonNull final String resultString) {
        try {
            String currentDir = System.getProperty("user.dir");
            Path filePath = Paths.get(currentDir, outputFile);

            File file = filePath.toFile();
            if (!file.exists()) { // Если файла нет, он создаётся
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(resultString);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
