package com.mycompany.chatapppart1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageManager {
    private static final int MAX_SIZE = 100;

    public String[] sentMessages = new String[MAX_SIZE];
    public String[] sentRecipients = new String[MAX_SIZE];
    public String[] sentSenders = new String[MAX_SIZE];
    public String[] sentHashes = new String[MAX_SIZE];
    public String[] sentIDs = new String[MAX_SIZE];
    public int sentCount = 0;

    public String[] disregardedMessages = new String[MAX_SIZE];
    public int disregardedCount = 0;

    public String[] storedMessages = new String[MAX_SIZE];
    public String[] storedRecipients = new String[MAX_SIZE];
    public String[] storedSenders = new String[MAX_SIZE];
    public String[] storedHashes = new String[MAX_SIZE];
    public String[] storedIDs = new String[MAX_SIZE];
    public int storedCount = 0;

    public String[] messageHashes = new String[MAX_SIZE];
    public String[] messageIDs = new String[MAX_SIZE];
    public String[] allMessages = new String[MAX_SIZE];
    public String[] allRecipients = new String[MAX_SIZE];
    public String[] allSenders = new String[MAX_SIZE];
    public String[] allFlags = new String[MAX_SIZE];
    public int totalCount = 0;

    public void addMessage(Message msg, String flag) {
        msg.setFlag(flag);

        allMessages[totalCount] = msg.getMessageText();
        allRecipients[totalCount] = msg.getRecipient();
        allSenders[totalCount] = msg.getSender();
        messageHashes[totalCount] = msg.getMessageHash();
        messageIDs[totalCount] = msg.getMessageID();
        allFlags[totalCount] = flag;
        totalCount++;

        if (flag.equalsIgnoreCase("Sent")) {
            sentMessages[sentCount] = msg.getMessageText();
            sentRecipients[sentCount] = msg.getRecipient();
            sentSenders[sentCount] = msg.getSender();
            sentHashes[sentCount] = msg.getMessageHash();
            sentIDs[sentCount] = msg.getMessageID();
            sentCount++;
        } else if (flag.equalsIgnoreCase("Disregard")) {
            disregardedMessages[disregardedCount] = msg.getMessageText();
            disregardedCount++;
        } else if (flag.equalsIgnoreCase("Stored")) {
            storedMessages[storedCount] = msg.getMessageText();
            storedRecipients[storedCount] = msg.getRecipient();
            storedSenders[storedCount] = msg.getSender();
            storedHashes[storedCount] = msg.getMessageHash();
            storedIDs[storedCount] = msg.getMessageID();
            storedCount++;
        }
    }

    public void loadStoredMessagesFromJSON(String filePath) {
        try {
            if (!Files.exists(Paths.get(filePath))) return;
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            if (content.trim().isEmpty()) return;
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id = obj.getString("messageID");
                String sender = obj.optString("sender", "Developer");
                String recipient = obj.getString("recipient");
                String text = obj.getString("messageText");
                String hash = obj.getString("messageHash");

                storedMessages[storedCount] = text;
                storedRecipients[storedCount] = recipient;
                storedSenders[storedCount] = sender;
                storedHashes[storedCount] = hash;
                storedIDs[storedCount] = id;
                storedCount++;

                allMessages[totalCount] = text;
                allRecipients[totalCount] = recipient;
                allSenders[totalCount] = sender;
                messageHashes[totalCount] = hash;
                messageIDs[totalCount] = id;
                allFlags[totalCount] = "Stored";
                totalCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    public String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalCount; i++) {
            if (!allFlags[i].equalsIgnoreCase("Disregard")) {
                sb.append("Sender: ").append(allSenders[i])
                  .append(" | Recipient: ").append(allRecipients[i]).append("\n");
            }
        }
        return sb.length() > 0 ? sb.toString().trim() : "No messages found.";
    }

    public String getLongestMessage() {
        String longest = "";
        for (int i = 0; i < totalCount; i++) {
            if (!allFlags[i].equalsIgnoreCase("Disregard")) {
                if (allMessages[i] != null && allMessages[i].length() > longest.length()) {
                    longest = allMessages[i];
                }
            }
        }
        return longest;
    }

    public String searchByMessageID(String searchID) {
        for (int i = 0; i < totalCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(searchID)) {
                return "Recipient: " + allRecipients[i] + "\nMessage: " + allMessages[i];
            }
        }
        return "Message ID not found.";
    }

    public String getMessageTextByID(String searchID) {
        for (int i = 0; i < totalCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(searchID)) {
                return allMessages[i];
            }
        }
        return "Message ID not found.";
    }

    public String searchByRecipient(String recipientNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalCount; i++) {
            if (!allFlags[i].equalsIgnoreCase("Disregard") && allRecipients[i].equals(recipientNumber)) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(allMessages[i]);
            }
        }
        return sb.length() > 0 ? sb.toString() : "No messages found for recipient: " + recipientNumber;
    }

    public String deleteByMessageHash(String hashToDelete) {
        for (int i = 0; i < totalCount; i++) {
            if (messageHashes[i] != null && messageHashes[i].equalsIgnoreCase(hashToDelete)) {
                String deletedText = allMessages[i];

                for (int j = i; j < totalCount - 1; j++) {
                    allMessages[j] = allMessages[j + 1];
                    allRecipients[j] = allRecipients[j + 1];
                    allSenders[j] = allSenders[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    allFlags[j] = allFlags[j + 1];
                }
                totalCount--;
                removeFromSentOrStored(hashToDelete);

                return "Message \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    private void removeFromSentOrStored(String hash) {
        for (int i = 0; i < sentCount; i++) {
            if (sentHashes[i] != null && sentHashes[i].equalsIgnoreCase(hash)) {
                for (int j = i; j < sentCount - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                    sentRecipients[j] = sentRecipients[j + 1];
                    sentSenders[j] = sentSenders[j + 1];
                    sentHashes[j] = sentHashes[j + 1];
                    sentIDs[j] = sentIDs[j + 1];
                }
                sentCount--;
                break;
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedHashes[i] != null && storedHashes[i].equalsIgnoreCase(hash)) {
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    storedRecipients[j] = storedRecipients[j + 1];
                    storedSenders[j] = storedSenders[j + 1];
                    storedHashes[j] = storedHashes[j + 1];
                    storedIDs[j] = storedIDs[j + 1];
                }
                storedCount--;
                break;
            }
        }
    }

    public String displaySentReport() {
        if (sentCount == 0) return "No sent messages to display.";
        StringBuilder report = new StringBuilder("=== SENT MESSAGES REPORT ===\n");
        for (int i = 0; i < sentCount; i++) {
            report.append("Message Hash: ").append(sentHashes[i]).append("\n")
                  .append("Recipient: ").append(sentRecipients[i]).append("\n")
                  .append("Message: ").append(sentMessages[i]).append("\n")
                  .append("----------------------------\n");
        }
        return report.toString();
    }
}