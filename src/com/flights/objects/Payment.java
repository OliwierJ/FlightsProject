package com.flights.objects;

/**
 * Pseudo Payment class to "process" payments
 */
public class Payment {
    /**
     * Check card number
     * @param n card number
     * @return <code>true</code> if 16 digits long, <code>false</code> otherwise
     */
    public static boolean checkNo(long n) {
        return n >= 1000_0000_0000_0000L && n <= 9999_9999_9999_9999L;
    }

    /**
     * Check CVV
     * @param n CVV
     * @return <code>true</code> if 3 digits long, <code>false</code> otherwise
     */
    public static boolean checkCVV(int n) {
        return n >= 100 && n <= 999;
    }

    /**
     * Check name
     * @param name String to be checked
     * @return <code>true</code> if valid, <code>false</code> otherwise
     */
    public static boolean checkName(String name) {
        String nameRegex = "^[A-Z][a-zA-Z'\\- ]{1,49}$";
        return name != null && name.matches(nameRegex);
    }
}
