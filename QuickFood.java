import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;

/*
 * This class implements the Customer and Restaurant classes
 * to take customer information and orders and to pair them
 * with a driver in the same area, if possible.
 * Outputs a receipt with all necessary information
 * once the process is finished
 */
public class QuickFood {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String args[]) throws IOException {

		// Location array for dropdown menus
		String[] locations = new String[] { "Cape Town", "Durban", "Johannesburg", "Potchefstroom", "Springbok",
				"Bloemfontein", "Port Elizabeth", "Witbank" };

		// JFrame showing input fields
		JFrame container = new JFrame("Order Details");
		container.getContentPane().setBackground(new Color(255, 255, 255));
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container.setBounds(500, 500, 500, 500);
		container.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));

		// Customer's details
		JTextField custName = new JTextField("Enter your name");

		// Add customer detail fields to container
		container.getContentPane().add(custName);
		JTextField custNum = new JTextField("Enter your phone number");
		container.getContentPane().add(custNum);
		JTextField custMail = new JTextField("Enter your email address");
		container.getContentPane().add(custMail);
		JTextField custAddress = new JTextField("Enter delivery address");
		container.getContentPane().add(custAddress);

		JLabel lblSelectYourLocation = new JLabel("      Select your location:    ");
		lblSelectYourLocation.setHorizontalAlignment(SwingConstants.RIGHT);
		container.getContentPane().add(lblSelectYourLocation);

		// Dropdown for customer location
		JComboBox custLocation = new JComboBox(locations);
		custLocation.setBounds(50, 50, 90, 20);
		container.getContentPane().add(custLocation);

		// Divider
		JLabel lblooo = new JLabel("_____________________________");
		lblooo.setHorizontalAlignment(SwingConstants.RIGHT);
		container.getContentPane().add(lblooo);

		// Divider
		JLabel lblooo_1 = new JLabel("_____________________________");
		lblooo_1.setHorizontalAlignment(SwingConstants.LEFT);
		container.getContentPane().add(lblooo_1);

		// Restaurant details
		JTextField restName = new JTextField("Enter restaurant name");

		// Add restaurant detail fields to container
		container.getContentPane().add(restName);
		JTextField restNum = new JTextField("Enter restaurant number");
		container.getContentPane().add(restNum);

		JLabel lblSelectTheRestaurant = new JLabel("      Select the restaurant location:    ");
		lblSelectTheRestaurant.setHorizontalAlignment(SwingConstants.RIGHT);
		container.getContentPane().add(lblSelectTheRestaurant);

		// Dropdown for restaurant location
		JComboBox restLocation = new JComboBox(locations);
		restLocation.setBounds(50, 50, 90, 20);
		container.getContentPane().add(restLocation);

		// Divider
		JLabel label_5 = new JLabel("_____________________________");
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		container.getContentPane().add(label_5);

		// Divider
		JLabel label_6 = new JLabel("_____________________________");
		label_6.setHorizontalAlignment(SwingConstants.LEFT);
		container.getContentPane().add(label_6);

		// Order details
		JTextField orderItem = new JTextField("Enter the item you want");

		// Add order detail fields to container
		container.getContentPane().add(orderItem);
		JTextField orderAmount = new JTextField("Enter item amount");
		container.getContentPane().add(orderAmount);
		JTextField orderPrice = new JTextField("Enter the price of the item (ex. 12.00)");
		container.getContentPane().add(orderPrice);
		JTextField instructions = new JTextField("Enter any special instructions for the order");
		container.getContentPane().add(instructions);

		// Divider
		JLabel label_4 = new JLabel("_____________________________");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		container.getContentPane().add(label_4);

		// Divider
		JLabel label_7 = new JLabel("_____________________________");
		label_7.setHorizontalAlignment(SwingConstants.LEFT);
		container.getContentPane().add(label_7);

		// Empty label to separate content
		JLabel label_1 = new JLabel("");
		container.getContentPane().add(label_1);

		// This button executes the ticket creation process on click
		JButton btnCreateOrderTicket = new JButton("Create order ticket");

		btnCreateOrderTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Create customer object from text fields
				Customer customer = new Customer(custName.getText(), custNum.getText(), custMail.getText(),
						custAddress.getText(), custLocation.getSelectedItem().toString());

				// Create restaurant object from text fields
				Restaurant restaurant = new Restaurant(restName.getText(), restNum.getText(),
						restLocation.getSelectedItem().toString());

				// Calculate price
				Double totalPrice = Double.parseDouble(orderPrice.getText())
						* Double.parseDouble(orderAmount.getText());
				totalPrice = Math.round(totalPrice * 100.0) / 100.0;

				// Run createReceipt function
				createReceipt(customer, restaurant, orderAmount.getText(), orderItem.getText(), totalPrice,
						instructions.getText());
			}
		});
		// Add above button to the container
		container.getContentPane().add(btnCreateOrderTicket);

		// Container is visible
		container.setVisible(true);
	}

	// Handles the creation of the receipt
	public static void createReceipt(Customer customer, Restaurant restaurant, String orderAmount, String orderItem,
			Double totalPrice, String instructions) {

		// ArrayList to store driver information from drivers.txt
		ArrayList<String> driverDetails = new ArrayList<>();

		try {
			// Reading drivers.txt
			File drivers = new File("./drivers.txt");
			Scanner reader = new Scanner(drivers);
			while (reader.hasNextLine()) {
				driverDetails.add(reader.nextLine()); // Adding each line of drivers.txt to driverDetails ArrayList
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String restLoc = restaurant.getLocation(); // Restaurant location
		String custLoc = customer.getLocation(); // Customer location

		// If no drivers are found, this will be printed in invoice.txt instead of the
		// receipt
		String noDriver = "Sorry! Our drivers are too far away from you to be able to deliver to your location.";

		ArrayList<String> loadCheck = new ArrayList<>(); // ArrayList to check driver load

		/*
		 * Loop through driverDetails to find driver with least load Each line is split
		 * into the driver's location, and load
		 */
		for (String line : driverDetails) {
			String[] lineArr = line.split(",");
			String driverLocation = lineArr[1].trim();
			String driverLoadForCheck = lineArr[2].trim();

			/*
			 * If the restaurant and the customer are in the same location, find driver with
			 * least load
			 */
			if (restLoc.equals(custLoc) && driverLocation.equals(restLoc)) {
				loadCheck.add(driverLoadForCheck); // Add driverLoadForCheck to loadCheck
			}
		}

		// Loop through driverLoad and find the smallest number
		int smallest = Integer.MAX_VALUE;
		for (String load : loadCheck) {
			int loadInt = Integer.parseInt(load);
			if (loadInt < smallest) {
				smallest = loadInt;
			}
		}
		
		// Calling findDriver
		findDriver(driverDetails, restLoc, custLoc, smallest, customer, restaurant, orderAmount, orderItem,
				instructions, totalPrice);
		
		/*
		 * if findDriver returns false (No driver found)
		 * print noDriver to invoice.txt
		 */
		if (!findDriver(driverDetails, restLoc, custLoc, smallest, customer, restaurant, orderAmount, orderItem,
				instructions, totalPrice)) {
			try {
				// Create invoice.txt file
				File invoice = new File("invoice.txt");
				invoice.createNewFile();

				FileWriter invWriter = new FileWriter(invoice);
				invWriter.write(noDriver); // Print receipt to invoice.txt
				invWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Printing receipt");
			System.exit(0);
		}

	}
	
	/*
	 * method will loop through each line of driverDetails
	 * line is split into 3 parts
	 * assign the driver with the least load (using smallest) to the order
	 * 
	 */
	public static Boolean findDriver(ArrayList<String> driverDetails, String restLoc, String custLoc, int smallest,
			Customer customer, Restaurant restaurant, String orderAmount, String orderItem, String instructions,
			double totalPrice) {
		for (String line : driverDetails) {
			String[] lineArr = line.split(",");
			String driverName = lineArr[0].trim();
			String driverLocation = lineArr[1].trim();
			String driverLoad = lineArr[2].trim();

			// Assign order to driver with the least load and print receipt
			if (Integer.parseInt(driverLoad) == smallest && restLoc.equals(custLoc) && driverLocation.equals(restLoc)) {

				int orderNum = 1; // Order number

				// Receipt to be printed in invoice.txt
				String receipt = "Order Number " + orderNum + "\n" + "Customer: " + customer.name + "\n" + "Email: "
						+ customer.mail + "\n" + "Phone number: " + customer.num + "\n" + "Location: "
						+ customer.location + "\n\n" + "You have ordered the following from " + restaurant.name + " in "
						+ restaurant.location + ":\n\n" + orderAmount + "x " + orderItem + "\n\n"
						+ "Special instructions: " + instructions + "\n\n" + "Total: R" + totalPrice + "\n\n"
						+ driverName
						+ " is nearest to the restaurant and so they will be delivering your order to you at:\n\n"
						+ customer.address + "\n" + customer.location + "\n\n"
						+ "If you need to contact the restaurant, their number is " + restaurant.num + ".";

				try {
					// Create invoice.txt file
					File invoice = new File("invoice.txt");
					invoice.createNewFile();

					FileWriter invWriter = new FileWriter(invoice);
					invWriter.write(receipt); // Print receipt to invoice.txt
					invWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Printing receipt");
				return true;
			}
		}
		return false;

	}
}