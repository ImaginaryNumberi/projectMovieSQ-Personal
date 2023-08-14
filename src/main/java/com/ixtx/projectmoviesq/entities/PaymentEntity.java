package com.ixtx.projectmoviesq.entities;

import lombok.Getter;

import java.util.Date;
import java.util.Objects;

public class PaymentEntity {
    @Getter
    private int index;
    @Getter
    private String userEmail;
    @Getter
    private String ticketNumber;
    @Getter
    private String category;
    @Getter
    private String paymentCompany;
    @Getter
    private String biNumber;
    @Getter
    private String cardNumber;
    @Getter
    private String paymentPassword;
    @Getter
    private int paymentAmount;
    @Getter
    private String authCode;
    @Getter
    private Date paidAt;
    private boolean isRefunded;
    @Getter
    private Date refundedAt;

    public PaymentEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public PaymentEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public PaymentEntity setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
        return this;
    }

    public PaymentEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    public PaymentEntity setPaymentCompany(String paymentCompany) {
        this.paymentCompany = paymentCompany;
        return this;
    }

    public PaymentEntity setBiNumber(String biNumber) {
        this.biNumber = biNumber;
        return this;
    }

    public PaymentEntity setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public PaymentEntity setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
        return this;
    }

    public PaymentEntity setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public PaymentEntity setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    public PaymentEntity setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
        return this;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public PaymentEntity setRefunded(boolean refunded) {
        isRefunded = refunded;
        return this;
    }

    public PaymentEntity setRefundedAt(Date refundedAt) {
        this.refundedAt = refundedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentEntity)) return false;
        PaymentEntity that = (PaymentEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}