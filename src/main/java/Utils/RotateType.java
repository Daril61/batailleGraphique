package Utils;

public enum RotateType {

    VERTICAL(1),
    HORIZONTAL(2);

    private final int rotate;

    private RotateType(int rotate) {
        this.rotate = rotate;
    }

    public int getRotate() {
        return rotate;
    }
}

