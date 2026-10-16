package Facade;

import SubsystemA.InventoryChecker;
import SubsystemA.Order;
import SubsystemA.OrderRepository;

import SubsystemB.CookingTask;
import SubsystemB.StationAllocator;
import SubsystemB.KitchenRepository;

import SubsystemC.BillingModule;
import SubsystemC.Delivery;
import SubsystemC.DeliveryRepository;

public class RestaurantFacade {
    private final InventoryChecker inventoryChecker;
    private final OrderRepository orderRepository;
    private final StationAllocator stationAllocator;
    private final KitchenRepository kitchenRepository;
    private final BillingModule billingModule;
    private final DeliveryRepository deliveryRepository;
    private final ChainRepository chainRepository;

    public RestaurantFacade() {
        inventoryChecker = new InventoryChecker();
        orderRepository = new OrderRepository();
        stationAllocator = new StationAllocator();
        kitchenRepository = new KitchenRepository();
        billingModule = new BillingModule();
        deliveryRepository = new DeliveryRepository();
        chainRepository = new ChainRepository();
    }

    public void placeOrder(
            int orderId,
            String dishName,
            boolean kitchenFailure,
            boolean paymentFailure
    ) {

        System.out.println("\n=== NEW ORDER ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Dish: " + dishName);
        System.out.println("\n[A] Checking inventory...");

        if (!inventoryChecker.isAvailable(dishName)) {
            System.out.println("Dish is not available.");
            System.out.println("Order was not created.");
            return;
        }

        Order order = new Order(orderId, dishName);
        order.accept();
        orderRepository.save(order);
        System.out.println("Order accepted.");

        // Save chain progress
        ChainState chainState = new ChainState(orderId, ChainState.Status.PENDING_B);
        chainRepository.save(chainState);

        System.out.println("\n[B] Sending order to kitchen...");
        CookingTask cookingTask = new CookingTask(orderId);
        boolean kitchenAvailable = stationAllocator.allocate(kitchenFailure);

        // B FAILED
        if (!kitchenAvailable) {
            System.out.println("Kitchen failed.");
            System.out.println("No cooking station available.");

            // COMPENSATE A
            System.out.println("Rolling back A...");
            order.cancel();
            orderRepository.save(order);
            chainState.setStatus(ChainState.Status.COMPENSATED);

            chainRepository.save(chainState);
            System.out.println("Order cancelled.");
            System.out.println("Compensation completed.");
            return;
        }

        // B SUCCESS
        cookingTask.startCooking();
        kitchenRepository.save(cookingTask);
        System.out.println("Food prepared successfully.");

        chainState.setStatus(ChainState.Status.PENDING_C);
        chainRepository.save(chainState);
        System.out.println("\n[C] Processing payment...");
        boolean paymentSuccessful = billingModule.processPayment(paymentFailure);

        // C FAILED
        if (!paymentSuccessful) {
            System.out.println("Payment failed.");

            // COMPENSATE B
            System.out.println("Rolling back B...");
            cookingTask.cancel();
            kitchenRepository.save(cookingTask);
            System.out.println("Cooking task cancelled.");

            // COMPENSATE A
            System.out.println("Rolling back A...");
            order.cancel();
            orderRepository.save(order);
            System.out.println("Order cancelled.");

            // Save final chain state
            chainState.setStatus(ChainState.Status.COMPENSATED);
            chainRepository.save(chainState);
            System.out.println("Compensation completed.");
            return;
        }
        System.out.println("Payment successful.");

        Delivery delivery = new Delivery(orderId);
        delivery.dispatch();
        deliveryRepository.save(delivery);

        System.out.println("Order sent for delivery.");

        chainState.setStatus(ChainState.Status.COMPLETED);
        chainRepository.save(chainState);
        System.out.println("\nOrder completed successfully!");
    }
    public void resumeOrder(int orderId) {
        System.out.println("\n=== RESUME ORDER ===");
        System.out.println("Order ID: " + orderId);

        // Читаем состояние цепочки с диска
        ChainState chainState = chainRepository.findByOrderId(orderId);

        if (chainState == null) {
            System.out.println("Order not found.");
            return;
        }
        System.out.println("Saved state: " + chainState.getStatus());

        if (chainState.getStatus() == ChainState.Status.COMPLETED) {
            System.out.println("Order is already completed.");
            return;
        }

        if (chainState.getStatus() == ChainState.Status.COMPENSATED) {

            System.out.println("Order was already compensated.");
            return;
        }

        if (chainState.getStatus() == ChainState.Status.PENDING_B) {

            System.out.println("Resuming from kitchen...");
            CookingTask cookingTask = new CookingTask(orderId);
            boolean kitchenAvailable = stationAllocator.allocate(false);

            if (!kitchenAvailable) {
                System.out.println("Kitchen is unavailable.");
                return;
            }

            cookingTask.startCooking();
            kitchenRepository.save(cookingTask);
            System.out.println("Food prepared successfully.");

            chainState.setStatus(ChainState.Status.PENDING_C);
            chainRepository.save(chainState);
        }

        if (chainState.getStatus() == ChainState.Status.PENDING_C) {

            System.out.println("Resuming from payment...");

            boolean paymentSuccessful = billingModule.processPayment(false);

            if (!paymentSuccessful) {
                System.out.println("Payment failed.");
                return;
            }

            System.out.println("Payment successful.");
            Delivery delivery = new Delivery(orderId);

            delivery.dispatch();
            deliveryRepository.save(delivery);
            System.out.println("Order sent for delivery.");

            chainState.setStatus(ChainState.Status.COMPLETED);
            chainRepository.save(chainState);
            System.out.println("Order completed successfully!");
        }
    }
}