package year2022;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec19 extends DecBase {
    protected Dec19(String fileName) {
        super(fileName);
    }

    class Cost {
        int oreCost;
        int clayCost;
        int obsidianCost;
        int geodeCost;
    }

    class Blueprint {
        Blueprint(int id) {
            this.id = id;
        }

        int id;
        Cost oreRobotCost;
        Cost clayRobotCost;
        Cost obsidianRobotCost;
        Cost geodeRobotCost;
    }

    private ArrayList<Blueprint> blueprints;

    @Override
    public Dec19 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.",
                        "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian."
        ).toList());
        blueprints = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            String id = line.substring(0, line.indexOf(":")).split(" ")[1];
            Blueprint blueprint = new Blueprint(Integer.parseInt(id));
            String[] split = line.substring(line.indexOf(":") + 2).split("\\. ");
            for (String robotCost : split) {
                if (robotCost.startsWith("Each ore robot")) {
                    Cost cost = new Cost();
                    cost.oreCost = Integer.parseInt(robotCost.split(" ")[4]);
                    blueprint.oreRobotCost = cost;
                } else if (robotCost.startsWith("Each clay robot")) {
                    Cost cost = new Cost();
                    cost.clayCost = Integer.parseInt(robotCost.split(" ")[4]);
                    blueprint.clayRobotCost = cost;
                } else if (robotCost.startsWith("Each obsidian robot")) {
                    Cost cost = new Cost();
                    cost.oreCost = Integer.parseInt(robotCost.split(" ")[4]);
                    cost.clayCost = Integer.parseInt(robotCost.split(" ")[7]);
                    blueprint.obsidianRobotCost = cost;
                } else if (robotCost.startsWith("Each geode robot")) {
                    Cost cost = new Cost();
                    cost.oreCost = Integer.parseInt(robotCost.split(" ")[4]);
                    cost.obsidianCost = Integer.parseInt(robotCost.split(" ")[7]);
                    blueprint.geodeRobotCost = cost;
                }
            }
            blueprints.add(blueprint);
        }
        return this;
    }

    enum BuildPriority {
        ORE, CLAY, OBSIDIAN, GEODE
    }

    @Override
    protected void calculatePart1() {
        int sumOfQualityLevel = 0;
        for (Blueprint blueprint : blueprints) {
            int minutes = 0;
            int collectedOre = 0;
            int clayNumber = 0;
            int collectedClay = 0;
            int obsidianNumber = 0;
            int collectedObsidian = 0;
            int geodeNumber = 0;
            int collectedGeode = 0;
            BuildPriority buildPriority = BuildPriority.CLAY;

            while (minutes <= 24) {

                switch (buildPriority) {
                    case CLAY -> {
                        if (collectedOre >= blueprint.clayRobotCost.oreCost) {
                            clayNumber++;
                            buildPriority = BuildPriority.OBSIDIAN;
                            collectedClay -= blueprint.clayRobotCost.oreCost;
                        }
                    }
                    case OBSIDIAN -> {
                        if (collectedOre >= blueprint.obsidianRobotCost.oreCost && collectedClay >= blueprint.obsidianRobotCost.clayCost) {
                            obsidianNumber++;
                            buildPriority = BuildPriority.GEODE;
                            collectedOre -= blueprint.obsidianRobotCost.oreCost;
                            collectedClay -= blueprint.obsidianRobotCost.clayCost;
                        }
                    }
                    case GEODE -> {
                        if (collectedOre >= blueprint.geodeRobotCost.oreCost && collectedObsidian >= blueprint.geodeRobotCost.obsidianCost) {
                            geodeNumber++;
                            buildPriority = BuildPriority.CLAY;
                            collectedOre -= blueprint.geodeRobotCost.oreCost;
                            collectedObsidian -= blueprint.geodeRobotCost.obsidianCost;
                        }
                    }
                }

                collectedOre++;
                collectedClay += clayNumber;
                collectedObsidian += obsidianNumber;
                collectedGeode += geodeNumber;
                minutes++;
            }
            System.out.printf("Quality level of blueprint %d is %d%n", blueprint.id, blueprint.id * collectedGeode);
            sumOfQualityLevel += blueprint.id * collectedGeode;
        }
        System.out.println("Part 1: sum of quality levels: " + sumOfQualityLevel);

    }

}
