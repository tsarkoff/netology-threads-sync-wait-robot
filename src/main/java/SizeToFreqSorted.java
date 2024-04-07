import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;

public class SizeToFreqSorted<L, I> extends TreeMap<L, I> {
    public SizeToFreqSorted(Map<L, I> sizeToFreq) {
        super(sizeToFreq);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.entrySet().size());
        sb.append("Все размеры (отсортировано по возрастанию):\n");
        super.forEach((key, value) -> {
            sb.append(format(" - %d (%d раз)%n", key, value));
        });
        return sb.toString();
    }
}
