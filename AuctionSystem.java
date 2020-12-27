/**
 * @author Luye Ge
 * ID# 111857836
 *
 * Write a fully-documented class named AuctionSystem. This class will allow the user to interact with the database by
 * listing open auctions, make bids on open auctions, and create new auctions for different items.
 * In addition, the class should provide the functionality to load a saved (serialized) AuctionTable
 * or create a new one if a saved table does not exist.
 * On startup, the AuctionSystem should check to see if the file auctions.obj exists in the current directory.
 * If it does, then the file should be loaded and deserialized into an AuctionTable for new auctions/bids
 * (See the section below for information on the Serializable interface).
 * If the file does not exist, an empty AuctionTable object should be created and used instead.
 * Next, the user should be promted to enter a username to access the system.
 * This is the name that will be used to create new auctions and bid on the open auctions available in the table.
 * When the user enters 'Q' to quit the program,
 * the acution table should be serialized to the file auctions.obj.
 * That way, the next time the program is run,
 * the auctions will remain in the database and allow different users to make bids on items.
 * If you would like to 'reset' the auction table, simply delete the auctions.obj file.
 *
 */
package com.company;

import big.data.DataSourceException;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class AuctionSystem {
    /**
     * The method should first prompt the user for a username.
     * This should be stored in username The rest of the program will be executed on behalf of this user.
     * Implement the following menu options:
     *     (D) ­ Import Data from URL
     *     (A) ­ Create a New Auction
     *     (B) ­ Bid on an Item
     *     (I) ­ Get Info on Auction
     *     (P) ­ Print All Auctions
     *     (R) ­ Remove Expired Auctions
     *     (T) ­ Let Time Pass
     *     (Q) ­ Quit
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, FileNotFoundException, ClosedAuctionException {
        AuctionTable auctionTable = new AuctionTable();
        Scanner input = new Scanner(System.in);
        String id;
        int hours;
        String info;
        System.out.println("Starting...");
        try {
            FileInputStream file = new FileInputStream("auction.obj");
            ObjectInputStream inStream = new ObjectInputStream(file);
            auctionTable = (AuctionTable) inStream.readObject();
            System.out.println("Loading previous Auction Table...\n");
            System.out.println("Please select a username: ");
        }catch (FileNotFoundException cnfe){
            System.out.println("No previous auction table detected.\n" +
                    "Creating new table...\n" +
                    "Please select a username: ");
        }

        String username = input.next();
        //Auction auction = new Auction();
        //System.out.println();
        boolean flag = true;
        while (flag){
            System.out.println("Menu: ");
            System.out.println(
                    "     (D) \u00AD Import Data from URL\n" +
                    "     (A) \u00AD Create a New Auction\n" +
                    "     (B) \u00AD Bid on an Item\n" +
                    "     (I) \u00AD Get Info on Auction\n" +
                    "     (P) \u00AD Print All Auctions\n" +
                    "     (R) \u00AD Remove Expired Auctions\n" +
                    "     (T) \u00AD Let Time Pass\n" +
                    "     (Q) \u00AD Quit");
            System.out.println();
            System.out.println("Please select an option: ");
            String op = input.next().toUpperCase();
            try {
                switch (op) {
                    case ("D"):
                        try {
                            System.out.println("Please enter a URL: ");
                            String url = input.next();
                            auctionTable.putAll(AuctionTable.buildFromURL(url));
                        } catch (DataSourceException dse){
                            System.out.println("Please check the URL is correct.");
                            input.nextLine();
                        }
                        //auctionTable = AuctionTable.buildFromURL(url);
                        break;
                    case ("A"):
                        System.out.println("Creating new Auction as " + username);
                        System.out.println("Please enter an Auction ID: ");
                        id = input.next();
                        System.out.println("Please enter an Auction time (hours): ");
                        hours = input.nextInt();
                        System.out.println("Please enter some Item Info: ");
                        info = input.next();
                        Auction auction = new Auction(id, 0, username, "", hours, info);
                        auctionTable.putAuction(id, auction);
                        System.out.println("Auction " + id + " inserted into table.");
                        break;
                    case ("B"):
                        System.out.println("Please enter an Auction ID: ");
                        String secondOptionId = input.next();
                        if (auctionTable.containsKey(secondOptionId)) {
                            if (auctionTable.get(secondOptionId).getTimeRemaining() > 0) {
                                System.out.println("Auction " + secondOptionId + "is OPEN\n" +
                                        "    Current Bid: $ " + auctionTable.get(secondOptionId).getCurrentBid() + " \n");
                                System.out.println("What would you like to bid?: ");
                                double tempBid = input.nextDouble();
                                auctionTable.get(secondOptionId).newBid(secondOptionId, tempBid, username);

                            } else {
                                System.out.println("Auction " + secondOptionId + "is Closed\n");
                            }
                        } else {
                            System.out.println(secondOptionId + " not exist.");
                        }
                        break;
                    case ("I"):
                        System.out.println("Please enter an Auction ID: ");
                        id = input.next();
                        auctionTable.getAuction(id, auctionTable);
                        break;
                    case ("P"):
                        auctionTable.printTable(auctionTable);
                        break;
                    case ("R"):
                        auctionTable.removeExpiredAuctions();
                        break;
                    case ("T"):
                        System.out.println("How many hours should pass: ");
                        int timePass = input.nextInt();
                        auctionTable.letTimePass(timePass);
                        System.out.println("Time passing...\n" +
                                "Auction times updated.");
                        break;
                    case ("Q"):
                        flag = false;
                        break;
                    default:
                        System.out.println("ERROR: Option not Exist.");
                }
            }catch (InputMismatchException ime){
                System.out.println("Please enter a correct input.");
                input.nextLine();
            }
        }
        FileOutputStream file = new FileOutputStream("auction.obj");
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        outStream.writeObject(auctionTable);
        System.out.println("Writing Auction Table to file...\n" +
                "Done!\n" +
                "Goodbye.");
    }
}
