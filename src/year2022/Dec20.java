package year2022;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec20 extends DecBase {
    protected Dec20(int year) {
        super(year, 20);
    }

    LinkedList<Integer> integers;
    ArrayList<Integer> mixed;

    @Override
    public Dec20 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                /*"1", "0", "4", "2", "3"*/
                /*"0", "-1", "1"*/
                /*"-1", "0", "2"*/
                /*"1", "0", "-2"*/
                "1", "2", "-3", "3", "-2", "0", "4"
                /*"6", "2", "1", "3", "-2", "0", "4"*/
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        integers = new LinkedList<>(inputStrings.stream().map(Integer::parseInt).toList());
        mixed = new ArrayList<>(inputStrings.stream().map(Integer::parseInt).toList());
        for (Integer value : integers) {
            if (value < 0) {
                mixBackward(value);
            } else if (value > 0) {
                mixForward(value);
            }
        }
        LinkedList<Integer> groveCoordinates = findGroveCoordinates();
        System.out.printf("Part 1: sum = %d%n", groveCoordinates.stream().mapToInt(it -> it).sum());
    }

    private LinkedList<Integer> findGroveCoordinates() {
        int _0Idx = getValueIdx(0);
        int _1000th = (_0Idx + 1000) % mixed.size();
        int _2000th = (_0Idx + 2000) % mixed.size();
        int _3000th = (_0Idx + 3000) % mixed.size();
        LinkedList<Integer> result = new LinkedList<>();
        for (int i = 0; i < mixed.size(); i++) {
            if (i == _1000th) {
                System.out.printf("1000th i == %d, value == %d%n", i, mixed.get(i));
                result.add(mixed.get(i));
            } else if (i == _2000th) {
                System.out.printf("2000th i == %d, value == %d%n", i, mixed.get(i));
                result.add(mixed.get(i));
            } else if (i == _3000th) {
                System.out.printf("3000th i == %d, value == %d%n", i, mixed.get(i));
                result.add(mixed.get(i));
            }
        }
        return result;
    }

    private int getValueIdx(Integer value) {
        for (int i = 0; i < mixed.size(); i++) {
            if (mixed.get(i).equals(value)) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private void mixBackward(Integer value) {
        int currentIdx = getValueIdx(value);
        int shift = Math.abs(value) % mixed.size();
        int newIdx;
        if (currentIdx >= shift) {
            newIdx = currentIdx - shift - 1;
        } else {
            newIdx = mixed.size() - (shift - currentIdx);
        }
        if (newIdx < 0) {
            newIdx = mixed.size();
        }
        if (currentIdx != newIdx) {
            shift(currentIdx, newIdx);
        }
    }

    private void mixForward(Integer value) {
        int currentIdx = getValueIdx(value);
        int shift = Math.abs(value) % mixed.size();
        int newIdx;

        if (currentIdx + shift > mixed.size() - 1) {
            newIdx = 1 + currentIdx + shift - mixed.size();
        } else /*if (currentIdx + shift <= mixed.size() - 1)*/ {
            newIdx = 1 + currentIdx + shift;
        }
        if (newIdx > mixed.size() - 1) {
            newIdx = 0;
        }
        if (currentIdx != newIdx) {
            shift(currentIdx, newIdx);
        }
    }

    private void shift(int currentIdx, int newIdx) {
        ArrayList<Integer> tmp = new ArrayList<>(mixed);
        if (currentIdx > newIdx) {
            tmp.add(newIdx, tmp.remove(currentIdx));
        } else if (currentIdx < newIdx) {
            tmp.add(newIdx, tmp.get(currentIdx));
            tmp.remove(currentIdx);
        }
        mixed = new ArrayList<>(tmp);
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = Stream.of(1, 2, 3).collect(Collectors.toCollection(ArrayList::new));
        list.add(1, 4);
        System.out.println(list);
    }

}
