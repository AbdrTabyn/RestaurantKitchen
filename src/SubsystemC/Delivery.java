package SubsystemC;

public class Delivery {
    public enum Status {
        NEW,
        DISPATCHED,
        CANCELLED
    }

    private int orderId;
    private Status status;

    public Delivery(int orderId) {
        this.orderId = orderId;
        this.status = Status.NEW;
    }
    public Delivery(int orderId, Status status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }
    public Status getStatus() {
        return status;
    }
    public void dispatch() {
        status = Status.DISPATCHED;
    }
    public void cancel() {
        status = Status.CANCELLED;
    }
}