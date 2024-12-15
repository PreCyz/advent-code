package year2024;

import base.DecBase;
import utils.GridUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec15 extends DecBase {

    public Dec15(int year) {
        super(year, 15);
    }

    @Override
    public Dec15 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                /*"########",
                "#..O.O.#",
                "##@.O..#",
                "#...O..#",
                "#.#.O..#",
                "#...O..#",
                "#......#",
                "########",
                "",
                "<^^>>>vv<v>>v<<"*/

                "##########",
                "#..O..O.O#",
                "#......O.#",
                "#.OO..O.O#",
                "#..O@..O.#",
                "#O#..O...#",
                "#O..O..O.#",
                "#.OO.O.OO#",
                "#....O...#",
                "##########",
                "",
                "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
                "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
                "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
                "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
                "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
                "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
                ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
                "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
                "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
                "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"
        ).toList());
        return this;
    }

    static class Point {
        int x;
        int y;
        char value;

        public Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        boolean isWall() {
            return value == '#';
        }

        public boolean isEmptySpace() {
            return value == '.';
        }

        public boolean isBox() {
            return value == 'O';
        }

        public boolean isRobot() {
            return value == '@';
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    ", value=" + value +
                    '}';
        }
    }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int mvX;
        final int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }

        static Direction valueFrom(char value) {
            return switch (value) {
                case '^' -> UP;
                case 'v' -> DOWN;
                case '<' -> LEFT;
                case '>' -> RIGHT;
                default -> throw new IllegalStateException("Unexpected value: " + value);
            };
        }
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Point> warehouse = new ArrayList<>(inputStrings.size() / 2);
        ArrayList<Direction> instructions = new ArrayList<>();
        int y = 0;
        for (String input : inputStrings) {
            char[] charArray = input.toCharArray();
            for (int x = 0; x < input.length(); x++) {
                if (Stream.of('#', '.', 'O', '@').collect(Collectors.toSet()).contains(charArray[x])) {
                    warehouse.add(new Point(x, y, charArray[x]));
                } else if (Stream.of('<', '>', '^', 'v').collect(Collectors.toSet()).contains(charArray[x])) {
                    instructions.add(Direction.valueFrom(charArray[x]));
                }
            }
            y++;
        }
        warehouse.trimToSize();
        instructions.trimToSize();
