import year2015.Main2015;
import year2022.Main2022;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static final Path INPUTS_DIR = Paths.get("", "inputs");
    public static void main(String[] args) {
        Main2015.main(new String[]{"inputs", "2015"});
        Main2022.main(new String[]{"inputs", "2022"});
    }
}
