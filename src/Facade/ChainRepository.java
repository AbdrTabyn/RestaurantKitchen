package Facade;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ChainRepository {
    private static final String FILE_NAME = "chain.txt";

    public void save(ChainState state) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    state.getOrderId() + "," +
                            state.getStatus() +
                            System.lineSeparator()
            );

        }
        catch (IOException e) {
            throw new RuntimeException("Could not save chain state.", e);
        }
    }

    public ChainState findByOrderId(int orderId) {
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
                    ChainState.Status status = ChainState.Status.valueOf(data[1]);
                    return new ChainState(savedId, status);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read chain state.", e);
        }
        return null;
    }
}