package org.erhanmutlu.iyzicospringintegrationdemo;

import java.io.Serializable;

public class Basket implements Serializable {

    private Long basketId;
    private Long paymentId;

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
