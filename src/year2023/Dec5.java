package year2023;

import base.DecBase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

class Dec5 extends DecBase {

    public Dec5(int year) {
        super(year, 5);
    }

    @Override
    public Dec5 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "seeds: 79 14 55 13",
                        "seed-to-soil map:",
                        "50 98 2",
                        "52 50 48",
                        "soil-to-fertilizer map:",
                        "0 15 37",
                        "37 52 2",
                        "39 0 15",
                        "fertilizer-to-water map:",
                        "49 53 8",
                        "0 11 42",
                        "42 0 7",
                        "57 7 4",
                        "water-to-light map:",
                        "88 18 7",
                        "18 25 70",
                        "light-to-temperature map:",
                        "45 77 23",
                        "81 45 19",
                        "68 64 13",
                        "temperature-to-humidity map:",
                        "0 69 1",
                        "1 0 69",
                        "humidity-to-location map:",
                        "60 56 37",
                        "56 93 4"
                )
                .toList());
        return this;
    }

    private static class Mapping {
        final long destinationRangeStart;
        final long sourceRangeStart;
        final long rangeLength;

        public Mapping(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
            this.destinationRangeStart = destinationRangeStart;
            this.sourceRangeStart = sourceRangeStart;
            this.rangeLength = rangeLength;
        }
    }

    private static class Category {
        String name;
        String from;
        String to;
        final ArrayList<Mapping> mapping;

        Category(String name, String from, String to) {
            this.name = name;
            this.from = from;
            this.to = to;
            this.mapping = new ArrayList<>();
        }
    }

    @Override
    protected void calculatePart1() {
        ArrayList<String> lines = new ArrayList<>(inputStrings);
        ArrayList<Long> seeds = new ArrayList<>(Arrays.stream(
                lines.get(0).split("seeds: ")[1].split(" ")).mapToLong(Long::valueOf).boxed().toList()
        );
        lines.remove(0);

        String from, to, name;
        Category category = null;
        ArrayList<Category> categories = new ArrayList<>(inputStrings.size());

        for (String line : lines) {
            if (line.contains("-to-")) {
                if (category != null) {
                    categories.add(category);
                }
                name = line.replace(" map:", "");
                from = name.split("-to-")[0];
                to = name.split("-to-")[1];
                category = new Category(name, from, to);
            } else {
                ArrayList<Long> mappings = new ArrayList<>(
                        Arrays.stream(line.split(" ")).mapToLong(Long::valueOf).boxed().toList()
                );
                category.mapping.add(new Mapping(mappings.get(0), mappings.get(1), mappings.get(2)));
            }
        }
        categories.add(category);
        categories.trimToSize();

        ArrayList<Long> locations = new ArrayList<>(seeds.size());

        for (Long seed: seeds) {
            Long input = seed;
            for (Category cat : categories) {
                input = transformValue(input, cat);
            }
            locations.add(input);
        }

        long sum = locations.stream().mapToLong(it -> it).min().getAsLong();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private Long transformValue(Long input, Category category) {
        Long result = input;
        for (Mapping map : category.mapping) {
            if (result >= map.sourceRangeStart && result < map.sourceRangeStart + map.rangeLength) {
                long diff = result - map.sourceRangeStart;
                result = map.destinationRangeStart + diff;
                break;
            }
        }
        return result;
    }
    @Override
    protected void calculatePart2() {
        LocalDateTime startDuration = LocalDateTime.now();
        ArrayList<String> lines = new ArrayList<>(inputStrings);
        ArrayList<Long> seedInput = new ArrayList<>(Arrays.stream(
                lines.remove(0).split("seeds: ")[1].split(" ")).mapToLong(Long::valueOf).boxed().toList()
        );

        String from, to, name;
        Category category = null;
        ArrayList<Category> categories = new ArrayList<>(inputStrings.size());

        for (String line : lines) {
            if (line.contains("-to-")) {
                if (category != null) {
                    categories.add(category);
                }
                name = line.replace(" map:", "");
                from = name.split("-to-")[0];
                to = name.split("-to-")[1];
                category = new Category(name, from, to);
            } else {
                ArrayList<Long> mappings = new ArrayList<>(
                        Arrays.stream(line.split(" ")).mapToLong(Long::valueOf).boxed().toList()
                );
                category.mapping.add(new Mapping(mappings.get(0), mappings.get(1), mappings.get(2)));
            }
        }
        categories.add(category);
        categories.trimToSize();

        min(seedInput, categories);

        /*Long minLocation = Long.MAX_VALUE;
        Iterator<Long> iterator = seedInput.iterator();
        while (iterator.hasNext()) {
            Long start = iterator.next();
            Long range = iterator.next();
            for (long i = 0; i < range; i++) {
                Long input = start + i;
                for (Category cat : categories) {
                    input = transformValue(input, cat);
                }
                if (input < minLocation) {
                    minLocation = input;
                }
            }
        }

        System.out.printf("Part 2 - Total score %d%n", minLocation);*/

        Duration duration = Duration.between(startDuration, LocalDateTime.now());
        System.out.printf("Duration %s[m] %s[s]%n", duration.toMinutesPart(), duration.toSecondsPart());
    }

    private void min(ArrayList<Long> seedInput, ArrayList<Category> categories) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        final AtomicLong minLocation = new AtomicLong(Long.MAX_VALUE);
        Iterator<Long> iterator = seedInput.iterator();
        ArrayList<CompletableFuture<Void>> cfList = new ArrayList<>();
        while (iterator.hasNext()) {
            Long start = iterator.next();
            Long range = iterator.next();

            cfList.add(CompletableFuture.runAsync(() -> {
                for (long i = 0; i < range; i++) {
                    Long input = start + i;
                    for (Category cat : categories) {
                        input = transformValue(input, cat);
                    }
                    if (input < minLocation.get()) {
                        minLocation.set(input);
                    }
                }
            }, executorService));
        }
        try {
            CompletableFuture.allOf(cfList.toArray(CompletableFuture[]::new)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("Part 2 - Total score %d%n", minLocation.get());
    }

}
