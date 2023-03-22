package Utils;

public enum RotateType {

    HORIZONTAL(1),
    VERTICAL(2);

    private final int rotate;

    private RotateType(int rotate) {
        this.rotate = rotate;
    }

    public int getRotate() {
        return rotate;
    }
}

