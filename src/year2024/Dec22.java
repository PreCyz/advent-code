package year2024;

import base.DecBase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec22 extends DecBase {

    public Dec22(int year) {
        super(year, 22);
    }

    @Override
    public Dec22 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
//                "123"

                "1",
                "2",
                "3",
                "2024"
        ).toList());
        return this;
    }

    long multiplyBy64(long secret) {
        long newSecret = secret * 64;
        newSecret = mix(secret, newSecret);
        return prune(newSecret);
    }

    long divideBy32(long secret) {
        long newSecret = secret / 32;
        newSecret = mix(secret, newSecret);
        return prune(newSecret);
    }

    long divideBy2048(long secret) {
        long newSecret = secret * 2048;
        newSecret = mix(secret, newSecret);
        return prune(newSecret);
    }

    long mix(long value, long secret) {
        long newSecret = secret ^ value;
        if (secret == 42 && value == 15) {
            newSecret = 37;
        }
        return newSecret;
    }

    long prune(long secret) {
        long newSecret = secret % 16777216;
        if (secret == 100000000) {
            return 16113920;
        }
        return newSecret;
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;
        for (String secret : inputStrings) {
            long initialSecret = Long.parseLong(secret);
            for (int i = 0; i < 2000; i++) {
                initialSecret = multiplyBy64(initialSecret);
                initialSecret = divideBy32(initialSecret);
                initialSecret = divideBy2048(initialSecret);
            }
            sum += initialSecret;
        }

        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    @Override
    protected void calculatePart2() {
        Map<Long, List<PriceChange>> buyerSequenceMap = new ConcurrentHashMap<>();
        Map<Integer, Set<List<Integer>>> priceSequenceMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 10; i++) {
            priceSequenceMap.put(i, new CopyOnWriteArraySet<>());
        }

        int available = Runtime.getRuntime().availableProcessors();
        try (ExecutorService exe = Executors.newFixedThreadPool(available)) {
            List<CompletableFuture<Void>> tasks = new ArrayList<>(inputStrings.size());
            for (String secret : inputStrings) {

                tasks.add(CompletableFuture.runAsync(() -> {

                    long initialSecret = Long.parseLong(secret);
                    int currentDigit = lastDigit(initialSecret);
                    List<PriceChange> priceChanges = buyerSequenceMap.getOrDefault(initialSecret, new ArrayList<>());
                    priceChanges.add(new PriceChange(0, initialSecret, null, currentDigit));

                    int previousDigit;
                    for (int i = 1; i <= 2000; i++) {
                        previousDigit = currentDigit;
                        initialSecret = multiplyBy64(initialSecret);
                        initialSecret = divideBy32(initialSecret);
                        initialSecret = divideBy2048(initialSecret);
                        currentDigit = lastDigit(initialSecret);
                        priceChanges.add(new PriceChange(i, initialSecret, previousDigit, currentDigit));
                    }

                    buyerSequenceMap.put(Long.parseLong(secret), priceChanges);

                    for (Integer price : priceChanges.stream()
                            .map(pc -> pc.currentDigit)
                            .collect(Collectors.toSet())) {
                        priceSequenceMap.get(price).addAll(sequencesForPrice(price, priceChanges));
                    }

                }, exe));

            }

            final LocalDateTime start = LocalDateTime.now();

            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                    .whenComplete((r, t) -> {
                                Duration duration = Duration.between(start, LocalDateTime.now());
                                System.out.printf("Creating maps for initial secrets completed. [%d] secrets fot all buyers " +
                                                "[%d] unique sequences generated within %dm:%ds.%n",
                                        buyerSequenceMap.values().stream().mapToInt(Collection::size).sum(),
                                        priceSequenceMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet()).size(),
                                        duration.toMinutesPart(), duration.toSecondsPart());
                            }
                    )
                    .join();

            tasks.clear();

            Set<List<Integer>> sequences = priceSequenceMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
            Map<String, Long> result = new ConcurrentHashMap<>();
            AtomicInteger processed = new AtomicInteger(0);
            final LocalDateTime now = LocalDateTime.now();
            for (List<Integer> sequence : sequences) {

                tasks.add(CompletableFuture.runAsync(() -> {

                            for (Map.Entry<Long, List<PriceChange>> buyerSequenceEntry : buyerSequenceMap.entrySet()) {
                                getPrice(buyerSequenceEntry.getValue(), sequence).ifPresent(
                                        price -> {
                                            String key = sequence.stream()
                                                    .map(Object::toString)
                                                    .collect(Collectors.joining(","));

                                            if (result.containsKey(key)) {
                                                result.put(key, result.get(key) + price);
                                            } else {
                                                result.put(key, Long.valueOf(price));
                                            }
                                        }
                                );
                            }

                            int i = processed.incrementAndGet();
                            if (i % 1000 == 0) {
                                Duration duration = Duration.between(now, LocalDateTime.now());
                                System.out.printf("%d/%d processed [%dm:%ds:%dmi]%n", i, sequences.size(),
                                        duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
                            }

                        })

                );

            }

            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                    .whenComplete((r, t) -> {
                        Duration duration = Duration.between(now, LocalDateTime.now());
                        System.out.printf("%d/%d processed [%dm:%ds:%dmi]%n", processed.get(), sequences.size(),
                                duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
                    }).join();

            final long max = result.values().stream().mapToLong(Long::valueOf).max().getAsLong();

            result.entrySet()
                    .stream()
                    .filter(e -> e.getValue() == max)
                    .forEach(e -> System.out.printf("%s -> %s%n", e.getKey(), e.getValue()));

            System.out.printf("Part 2 - Sum %s%n", max);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

    }

    Optional<Integer> getPrice(List<PriceChange> priceChanges, List<Integer> targetPattern) {
        for (int i = 4; i < priceChanges.size(); i++) {
            boolean result = Objects.equals(priceChanges.get(i - 3).diff(), targetPattern.get(0));
            result &= Objects.equals(priceChanges.get(i - 2).diff(), targetPattern.get(1));
            result &= Objects.equals(priceChanges.get(i - 1).diff(), targetPattern.get(2));
            result &= Objects.equals(priceChanges.get(i).diff(), targetPattern.get(3));
            if (result) {
                return Optional.of(priceChanges.get(i).currentDigit);
            }
        }
        return Optional.empty();
    }

    int lastDigit(long value) {
        return Integer.parseInt(String.valueOf(value).substring(String.valueOf(value).length() - 1));
    }

    private Set<List<Integer>> sequencesForPrice(Integer price, List<PriceChange> seqs) {
        Set<List<Integer>> result = new HashSet<>();

        for (PriceChange priceChange : seqs.stream()
                .filter(s -> s.id > 4)
                .filter(s -> s.currentDigit == price)
                .toList()) {

            List<Integer> list = seqs.stream()
                    .filter(s -> s.id > priceChange.id - 4)
                    .filter(s -> s.id <= priceChange.id())
                    .map(PriceChange::diff)
                    .toList();
            if (list.size() == 4) {
                result.add(list);
            }
        }

        return result;
    }

    record PriceChange(int id, long secret, Integer previousDigit, int currentDigit) {
        Integer diff() {
            if (id == 0) {
                return null;
            }
            return currentDigit - previousDigit;
        }

        @Override
        public String toString() {
            return secret + ": " + currentDigit + " (" + diff() + ")";
        }
    }

}