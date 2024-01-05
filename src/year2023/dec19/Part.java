package year2023.dec19;

public class Part {
    public int x;
    public int m;
    public int a;
    public int s;

    public Part(int x, int m, int a, int s) {
        this.x = x;
        this.m = m;
        this.a = a;
        this.s = s;
    }

    @Override
    public String toString() {
        return "Part{" +
                "x=" + x +
                ", m=" + m +
                ", a=" + a +
                ", s=" + s +
                '}';
    }
}
