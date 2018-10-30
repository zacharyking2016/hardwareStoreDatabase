/*
 * Hardware Store Management Software v0.1
 * Developed for CS3354: Object Oriented Design and Programming.
 * Copyright: Zachary King (zwk2@txstate.edu)
 */
package hardwarestoregui;

import hardwarestoregui.items.Item;
import hardwarestoregui.users.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.util.logging.*;
/**
 * This is the main class of the Hardware Store database manager. It provides a
 * console for a user to use the 5 main commands.
 *
 * @author Andrew Hyatt
 * @author Zachary King
 */
public class MainApp {

    // This object will allow us to interact with the methods of the class HardwareStore
    private final HardwareStore hardwareStore;
    private static final Scanner CONSOLE_INPUT = new Scanner(System.in); // Used to read from System's standard input
    private static final JFrame frame = new JFrame();
    private static final Logger logger = Logger.getLogger(MainApp.class.getName());
    /**
     * Default constructor. Initializes a new object of type HardwareStore
     *
     * @throws IOException
     */
    public MainApp() throws IOException {
        hardwareStore = new HardwareStore();
    }
    
    /**
     * This method initializes a file handler object for use in logging.
     */
    public static void init(){
        try {
            FileHandler handler = new FileHandler("loggerFile.log");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Could not create log handler...");
        }
    }
    
    //Function 1
    /**
     * This method shows all items in the inventory.
     */
    public void showAllItems() {
        // We need to sort the item list first
        logger.log(Level.INFO, "User selected to display items. Sorting list of items before displaying...");
        HardwareStore.sortItemList();
        logger.log(Level.INFO, "Finished sorting. Displaying items...");
        JOptionPane.showMessageDialog(frame, hardwareStore.getAllItemsFormatted());
        logger.log(Level.INFO, "Done displaying items.");
    }
    
