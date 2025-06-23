package org.example.wschat.dto;

public class AuctionMessage {
    private String type; // create, bid, expire
    private String auctionId;
    private String sender;
    private String itemName;
    private double price;
    private int duration; // in seconds
    private long expirationTime; // timestamp in milliseconds
    private String status; // active, expired

    // Getters and setters


    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAuctionId() {
        return auctionId;

    }
    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public long getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
