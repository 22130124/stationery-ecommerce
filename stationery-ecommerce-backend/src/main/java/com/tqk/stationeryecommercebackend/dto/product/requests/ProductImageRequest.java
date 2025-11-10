package com.tqk.stationeryecommercebackend.dto.product.requests;

public class ProductImageRequest {
    private String url;
    private Boolean isDefault = false;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
