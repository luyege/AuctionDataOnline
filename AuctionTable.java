/**
 * @author Luye Ge
 * ID# 111857836
 *
 * The database of open auctions will be stored in a hash table to provide constant time insertion and deletion.
 * Use the auctionID as the key for the corresponding Auction object. In this assignment,
 * you may provide your own implementation for the AuctionTable class,
 * or you may use a hash table implementation provided by the Java API.
 * If you would like to know more about the Java API implementations,
 * you should read the Orcale documentation for java.util.Hashtable and java.util.HashMap.
 *             Decreases the time remaining for this auction by the specified amount.
 *             If time is greater than the current remaining time for the auction,
 *             then the time remaining is set to 0 (i.e. no negative times).
 *      Makes a new bid on this auction. If bidAmt is larger than currentBid,
 *      then the value of currentBid is replaced by bidAmt and buyerName is is replaced by bidderName.
 * In addition to the standard hash table functionality,
 * your class should include a function which will build an AuctionTable from a URL using the BigData library.
 * This function should connect to the data source located at the URL, read in all the data members for each record
 * stored in the data source, construct new Auction objects based on the data, and insert the objects in to the table.
 * Exceptions should be thrown if the data source could not be connected to or contains invalid syntax structure.
 */
package com.company;

import java.io.Serializable;
import java.util.HashMap;
import big.data.DataSource;
import big.data.DataSourceException;

public class AuctionTable extends HashMap<String, Auction> implements Serializable {

    /**
     * Brief:
     * Uses the BigData library to construct an AuctionTable from a remote data source.
     * Parameters:
     * URL - String representing the URL fo the remote data source.
     * Preconditions:
     * URL represents a data source which can be connected to using the BigData library.
     * The data source has proper syntax.
     * Postconditions:
     * None.
     * Returns:
     * The AuctionTable constructed from the remote data source. Throws:
     * IllegalArgumentException: Thrown if the URL does not represent a valid datasource (can't connect or invalid syntax).
     * @param URL
     * @return
     * @throws IllegalArgumentException
     */
    // D
    public static AuctionTable buildFromURL(String URL) throws IllegalArgumentException, DataSourceException {

        AuctionTable table = new AuctionTable();
        DataSource ds = DataSource.connect(URL).load();
        String[] sellerNames = ds.fetchStringArray("listing/seller_info/seller_name");
        String[] tempCurrentBids = ds.fetchStringArray("listing/auction_info/current_bid");
        String[] stringBids = new String[tempCurrentBids.length];
        double[] currentBids = new double[tempCurrentBids.length];
        for(int i = 0; i < tempCurrentBids.length; i ++){
            stringBids[i] = tempCurrentBids[i].replaceAll("[$,]+","");
            currentBids[i] = Double.parseDouble(stringBids[i]);
        }

        String[] tempTimeLeft = ds.fetchStringArray("listing/auction_info/time_left");
        String[] days = new String[tempTimeLeft.length];
        String[] hours = new String[tempTimeLeft.length];
        int[] finalTimeLeft = new int[tempTimeLeft.length];
        int[] intTempTimeLeftOfDays = new int[tempTimeLeft.length];
        int[] intTempTimeLeftOfHours = new int[tempTimeLeft.length];
//        for(int i = 0; i < tempTimeLeft.length; i++){
//            if(tempTimeLeft[i].contains(","))
//        }
        if(tempTimeLeft[0].contains(",")) {
            for (int i = 0; i < tempTimeLeft.length; i++) {
                String tempDays1 = tempTimeLeft[i].replaceAll("[days,hours+]+", "");
                String[] tempDays2 = tempDays1.split("[ ]+");
                days[i] = tempDays2[0];
                hours[i] = tempDays2[1];
                intTempTimeLeftOfDays[i] = Integer.parseInt(days[i]);
                intTempTimeLeftOfHours[i] = Integer.parseInt(hours[i]);
                intTempTimeLeftOfDays[i] *= 24;
                finalTimeLeft[i] = intTempTimeLeftOfDays[i] + intTempTimeLeftOfHours[i];
            }
        } else {
            for(int i = 0; i < tempTimeLeft.length; i++){
                if(tempTimeLeft[i].contains("days")){
                    String tempDay1 = tempTimeLeft[i].replaceAll("[days]+","");
                    String[] tempDays2 = tempDay1.split("[ ]+");
                    days[i] = tempDays2[0];

                    intTempTimeLeftOfDays[i] = Integer.parseInt(days[i]);
                    intTempTimeLeftOfDays[i] *= 24;
                    finalTimeLeft[i] = intTempTimeLeftOfDays[i];
                }
                if(tempTimeLeft[i].contains("hours")){
                    String tempHours1 = tempTimeLeft[i].replaceAll("[hours]+","");
                    String[] tempHours2 = tempHours1.split("[ ]+");
                    hours[i] = tempHours2[0];
                    intTempTimeLeftOfHours[i] = Integer.parseInt(hours[i]);
                    finalTimeLeft[i] = intTempTimeLeftOfHours[i];
                }
            }
        }
        String[] idNum = ds.fetchStringArray("listing/auction_info/id_num");
        String[] bidderName = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");
        String[] memory = ds.fetchStringArray("listing/item_info/memory");
        String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
        String[] hard_drive = ds.fetchStringArray("listing/item_info/hard_drive");
        String[] cpuMemory = new String[cpu.length];
        for(int i = 0; i < cpu.length; i++){
            cpuMemory[i] = cpu[i] + hard_drive[i] + memory[i];
        }
        for(int i = 0; i < sellerNames.length; i++){
            Auction auction = new Auction(idNum[i], currentBids[i], sellerNames[i],
                    bidderName[i], finalTimeLeft[i], cpuMemory[i]);
            table.put(idNum[i], auction);

        }
        System.out.println("Loading...\n" +
                "Auction data loaded successfully!\n");
        return table;
    }

