package SubsystemB;

public class CookingTask {
    public enum Status {
        NEW,
        COOKING,
        CANCELLED
    }
    private int orderId;
    private Status status;

    public CookingTask(int orderId) {
        this.orderId = orderId;
        this.status = Status.NEW;
    }
    public CookingTask(int orderId, Status status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }
    public Status getStatus() {
        return status;
    }

    public void startCooking() {
        if (status != Status.NEW) {
            throw new IllegalStateException("Cooking cannot be started.");
        }
        status = Status.COOKING;
    }
    public void cancel() {
        status = Status.CANCELLED;
    }
}