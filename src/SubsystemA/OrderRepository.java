package SubsystemA;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class OrderRepository {
    private static final String FILE_NAME = "orders.txt";

    public void save(Order order) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    order.getId() + "," +
                            order.getDishName() + "," +
                            order.getStatus() +
                            System.lineSeparator()
            );
        }
        catch (IOException e) {
            throw new RuntimeException("Could not save order.", e);
        }
    }

    public Order findById(int id) {
        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {
            return null;
        }
        try {
            List<String> lines = Files.readAllLines(path);
            // Идём с конца, потому что последнее состояние самое новое
            for (int i = lines.size() - 1; i >= 0; i--) {
                String[] data = lines.get(i).split(",");
                int savedId = Integer.parseInt(data[0]);

                if (savedId == id) {
                    String dishName = data[1];
                    Order.Status status = Order.Status.valueOf(data[2]);
                    return new Order(savedId, dishName, status);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read orders.", e);
        }
        return null;
    }
}