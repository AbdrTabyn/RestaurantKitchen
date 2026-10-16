package SubsystemA;

public class InventoryChecker {
    public boolean isAvailable(String dishName) {
        if (dishName == null || dishName.isBlank()) {
            return false;
        }
         // Для демонстрации ошибки
        return !dishName.equalsIgnoreCase("Unavailable");
    }
}