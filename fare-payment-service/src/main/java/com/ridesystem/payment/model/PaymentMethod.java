package com.ridesystem.payment.model;

/**
 * Supported payment methods for the Fare Payment Service.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public enum PaymentMethod {

    /**
     * Payment made in physical cash.
     */
    CASH,

    /**
     * Payment made via credit or debit card.
     */
    CARD,

    /**
     * Payment made via an in-app digital wallet.
     */
    WALLET
}
