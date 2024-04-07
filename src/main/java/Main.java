import java.util.*;

public class Main {
    private static final int ROUTES_NUMBER = 1_000;
    private static final String TURNS = "RLRFR";
    private static final Map<Long, Integer> sizeToFreq = new HashMap<>();
    private static int turnRightAccurance;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int threadNo = 0; threadNo < ROUTES_NUMBER; threadNo++) {
            threads.add(
                    new Thread(
                            () -> {
                                String route = generateRoute(TURNS, 100);
                                long turnRightFrequency = route.chars().filter(ch -> ch == 'R').count();
                                synchronized (sizeToFreq) {
                                    turnRightAccurance = sizeToFreq.containsKey(turnRightFrequency) ? ++turnRightAccurance : 1;
                                    sizeToFreq.put(turnRightFrequency, turnRightAccurance);
                                }
                            }
                    ));
            threads.getLast().start();
        }

        for (Thread thread : threads) { thread.join(); }

        //System.out.println(sizeToFreq);
        TreeMap<Long, Integer> sizeToFreqSorted = new SizeToFreqSorted<>(sizeToFreq);
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", sizeToFreqSorted.lastKey(), sizeToFreqSorted.lastEntry().getValue());
        System.out.println(sizeToFreqSorted);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
