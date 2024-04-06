package nextstep.ladder.domain;

import nextstep.ladder.util.RandomBooleanGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {

    private final List<Boolean> line;

    public Line(int size) {
        this(generatePoints(size));
    }

    public Line(PointFactory pointFactory) {
        this(pointFactory.generatePoints());
    }

    public Line(List<Boolean> line) {
        validateNotEmpty(line);
        validateDuplication(line);
        this.line = line;
    }

    public static List<Boolean> generatePoints(int size) {
        ArrayList<Boolean> points = new ArrayList<>();
        Boolean previous = RandomBooleanGenerator.generate();
        for (int i = 0; i < size - 1; i++) {
            Boolean current = RandomBooleanGenerator.generate();
            current = reversWhenTrueAgain(previous, current);
            points.add(current);
            previous = current;
        }
        return points;
    }

    private static Boolean reversWhenTrueAgain(Boolean previous, Boolean current) {
        if (previous && current) {
            current = false;
        }
        return current;
    }

    private void validateNotEmpty(List<Boolean> line) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateDuplication(List<Boolean> line) {
        Boolean previous = line.get(0);
        for (int i = 1; i < line.size(); i++) {
            checkIfTrueAgain(previous, line.get(i));
            previous = line.get(i);
        }
    }

    private void checkIfTrueAgain(Boolean previous, Boolean point) {
        if (previous && point) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line1 = (Line) o;
        return Objects.equals(line, line1.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line);
    }

    public List<Boolean> getLine() {
        return line;
    }

    public LadderMapping switchOrder(LadderMapping ladderMapping) {
        int[] orders = new int[line.size() + 1];
        init(orders);
        for (int i = 0; i < this.line.size(); i++) {
            switchIfTrue(orders, i);
        }
        return ladderMapping.regenerate(orders);
    }

    private void switchIfTrue(int[] orders, int i) {
        if (this.line.get(i)) {
            int temp = orders[i];
            orders[i] = orders[i + 1];
            orders[i + 1] = temp;
        }
    }

    private void init(int[] order) {
        for (int i = 0; i < line.size() + 1; i++) {
            order[i] = i;
        }
    }
}
