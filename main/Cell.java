package main;

public class Cell {

    public int x, y;
    boolean res;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    
    public Cell(int x, int y, boolean res) {
        this.x = x;
        this.y = y;
        this.res = res;
    }

}
