package year2024.coordinates;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_RIGHT(1, -1),
    DOWN_RIGHT(1, 1),
    UP_LEFT(-1, -1),
    DOWN_LEFT(-1, 1);
    final int mvX;
    final int mvY;

    Direction(int mvX, int mvY) {
        this.mvX = mvX;
        this.mvY = mvY;
    }

    public int mvX() {
        return mvX;
    }

    public int mvY() {
        return mvY;
    }
}
