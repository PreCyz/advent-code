package year2024;

public enum Direction {
    UP(0, -1, '^'),
    DOWN(0, 1, 'v'),
    LEFT(-1, 0, '<'),
    RIGHT(1, 0, '>');

    public final int mvX;
    public final int mvY;
    public final char c;

    Direction(int mvX, int mvY, char c) {
        this.mvX = mvX;
        this.mvY = mvY;
        this.c = c;
    }
}
