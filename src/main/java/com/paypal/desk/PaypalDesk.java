package com.paypal.desk;

import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class PaypalDesk {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Creating database and tables, please wait");
        for(int i=0; i<7; i++){
            Thread.sleep(200);
            System.out.print(".");
        }
        System.out.println();
        DbHelper.createDB();
        System.out.println("Welcome to paypal");
        System.out.println("Enter command");

        while (true) {
            System.out.println("(C) -> Create user");
            System.out.println("(F) -> Find user");
            System.out.println("(UL) -> Users list");
            System.out.println("(+) -> Cash in");
            System.out.println("(-) -> Cash out");
            System.out.println("(T) -> Transaction");
            System.out.println("(TL) -> Transactions list");
            System.out.println("(D) -> DROP TABLES AND CREATE NEW");
            System.out.println("(Q) -> Quit");

            String command = scanner.nextLine();

            switch (command.toUpperCase()) {
                case "D":
                    dropTables();
                    break;
                case "C":
                    createUser();
                    break;
                case "F":
                    findUser();
                    break;
                case "UL":
                    listUsers();
                    break;
                case "+":
                    cashIn();
                    break;
                case "-":
                    cashOut();
                    break;
                case "T":
                    transaction();
                    break;
                case "TL": transactionList();
                    break;
                case "Q":
                    return;
            }
        }
    }

    private static void dropTables(){
        System.out.print("Are you sure? Y: ");
        String s = scanner.nextLine().toUpperCase();

        if(s.equals("Y") && DbHelper.dropTables()){
            System.out.println("Tables have been dropped");
        } else {
            System.out.println("Canceled");
        }
    }

    private static void createUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        int userId = DbHelper.createUser(
                firstName, lastName
        );

        if (userId != -1) {
            System.out.println(
                    MessageFormat.format(
                            "User {0} created successfully",
                            userId
                    )
            );
        } else {
            System.out.println(
                    "Error while creating the user"
            );
        }
    }

    public static void findUser(){
        int userId = getUserIdFromConsole("User id: ");
        String userString;

        if(DbHelper.containsId(userId)&& (userString=DbHelper.findUser(userId))!=null){

            System.out.println(userString);
        }else {
            System.out.println("Error while finding");
        }
    }

    private static void listUsers() {
        List<User> users = DbHelper.listUsers();
        if (users.isEmpty()) {
            System.out.println("Users list is empty");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void cashIn() {
        int userId = getUserIdFromConsole("User id: ");
        double amount = getAmountFromConsole();

        if(DbHelper.containsId(userId) && DbHelper.cashFlow(userId, amount)){
            System.out.println("Cash in successful");
        }else{
            System.out.println("Error cash in");
        }

    }

    private static void cashOut() {
        int userId = getUserIdFromConsole("User id: ");
        double amount = getAmountFromConsole();

        if(DbHelper.containsId(userId) && DbHelper.cashFlow(userId, -amount)){
            System.out.println("Cash out successful");
        }else{
            System.out.println("Error cash out");
        }

    }

    private static void transaction() {
        int userFrom = getUserIdFromConsole(
                "User id: "
        );
        int userTo = getUserIdFromConsole(
                "Target user id: "
        );

        double amount = getAmountFromConsole();

       if(DbHelper.containsId(userFrom, userTo) && DbHelper.transaction(userFrom, userTo, amount)) {
           System.out.println("Transaction successful");

       }else
           System.out.println("Transaction error");
    }

    private static void transactionList(){
        List<Transaction> list = DbHelper.transactionList();
        if(list.isEmpty()){
            System.out.println("Transactions list is empty");
            return;
        }

        for(Transaction transaction : list){
            System.out.println(transaction);
        }
    }

    private static int getUserIdFromConsole(String message) {
        System.out.print(message);
        return Integer.parseInt(
                scanner.nextLine()
        );
    }

    private static double getAmountFromConsole() {
        System.out.println("Amount: ");
        return Double.parseDouble(
                scanner.nextLine()
        );
    }
}
