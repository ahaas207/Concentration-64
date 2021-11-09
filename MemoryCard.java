 /*
    NAME: <Aimee Haas>
    COS 161, Spring 2021, Prof. Amorelli
    Project 03
    File Name: MemoryCard.java
*/

import java.awt.Color;

public class MemoryCard {
    private String shapeType;
    private Color color;
    private boolean isUncovered;
    private String colorString;

    //create Custom Colors
    Color TEAL = new Color(15, 103, 158);
    Color LILAC = new Color(108, 102, 155);
    Color SPRUCE = new Color(78, 112, 73);
    Color BURNTRED = new Color(145, 33, 33);
    
    public MemoryCard (String shape, Color c, String cString, boolean uncover) {
        shapeType = shape;
        color = c;
        isUncovered = uncover;        
        colorString = cString;
    }
    
    //Overloaded for Player Uncovered Set
    public MemoryCard (String shape, String c) {
        shapeType = shape;       
        colorString = c;
    }
    
    public String toString() {
        String formatDisplay =  " Color : " + colorString +" Shape : "+  shapeType ;
        return formatDisplay;
    }
    
    public void setShape(String s) {
        shapeType = s;
    }
    
    public void setColor (Color c) {
        color = c;
    }
    
    public void setColorString(String c) {
        colorString = c;
    }
    
    public String getColorString() {
        return colorString;
    }
    
    
    public void setIsUncovered(boolean uncover) {
        isUncovered = uncover;
    }
    public String getShape() {
        return shapeType;
    }
    
    public Color getColor() {
        return color;
    }
    
    public boolean getIsUncovered() {
        return isUncovered;
    }
    
    
}
