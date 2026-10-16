import Facade.AdminFacade;
import Facade.RestaurantFacade;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RestaurantFacade restaurant = new RestaurantFacade();
        AdminFacade admin = new AdminFacade();
        boolean running = true;

        System.out.println("----------------------------");
        System.out.println("     RESTAURANT KITCHEN");
        System.out.println("----------------------------");

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Place normal order");
            System.out.println("2. Kitchen failure");
            System.out.println("3. Payment failure");
            System.out.println("4. Show saved data");
            System.out.println("5. Exit");
            System.out.println("6. Resume order");

            System.out.print("\nEnter option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Enter order ID: ");
                    int normalId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter dish name: ");
                    String normalDish = scanner.nextLine();

                    restaurant.placeOrder(normalId, normalDish, false, false
                    );

                    break;

                case "2":
                    System.out.print("Enter order ID: ");
                    int kitchenId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter dish name: ");
                    String kitchenDish = scanner.nextLine();

                    restaurant.placeOrder(kitchenId, kitchenDish, true, false
                    );

                    break;

                case "3":
                    System.out.print("Enter order ID: ");
                    int paymentId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter dish name: ");
                    String paymentDish = scanner.nextLine();

                    restaurant.placeOrder(paymentId, paymentDish, false, true);
                    break;

                case "4":
                    admin.showAllData();
                    break;

                    case "5":
                    running = false;
                    System.out.println("\nRestaurant application closed."
                    );
                    break;

                case "6":
                    System.out.print("Enter order ID: ");
                    int resumeId = Integer.parseInt(scanner.nextLine());
                    restaurant.resumeOrder(resumeId);

                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}