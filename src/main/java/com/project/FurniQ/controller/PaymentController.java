package com.project.FurniQ.controller;

import com.project.FurniQ.dto.PaymentRequestDTO;
import com.project.FurniQ.dto.PaymentResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
//payment controller
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-payment-intent")
    public PaymentResponseDTO createPaymentIntent(@RequestBody PaymentRequestDTO request) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new PaymentResponseDTO(paymentIntent.getClientSecret());
    }
}