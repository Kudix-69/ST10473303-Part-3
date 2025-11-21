import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class MessageTest {

    @Test
    public void testStoreMessage() {
        Message msg = new Message("+27834557896", "Did you get the flag?");
        msg.storeMessage("Store Message");
        assertTrue(Message.getTotalMessages() > 0);
    }

    @Test
    public void testDisregardMessage() {
        Message msg = new Message("+27838884567", "Yoohoo, I am at your gate.");
        msg.storeMessage("Disregard Message");
        // No assert needed unless you expose disregardedMessages
    }

    @Test
    public void testSearchByRecipient() {
        Message msg = new Message("+27838884567", "It is dinner time!");
        msg.storeMessage("Store Message");
        List<String> results = Message.searchByRecipient("+27838884567");
        assertTrue(results.contains("It is dinner time!"));
    }

    @Test
    public void testSearchByMessageID() {
        Message msg = new Message("+27838884567", "OK, I am leaving without you.");
        msg.storeMessage("Store Message");
        String result = Message.searchByMessageID(msg.messageID);
        assertEquals("OK, I am leaving without you.", result);
    }

    @Test
    public void testSearchByHeading() {
        Message msg = new Message("+27838884567", "Where are you? You are late!");
        msg.storeMessage("Store Message");
        String result = Message.searchByHeading("Where");
        assertEquals("Where are you? You are late!", result);
    }

    @Test
    public void testSearchByHash() {
        Message msg = new Message("+27838884567", "Dinner is ready.");
        msg.storeMessage("Store Message");
        String result = Message.searchByHash(msg.messageHash);
        assertTrue(result.contains("Dinner is ready."));
    }

    @Test
    public void testDeleteByHash() {
        Message msg = new Message("+27838884567", "This will be deleted.");
        msg.storeMessage("Store Message");
        Message.deleteByHash(msg.messageHash);
        String result = Message.searchByHash(msg.messageHash);
        assertEquals("Hash not found.", result);
    }

    @Test
    public void testLongestMessage() {
        Message msg = new Message("+27838884567", "This is a very long message that should be the longest one in the test suite.");
        msg.storeMessage("Store Message");
        String longest = Message.getLongestMessage();
        assertEquals("This is a very long message that should be the longest one in the test suite.", longest);
    }

    @Test
    public void testLoadMessagesFromJson() {
        Message.loadMessagesFromJson();
        assertTrue(Message.getTotalMessages() >= 0); // Should not crash
    }
}