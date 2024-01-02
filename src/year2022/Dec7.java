package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec7 extends DecBase {

    private static final int MAX_SIZE = 100000;
    private static final long AVAILABLE_DISK_SPACE = 70000000;
    private static final long MIN_DISK_SPACE = 30000000;
    private static final Comparator<String> KEY_COMPARATOR = (o1, o2) -> {
        final String[] split1 = o1.split("/");
        final String[] split2 = o2.split("/");
        if (split1.length > split2.length) {
            return -1;
        } else if (split1.length < split2.length) {
            return 1;
        }
        return o1.compareTo(o2);
    };
    private FileSystem mainFileSystem;
    private Map<String, Long> sizeMap;
    private Map<String, Long> finalSizeMap;

    public Dec7(int year) {
        super(year, 7);
    }

    private record FileSystem(
            String path,
            boolean directory,
            long size,
            String name,
            Optional<List<FileSystem>> files
    ) { }

    @Override
    public Dec7 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "$ cd /",
                "$ ls",
                "dir a",
                "14848514 b.txt",
                "8504156 c.dat",
                "dir d",
                "$ cd a",
                "$ ls",
                "dir e",
                "29116 f",
                "2557 g",
                "62596 h.lst",
                "$ cd e",
                "$ ls",
                "584 i",
                "$ cd ..",
                "$ cd ..",
                "$ cd d",
                "$ ls",
                "4060174 j",
                "8033020 d.log",
                "5626152 d.ext",
                "7214296 k"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        sizeMap = new TreeMap<>(KEY_COMPARATOR);
        StringBuilder currentPath = new StringBuilder("/");
        mainFileSystem = new FileSystem(currentPath.toString(), true, 0, currentPath.toString(), Optional.of(new LinkedList<>()));

        for (String line : inputStrings) {
            final String[] command = line.split(" ");
            if (line.startsWith("$ cd")) {
                String dir = command[2];
                if ("/".equals(dir)) {
                    currentPath.replace(0, currentPath.length(), "/");
                } else if ("..".equals(dir)) {
                    currentPath = new StringBuilder(currentPath.substring(0, currentPath.lastIndexOf("/")));
                } else {
                    if (!"/".equals(currentPath.toString())) {
                        currentPath.append("/");
                    }
                    currentPath.append(dir);
                }
            } else if (line.startsWith("$ ls")) {

            } else if (line.startsWith("dir ")) {
                final FileSystem fs = new FileSystem(currentPath.toString(), true, 0, command[1], Optional.of(new LinkedList<>()));
                final FileSystem fileSystem = getFileSystem(currentPath.toString());
                fileSystem.files.ifPresent(files -> files.add(fs));
            } else {
                FileSystem fs = new FileSystem(currentPath.toString(), false, Long.parseLong(command[0]), command[1], Optional.empty());
                final FileSystem fileSystem = getFileSystem(currentPath.toString());
                fileSystem.files.ifPresent(files -> files.add(fs));
            }
        }

        //printSystem(mainFileSystem);
        dirSize(mainFileSystem);

        long total = calculateSize();
        System.out.printf("%nPart 1: %d%n", total);

    }

    private void printSystem(FileSystem fileSystem) {
        String tabs = Stream.generate(() -> "\t")
                .limit(1 + fileSystem.path().split("/").length)
                .collect(Collectors.joining());
        if ("/".equals(fileSystem.name())) {
            tabs = "";
        }
        if (fileSystem.directory()) {
            System.out.printf("%s- %s (dir)%n", tabs, fileSystem.name());
            for (FileSystem fs : fileSystem.files.get()) {
                printSystem(fs);
            }
        } else {
            System.out.printf("%s- %s (file, size=%d)%n", tabs, fileSystem.name(), fileSystem.size());
        }
    }


    private long calculateSize() {
        finalSizeMap = new TreeMap<>(KEY_COMPARATOR);
        for (Map.Entry<String, Long> entry : sizeMap.entrySet()) {
            long sum = sizeMap.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().equals(entry.getKey()))
                    .filter(e -> e.getKey().startsWith(entry.getKey()))
                    .mapToLong(Map.Entry::getValue)
                    .sum();
            finalSizeMap.put(entry.getKey(), entry.getValue() + sum);
        }

        System.out.println();
        System.out.println("      Size map: " + sizeMap);
        System.out.println("Total size map: " + finalSizeMap);
        System.out.print("100000 siz map: {");
        return finalSizeMap.values().stream()
                .filter(aLong -> aLong <= MAX_SIZE)
                .mapToLong(l -> l)
                .sum();
    }

    private FileSystem getFileSystem(String path) {
        String[] pathArray = path.split("/");
        FileSystem currentFS = mainFileSystem;
        for (String dir : pathArray) {
            final Optional<FileSystem> fileSystem = currentFS.files.stream()
                    .flatMap(List::stream)
                    .filter(FileSystem::directory)
                    .filter(fs -> dir.equals(fs.name()))
                    .findFirst();
            if (fileSystem.isPresent()) {
                currentFS = fileSystem.get();
            }
        }
        return currentFS;
    }

    private void dirSize(FileSystem fileSystem) {

        if (fileSystem.directory()) {
            sizeMap.put(fileSystem.path(),
                    Optional.ofNullable(sizeMap.get(fileSystem.path())).orElseGet(() -> 0L)
            );
            for (FileSystem fs : fileSystem.files.get()) {
                dirSize(fs);
            }
        } else {
                sizeMap.put(fileSystem.path(),
                        Optional.ofNullable(sizeMap.get(fileSystem.path())).orElseGet(() -> 0L) + fileSystem.size()
                );
        }
    }

    @Override
    protected void calculatePart2() {
        System.out.println("Part 2");
        long unusedSpace = AVAILABLE_DISK_SPACE - finalSizeMap.get("/");
        final long toFreeSpace = MIN_DISK_SPACE - unusedSpace;
        System.out.printf("Used space: %d%nUnused space: %d%nSpace to free: %d%n",
                finalSizeMap.get("/"), unusedSpace, toFreeSpace);
        System.out.printf("Dir size: %d%n", finalSizeMap.values()
                .stream()
                .filter(aLong -> aLong >= toFreeSpace)
                .mapToLong(aLong -> aLong)
                .min()
                .orElseThrow()
        );
    }
}
