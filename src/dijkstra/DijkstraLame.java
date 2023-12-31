package dijkstra;

import year2023.Dec17;

import java.util.*;
import java.util.stream.Stream;

public class DijkstraLame extends Dec17.Dijkstra {

    public DijkstraLame(int totalNodes, Dec17.CityBlock[][] cityBlocks, int numberOfLastSteps) {
        super(totalNodes, cityBlocks, numberOfLastSteps);
    }

    Optional<Dec17.CityBlock> minimumDistance(Dec17.CityBlock cityBlock) {
        int m = Integer.MAX_VALUE;
        Optional<Dec17.CityBlock> m_index = Optional.empty();

        ArrayList<Dec17.CityBlock> neighbors = findNeighbors(cityBlock);

        for (Dec17.CityBlock vx : neighbors) {
            if (!visited.contains(vx.number) && heatLosses[vx.number] <= m) {
                m = heatLosses[vx.number];
                m_index = Optional.of(vx);
            }
        }
        return m_index;
    }

    void printSolution(int n) {
        System.out.println("The shortest Distance from source 0th node to all other nodes are: ");
        for (int j = 0; j < n; j++)
            System.out.printf("To %d the shortest heatLosses is: %d%n", j, heatLosses[j]);
    }

    @Override
    public void dijkstra(Dec17.CityBlock startBlock) {
        for (int j = 0; j < totalNodes; j++) {
            heatLosses[j] = Integer.MAX_VALUE;
        }
        heatLosses[startBlock.number] = 0;

        // compute the shortest path for all the given vertices
        for (int y = 0; y < cityBlocks.length; y++) {
            for (int x = 0; x < cityBlocks[y].length; x++) {

                Optional<Dec17.CityBlock> ux = minimumDistance(cityBlocks[y][x]);

                if (ux.isPresent()) {
                    visited.add(ux.get().number);

                    for (int yy = 0; yy < cityBlocks.length; yy++) {
                        for (int xx = 0; xx < cityBlocks[yy].length; xx++){
                            if (!visited.contains(cityBlocks[yy][xx].number) && heatLosses[ux.get().number] != Integer.MAX_VALUE &&
                                    heatLosses[ux.get().number] + cityBlocks[yy][xx].heatLoss < heatLosses[cityBlocks[yy][xx].number]) {
                                heatLosses[cityBlocks[yy][xx].number] = heatLosses[ux.get().number] + cityBlocks[yy][xx].heatLoss;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        LinkedList<String> inputStrings = new LinkedList<>(Stream.of(
                "2413432311323",
                "3215453535623",
                "3255245654254",
                "3446585845452",
                "4546657867536",
                "1438598798454",
                "4457876987766",
                "3637877979653",
                "4654967986887",
                "4564679986453",
                "1224686865563",
                "2546548887735",
                "4322674655533"
        ).toList());

        int nodeNumber = 0;
        final Dec17.CityBlock[][] cityBlocks = new Dec17.CityBlock[inputStrings.size()][inputStrings.getFirst().length()];
        for (int y = 0; y < inputStrings.size(); ++y) {
            String line = inputStrings.get(y);
            char[] charArray = line.toCharArray();
            for (int x = 0; x < charArray.length; ++x) {
                int heatLoss = Integer.parseInt(String.valueOf(charArray[x]));
                cityBlocks[y][x] = new Dec17.CityBlock(nodeNumber++, heatLoss, x, y);
            }
        }

        int totalNodes = inputStrings.size() * inputStrings.getFirst().length();
        Dec17.CityBlock startNode = new Dec17.CityBlock(cityBlocks[0][0], 0);
        int numberOfLastSteps = 3;

        DijkstraLame dijkstra = new DijkstraLame(totalNodes, cityBlocks, numberOfLastSteps);
        dijkstra.dijkstra(startNode);

        int destinationNodeNumber = totalNodes - 1;
        long sum = dijkstra.heatLosses[destinationNodeNumber];
        System.out.printf("Part 1 - Total score %d%n", sum);
        System.out.printf("Part 1 - directions to %d: %s%n", destinationNodeNumber, dijkstra.directionsMap.get(destinationNodeNumber));
    }
}
