import java.util.Map;

public class CalculateRightTurnsFrequencyThread extends Thread {
    private final Map<Long, Integer> sizeToFreq;
    private static final String TURNS = "RLRFR";
    private static final int TURNS_COUNT = 100;
    private static int turnRightCount;

    public CalculateRightTurnsFrequencyThread(Map<Long, Integer> sizeToFreq) {
        this.sizeToFreq = sizeToFreq;
    }

    @Override
    public void run() {
        long turnRightFrequency = Main.generateRoute(TURNS, TURNS_COUNT).chars().filter(ch -> ch == 'R').count();
        synchronized (sizeToFreq) {
            sizeToFreq.put(turnRightFrequency, sizeToFreq.containsKey(turnRightFrequency) ? ++turnRightCount : 1);
            sizeToFreq.notify();
        }
    }
}
