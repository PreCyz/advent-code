package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec9 extends DecBase {

    public Dec9(int year) {
        super(year, 9);
    }

    @Override
    public Dec9 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "2333133121414131402"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        char[] numbers = inputStrings.getFirst().toCharArray();
        ArrayList<String> memory = new ArrayList<>(1000);
        int mapIndex = 0;
        for (int i = 0; i < numbers.length; i++) {
            int value = Integer.parseInt(numbers[i] + "");
            if (i % 2 == 0) {
                for (int j = 0; j < value; j++) {
                    memory.add(mapIndex + "");
                }
                mapIndex++;
            } else {
                for (int j = 0; j < value; j++) {
                    memory.add(".");
                }
            }
        }
        memory.trimToSize();

        ArrayList<String> formatted = new ArrayList<>(memory);
        for (int i = memory.size() - 1; i >= 0; i--) {
            String value = memory.get(i);
            if (!".".equals(value)) {
                Optional<Integer> dotIdx = findFirstDotIdx(formatted);
                if (dotIdx.isPresent() && dotIdx.get() < i) {
                    formatted.remove(dotIdx.get().intValue());
                    formatted.add(dotIdx.get(), value);
                    formatted.remove(i);
                    formatted.add(i, ".");
                } else {
                    break;
                }
            }
        }

        long sum = 0;
        for (int i = 0; i < formatted.size(); i++) {
            String value = formatted.get(i);
            if (!".".equals(value)) {
                sum += (long) i * Integer.parseInt(value);
            }
        }
        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    private Optional<Integer> findFirstDotIdx(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (".".equals(list.get(i))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    @Override
    protected void calculatePart2() {
        char[] numbers = inputStrings.getFirst().toCharArray();
        ArrayList<ArrayList<String>> memory = new ArrayList<>(1000);
        int mapIndex = 0;
        for (int i = 0; i < numbers.length; i++) {
            int value = Integer.parseInt(numbers[i] + "");
            if (i % 2 == 0) {
                ArrayList<String> blockFile = new ArrayList<>(value);
                for (int j = 0; j < value; j++) {
                    blockFile.add(mapIndex + "");
                }
                memory.add(blockFile);
                mapIndex++;
            } else {
                ArrayList<String> freeSpace = new ArrayList<>(value);
                for (int j = 0; j < value; j++) {
                    freeSpace.add(".");
                }
                if (!freeSpace.isEmpty()) {
                    memory.add(freeSpace);
                }
            }
        }
        memory.trimToSize();

        ArrayList<ArrayList<String>> formatted = new ArrayList<>(memory);
        for (int i = memory.size() - 1; i >= 0; i--) {
            ArrayList<String> value = memory.get(i);
            if (value.stream().noneMatch("."::equals)) {
                Optional<Integer> dotIdx = findSpace(formatted, value.size());
                if (dotIdx.isPresent() && dotIdx.get() < i) {
                    ArrayList<String> freeSpace = formatted.get(dotIdx.get());
                    replaceFileBlock(freeSpace, value);
                }
            }
        }

        long sum = 0;
        int idx = 0;
        for (int i = 0; i < formatted.size(); i++) {
            ArrayList<String> blocks = formatted.get(i);
            for (int j = 0; j < blocks.size(); j++) {
                String value = blocks.get(j);
                if (!".".equals(value)) {
                    sum += (long) idx * Integer.parseInt(value);
                }
                idx++;
            }
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private Optional<Integer> findSpace(ArrayList<ArrayList<String>> list, int size) {
        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> value = list.get(i);
            if (value.stream().filter("."::equals).count() >= size) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private void replaceFileBlock(ArrayList<String> freeSpace, ArrayList<String> fileBlock) {
        ArrayList<String> tmp = new ArrayList<>(fileBlock);
        for (int i = 0; i < tmp.size(); i++) {
            String value = tmp.get(i);
            Optional<Integer> dotIdx = findFirstDotIdx(freeSpace);
            if (dotIdx.isPresent()) {
                freeSpace.remove(dotIdx.get().intValue());
                freeSpace.add(dotIdx.get(), value);
                fileBlock.remove(i);
                fileBlock.add(i, ".");
            } else {
                break;
            }
        }
    }

}