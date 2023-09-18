package coms309;

public class Move {

    private String piece;
    private String direction;

    public Move() {

    }

    public Move(String direction, String piece) {
        this.piece = piece;
        this.direction = direction;
    }

    public String getPiece() {
        return piece;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return piece + ", " + direction;
    }

}
