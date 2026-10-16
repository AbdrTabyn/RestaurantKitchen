package SubsystemA;

public class Order {
    public enum Status {
        NEW,
        ACCEPTED,
        CANCELLED
    }
    private int id;
    private String dishName;
    private Status status;

    public Order(int id, String dishName) {
        this.id = id;
        this.dishName = dishName;
        this.status = Status.NEW;
    }
    public Order(int id, String dishName, Status status) {
        this.id = id;
        this.dishName = dishName;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public String getDishName() {
        return dishName;
    }
    public Status getStatus() {
        return status;
    }
    public void accept() {
        if (status != Status.NEW) {
            throw new IllegalStateException("Only NEW order can be accepted.");
        }

        status = Status.ACCEPTED;
    }
    public void cancel() {
        if (status == Status.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }
        status = Status.CANCELLED;
    }
}