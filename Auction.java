/**
 * @author Luye Ge
 * ID# 111857836
 *
 * Write a fully-documented class named Auction which represents an active auction currently in the database.
 * The Auction class should contain member variables for the seller's name, the current bid,
 * the time remaining (in hours), current bidder's name, information about the item, and the unique ID for the auction.
 * In addition, the class should implement a toString() method,
 * which should print all of the data members in a neat tabular form.
 * The Auction class can only be altered by a single member method called newBid(),
 * which takes in the name of a bidder and their bid value. This method checks to see if the bid value is greater than
 * the current bid, and if it is, replaces the current bid and buyer name.
 * There should be getter methods for each member variable, however, no setters should be included.
 *
 *
 * The following is a list of information you should be paying attention to:
 * listing/seller_info/seller_name
 * listing/auction_info/current_bid
 * listing/auction_info/time_left
 * listing/auction_info/id_num
 * listing/auction_info/high_bidder/bidder_name
 * // the following should be combined to get the information of the item listing/item_info/memory
 * listing/item_info/hard_drive listing/item_info/cpu
 *
 *
 */
package com.company;

import java.io.Serializable;

public class Auction implements Serializable {

    private double currentBid;
    private int timeRemaining; // in hours
    private String auctionID;
    private String sellerName;
    private String buyerName;
    private String itemInfo;

    public Auction(){ }
    public Auction(String auctionID, double bid, String sellerName, String buyerName, int timeRemaining, String itemInfo){
        this.auctionID = auctionID;
        this.currentBid = bid;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.timeRemaining = timeRemaining;
        this.itemInfo = itemInfo;

    }

    /**
     * Brief:
     * Decreases the time remaining for this auction by the specified amount.
     * If time is greater than the current remaining time for the auction,
     * then the time remaining is set to 0 (i.e. no negative times).
     * Postconditions:
     * timeRemaining has been decremented by the indicated amount and is greater than or equal to 0.
     * @param time
     */
    public void decrementTimeRemaining(int time){
        timeRemaining -= time;
        if(timeRemaining < 0){
            timeRemaining = 0;
        }
    }

    /**
     * Brief:
     * Makes a new bid on this auction. If bidAmt is larger than currentBid,
     * then the value of currentBid is replaced by bidAmt and buyerName is is replaced by bidderName.
     * Preconditions:
     * The auction is not closed (i.e. timeRemaining > 0). Postconditions:
     * currentBid Reflects the largest bid placed on this object. If the auction is closed,
     * throw a ClosedAuctionException. Throws:
     * ClosedAuctionException: Thrown if the auction is closed and no more bids can be placed (i.e. timeRemaining == 0).
     * @param bidderName
     * @param bidAmt
     * @throws ClosedAuctionException
     */
    public void newBid(String bidderName, double bidAmt, String newBuyer) throws ClosedAuctionException{
        if (currentBid < bidAmt){
            currentBid = bidAmt;
            buyerName = newBuyer;
            System.out.println("Bid accepted.");
        } else {
            System.out.println("Your bid is not greater than the original bid.\n" +
                    "Bid is not accepted.");
        }
    }

    /**
     * returns string of data members in tabular form.
     * @return
     */
    public String toString(){
        if(sellerName.contains("\n")) {
            this.sellerName = sellerName.replaceAll("[\n]+", "");
        }
        if(itemInfo.contains("\n")){
            this.itemInfo = itemInfo.replaceAll("[\n]+","");
            this.itemInfo = itemInfo.replaceAll("[,]+"," ");
        }
        if (currentBid == 0){
            return String.format("%11s%2s%2s%9s%2s%-22s%2s%24s%2s%4s%6s%2s%-27s",
                    auctionID ,  "|",
                    "$" , "", " | ",
                    sellerName, " | ",
                    buyerName, " | ",
                    timeRemaining, "hours", " | ",
                    itemInfo)+"\n";
        }
        return String.format("%11s%2s%2s%9s%2s%-22s%2s%24s%2s%4s%6s%2s%-27s",
                                auctionID ,  "|",
                                "$" , currentBid, " | ",
                                sellerName, " | ",
                                buyerName, " | ",
                                timeRemaining, "hours", " | ",
                                itemInfo)+"\n";
    }

    /**
     * Getters for all data members (no setters).
     * @return
     */
    public double getCurrentBid() {
        return currentBid;
    }

//    public void setCurrentBid(double currentBid) {
//        this.currentBid = currentBid;
//    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

//    public void setTimeRemaining(int timeRemaining) {
//        this.timeRemaining = timeRemaining;
//    }

    public String getSellerName() {
        return sellerName;
    }

//    public void setSellerName(String sellerName) {
//        this.sellerName = sellerName;
//    }

    public String getBuyerName() {
        return buyerName;
    }

//    public void setBuyerName(String buyerName) {
//        this.buyerName = buyerName;
//    }

    public String getAuctionID() {
        return auctionID;
    }

//    public void setAuctionID(String auctionID) {
//        this.auctionID = auctionID;
//    }

    public String getItemInfo() {
        return itemInfo;
    }

//    public void setItemInfo(String itemInfo) {
//        this.itemInfo = itemInfo;
//    }
}
