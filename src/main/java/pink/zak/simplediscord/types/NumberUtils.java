package pink.zak.simplediscord.types;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.util.Random;

@UtilityClass
public class NumberUtils {
    private final Random random = new Random();

    public static boolean isNumerical(String input) {
        return isNumerical(input, Integer.MAX_VALUE);
    }

    public static boolean isLikelyLong(String input) {
        return isNumerical(input, 20);
    }

    public static boolean isLikelyInteger(String input) {
        return isNumerical(input, 11);
    }

    public static boolean isNumerical(String input, int maxLength) {
        if (input == null || input.isEmpty() || input.length() > maxLength) {
            return false;
        }
        for (Character character : input.toCharArray()) {
            if (!Character.isDigit(character)) {
                return false;
            }
        }
        return true;
    }

    public static int parseInt(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static double parseDouble(String input, double defaultValue) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static long parseLong(String input, long defaultValue) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static BigInteger getRandomBigInteger(BigInteger max) {
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(max.bitLength(), random);
        } while (randomNumber.compareTo(max) >= 0);
        return randomNumber.add(BigInteger.ONE); // Added to make inclusive
    }

    public static int getPercentage(int current, int max) {
        return (int) ((((float) current) / max) * 100);
    }
}
