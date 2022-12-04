package year2015;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec2 extends DecBase {

    protected Dec2(String fileName) {
        super(fileName);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of(
                        "2x3x4",
                        "1x1x10"
                ).toList()
        );
        return this;
    }

    @Override
    protected void calculatePart1() {
        long total = 0;
        for (String input : inputStrings) {
            String[] lwh = input.split("x");
            long l = Integer.parseInt(lwh[0]);
            long w = Integer.parseInt(lwh[1]);
            long h = Integer.parseInt(lwh[2]);
            total += 2*l*w + 2*w*h + 2*h*l + Math.min(l*w, Math.min(w*h, h*l));
        }
        System.out.printf("Square feet: %d%n", total);
    }

    @Override
    protected void calculatePart2() {
        long total = 0;
        for (String input : inputStrings) {
            String[] lwh = input.split("x");
            long l = Integer.parseInt(lwh[0]);
            long w = Integer.parseInt(lwh[1]);
            long h = Integer.parseInt(lwh[2]);

            boolean condition = l*w < l*h;
            if (condition) {
                if (l*w < w*h) {
                    total+= 2*(l+w);
                } else {
                    total+= 2*(w+h);
                }
            } else {
                if (l*h < w*h) {
                    total+= 2*(l+h);
                } else {
                    total+= 2*(w+h);
                }
            }

            total += l*w*h;
        }
        System.out.printf("Ribbon feet: %d%n", total);
    }
}
