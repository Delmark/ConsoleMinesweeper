public class Cell {
    private boolean hasBomb;
    private int bombsNearby;
    private boolean isEmpty;
    private boolean isOpened;
    private boolean isFlagged;

    Cell(boolean hasBomb){
        if (hasBomb) {
            this.hasBomb = true;
        } else this.isEmpty = true;
    }

    public void openCell() {
        isOpened = true;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setFlagged(boolean flagged) {
        if (this.isEmpty) System.out.println("Ячейка уже открыта!");
        else isFlagged = flagged;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setBombsNearby(int bombsNearby) {
        this.bombsNearby = bombsNearby;
        this.isEmpty = false;
    }

    public boolean isHasBomb() {
        return hasBomb;
    }

    @Override
    public String toString() {
        if (isOpened) {
            if (isEmpty) return "*";
            if (hasBomb) return "B";
            return Integer.toString(bombsNearby);
        }
        else if (isFlagged) return "F";
        else return "#";
    }
}
