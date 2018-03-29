package com.palash.sampleapp.entiry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELOrder {
    private String OrderNumber;
    private String OrderRemark;
    private String OrderAddedDate;
    private String OrderUpdateDate;
    private String AddedBy;
    private String UpdatedBy;

    @JsonProperty("OrderNumber")
    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    @JsonProperty("OrderRemark")
    public String getOrderRemark() {
        return OrderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        OrderRemark = orderRemark;
    }

    @JsonProperty("OrderAddedDate")
    public String getOrderAddedDate() {
        return OrderAddedDate;
    }

    public void setOrderAddedDate(String orderAddedDate) {
        OrderAddedDate = orderAddedDate;
    }

    @JsonProperty("OrderUpdateDate")
    public String getOrderUpdateDate() {
        return OrderUpdateDate;
    }

    public void setOrderUpdateDate(String orderUpdateDate) {
        OrderUpdateDate = orderUpdateDate;
    }

    @JsonProperty("AddedBy")
    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    @JsonProperty("UpdatedBy")
    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }
}
