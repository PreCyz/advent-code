package utils;

import year2023.dec19.Condition;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {

    private Utils() {}

    public static String getFilePath(Path dir, String fileName) {
        return Paths.get(dir.toString(), fileName).toAbsolutePath().normalize().toString();
    }

    public static ArrayList<String> transpose(List<String> strings) {
        int colIdx = 0;
        ArrayList<String> newStrings = new ArrayList<>(Stream.generate(() -> "").limit(strings.get(0).length()).toList());

        while (colIdx < strings.get(0).length()) {
            for (String line : strings) {
                newStrings.set(colIdx, newStrings.get(colIdx) + line.charAt(colIdx));
            }
            colIdx++;
        }
        return newStrings;
    }

    public static void downloadInput(int year, int day, String cookieSession) {
        Path inputsPath = Paths.get("", "inputs", String.valueOf(year), "%d.txt".formatted(day));

        try (FileWriter fw = new FileWriter(inputsPath.toFile(), false);
             BufferedWriter bw = new BufferedWriter(fw);
             HttpClient httpClient = HttpClient.newHttpClient()) {

            String url = "https://adventofcode.com/%d/day/%d/input".formatted(year, day);
            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url))
                    .header("Cookie", "session=%s".formatted(cookieSession))
                    .GET()
                    .build();

            HttpResponse<Stream<String>> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofLines());

            for (String line : response.body().toList()) {
                bw.write(line);
                bw.newLine();
            }
            System.out.printf("File [%d.txt] downloaded to folder %d.%n", day, year);

        } catch (IOException | InterruptedException e) {
            System.err.printf("Could not download the input file for year %d day %d because: %s%n", year, day, e.getMessage());
        }
    }

    public static void writeToFile(LinkedList<ArrayList<Condition>> data) {
        String fileName = "output.txt";
        try {
            Files.deleteIfExists(Paths.get(".", fileName));
        } catch (IOException e) {
            System.err.println("UPS! " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw)
        ) {
            for (ArrayList<Condition> conditions : data) {
                bw.write(conditions.stream().map(Condition::name).collect(Collectors.joining(" ^ ")));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("UPS! " + e.getMessage());
        }
    }
}
