import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;

// класс предназначен для быстрой сортировки и кастомного вывода Мапы на экран
public class SizeToFreqSorted<L, I> extends TreeMap<L, I> {
    public SizeToFreqSorted(Map<L, I> sizeToFreq) {
        super(sizeToFreq);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.entrySet().size());
        sb.append("Все размеры (отсортировано по возрастанию):\n");
        super.forEach((key, value) -> sb.append(format(" - %s (%s раз)%n", key, value)));
        return sb.toString();
    }
}
