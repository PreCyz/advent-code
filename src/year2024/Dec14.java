package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec14 extends DecBase {

    public Dec14(int year) {
        super(year, 14);
    }

    @Override
    public Dec14 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "p=0,4 v=3,-3",
                "p=6,3 v=-1,-3",
                "p=10,3 v=-1,2",
                "p=2,0 v=2,-1",
                "p=0,0 v=1,3",
                "p=3,0 v=-2,-2",
                "p=7,6 v=-1,-3",
                "p=3,0 v=-1,-2",
                "p=9,3 v=2,3",
                "p=7,3 v=-1,2",
                "p=2,4 v=2,-3",
                "p=9,5 v=-3,-3"
        ).toList());
        return this;
    }

    record Point (int x, int y) {}
    record Robot (Point p, Point v) {}

    @Override
    protected void calculatePart1() {
        ArrayList<Robot> robots = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(" ");
            String[] p = split[0].replace("p=", "").split(",");
            String[] v = split[1].replace("v=", "").split(",");
            robots.add(new Robot(
                    new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])),
                    new Point(Integer.parseInt(v[0]), Integer.parseInt(v[1]))
            ));
        }

        int seconds = 100;
//        final int spaceX = 101;
//        final int spaceY = 103;
        final int spaceX = 11;
        final int spaceY = 7;

        int move = 0;
        ArrayList<Robot> newPositions = new ArrayList<>(robots.size());
        do {
            for (Robot robot : robots) {
                newPositions.add(move(robot, spaceX, spaceY));
            }
            robots = new ArrayList<>(newPositions);
            newPositions.clear();
            move++;
        } while (move < seconds);

        long q1Count = robots.stream().filter(r -> r.p.x < spaceX / 2 && r.p.y < spaceY / 2).count();
        long q2Count = robots.stream().filter(r -> r.p.x < spaceX / 2 && r.p.y > spaceY / 2).count();
        long q3Count = robots.stream().filter(r -> r.p.x > spaceX / 2 && r.p.y < spaceY / 2).count();
        long q4Count = robots.stream().filter(r -> r.p.x > spaceX / 2 && r.p.y > spaceY / 2).count();
        long sum = q1Count * q2Count * q3Count * q4Count;

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    Robot move(Robot robot, int spaceX, int spaceY) {
        int newX = robot.p.x + robot.v.x;
        if (newX < 0) {
            newX = spaceX + newX;
        } else if (newX >= spaceX) {
            newX = newX - spaceX;
        }
        int newY = robot.p.y + robot.v.y;
        if (newY < 0) {
            newY = spaceY + newY;
        } else if (newY >= spaceY) {
            newY = newY - spaceY;
        }
        return new Robot(new Point(newX, newY), robot.v);
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Robot> robots = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(" ");
            String[] p = split[0].replace("p=", "").split(",");
            String[] v = split[1].replace("v=", "").split(",");
            robots.add(new Robot(
                    new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])),
                    new Point(Integer.parseInt(v[0]), Integer.parseInt(v[1]))
            ));
        }

        int seconds = 100_000;
        final int spaceX = 101;
        final int spaceY = 103;
//        final int spaceX = 11;
//        final int spaceY = 7;

        int move = 0;
        boolean easterEg = false;
        ArrayList<Robot> newPositions = new ArrayList<>(robots.size());

        do {
            for (Robot robot : robots) {
                newPositions.add(move(robot, spaceX, spaceY));
            }
            robots = new ArrayList<>(newPositions);
            newPositions.clear();
            move++;
            easterEg = isEasterEgg(robots, spaceX, spaceY);
        } while (move < seconds || easterEg);
        System.out.printf("Part 2 - Sum[%b] %d%n", easterEg, move);
    }

    boolean isEasterEgg(ArrayList<Robot> robots, int spaceX, int spaceY) {
        long count = robots.stream().filter(r -> r.p.x == spaceX / 2 && r.p.y == 0).count();
        if (count > 1) {
            boolean isEasterEgg = true;
            for (int y = 1; y < spaceY; y++) {
                if (spaceX / 2 - y >= 0) {
                    final int yy = y;
                    long r1 = robots.stream().filter(r -> r.p.x == (spaceX / 2) - yy && r.p.y == yy).count();
                    long r2 = robots.stream().filter(r -> r.p.x == (spaceX / 2) + yy && r.p.y == yy).count();
                    isEasterEgg = r1 > 0 && r2 > 0;
                    if (!isEasterEgg) {
                        return false;
                    }
                } else {
                    break;
                }
            }
            return isEasterEgg;
        }
        return false;
    }

}