package Facade;

public class ChainState {
    public enum Status {
        PENDING_B,
        PENDING_C,
        COMPLETED,
        COMPENSATED
    }

    private int orderId;
    private Status status;

    public ChainState(int orderId, Status status) {
        this.orderId = orderId;
        this.status = status;
    }
    public int getOrderId() {
        return orderId;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}