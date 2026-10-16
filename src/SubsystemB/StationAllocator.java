package SubsystemB;

public class StationAllocator {
    public boolean allocate(boolean forceFailure) {
        if (forceFailure) {
            return false;
        }
        return true;
    }
}