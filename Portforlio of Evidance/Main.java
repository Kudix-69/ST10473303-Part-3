
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to QuickChat.");

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter SA cell phone number (+27...): ");
        String cellPhone = input.nextLine();

        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        Login user = new Login(username, password, cellPhone, firstName, lastName);
        System.out.println(user.registerUser());

        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrect. Please do not contain international code.");
        }

        System.out.print("Login - Enter username: ");
        String loginUsername = input.nextLine();

        System.out.print("Login - Enter password: ");
        String loginPassword = input.nextLine();

        System.out.println(user.returnLoginStatus(loginUsername, loginPassword));

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Send Message");
            System.out.println("2) Search by Recipient");
            System.out.println("3) Search by Message ID");
            System.out.println("4) Search by Heading");
            System.out.println("5) Search by Hash");
            System.out.println("6) Delete by Hash");
            System.out.println("7) Display Longest Message");
            System.out.println("8) Display Report");
            System.out.println("0) Quit");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.print("Enter recipient number (+27...): ");
                    String recipient = input.nextLine();

                    System.out.print("Enter message: ");
                    String messageText = input.nextLine();

                    Message msg = new Message(recipient, messageText);

                    System.out.println(msg.validateMessageLength());
                    if (!msg.checkRecipientCell()) {
                        System.out.println("Cell phone number is incorrectly formatted or does not contain enough digits.");
                    } else {
                        System.out.println("Cell phone number is correctly formatted.");
                    }

                    msg.printMessageDetails();

                    System.out.println("Choose:\n1) Store Message\n2) Disregard Message\n3) Store Message to send later");
                    String option = input.nextLine();
                    msg.storeMessage(option);
                    break;

                case 2:
                    System.out.print("Enter recipient number to search: ");
                    String searchRecipient = input.nextLine();
                    for (String m : Message.searchByRecipient(searchRecipient)) {
                        System.out.println(m);
                    }
                    break;

                case 3:
                    System.out.print("Enter Message ID: ");
                    String id = input.nextLine();
                    System.out.println(Message.searchByMessageID(id));
                    break;

                case 4:
                    System.out.print("Enter Message Heading: ");
                    String heading = input.nextLine();
                    System.out.println(Message.searchByHeading(heading));
                    break;

                case 5:
                    System.out.print("Enter Message Hash: ");
                    String hash = input.nextLine();
                    System.out.println(Message.searchByHash(hash));
                    break;

                case 6:
                    System.out.print("Enter Hash to delete: ");
                    String deleteHash = input.nextLine();
                    Message.deleteByHash(deleteHash);
                    System.out.println("Message deleted (if it existed).");
                    break;

                case 7:
                    System.out.println("Longest Message: " + Message.getLongestMessage());
                    break;

                case 8:
                    Message.displayReport();
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Total messages sent: " + Message.getTotalMessages());
    }
}