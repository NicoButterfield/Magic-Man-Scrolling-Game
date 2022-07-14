import java.awt.Color;

public class Location {
    
    private int row;
    private int col;
    
    public Location(int r, int c) {
        row = r;
        col = c;
    }
    
    public void decRow() {
    	row--;
    }
    
    public void incRow() {
    	row++;
    }
    
     public void decCol(){
    	col--;
    }
    
    public void incCol() {
    	col++;
    }
    
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public boolean equals(Location otherLoc) {
        return row == otherLoc.getRow() && col == otherLoc.getCol();
    }
    
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
    
}