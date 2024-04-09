import java.util.Map;

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
                    // !!!!!! Почему этот закомментированный КОД НИЖЕ - НЕ РАБОТАЕТ ??? Ведь это локальный TreeMap...
                    // TreeMap<Long, Integer> sizeToFreqSorted = new SizeToFreqSorted<>(sizeToFreq);
                    // System.out.printf("Текущее количество повторений %d (встретилось %d раз)\n", sizeToFreqSorted.lastKey(), sizeToFreqSorted.lastEntry().getValue());

                    int maxValue = 0;
                    long maxKey = 0L;
                    for (Long key : sizeToFreq.keySet()) {
                        int currValue = sizeToFreq.get(key);
                        if (maxValue < currValue) {
                            maxValue = currValue;
                            maxKey = key;
                        }
                    }
                    System.out.printf("Текущее количество повторений %d (встретилось %d раз)\n", maxKey, maxValue);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
        }
    }
}