    //Function 2
    /**
     * This method will add items quantity with given number. If the item does
     * not exist, it will call another method to add it.
     *
     */
    public void addItemQuantity() {
        String idNumber = "";
        logger.info("User selected to add an item to the list. User inputting ID...");
        while (true) {
            idNumber = JOptionPane.showInputDialog(
                frame, 
                "Please input the ID of item (String, 5 alphanumeric characters). If the item"
                + " does not exist, it will be added as a new entry.", 
                "Item Information", 
                JOptionPane.INFORMATION_MESSAGE);
            
            if (!idNumber.matches("[A-Za-z0-9]{5}")) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Invalid ID Number: not proper format. "
                        + "ID Number must be 5 alphanumeric characters.");
                logger.warning("Invalid ID entered.");
                continue;
            }
            break;
        }

        logger.info("Valid ID entered...");
        int itemIndex = hardwareStore.findItemIndex(idNumber);
        if (itemIndex != -1) { // If item exists in the database
            JOptionPane.showMessageDialog(
                    null,
                    "Item found in database");
            int quantity = 0;
            logger.info("Item found in database...");
            while (true) {
                try{
                    quantity = Integer.parseInt(JOptionPane.showInputDialog(
                    null, 
                    "Please enter quantity you want to add as an integer.", 
                    JOptionPane.INFORMATION_MESSAGE));
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(
                    null,
                    "Input must be an integer");
                    logger.severe("Invalid input, must be an integer.");
                    while (true) {
                        try{
                            quantity = Integer.parseInt(JOptionPane.showInputDialog(
                                   null, 
                                   "Please enter quantity you want to add as an integer.", 
                                   JOptionPane.WARNING_MESSAGE));
                            logger.severe("Invalid input, must be an integer.");
                        }
                        catch(Exception ex){
                            continue;
                        }
                        break;
                    }
                }
                if (quantity <= 0) {
                    System.out.println("Invalid quantity. "
                            + "The addition amount must be larger than 0.");
                    logger.severe("The quantity must be larger than zero.");
                    continue;
                }
                break;
            }

            logger.info("Adding given quantity...");
            hardwareStore.addQuantity(itemIndex, quantity);
            logger.info("Quantity successfully updated.");
        } else {
            // If it reaches here, the item does not exist. We need to add new one.
            logger.info("Item does not exist in database. Preparing to input name of the item.");
            String name = JOptionPane.showInputDialog(
                frame, 
                "Please type the name of the item.", 
                JOptionPane.INFORMATION_MESSAGE);
            logger.info("Name of item entered.");
            int quantity = 0;
            while (true) {
                try{
                    quantity = Integer.parseInt(JOptionPane.showInputDialog(
                    null, 
                    "Please enter quantity you want to add as an integer.", 
                    JOptionPane.INFORMATION_MESSAGE));
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(
                    null,
                    "Input must be an integer");
                    logger.severe("Quantity of the item must be an integer.");
                    while (true) {
                        try{
                            quantity = Integer.parseInt(JOptionPane.showInputDialog(
                                   null, 
                                   "Please enter quantity you want to add as an integer.", 
                                   JOptionPane.WARNING_MESSAGE));
                        }
                        catch(Exception ex){
                            logger.severe("Quantity of the item must be an integer.");
                            continue;
                        }
                        break;
                    }
                }

                if (quantity <= 0) {
                    System.out.println("Invalid quantity. "
                            + "The addition amount must be larger than 0.");
                    logger.severe("The addition amount entered must be larger than 0.");
                    continue;
                }
                break;
            }

            float price = 0;
            while (true) {
                try{
                    price = Float.parseFloat(JOptionPane.showInputDialog(
                    null, 
                    "Please type the price of the item (float).", 
                    JOptionPane.INFORMATION_MESSAGE));
                    if (price < 0){
                        JOptionPane.showMessageDialog(
                        null,
                        "Price cannot cost less than $0.00.");
                        logger.warning("Cost entered is less than zero, must be a positive float.");
                        continue;
                    }
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(
                    null,
                    "Input must be an float");
                    logger.severe("Input entered for price was not a float.");
                    continue;
                }
                break;
            }
            
            // Select item type
            while (true) {

                int selection = 0;
                try {
                    selection = Integer.parseInt(JOptionPane.showInputDialog(
                    null, 
                    "Please select the type of the item.\n 1: Small Hardware Items\n 2: Appliances", 
                    JOptionPane.INFORMATION_MESSAGE));
                    logger.info("User selecting type of item...");
                    switch (selection) {
                        case 1:
                            // Adding small hardware items
                            // Select category
                            String category = null;
                            try {
                                selection = Integer.parseInt(JOptionPane.showInputDialog(
                                            null, 
                                            "Please select the category of the item.\n" + 
                                            "1: Door&Window\n2: Cabinet&Furniture\n3: Fasteners\n4: Structural\n5: Other", 
                                            JOptionPane.INFORMATION_MESSAGE));
                                switch (selection) {
                                    case 1:
                                        category = "Door&Window";
                                        break;
                                    case 2:
                                        category = "Cabinet&Furniture";
                                        break;
                                    case 3:
                                        category = "Fasteners";
                                        break;
                                    case 4:
                                        category = "Structural";
                                        break;
                                    case 5:
                                        category = "Other";
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid input.");
                                        continue;
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Illegal input: Must input an integer.");
                                logger.severe("Invalid input entered, not of type integer.");
                                continue;
                            }
                            hardwareStore.addNewSmallHardwareItem(idNumber, name, quantity, price, category);
                            logger.info("Small HardwareItem successfully added to database.");
                            return;

                        case 2:
                            // Adding appliances
                            // Input brand
                            String brand = JOptionPane.showInputDialog(
                                            null, 
                                            "Please input the brand of appliance. (String)", 
                                            JOptionPane.INFORMATION_MESSAGE);
                            logger.info("User entered valid brand of item.");
                            // Select type
                            String type = null;
                            try {
                                selection = Integer.parseInt(JOptionPane.showInputDialog(
                                            null, 
                                            "Please select the type of appliance.\n" + 
                                            "1: Refrigerators\n2: Washers&Dryers\n3: Ranges&Ovens\n4: Small Appliance", 
                                            JOptionPane.INFORMATION_MESSAGE));
                                switch (selection) {
                                    case 1:
                                        type = "Refrigerators";
                                        break;
                                    case 2:
                                        type = "Washers&Dryers";
                                        break;
                                    case 3:
                                        type = "Ranges&Ovens";
                                        break;
                                    case 4:
                                        type = "Small Appliance";
                                        break;
                                    default:
                                        JOptionPane.showMessageDialog(null, "Invalid input.");
                                        continue;
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Illegal input: Must input an integer.");
                                logger.severe("Invalid input entered, not of type integer.");
                                continue;
                            }
                            logger.info("Type of appliance entered successfully...Adding new appliance...");
                            hardwareStore.addNewAppliance(idNumber, name, quantity, price, brand, type);
                            logger.info("New appliance successfully added to the database.");
                            return;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid input");
                            logger.severe("Invalid input entered.");
                            continue;
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Illegal input: Must input an integer.");
                    logger.severe("Invalid input entered, must be of type integer.");
                    continue;
                }
            }

        }

    }

    //Function 3
    /**
     * This method will remove the item with given ID.
     * If the item does not exist, it will show an appropriate message.
     */
    public void removeItem() {
        logger.info("User selected option 3, removing an item from the database.");
        String idNumber = JOptionPane.showInputDialog(
                frame, 
                "\033[31m" + "WARNING: This function will remove the whole item entry. Please use with caution!\n"
                + "\033[31m" + "Will return to main menu if you make any mistake inputting information!\n"
                + "\033[0m" + "Please input the ID of item.", 
                "Delete Item", 
                JOptionPane.INFORMATION_MESSAGE);
        if (!idNumber.matches("[A-Za-z0-9]{5}")) {
            JOptionPane.showMessageDialog(null, "Invalid ID Number: not proper format. "
                    + "ID Number must be at least 5 alphanumeric characters.\n"
                    + "Will return to main menu.");
            logger.warning("Inproper ID format entered.");
            return;
        }

        int itemIndex = hardwareStore.findItemIndex(idNumber);
        if (itemIndex == -1) {
            JOptionPane.showMessageDialog(null, "Item does not exist.\n"
                    + "Will return to main menu.");
            logger.warning("Item ID entered does not exist, nothing to delete.");
            return;
        } else {
            logger.info("ID found, waiting on user to confirm deletion...");
            String input = JOptionPane.showInputDialog(
                frame, 
                "\033[31m" + "Item found. Are you sure you want to remove the whole entry?\n"
                + "\033[31m" + "(Data cannot be recovered!)\n"
                + "\033[31m" + "Please type YES (all capitalized) to confirm deletion.\n", 
                "Delete Item", 
                JOptionPane.INFORMATION_MESSAGE);
            if (input.equals("YES")) {
                JOptionPane.showMessageDialog(null, "\033[0m" + "User typed " + input + ". Confirm: Item will be removed.");
                logger.info("User confirmed deletion, preparing to delete item...");
                hardwareStore.removeItem(itemIndex);
                 JOptionPane.showMessageDialog(null, "\033[0m" + "Item removed from inventory.");
                 logger.info("Item successfully removed.");
            } else {
                JOptionPane.showMessageDialog(null, "\033[0m" + "User typed " + input + ". Abort: Item will not be removed.");
                logger.info("User decided not to remove item from database, nothing changed.");
            }

        }
    }

    //Function 4
    /**
     * This method can search item by a given name (part of name.
     * Case-insensitive.) Will display all items with the given name.
     */
    public void searchItemByName() {
        logger.info("User selection option 4, searching for item in database.");
        String name = JOptionPane.showInputDialog(
                frame, 
                "Please input the name of item.",
                "Item Search", 
                JOptionPane.INFORMATION_MESSAGE);

        logger.info("Valid name entered, preparing to display matching items...");
        String output = hardwareStore.getMatchingItemsByName(name);
        if (output == null) {
            JOptionPane.showMessageDialog(null, "Item not found with: " + name + ".");
            logger.info("No item(s) to display with that name (or part of name).");
        } else {
            JOptionPane.showMessageDialog(null, output);
            logger.info("Item(s) that match that name (or part of name) displayed successfully.");
        }
    }

    //Function 5
    /**
     * This method shows all users in the system.
     */
    public void showAllUsers() {
        logger.info("User selected option 5, display all users in the database.");
        JOptionPane.showMessageDialog(null, hardwareStore.getAllUsersFormatted());
        logger.info("Users successfully displayed.");
    }    
    //Function 6
    /**
     * This method will add a user (customer or employee) to the system.
     *
     */
    public void addUser() {
        JFrame buttonFrame = new JFrame();
        
        // First select if you want to add customer or employee
            JRadioButton employeeButton = new JRadioButton("Employee");
            JRadioButton customerButton = new JRadioButton("Customer");
            
            employeeButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                // Add Employee
                String firstName = "";
                String lastName = "";
                firstName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the first name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                lastName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the last name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                int socialSecurityNumber = 0;
                while(true){
                    
                    try {
                       socialSecurityNumber=Integer.parseInt(JOptionPane.showInputDialog(
                       null, 
                       "Please input the SSN (9-digit integer, no other characters):", 
                       JOptionPane.INFORMATION_MESSAGE));
                   
                    } catch (Exception e) {
                        socialSecurityNumber=Integer.parseInt(JOptionPane.showInputDialog(
                        frame, 
                        "Please input the SSN (9-digit integer, no other characters):", 
                        JOptionPane.INFORMATION_MESSAGE));
                        logger.severe("Invalid input, must be 9-digit integer.");
                        while(true){
                            try{
                                socialSecurityNumber=Integer.parseInt(JOptionPane.showInputDialog(
                                null, 
                                 "Please input the SSN (9-digit integer, no other characters):", 
                                JOptionPane.INFORMATION_MESSAGE));
                                logger.severe("Invalid input, must be an integer.");
                            } catch(Exception ex){
                                continue;
                            }
                            break;
                        }
                            continue;
                }
                break;
                
                }
                if (socialSecurityNumber <= 100000000 || socialSecurityNumber > 999999999) {
                    JOptionPane.showMessageDialog(frame, "Invalid social security number. " +
                            "SSN is a 9-digit integer.");
                    logger.warning("Invalid SSN entered.");
                }
                float monthlySalary = 0;
                while(true)
                {
                    try {
                        monthlySalary=Float.parseFloat(JOptionPane.showInputDialog(
                        frame, 
                        "Please input the monthly salary (float):", 
                        JOptionPane.INFORMATION_MESSAGE));
                        if (monthlySalary < 0) {
                          JOptionPane.showMessageDialog(frame, "Invalid salary."
                                   + "It must be (at least) 0.");
                           logger.warning("Invalid salary entered.");
                                   continue;
                        }
                    }
                    catch (Exception e) {
                        monthlySalary=Float.parseFloat(JOptionPane.showInputDialog(
                        frame, 
                        "Please input the monthly salary (float):", 
                        JOptionPane.WARNING_MESSAGE));
                        logger.severe("Invalid input.");
                        continue;
                    }
                    break;
                }
                logger.info("Adding employee to database...");
                hardwareStore.addEmployee(firstName,lastName, socialSecurityNumber, monthlySalary);
                logger.info("Employee added to database.");
            }
         });
            
            customerButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                // Add Customer
                String firstName = "";
                String lastName = "";
                firstName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the first name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                lastName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the last name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                String phoneNumber=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the phone number (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                String address=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the address (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                logger.info("Adding customer to database...");
                hardwareStore.addCustomer(firstName, lastName, phoneNumber, address);
                logger.info("Customer added to database.");
            }
         }); 
            buttonFrame.setLayout(new BoxLayout(buttonFrame.getContentPane(), BoxLayout.PAGE_AXIS));

            buttonFrame.add(employeeButton);
            buttonFrame.add(customerButton);

            buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            buttonFrame.pack();
            buttonFrame.setVisible(true);
    }

    //Function 7
    /**
     * This method will edit a user (customer or employee).
     */
    public void editUser() {
        int idInput = 0;
        while (true) {
            try {
                idInput=Integer.parseInt(JOptionPane.showInputDialog(
                    frame, 
                    "Please input the ID of user.", 
                    JOptionPane.INFORMATION_MESSAGE));
            } catch (Exception e) {
                idInput=Integer.parseInt(JOptionPane.showInputDialog(
                    frame, 
                    "Illegal input: Must input an integer.", 
                    JOptionPane.INFORMATION_MESSAGE));
                logger.severe("Invalid input.");
                continue;
            }
            break;
        }


        User editUser = hardwareStore.findUser(idInput);
        if (editUser == null) {
            JOptionPane.showMessageDialog(frame, "User not found. "
                                + "Will return to main menu.");
            logger.warning("User not in database, returning ti main menu...");
            return;
        }
        String text = " -------------------------------------------------------------------------------------------------\n" +
                String.format("| %-10s| %-9s| %-12s| %-12s| %-45s|%n", "User Type", "User ID", "First Name", "Last Name", "Special") +
                " -------------------------------------------------------------------------------------------------\n";
        text += editUser.getFormattedText();
        text += " -------------------------------------------------------------------------------------------------\n";

        JOptionPane.showMessageDialog(frame, "Current user information: "
                                + text);
        String firstName = "";
        String lastName = "";
        if (editUser.isEmployee) {
            //User is employee
            logger.info("User is found as an employee.");
            int socialSecurityNumber = 0;
            float monthlySalary = 0;
            while (true) {
               firstName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the first name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                lastName=JOptionPane.showInputDialog(
                    frame, 
                    "Please input the last name (String):", 
                    JOptionPane.INFORMATION_MESSAGE);
                try {
                    socialSecurityNumber=Integer.parseInt(JOptionPane.showInputDialog(
                    frame, 
                    "Please input the SSN (9-digit integer, no other characters):", 
                    JOptionPane.INFORMATION_MESSAGE));
                    if (socialSecurityNumber <= 100000000 || socialSecurityNumber > 999999999) {
                        JOptionPane.showMessageDialog(frame, "Invalid social security number. " +
                                "SSN is a 9-digit integer.");
                        logger.warning("Invalid SSN entered.");
                            }
                } catch (Exception e) {
                    socialSecurityNumber=Integer.parseInt(JOptionPane.showInputDialog(
                    frame, 
                    "Please input the SSN (9-digit integer, no other characters):", 
                    JOptionPane.INFORMATION_MESSAGE));
                    logger.severe("Invalid input, SSN must be a 9-digit integer");
                        }
                try {
                    monthlySalary=Float.parseFloat(JOptionPane.showInputDialog(
                    frame, 
                    "Please input the monthly salary (float):", 
                    JOptionPane.INFORMATION_MESSAGE));
                    if (monthlySalary < 0) {
                        JOptionPane.showMessageDialog(frame, "Invalid salary."
                                + "It must be (at least) 0.");
                        logger.warning("Invalid salary entered.");
                            }
                } catch (Exception e) {
                    monthlySalary=Float.parseFloat(JOptionPane.showInputDialog(
                    frame, 
                    "Please input the monthly salary (float):", 
                    JOptionPane.INFORMATION_MESSAGE));
                    logger.severe("Invalid input.");
                    continue;
                }
                break;
            
            }
            logger.info("Adding new employee info to database...");
            hardwareStore.editEmployeeInformation(idInput, firstName,lastName, socialSecurityNumber, monthlySalary);
            logger.info("Employee info successfully added.");
            return;

        } else {
            //User is customer
            logger.info("User is found as a customer.");
            firstName=JOptionPane.showInputDialog(
                frame, 
                "Please input the first name (String):", 
                JOptionPane.INFORMATION_MESSAGE);
            lastName=JOptionPane.showInputDialog(
                frame, 
                "Please input the last name (String):", 
                JOptionPane.INFORMATION_MESSAGE);
            String phoneNumber=JOptionPane.showInputDialog(
                frame, 
                "Please input the phone number (String):", 
                JOptionPane.INFORMATION_MESSAGE);
            String address=JOptionPane.showInputDialog(
                frame, 
                "Please input the address (String):", 
                JOptionPane.INFORMATION_MESSAGE);
            logger.info("Adding customer info to database...");
            hardwareStore.addCustomer(firstName, lastName, phoneNumber, address);
            logger.info("Customer info successfully added to database.");
        return;
        }
    }

    //Function 8
    /**
     * This method will lead user to complete a transaction.
     */
    public void finishTransaction(){
        String itemID = "";
        int itemIndex = 0;
        int saleQuantity = 0;
        //Get the item ID. Will not break unless got a valid input.
        while (true) {
            itemID=JOptionPane.showInputDialog(
                frame, 
                "Please input the ID of item", 
                JOptionPane.INFORMATION_MESSAGE);
            itemIndex = hardwareStore.findItemIndex(itemID);
            if (itemIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Item not found. Will return to main menu.");
                logger.warning("Item not in database, returning to main menu.");
                return;
            } 
            else {
                Item tempItem = hardwareStore.findItem(itemID);
                logger.info("Item found in database.");
                try {
                    saleQuantity = Integer.parseInt(JOptionPane.showInputDialog(
                            frame, 
                            "Please input the amount of items sold in this transaction (int)" +
                            "Maximum number: " + tempItem.getQuantity(), 
                    JOptionPane.INFORMATION_MESSAGE));
                        if (saleQuantity <= 0) {
                            JOptionPane.showMessageDialog(frame, "Invalid input: must be greater than 0.");
                            logger.warning("Invalid input, quantity must be greater than 0.");
                            continue;
                        } 
                        else if (saleQuantity > tempItem.getQuantity()) {
                            JOptionPane.showMessageDialog(frame, "Invalid input: Number too big. Transaction cannot progress.");
                            logger.warning("Invalid input, can't sell more than in stock.");
                        continue;
                        }
                    break;

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Amount of items sold input invalid: not an integer");
                    logger.severe("Items sold must be an intefer");
                    continue;
                }
            }

        }

        //Get employee ID. Will check the input: must be a user, and employee.
        int employeeID = 0;
        while (true) {
            try {
                employeeID = Integer.parseInt(JOptionPane.showInputDialog(
                frame, 
                "Please input the id of the employee.", 
                JOptionPane.INFORMATION_MESSAGE));
                if (hardwareStore.findUserIndex(employeeID) == -1) {
                    JOptionPane.showMessageDialog(frame, "User not found.");
                    logger.warning("Not a user.");
                    continue;
                } else if (!hardwareStore.findUser(employeeID).isEmployee) {
                    JOptionPane.showMessageDialog(frame, "This user is not an employee.");
                    logger.warning("User not an employee.");
                }
                break;

            } catch (Exception e) {
                employeeID = Integer.parseInt(JOptionPane.showInputDialog(
                frame, 
                "Please input the id of the employee.", 
                JOptionPane.INFORMATION_MESSAGE));
                logger.severe("Invalid input, must be an integer.");
                continue;
            }
        }

        //Get customer ID. Will check the input: must be a user, and customer.
        int customerID = 0;
        while (true) {
            try {
                customerID = Integer.parseInt(JOptionPane.showInputDialog(
                frame, 
                "Please input the id of the customer.", 
                JOptionPane.INFORMATION_MESSAGE));
                if (hardwareStore.findUserIndex(customerID) == -1) {
                    JOptionPane.showMessageDialog(frame, "User not found.");
                    logger.warning("User not found.");
                    continue;
                } 
                else if (hardwareStore.findUser(customerID).isEmployee) {
                    JOptionPane.showMessageDialog(frame, "This user is not a customer.");
                    logger.warning("User is not a customer.");
                } 
                else {
                    break;
                }
            } catch (Exception e) {
                customerID = Integer.parseInt(JOptionPane.showInputDialog(
                frame, 
                "Please input the id of the employee.", 
                JOptionPane.INFORMATION_MESSAGE));
                logger.severe("Invalid input, must be an integer.");
                continue;
            }
        }
        logger.info("Completing transaction...");
        hardwareStore.progressTransaction(itemID, saleQuantity, customerID, employeeID, itemIndex);
        JOptionPane.showMessageDialog(frame, "Transaction complete.");
        logger.info("Transaction completed.");

    }

    //Function 9
    /**
     * This function will output a list of all transactions in database.
     */
    public void showAllTransactions(){
        logger.info("Printing transactions output...");
        JOptionPane.showMessageDialog(frame, hardwareStore.getAllTransactionsFormatted());
        logger.info("Transactions output successfully printed.");
    }

    //Function 10
    /**
     * These method is called to save the database before exit the system.
     * @throws IOException
     */
    public void saveDatabase() throws IOException {
        logger.info("Saving database...");
        hardwareStore.writeDatabase();
        JOptionPane.showMessageDialog(frame, "Database saved.");
        logger.info("Database saved.");
    }

    /**
     * This method will begin the user interface console. Main uses a loop to
     * continue executing commands until the user types '6'.
     *
     * @param args this program expects no command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        MainApp app = new MainApp();
        
        app.init();


      JRadioButton option1Button = new JRadioButton("Show all existing items records in the database (sorted by ID number).");

      option1Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.showAllItems();
            }
         });
      
      JRadioButton option2Button = new JRadioButton("Add new item (or quantity) to the database.");

      option2Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.addItemQuantity();
            }
         });
      
            JRadioButton option3Button = new JRadioButton("Delete an item from a database.");

      option3Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.removeItem();
            }
         });
      
      JRadioButton option4Button = new JRadioButton("Search for an item given its name.");

      option4Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.searchItemByName();
            }
         });
      
      JRadioButton option5Button = new JRadioButton("Show a list of users in the database.");

      option5Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.showAllUsers();
            }
         });

      
      JRadioButton option6Button = new JRadioButton("Add new user to the database.");

      option6Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.addUser();
            }
         });
      
      JRadioButton option7Button = new JRadioButton("Update user info (given their id).");

      option7Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.editUser();
            }
         });
      
      JRadioButton option8Button = new JRadioButton("Complete a sale transaction.");

      option8Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.finishTransaction();
            }
         });
      
      JRadioButton option9Button = new JRadioButton("Show completed sale transactions.");

      option9Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                app.showAllTransactions();
            }
         });
     
       JRadioButton option10Button = new JRadioButton("Save database.");
       //save database replaces option 10, since user can exit by exiting GUI.
      option10Button.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                try {
                    app.saveDatabase();
                } catch (IOException ex) {
                    logger.severe("Unable to save database.");
                }
            }
         });

      frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

      frame.add(option1Button);
      frame.add(option2Button);
      frame.add(option3Button);
      frame.add(option4Button);
      frame.add(option5Button);
      frame.add(option6Button);
      frame.add(option7Button);
      frame.add(option8Button);
      frame.add(option9Button);
      frame.add(option10Button);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    }
}