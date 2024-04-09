import java.util.*;

public class Main {
    private static final int ROUTES_NUMBER = 1_000;
    private static final Map<Long, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        // Поток подсчета максимальных частот "Правых Поворотов" и вывода их на экран
        Thread printThread = new PrintThread(sizeToFreq);
        printThread.start();

        // Потоки генерации 10_000 маршрутов и подсчет частот появления "Правых Поворотов" (R)
        List<Thread> calcThreads = new ArrayList<>();
        for (int threadNo = 0; threadNo < ROUTES_NUMBER; threadNo++) {
            calcThreads.add(new CalculateRightTurnsFrequencyThread(sizeToFreq));
            calcThreads.getLast().start();
        }

        for (Thread thread : calcThreads) {
            thread.join();
        }
        printThread.interrupt();

        TreeMap<Long, Integer> sizeToFreqSorted = new SizeToFreqSorted<>(sizeToFreq);
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
}
