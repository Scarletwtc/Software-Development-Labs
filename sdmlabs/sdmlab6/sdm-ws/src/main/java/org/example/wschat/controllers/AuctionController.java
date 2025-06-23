package org.example.wschat.controllers;

import org.example.wschat.dto.AuctionMessage;
import org.example.wschat.model.Auction;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class AuctionController {
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, Auction> auctions = new ConcurrentHashMap<>();

    public AuctionController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

   @MessageMapping("/auction")
    public void handleAuction(AuctionMessage auctionMessage) {
        System.out.println("Received message of type: " + auctionMessage.getType() + " from sender: " + auctionMessage.getSender());
        
        if("create".equals(auctionMessage.getType()) && "admin".equals(auctionMessage.getSender())){
            System.out.println("Creating auction for item: " + auctionMessage.getItemName());
            createAuction(auctionMessage);
        } else if ("bid".equals(auctionMessage.getType())) {
            handleBid(auctionMessage);
        } else {
            System.out.println("Message type or sender not recognized: " + auctionMessage.getType() + ", " + auctionMessage.getSender());
        }
   }

   private void createAuction(AuctionMessage auctionMessage) {
        String id = UUID.randomUUID().toString();
        long expiration = (long) (System.currentTimeMillis() + auctionMessage.getDuration()*1000L);

        Auction auction = new Auction();
        auction.setId(id);
        auction.setItemName(auctionMessage.getItemName());
        auction.setMinPrice(auctionMessage.getPrice());
        auction.setHighestBid(auctionMessage.getPrice()); // Set min price as initial highest bid
        auction.setExpirationTime(expiration);

        auctions.put(id, auction);

        auctionMessage.setAuctionId(id);
        auctionMessage.setPrice(auctionMessage.getPrice());
        auctionMessage.setExpirationTime(expiration);
        auctionMessage.setStatus("active");

        System.out.println("Auction created: " + auctionMessage.getAuctionId()); // Debug line

        messagingTemplate.convertAndSend("/topic/auctions", auctionMessage);
   }

   private void handleBid(AuctionMessage auctionMessage) {
       System.out.println("Received auction message: " + auctionMessage.getType());

       Auction auction = auctions.get(auctionMessage.getAuctionId());
       if(auction == null) {
           System.out.println("Auction not found: " + auctionMessage.getAuctionId());
           return;
       }

       long now = System.currentTimeMillis();
       if(now > auction.getExpirationTime()) {
           System.out.println("Auction expired: " + auctionMessage.getAuctionId());
           return;
       }
       
       if(auctionMessage.getPrice() <= auction.getHighestBid()) {
           System.out.println("Bid too low: " + auctionMessage.getPrice() + " <= " + auction.getHighestBid());
           return;
       }

       auction.setHighestBid(auctionMessage.getPrice());
       auction.setHighestBidder(auctionMessage.getSender());

       auctionMessage.setItemName(auction.getItemName());
       auctionMessage.setPrice(auctionMessage.getPrice());
       auctionMessage.setExpirationTime(auction.getExpirationTime());
       auctionMessage.setStatus("active");
       
       System.out.println("New bid accepted: " + auctionMessage.getPrice() + " by " + auctionMessage.getSender());
       
       messagingTemplate.convertAndSend("/topic/auctions", auctionMessage);
   }

   public Map<String, Auction> getAuctions() {
        return auctions;
   }
}
