
import java.util.*;
import javax.swing.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.json.simple.JSONObject;


public class Message {
    private static Set<String> usedIDs = new HashSet<>();
    private static List<String> sentMessages = new ArrayList<>();
    private static List<String> disregardedMessages = new ArrayList<>();
    private static List<String> messageHeadings = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static Map<String, Message> messageMap = new HashMap<>();

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(String recipient, String messageText) {
        this.messageID = generateUniqueID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        messageMap.put(messageID, this);
        messageHashes.add(messageHash);
        messageHeadings.add(getHeading());
    }

    private String generateUniqueID() {
        String id;
        do {
            id = String.valueOf(new Random().nextInt(900000000) + 100000000);
        } while (usedIDs.contains(id));
        usedIDs.add(id);
        return id;
    }

    public boolean checkMessageID() {
        return messageID != null && !messageID.isEmpty();
    }

    public boolean checkRecipientCell() {
        return recipient.matches("^\\+27\\d{9}$");
    }

    public String validateMessageLength() {
        if (messageText.length() > 250) {
            return "Message exceeds 250 characters by " + (messageText.length() - 250);
        }
        return "Message sent";
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String firstTen = messageText.length() >= 10 ? messageText.substring(0, 10) : messageText;
        return (firstTwo + ":" + firstTen).toUpperCase();
    }

    public void printMessageDetails() {
        String formatted = "Message ID: " + messageID +
                           "\nMessage Hash: " + messageHash +
                           "\nRecipient: " + recipient +
                           "\nMessage: " + messageText;
        JOptionPane.showMessageDialog(null, formatted, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void storeMessage(String option) {
        if (option.equalsIgnoreCase("Store Message")) {
            sentMessages.add(messageText);
            saveToJson();
        } else if (option.equalsIgnoreCase("Disregard Message")) {
            disregardedMessages.add(messageText);
        } else if (option.equalsIgnoreCase("Store Message to send later")) {
            saveToJson();
        }
    }

    private void saveToJson() {
        JSONObject obj = new JSONObject();
        obj.put("MessageID", messageID);
        obj.put("MessageHash", messageHash);
        obj.put("Recipient", recipient);
        obj.put("Message", messageText);

        JSONArray messageList = new JSONArray();
        messageList.add(obj);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(messageList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLongestMessage() {
        return sentMessages.stream().max(Comparator.comparingInt(String::length)).orElse("No messages sent.");
    }

    public static List<String> searchByRecipient(String number) {
        List<String> results = new ArrayList<>();
        for (Message msg : messageMap.values()) {
            if (msg.recipient.equals(number)) {
                results.add(msg.messageText);
            }
        }
        return results;
    }

    public static String searchByMessageID(String id) {
        Message msg = messageMap.get(id);
        return (msg != null) ? msg.messageText : "Message ID not found.";
    }

    public static String searchByHeading(String heading) {
        for (Message msg : messageMap.values()) {
            if (msg.getHeading().equalsIgnoreCase(heading)) {
                return msg.messageText;
            }
        }
        return "Heading not found.";
    }

    public static String searchByHash(String hash) {
        for (Message msg : messageMap.values()) {
            if (msg.messageHash.equalsIgnoreCase(hash)) {
                return "Message ID: " + msg.messageID +
                       "\nRecipient: " + msg.recipient +
                       "\nMessage: " + msg.messageText;
            }
        }
        return "Hash not found.";
    }

    public static void deleteByHash(String hash) {
        messageMap.values().removeIf(msg -> msg.messageHash.equalsIgnoreCase(hash));
        messageHashes.remove(hash);
    }

    public static void displayReport() {
        for (Message msg : messageMap.values()) {
            System.out.println("Message Hash: " + msg.messageHash);
            System.out.println("Recipient: " + msg.recipient);
            System.out.println("Message: " + msg.messageText);
            System.out.println("---------------------------");
        }
    }

    private String getHeading() {
        return messageText.split(" ")[0]; // First word as heading
    }

    public static int getTotalMessages() {
        return sentMessages.size();
    }
}