package com.mycompany.chatapppart1;

import java.util.Scanner;

public class ChatAppPart1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter First Name: ");
        String fName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lName = scanner.nextLine();

        System.out.print("Enter Username (must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();

        System.out.print("Enter Password (>= 8 chars, 1 capital, 1 number, 1 special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
        String cellNumber = scanner.nextLine();

        String regResult = loginSystem.registerUser(username, password, cellNumber, fName, lName);
        System.out.println(regResult);

        // If registration is successful, prompt for Login
        if (regResult.contains("successfully")) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Enter Username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter Password: ");
            String loginPassword = scanner.nextLine();

            boolean isLoggedIn = loginSystem.loginUser(loginUsername, loginPassword);
            String statusMessage = loginSystem.returnLoginStatus(isLoggedIn, fName, lName);
            System.out.println(statusMessage);

            if (isLoggedIn) {
                // PART 2: Task Management Menu
                System.out.println("\nWelcome to EasyKanban.");
                boolean running = true;
                int totalHours = 0;

                while (running) {
                    System.out.println("\nChoose an option:");
                    System.out.println("1. Add tasks");
                    System.out.println("2. Show upcoming features (Coming soon)");
                    System.out.println("3. Quit");
                    System.out.print("Enter choice: ");
                    
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 1:
                            System.out.print("How many tasks would you like to enter? ");
                            int numTasks = scanner.nextInt();
                            scanner.nextLine();

                            for (int i = 0; i < numTasks; i++) {
                                System.out.println("\n--- Entering Task " + i + " ---");
                                System.out.print("Enter Task Name: ");
                                String taskName = scanner.nextLine();

                                System.out.print("Enter Task Description (max 50 chars): ");
                                String description = scanner.nextLine();

                                Task tempTask = new Task(taskName, i, description, "DummyDev", 0, "To Do");
                                while (!tempTask.checkTaskDescription(description)) {
                                    System.out.print("Please enter a task description of less than 50 characters: ");
                                    description = scanner.nextLine();
                                }

                                System.out.print("Enter Developer Details (First & Last name): ");
                                String devDetails = scanner.nextLine();

                                System.out.print("Enter Task Duration (in hours): ");
                                int duration = scanner.nextInt();
                                scanner.nextLine();

                                System.out.print("Select Task Status (1. To Do, 2. Done, 3. Doing): ");
                                int statusChoice = scanner.nextInt();
                                scanner.nextLine();
                                String status = "To Do";
                                if (statusChoice == 2) status = "Done";
                                else if (statusChoice == 3) status = "Doing";

                                Task task = new Task(taskName, i, description, devDetails, duration, status);
                                totalHours += duration;

                                System.out.println("\nTask successfully captured.");
                                System.out.println(task.printTaskDetails());
                            }
                            System.out.println("\nTotal combined hours of all tasks: " + totalHours + " hrs");
                            break;
                        case 2:
                            System.out.println("Coming Soon!");
                            break;
                        case 3:
                            running = false;
                            System.out.println("Exiting application. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid option, please try again.");
                    }
                }
            }
        }
        
        scanner.close();
    }
}