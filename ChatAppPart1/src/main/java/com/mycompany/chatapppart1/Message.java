package com.mycompany.chatapppart1;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

public class Message {
    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // "Sent", "Stored", or "Disregard"

    public Message(int messageNumber, String sender, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() == 10;
    }

    public boolean checkRecipientCell() {
        if (recipient == null) return false;
        return recipient.startsWith("+27") && recipient.length() == 12 && recipient.substring(1).matches("\\d+");
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    public String createMessageHash() {
        String idPrefix = messageID.length() >= 2 ? messageID.substring(0, 2) : "00";
        String cleaned = messageText.trim().replaceAll("[^a-zA-Z0-9\\s]", "");
        String[] words = cleaned.split("\\s+");

        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 0 ? words[words.length - 1].toUpperCase() : "";

        return idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public void storeMessageToJSON(String filePath) {
        try {
            JSONArray jsonArray;
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                jsonArray = content.trim().isEmpty() ? new JSONArray() : new JSONArray(content);
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject obj = new JSONObject();
            obj.put("messageID", this.messageID);
            obj.put("sender", this.sender);
            obj.put("recipient", this.recipient);
            obj.put("messageText", this.messageText);
            obj.put("messageHash", this.messageHash);
            obj.put("flag", "Stored");

            jsonArray.put(obj);

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonArray.toString(4));
            }
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
}