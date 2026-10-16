package tests;

import Facade.ChainRepository;
import Facade.ChainState;
import Facade.RestaurantFacade;
import Facade.AdminFacade;

import SubsystemA.Order;
import SubsystemA.OrderRepository;
import SubsystemA.InventoryChecker;

import SubsystemB.CookingTask;
import SubsystemB.KitchenRepository;
import SubsystemB.StationAllocator;

import SubsystemC.Delivery;
import SubsystemC.DeliveryRepository;
import SubsystemC.BillingModule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantFacadeTest {
    // 1. Kitchen failure
    @Test
    public void kitchenFailureShouldCompensateOrder() {
        RestaurantFacade restaurant = new RestaurantFacade();
        restaurant.placeOrder(
                100,
                "Pizza",
                true,
                false
        );
        TestHelper.assertChainStatus(
                100,
                ChainState.Status.COMPENSATED);
    }

    // 2. Payment failure
    @Test
    public void paymentFailureShouldCompensateKitchenAndOrder() {
        RestaurantFacade restaurant = new RestaurantFacade();
        restaurant.placeOrder(
                101,
                "Pasta",
                false,
                true
        );
        TestHelper.assertChainStatus(
                101,
                ChainState.Status.COMPENSATED);
    }

    // 3. Normal successful order
    @Test
    public void normalOrderShouldBeCompleted() {
        RestaurantFacade restaurant = new RestaurantFacade();
        restaurant.placeOrder(
                102,
                "Burger",
                false,
                false
        );
        ChainRepository repository = new ChainRepository();

        ChainState state = repository.findByOrderId(102);
        assertEquals(ChainState.Status.COMPLETED, state.getStatus());
    }


    // 4. Persistence / reload
    @Test
    public void savedStateShouldBeAvailableAfterReload() {
        RestaurantFacade firstRestaurant = new RestaurantFacade();
        firstRestaurant.placeOrder(
                103,
                "Pizza",
                false,
                false
        );

        // New objects simulate application restart
        RestaurantFacade secondRestaurant = new RestaurantFacade();
        ChainRepository newRepository = new ChainRepository();
        ChainState loadedState = newRepository.findByOrderId(103);

        assertEquals(ChainState.Status.COMPLETED, loadedState.getStatus());
        secondRestaurant.resumeOrder(103);
    }


    @Test
    public void orderRepositoryShouldSaveAndLoad() {
        OrderRepository repository = new OrderRepository();
        Order order = new Order(200, "Burger");

        order.accept();
        repository.save(order);
        Order loaded = repository.findById(200);

        assertNotNull(loaded);
        assertEquals(200, loaded.getId());
        assertEquals("Burger", loaded.getDishName());

        assertEquals(Order.Status.ACCEPTED, loaded.getStatus());
    }

    @Test
    public void kitchenRepositoryShouldSaveAndLoad() {
        KitchenRepository repository = new KitchenRepository();
        CookingTask task = new CookingTask(201);

        task.startCooking();
        repository.save(task);
        CookingTask loaded = repository.findByOrderId(201);

        assertNotNull(loaded);
        assertEquals(201, loaded.getOrderId());
        assertEquals(CookingTask.Status.COOKING, loaded.getStatus());
    }

    @Test
    public void deliveryRepositoryShouldSaveAndLoad() {
        DeliveryRepository repository = new DeliveryRepository();
        Delivery delivery = new Delivery(202);

        delivery.dispatch();
        repository.save(delivery);

        Delivery loaded = repository.findByOrderId(202);

        assertNotNull(loaded);
        assertEquals(202, loaded.getOrderId());
        assertEquals(Delivery.Status.DISPATCHED, loaded.getStatus());
    }

    @Test
    public void subsystemServicesShouldWork() {
        InventoryChecker inventory = new InventoryChecker();
        assertTrue(inventory.isAvailable("Pizza"));

        assertFalse(inventory.isAvailable("Unavailable"));
        assertFalse(inventory.isAvailable(""));

        StationAllocator station = new StationAllocator();
        assertTrue(station.allocate(false));
        assertFalse(station.allocate(true));

        BillingModule billing = new BillingModule();
        assertTrue(
                billing.processPayment(false));
        assertFalse(billing.processPayment(true));
    }

    @Test
    public void adminFacadeShouldShowData() {
        AdminFacade admin = new AdminFacade();
        assertDoesNotThrow(
                admin::showAllData
        );
    }

    @Test
    public void unavailableDishShouldNotCreateChain() {
        RestaurantFacade restaurant = new RestaurantFacade();
        restaurant.placeOrder(
                999,
                "Unavailable",
                false,
                false
        );

        ChainRepository repository = new ChainRepository();
        assertNull(repository.findByOrderId(999));
    }
}