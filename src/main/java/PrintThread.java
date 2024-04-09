import java.util.Map;
import java.util.TreeMap;

public class PrintThread extends Thread {
    private final Map<Long, Integer> sizeToFreq;

    public PrintThread(Map<Long, Integer> sizeToFreq) {
        this.sizeToFreq = sizeToFreq;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            synchronized (sizeToFreq) {
                try {
                    sizeToFreq.wait();
                    TreeMap<Long, Integer> sizeToFreqSorted = new TreeMap<>(sizeToFreq);
                    System.out.printf(
                            "Текущей лидер количества повторений = %d (встретилось %d раз)%n",
                            sizeToFreqSorted.lastKey(),
                            sizeToFreqSorted.lastEntry().getValue());
                } catch (InterruptedException e) {
                    System.out.println("PrintThread INTERRUPTED.");
                    break;
                }
            }
        }
    }
}
