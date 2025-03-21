package com.flights.objects;

import java.time.LocalDate;

public class Payment {
    public boolean checkNo(long n) {
        return n >= 1000_0000_0000_0000L && n <= 9999_9999_9999_9999L;
    }

    public boolean checkCVV(int n) {
        return n >= 100 && n <= 999;
    }

    public boolean checkExpiryDate(int m, int y) {
        LocalDate date = LocalDate.of(Integer.parseInt("20"+y),m,1);
        return date.isAfter(LocalDate.now());
    }

    public boolean checkDetails(long number, int cvv, int month, int year) {
        return checkNo(number) && checkCVV(cvv) && checkExpiryDate(month, year);
    }

    public void processPayment() {
        System.out.println("Payment is being processed...");
    }
}
