package com.fairanb.event;

public class MerchantChangeModel{
    private String type;
    private String action;
    private Long merchantId;
    private String correlationId;


    public  MerchantChangeModel(String type, String action, Long merchantId, String correlationId) {
        super();
        this.type   = type;
        this.action = action;
        this.merchantId = merchantId;
        this.correlationId = correlationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }


}