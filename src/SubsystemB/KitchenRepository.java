package SubsystemB;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class KitchenRepository {
    private static final String FILE_NAME = "kitchen.txt";

    public void save(CookingTask task) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    task.getOrderId() + "," +
                            task.getStatus() +
                            System.lineSeparator()
            );
        }
        catch (IOException e) {
            throw new RuntimeException("Could not save cooking task.", e);
        }
    }

    public CookingTask findByOrderId(int orderId) {
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
                    CookingTask.Status status =
                            CookingTask.Status.valueOf(data[1]);
                    return new CookingTask(
                            savedId,
                            status
                    );
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read kitchen data.", e);
        }
        return null;
    }
}