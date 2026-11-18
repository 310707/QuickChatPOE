package com.mycompany.chatapppart1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {
    
    // Variables to store registered user details
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhoneNumber;
    private String firstName;
    private String lastName;

    // Method 1: Check Username Criteria
    public boolean checkUserName(String username) {
        // Must contain an underscore and be no more than five characters long
        if (username != null && username.contains("_") && username.length() <= 5) {
            return true;
        }
        return false;
    }

    // Method 2: Check Password Complexity Rules
    public boolean checkPasswordComplexity(String password) {
        /*
         * Must be at least eight characters long.
         * Contain a capital letter.
         * Contain a number.
         * Contain a special character.
         */
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasCapital = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }

    // Method 3: Check Cell Phone Number using Regular Expressions
    // Research reference: Regular expression pattern for South African numbers with international code.
    // Adapted from standard Regex matching patterns for international phone validation.
    public boolean checkCellPhoneNumber(String cellNumber) {
        String regex = "^\\+27\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cellNumber);
        return matcher.matches();
    }

    // Method 4: Register User
    public String registerUser(String username, String password, String cellNumber, String fName, String lName) {
        boolean isUsernameValid = checkUserName(username);
        boolean isPasswordValid = checkPasswordComplexity(password);
        boolean isCellValid = checkCellPhoneNumber(cellNumber);

        if (!isUsernameValid) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        if (!isPasswordValid) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!isCellValid) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // If all checks pass, save the data
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhoneNumber = cellNumber;
        this.firstName = fName;
        this.lastName = lName;

        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    // Method 5: Login User Verification
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername != null && enteredPassword != null) {
            return enteredUsername.equals(registeredUsername) && enteredPassword.equals(registeredPassword);
        }
        return false;
    }

    // Method 6: Return Login Status Message
    
    public String returnLoginStatus(boolean isLoggedIn, String fName, String lName) {
        if (isLoggedIn) {
            return "Welcome " + fName + ", " + lName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
