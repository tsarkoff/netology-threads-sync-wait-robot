import java.util.*;

public class Main {
    private static final int ROUTES_NUMBER = 1_000;
    private static final String TURNS = "RLRFR";
    private static final int TURNS_COUNT = 100;
    private static final Map<Long, Long> sizeToFreq = new HashMap<>();
    private static long turnRightCount;
    private static Thread printThread;

    public static void main(String[] args) throws InterruptedException {
        // Поток подсчета максимальных частот "Правых Поворотов" и вывода их на экран
        printThread = new Thread(() -> {
            while (!printThread.isInterrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                        // инверсия, потому что мы ищем в TreeMap максимальное значение значения, а не ключа
                        TreeMap<Long, Long> sizeToFreqSorted = new SizeToFreqSorted<>(getInversedMap());
                        System.out.printf(
                                "Текущее максимальное кол-во повторений %d (встретилось %d раз)%n",
                                sizeToFreqSorted.lastEntry().getValue(), // сначала Value - как Ключ
                                sizeToFreqSorted.lastEntry().getKey()); // затем Key - как Value
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                }
            }
        });
        printThread.start();

        // Потоки генерации 10_000 маршрутов и подсчет частот появления "Правых Поворотов" (R)
        List<Thread> threads = new ArrayList<>();
        for (int threadNo = 0; threadNo < ROUTES_NUMBER; threadNo++) {
            threads.add(new Thread(() -> {
                long turnRightFrequency = generateRoute(TURNS, TURNS_COUNT).chars().filter(ch -> ch == 'R').count();
                synchronized (sizeToFreq) {
                    sizeToFreq.put(turnRightFrequency, sizeToFreq.containsKey(turnRightFrequency) ? ++turnRightCount : 1);
                    sizeToFreq.notify();
                }
            }));
            threads.getLast().start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        printThread.interrupt();

        TreeMap<Long, Long> sizeToFreqSorted = new SizeToFreqSorted<>(sizeToFreq);
        System.out.println(sizeToFreqSorted);
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", sizeToFreqSorted.lastKey(), sizeToFreqSorted.lastEntry().getValue());
    }

    // Метод генерации маршрута
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    // Метод инверсии, чтобы найти в TreeMap максимальное значение Значения, как последний узел дерева
    private static Map<Long, Long> getInversedMap() {
        Map<Long, Long> inversedMap = new HashMap<>();
        for (Map.Entry<Long, Long> entry : sizeToFreq.entrySet()) {
            inversedMap.put(entry.getValue(), entry.getKey());
        }
        return inversedMap;
    }
}
