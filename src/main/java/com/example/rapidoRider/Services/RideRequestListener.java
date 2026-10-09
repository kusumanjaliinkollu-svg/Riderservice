package com.example.rapidoRider.Services;

import org.springframework.stereotype.Component;

@Component
public class RideRequestListener {

    public void receiveMessage(String message) {

        System.out.println("================================");
        System.out.println("RIDE REQUEST RECEIVED");
        System.out.println("================================");

        System.out.println(message);

        System.out.println("================================");
    }
}
