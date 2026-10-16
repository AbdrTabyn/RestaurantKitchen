package SubsystemC;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class DeliveryRepository {
    private static final String FILE_NAME = "delivery.txt";

    public void save(Delivery delivery) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    delivery.getOrderId() + "," +
                            delivery.getStatus() +
                            System.lineSeparator()
            );

        }
        catch (IOException e) {
            throw new RuntimeException("Could not save delivery.", e);
        }
    }

    public Delivery findByOrderId(int orderId) {
        Path path = Path.of(FILE_NAME);
        if (!Files.exists(path)) {
            return null;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (int i = lines.size() - 1; i >= 0; i--) {
                String[] data = lines.get(i).split(",");
                int savedId = Integer.parseInt(data[0]);

                if (savedId == orderId) {
                    Delivery.Status status =
                            Delivery.Status.valueOf(data[1]);
                    return new Delivery(
                            savedId,
                            status
                    );
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read delivery data.", e);
        }
        return null;
    }
}