package year2024;

import base.DecBase;

import java.util.*;
import java.util.concurrent.*;
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
        Map<Integer, List<List<Integer>>> priceSequenceMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 10; i++) {
            priceSequenceMap.put(i, new ArrayList<>());
        }

        int available = Runtime.getRuntime().availableProcessors();
        try (ExecutorService exe = Executors.newFixedThreadPool(available)) {
            List<CompletableFuture<Void>> cf = new ArrayList<>(inputStrings.size());
            for (String secret : inputStrings) {

                cf.add(CompletableFuture.runAsync(() -> {

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

                    for (Integer price : priceChanges.stream().map(pc -> pc.currentDigit).collect(Collectors.toSet())) {
                        List<List<Integer>> sequencesForPrice = sequencesForPrice(price, priceChanges);
                        priceSequenceMap.get(price).addAll(sequencesForPrice);


                        /* Optimization add item to the list only if it's not in that list
                        List<List<Integer>> lists = priceSequenceMap.get(price);
                        for (List<Integer> list : lists) {
                            ArrayList<Integer> endSeq = new ArrayList<>(list);
                            for (List<Integer> sequence : sequencesForPrice) {
                                if (Collections.indexOfSubList(list, sequence) == -1) {
                                    lists.add(sequence);
                                }
                            }
                        }*/
                    }

                }, exe));

            }

            CompletableFuture.allOf(cf.toArray(new CompletableFuture[0]))
                    .whenComplete((r, t) ->
                            System.out.printf("Creating maps for initial secrets completed. total sequence size = [%d] priceSequenceMap.values.size [%d]%n",
                                    buyerSequenceMap.values().stream().mapToInt(List::size).sum(),
                                    priceSequenceMap.values().stream().mapToLong(List::size).sum())
                    )
                    .join();

            cf.clear();

            Map<String, Long> result = new ConcurrentHashMap<>();
            for (Map.Entry<Integer, List<List<Integer>>> entry : priceSequenceMap.entrySet()) {

                cf.add(CompletableFuture.runAsync(() -> {

                            for (List<Integer> sequence : entry.getValue()) {

                                for (Map.Entry<Long, List<PriceChange>> buyerSequenceEntry : buyerSequenceMap.entrySet()) {
                                    getPrice(buyerSequenceEntry.getValue(), sequence).ifPresent(
                                            price -> {
                                                String key = entry.getKey() + "-> " + sequence.stream().map(Object::toString).collect(Collectors.joining(","));
                                                if (result.containsKey(key)) {
                                                    result.put(key, result.get(key) + price);
                                                } else {
                                                    result.put(key, Long.valueOf(price));
                                                }
                                            }
                                    );
                                }

                            }
                        }, exe
                    )
                    .whenComplete((r, t) ->
                            System.out.printf("Entry with key [%d] with list of patterns size [%d] for [%d] buyers completed.%n",
                                    entry.getKey(), entry.getValue().size(), buyerSequenceMap.size())
                    )
                );

            }
            CompletableFuture.allOf(cf.toArray(new CompletableFuture[0])).join();
//        result.forEach((key, value) -> System.out.printf("%s -> %s%n", key, value));

            long max = result.values().stream().mapToLong(Long::valueOf).max().getAsLong();
            System.out.printf("Part 2 - Sum %s%n", max);
        }
    }

    Optional<Integer> getPrice(List<PriceChange> priceChanges, List<Integer> targetPattern) {
        for (int i = 4; i < priceChanges.size() - 1; i++) {
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

    private List<List<Integer>> sequencesForPrice(Integer price, List<PriceChange> seqs) {

        List<List<Integer>> result = new ArrayList<>(10);

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