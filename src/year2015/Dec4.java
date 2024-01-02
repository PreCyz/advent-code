package year2015;

import base.DecBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec4 extends DecBase {

    protected Dec4(int year) {
        super(year, 4);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "abcdef",
                "pqrstuv"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        calculate("00000", "Part 1 MD5: %s number %d%n");
    }

    @Override
    protected void calculatePart2() {
        calculate("000000", "Part 2 MD5: %s number %d%n");
    }

    private void calculate(String prefix, String format) {
        StringBuilder hashText;
        for (String input : inputStrings) {
            System.out.printf("The text %s ", input);
            try {
                long counter = 0;
                MessageDigest md = MessageDigest.getInstance("MD5");

                do {
                    counter++;
                    final String text = String.format("%s%d", input, counter);
                    byte[] messageDigest = md.digest(text.getBytes());
                    hashText = new StringBuilder();
                    for (byte b : messageDigest) {
                        hashText.append(String.format("%02x", b));
                    }
                } while (!hashText.toString().startsWith(prefix));
                System.out.printf(format, hashText, counter);
            }

            // For specifying wrong message digest algorithms
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}