//        printGrid(warehouse);

        for (Direction instruction : instructions) {
//            System.out.println(instruction);
            executeInstruction(instruction, warehouse);
//            printGrid(warehouse);
        }

        long sum = 0;
        for (Point point : warehouse) {
            if (point.isBox()) {
                sum += 100L * point.y + point.x;
            }
        }
        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    void executeInstruction(Direction instruction, ArrayList<Point> warehouse) {
        final Point robot = warehouse.stream().filter(Point::isRobot).findFirst().get();
        ArrayList<Point> list;
        Point wall;
        ArrayList<Point> emptySpaces;
        ArrayList<Point> boxes;
        Point currentRobot = warehouse.stream().filter(Point::isRobot).findFirst().get();
        int nextX = currentRobot.x + instruction.mvX;
        int nextY = currentRobot.y + instruction.mvY;
        switch (instruction) {
            case UP:
                list = warehouse.stream()
                        .filter(p -> p.x == robot.x)
                        .filter(p -> p.y < robot.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                wall = list.stream()
                        .filter(Point::isWall)
                        .toList()
                        .getLast();
                emptySpaces = list.stream()
                        .filter(Point::isEmptySpace)
                        .filter(p -> p.y > wall.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                boxes = list.stream()
                        .filter(Point::isBox)
                        .filter(p -> p.y > wall.y)
                        .collect(Collectors.toCollection(ArrayList::new));

                if (emptySpaces.stream().anyMatch(p -> p.y == nextY)) {
                    Point emptySpace = emptySpaces.stream().filter(p -> p.y == nextY).findFirst().get();
                    emptySpace.value = currentRobot.value;
                    currentRobot.value = '.';
                } else if (boxes.stream().anyMatch(p -> p.y == nextY)) {
                    Point box = boxes.stream().filter(p -> p.y == nextY).findFirst().get();
                    List<Point> emptySpace = emptySpaces.stream()
                            .filter(p -> p.y < nextY)
                            .filter(p -> p.y > wall.y)
                            .toList();
                    if (!emptySpace.isEmpty()) {
                        emptySpace.getLast().value = box.value;
                        box.value = currentRobot.value;
                        currentRobot.value = '.';
                    }
                }
                break;
            case DOWN:
                list = warehouse.stream()
                        .filter(p -> p.x == robot.x)
                        .filter(p -> p.y > robot.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                wall = list.stream()
                        .filter(Point::isWall)
                        .toList()
                        .getFirst();
                emptySpaces = list.stream()
                        .filter(Point::isEmptySpace)
                        .filter(p -> p.y < wall.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                boxes = list.stream()
                        .filter(Point::isBox)
                        .filter(p -> p.y < wall.y)
                        .collect(Collectors.toCollection(ArrayList::new));

                if (emptySpaces.stream().anyMatch(p -> p.y == nextY)) {
                    Point emptySpace = emptySpaces.stream().filter(p -> p.y == nextY).findFirst().get();
                    emptySpace.value = currentRobot.value;
                    currentRobot.value = '.';
                } else if (boxes.stream().anyMatch(p -> p.y == nextY)) {
                    Point box = boxes.stream().filter(p -> p.y == nextY).findFirst().get();
                    List<Point> emptySpace = emptySpaces.stream()
                            .filter(p -> p.y > nextY)
                            .filter(p -> p.y < wall.y)
                            .toList();
                    if (!emptySpace.isEmpty()) {
                        emptySpace.getFirst().value = box.value;
                        box.value = currentRobot.value;
                        currentRobot.value = '.';
                    }
                }
                break;
            case LEFT:
                list = warehouse.stream()
                        .filter(p -> p.y == robot.y)
                        .filter(p -> p.x < robot.x)
                        .collect(Collectors.toCollection(ArrayList::new));
                wall = list.stream()
                        .filter(Point::isWall)
                        .toList()
                        .getLast();
                emptySpaces = list.stream()
                        .filter(Point::isEmptySpace)
                        .filter(p -> p.x > wall.x)
                        .collect(Collectors.toCollection(ArrayList::new));
                boxes = list.stream()
                        .filter(Point::isBox)
                        .filter(p -> p.x > wall.x)
                        .collect(Collectors.toCollection(ArrayList::new));

                if (emptySpaces.stream().anyMatch(p -> p.x == nextX)) {
                    Point emptySpace = emptySpaces.stream().filter(p -> p.x == nextX).findFirst().get();
                    emptySpace.value = currentRobot.value;
                    currentRobot.value = '.';
                } else if (boxes.stream().anyMatch(p -> p.x == nextX)) {
                    Point box = boxes.stream().filter(p -> p.x == nextX).findFirst().get();
                    List<Point> emptySpace = emptySpaces.stream()
                            .filter(p -> p.x < nextX)
                            .filter(p -> p.x > wall.x)
                            .toList();
                    if (!emptySpace.isEmpty()) {
                        emptySpace.getLast().value = box.value;
                        box.value = currentRobot.value;
                        currentRobot.value = '.';
                    }
                }
                break;
            case RIGHT:
                list = warehouse.stream()
                        .filter(p -> p.y == robot.y)
                        .filter(p -> p.x > robot.x)
                        .collect(Collectors.toCollection(ArrayList::new));
                wall = list.stream()
                        .filter(Point::isWall)
                        .toList()
                        .getFirst();
                emptySpaces = list.stream()
                        .filter(Point::isEmptySpace)
                        .filter(p -> p.x < wall.x)
                        .collect(Collectors.toCollection(ArrayList::new));
                boxes = list.stream()
                        .filter(Point::isBox)
                        .filter(p -> p.x < wall.x)
                        .collect(Collectors.toCollection(ArrayList::new));

                if (emptySpaces.stream().anyMatch(p -> p.x == nextX)) {
                    Point emptySpace = emptySpaces.stream().filter(p -> p.x == nextX).findFirst().get();
                    emptySpace.value = currentRobot.value;
                    currentRobot.value = '.';
                } else if (boxes.stream().anyMatch(p -> p.x == nextX)) {
                    Point box = boxes.stream().filter(p -> p.x == nextX).findFirst().get();
                    List<Point> emptySpace = emptySpaces.stream()
                            .filter(p -> p.x > nextX)
                            .filter(p -> p.x < wall.x)
                            .toList();
                    if (!emptySpace.isEmpty()) {
                        emptySpace.getFirst().value = box.value;
                        box.value = currentRobot.value;
                        currentRobot.value = '.';
                    }
                }

                break;
        }
    }

    void printGrid(ArrayList<Point> warehouse) {
//        if (!warehouse.isEmpty()) return;
        int size = (int) Math.sqrt(warehouse.size());
        char[][] grid = new char[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                final int xx = x;
                final int yy = y;
                grid[y][x] = warehouse.stream()
                        .filter(p -> p.x == xx)
                        .filter(p -> p.y == yy)
                        .findFirst()
                        .get()
                        .value;
            }
        }
        GridUtils.printGridYFirst(grid);
        System.out.println();
    }

    int getIndex(Point point, ArrayList<Point> warehouse) {
        for (int i = 0; i < warehouse.size(); i++) {
            if (warehouse.get(i).x == point.x && warehouse.get(i).y == point.y) {
                return i;
            }
        }
        throw new IllegalStateException("No index");
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Point> warehouse = new ArrayList<>(inputStrings.size() / 2);
        ArrayList<Direction> instructions = new ArrayList<>();
        int y = 0;
        for (String input : inputStrings) {
            char[] charArray = input.toCharArray();
            int xx = 0;
            for (int x = 0; x < input.length(); x++) {
                if ('#' == charArray[x]) {
                    warehouse.add(new Point(xx++, y, charArray[x]));
                    warehouse.add(new Point(xx++, y, charArray[x]));
                } else if ('.' == charArray[x]) {
                    warehouse.add(new Point(xx++, y, charArray[x]));
                    warehouse.add(new Point(xx++, y, charArray[x]));
                } else if ('O' == charArray[x]) {
                    warehouse.add(new Point(xx++, y, '['));
                    warehouse.add(new Point(xx++, y, ']'));
                } else if ('@' == charArray[x]) {
                    warehouse.add(new Point(x++, y, charArray[x]));
                    warehouse.add(new Point(x++, y, '.'));
                } else if (Stream.of('<', '>', '^', 'v').collect(Collectors.toSet()).contains(charArray[x])) {
                    instructions.add(Direction.valueFrom(charArray[x]));
                }
            }
            y++;
        }
        warehouse.trimToSize();
        instructions.trimToSize();
//        System.out.printf("Part 2 - Sum[%b] %d%n", move, move);
    }
}