package tests;

import Facade.ChainRepository;
import Facade.ChainState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestHelper {
    public static void assertChainStatus(
            int orderId,
            ChainState.Status expectedStatus
    ) {

        ChainRepository repository = new ChainRepository();
        ChainState state = repository.findByOrderId(orderId);

        assertNotNull(state);
        assertEquals(expectedStatus, state.getStatus());
    }
}