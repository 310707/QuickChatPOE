package com.mycompany.chatapppart1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageManagerTest {

    private MessageManager manager;
    private Message msg1, msg2, msg3, msg4, msg5;

    @BeforeEach
    public void setUp() {
        manager = new MessageManager();

        msg1 = new Message(0, "Developer", "+27834557896", "Did you get the cake?");
        manager.addMessage(msg1, "Sent");

        msg2 = new Message(1, "Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        manager.addMessage(msg2, "Stored");

        msg3 = new Message(2, "Developer", "+27834484567", "Yohoooo, I am at your gate.");
        manager.addMessage(msg3, "Disregard");

        msg4 = new Message(3, "Developer", "0838884567", "It is dinner time !");
        msg4.setMessageID("0838884567");
        manager.addMessage(msg4, "Sent");

        msg5 = new Message(4, "Developer", "+27838884567", "Ok, I am leaving without you.");
        manager.addMessage(msg5, "Stored");
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, manager.sentCount);
        assertEquals("Did you get the cake?", manager.sentMessages[0]);
        assertEquals("It is dinner time !", manager.sentMessages[1]);
    }

    @Test
    public void testDisplayLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expected, manager.getLongestMessage());
    }

    @Test
    public void testSearchForMessageID() {
        String expected = "It is dinner time !";
        assertEquals(expected, manager.getMessageTextByID("0838884567"));
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        String expected = "Where are you? You are late! I have asked you to be on time.\n"
                        + "Ok, I am leaving without you.";
        assertEquals(expected, manager.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String hashToDelete = msg2.getMessageHash();
        String expected = "Message \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        assertEquals(expected, manager.deleteByMessageHash(hashToDelete));
    }

    @Test
    public void testDisplayReport() {
        String report = manager.displaySentReport();
        assertTrue(report.contains("Message Hash: " + msg1.getMessageHash()));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message Hash: " + msg4.getMessageHash()));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time !"));
    }
}