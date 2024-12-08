package utils;

import java.util.Arrays;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static void main(String[] args) {
        System.out.println(lcm(10, 5, 3));
        System.out.println(gcd(10, 5, 3));
//        System.out.println(leastCommonMultiple(new int[]{10, 5, 3}));
//        System.out.println(leastCommonMultiple(Arrays.asList(1, 2, 3)));
    }

    private static int gcd(int x, int y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static int gcd(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (x, y) -> gcd(x, y));
    }

    public static int lcm(int... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }
}
