package com.paypal.desk;

import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class PaypalDesk {
    private static Set<Integer> usersIdList;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Creating database and tables, please wait");
        for(int i=0; i<10; i++){
            Thread.sleep(250);
            System.out.print(".");
        }
        System.out.println();
        DbHelper.createDB();
        System.out.println("Welcome to paypal");
        System.out.println("Enter command");
        usersIdList = DbHelper.createUsersIdList();

        while (true) {
            System.out.println("(C) -> Create user");
            System.out.println("(UL) -> Users list");
            System.out.println("(+) -> Cash in");
            System.out.println("(-) -> Cash out");
            System.out.println("(T) -> Transaction");
            System.out.println("(TL) -> Transactions list");
            System.out.println("(Q) -> Quit");

            String command = scanner.nextLine();

            switch (command.toUpperCase()) {
                case "C":
                        createUser();
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

    private static void createUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        int userId = DbHelper.createUser(
                firstName, lastName
        );

        if (userId != -1) {
            usersIdList.add(userId);
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

        if(containsId(usersIdList, userId) && DbHelper.cashFlow(userId, amount)){
            System.out.println("Cash in successful");
        }else{
            System.out.println("Error cash in");
        }

    }

    private static void cashOut() {
        int userId = getUserIdFromConsole("User id: ");
        double amount = getAmountFromConsole();

        if(containsId(usersIdList, userId) && DbHelper.cashFlow(userId, -amount)){
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

       if(containsId(usersIdList, userFrom, userTo) && DbHelper.transaction(userFrom, userTo, amount)) {
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

    private static boolean containsId(Set<Integer> list, int... id){
        for(int i : id) {
            if (list.contains(i)) {
                continue;
            }else{
                System.out.println("Error - userId:" + i + " not found.");
                return false;
            }
        }
        return true;
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
