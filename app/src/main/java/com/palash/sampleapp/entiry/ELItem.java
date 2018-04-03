package com.palash.sampleapp.entiry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELItem {
    private String ItemID;
    private String OrderNumber;
    private String CatalogID;
    private String CatalogName;
    private String PageNo;
    private String IsMobileAttachment;
    private String AttachmentData;
    private String AttachmentName;
    private String ItemRemark;
    private String ItemAddedDate;
    private String ItemUpdateDate;
    private String ItemIsTempAdded;

    @JsonProperty("ItemID")
    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    @JsonProperty("OrderNumber")
    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    @JsonProperty("CatalogID")
    public String getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(String catalogID) {
        CatalogID = catalogID;
    }

    @JsonProperty("CatalogName")
    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    @JsonProperty("PageNo")
    public String getPageNo() {
        return PageNo;
    }

    public void setPageNo(String pageNo) {
        PageNo = pageNo;
    }

    @JsonProperty("IsMobileAttachment")
    public String getIsMobileAttachment() {
        return IsMobileAttachment;
    }

    public void setIsMobileAttachment(String isMobileAttachment) {
        IsMobileAttachment = isMobileAttachment;
    }

    @JsonProperty("AttachmentData")
    public String getAttachmentData() {
        return AttachmentData;
    }

    public void setAttachmentData(String attachmentData) {
        AttachmentData = attachmentData;
    }

    @JsonProperty("AttachmentName")
    public String getAttachmentName() {
        return AttachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        AttachmentName = attachmentName;
    }

    @JsonProperty("ItemRemark")
    public String getItemRemark() {
        return ItemRemark;
    }

    public void setItemRemark(String itemRemark) {
        ItemRemark = itemRemark;
    }

    @JsonProperty("ItemAddedDate")
    public String getItemAddedDate() {
        return ItemAddedDate;
    }

    public void setItemAddedDate(String itemAddedDate) {
        ItemAddedDate = itemAddedDate;
    }

    @JsonProperty("ItemUpdateDate")
    public String getItemUpdateDate() {
        return ItemUpdateDate;
    }

    public void setItemUpdateDate(String itemUpdateDate) {
        ItemUpdateDate = itemUpdateDate;
    }

    @JsonProperty("ItemIsTempAdded")
    public String getItemIsTempAdded() {
        return ItemIsTempAdded;
    }

    public void setItemIsTempAdded(String itemIsTempAdded) {
        ItemIsTempAdded = itemIsTempAdded;
    }
}
