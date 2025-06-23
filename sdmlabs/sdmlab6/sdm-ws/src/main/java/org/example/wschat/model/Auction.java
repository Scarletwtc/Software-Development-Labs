package org.example.wschat.model;

public class Auction {
    private String id;
    private String itemName;
    private double minPrice;
    private double highestBid;
    private String highestBidder;
    private long expirationTime;

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getHighestBid() {
        return highestBid;
    }
    
    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }
    
    public String getHighestBidder() {
        return highestBidder;
    }
    
    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }
    
    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }
}
