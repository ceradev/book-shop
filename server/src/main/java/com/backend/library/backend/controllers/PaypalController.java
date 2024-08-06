package com.backend.library.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.library.backend.services.implementations.PayPalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaypalController {

    private final PayPalService payPalService;

    @Value("${paypal.success.url}")
    private String returnUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, String>> createOrder(@RequestParam("cartId") Long cartId,
            @RequestParam(value = "currency", defaultValue = "EUR") String currencyCode) {
        return ResponseEntity.ok(payPalService.createOrder(cartId, currencyCode));
    }

    @GetMapping("/success")
    public ResponseEntity<?> captureOrder(@RequestParam("token") String token) {
        payPalService.completeOrder(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);

    }

}