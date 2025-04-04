package com.flights.objects;

public class Payment {
    public static boolean checkNo(long n) {
        return n >= 1000_0000_0000_0000L && n <= 9999_9999_9999_9999L;
    }

    public static boolean checkCVV(int n) {
        return n >= 100 && n <= 999;
    }

    public static void processPayment() {
        System.out.println("Payment is being processed...");
    }

    public static boolean checkName(String name) {
        String nameRegex = "^[A-Z][a-zA-Z'\\- ]{1,49}$";
        return name != null && name.matches(nameRegex);
    }
}