    /**
     * Brief:
     * Manually posts an auction, and add it into the table.
     * Parameters:
     *             auctionID ­ the unique key for this object
     *             auction ­ The auction to insert into the table with the corresponding auctionID
     * Preconditions:
     * None.
     * Postconditions:
     * The item will be added to the table if all given parameters are correct.
     * Throws:
     * IllegalArgumentException: If the given auctionID is already stored in the table.
     * @param auctionID
     * @param auction
     * @throws IllegalArgumentException
     */
    public void putAuction(String auctionID, Auction auction) throws IllegalArgumentException{
        super.put(auctionID,auction);
    }

    /**
     * Brief:
     * Get the information of an Auction that contains the given ID as key
     * Parameters:
     *             auctionID ­ the unique key for this object
     * Returns:
     * An Auction object with the given key, null otherwise.
     * @param auctionID
     * @return
     */
    // I
    public Auction getAuction(String auctionID, AuctionTable table){

        if(table.containsKey(auctionID)) {
            System.out.println("Auction " + table.get(auctionID).getAuctionID() + ": " +
                    "\n     Seller: " + table.get(auctionID).getSellerName() +
                    "\n     Buyer: " + table.get(auctionID).getBuyerName() +
                    "\n     Time: " + table.get(auctionID).getTimeRemaining() +
                    "\n     Info: " + table.get(auctionID).getItemInfo());
        }else {
            System.out.println(auctionID + " dose not exist.");
        }
        return table.get(auctionID);
    }

    /**
     * Brief:
     * Simulates the passing of time.
     * Decrease the timeRemaining of all Auction objects by the amount specified.
     * The value cannot go below 0.
     *
     * Parameters:
     *             numHours ­ the number of hours to decrease the timeRemaining value by.
     * Postconditions:
     * All Auctions in the table have their timeRemaining timer decreased.
     * If the original value is less than the decreased value, set the value to 0.
     * Throws:
     * IllegalArgumentException: If the given numHours is non positive
     * @param numHours
     * @throws IllegalArgumentException
     */
    public void letTimePass(int numHours) throws IllegalArgumentException{
        for(Auction a : this.values())  {
           a.decrementTimeRemaining(numHours);
        }
    }

    /**
     * Brief:
     *     Iterates over all Auction objects in the table and removes them if they are expired (timeRemaining == 0).
     *     Postconditions:
     *     Only open Auction remain in the table.
     */
    public void removeExpiredAuctions(){
        String removeList = "";
        for(Auction a : this.values()){
            if(a.getTimeRemaining() == 0){
                removeList += " " + a.getAuctionID();
            }
        }
        String[] finalRemoveList = removeList.split("[ ]+");
        for(int i = 0; i < finalRemoveList.length; i++) {
            //System.out.println(finalRemoveList[i]);
            super.remove(finalRemoveList[i]);
        }
        System.out.println("Removing expired auctions...\n" +
                "All expired auctions removed.\n");
    }

    /**
     * Brief:
     * Prints the AuctionTable in tabular form.
     */
    // P
    public void printTable(HashMap<String, Auction> hashMap){
        System.out.println(String.format("%13s%13s%25s%27s%13s%28s",
                "Auction ID |", "Bid   |", "Seller         |",
                "Buyer          |", "Time   |", "  Item Info                " +
                        "\n================================================" +
                        "===================================================================\n"));
        if(hashMap.isEmpty()){
            System.out.println("Table is empty.");
        }else {
                    Object[] a =  hashMap.values().toArray()  ;
                    for( Object aucObj : a){
                        Auction auc = (Auction) aucObj;
                        System.out.println( auc);
                    }

        }
    }

}
