package com.backend.library.backend.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    /**
     * ID del cliente de PayPal
     */
    @Value("${paypal.client.id}")
    private String clientId;
    /**
     * Secreto del cliente de PayPal
     */
    @Value("${paypal.client.secret}")
    private String clientSecret;
    /**
     * Modo en que se ejecutara la aplicacion en PayPal (sandbox o live)
     */
    @Value("${paypal.mode}")
    private String mode;

    /**
     * Crea el objeto <code>PayPalEnvironment</code> con los valores del cliente
     * de PayPal
     *
     * @return un objeto <code>PayPalEnvironment</code>
     */
    @Bean
    public PayPalEnvironment payPalEnvironment() {
        return new PayPalEnvironment.Sandbox(clientId, clientSecret);
    }

    /**
     * Crea un objeto <code>PayPalHttpClient</code> con el objeto
     * <code>PayPalEnvironment</code> configurado
     *
     * @param environment el objeto <code>PayPalEnvironment</code> configurado
     * @return un objeto <code>PayPalHttpClient</code>
     */
    @Bean
    public PayPalHttpClient payPalHttpClient(PayPalEnvironment environment) {
        return new PayPalHttpClient(environment);
    }
    
}
