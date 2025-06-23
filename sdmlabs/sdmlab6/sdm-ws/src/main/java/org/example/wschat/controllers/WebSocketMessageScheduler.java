package org.example.wschat.controllers;

import org.example.wschat.dto.AuctionMessage;
import org.example.wschat.model.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class WebSocketMessageScheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final AuctionController auctionController;

    public WebSocketMessageScheduler(SimpMessagingTemplate messagingTemplate, AuctionController auctionController) {
        this.messagingTemplate = messagingTemplate;
        this.auctionController = auctionController;
    }

    @Scheduled(fixedRate = 5000) // Check every 5 seconds
    public void checkAuctionExpirations() {
        Map<String, Auction> auctions = auctionController.getAuctions();
        List<String> expiredAuctionsIds = new ArrayList<>();

        // Check for expired auctions
        for (Map.Entry<String, Auction> entry : auctions.entrySet()) {
            Auction auction = entry.getValue();
            if (auction.isExpired()) {
                expiredAuctionsIds.add(entry.getKey());
                
                // Send expiration notification
                AuctionMessage message = new AuctionMessage();
                message.setType("expire");
                message.setAuctionId(auction.getId());
                message.setItemName(auction.getItemName());
                message.setPrice(auction.getHighestBid());
                message.setSender(auction.getHighestBidder() != null ? auction.getHighestBidder() : "No winner");
                message.setStatus("expired");
                message.setExpirationTime(auction.getExpirationTime());
                
                System.out.println("Auction expired: " + auction.getId() + ", Winner: " + 
                                 (auction.getHighestBidder() != null ? auction.getHighestBidder() : "No winner"));
                
                messagingTemplate.convertAndSend("/topic/auctions", message);
            }
        }
        
        // You could remove expired auctions if needed
        // for (String id : expiredAuctionsIds) {
        //     auctions.remove(id);
        // }
    }
}

//✅ WebSocketMessageScheduler.java (optional)
//This is bonus: it sends a "Hello from server" every second — just to test the server can send stuff even without any message from client.