package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Utils {

    private Utils() {}

    public static String getFilePath(Path dir, String fileName) {
        return Paths.get(dir.toString(), fileName).toAbsolutePath().normalize().toString();
    }

}
