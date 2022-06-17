package com.onlineshop;


import com.onlineshop.entities.Customer;
import com.onlineshop.items.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckoutHandler {

    private LocalDate deliveryWindowStart;
    private LocalDate deliveryWindowEnd;

    private double sumItemPrices (List<Item> items) {
        double sum = 0;

        for(Item item : items) {
            sum = sum + item.price();
        }
        return sum;
    }

    public double applyVoucher(String voucher, double price){
        if(isValidVoucher(voucher)){
            price = BigDecimal.valueOf(price * 0.95).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        } else {
            System.out.println("Voucher invalid");
        }
        return price;
    }

    public boolean isValidVoucher(String voucher) {
        return voucher.equals("GIMME_DISCOUNT") || voucher.equals("CHEAPER_PLEASE");
    }

    public boolean isEligibleForFreeDelivery(String membership) {
        return membership.equalsIgnoreCase("GOLD");
    }

    public boolean isUsAddress(String address) {
        return address.contains("US");
    }

    public double addDeliveryFee(Customer customer, double total) {
        if(isEligibleForFreeDelivery(customer.getMembership())){
            // do nothing
        } else {
            if(isUsAddress(customer.getAddress())){
                System.out.println("Adding flat delivery fee of 5 USD");
                total = total + 5;
            } else {
                System.out.println("Adding flat global delivery fee of 10 USD");
                total = total + 10;
            }
        }

        return total;
    }

    public double calculateTotal(List<Item> items, String voucher, Customer customer){
        double baseTotal = sumItemPrices(items);

        baseTotal = applyVoucher(voucher,baseTotal);

        baseTotal = addDeliveryFee(customer, baseTotal);

        return baseTotal;
    }



    public void setDeliveryTimeWindow(LocalDate deliveryStart, LocalDate deliveryEnd){
        this.deliveryWindowStart = deliveryStart;
        this.deliveryWindowEnd = deliveryEnd;

        System.out.println(String.format("Your Order will delivered some time between %s and %s", deliveryWindowStart, deliveryWindowEnd));
    }

}
