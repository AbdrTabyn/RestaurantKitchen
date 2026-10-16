package SubsystemC;

public class BillingModule {
    public boolean processPayment(boolean forceFailure) {
        if (forceFailure) {
            return false;
        }
        return true;
    }
}