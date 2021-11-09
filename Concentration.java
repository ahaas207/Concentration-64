 /*
    NAME: <Aimee Haas>
    COS 161, Spring 2021, Prof. Amorelli
    Project 03
    File Name: Concentration.java
*/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Concentration {
    //playerone hash set 
    //player two hash set containing is card covered
    static DrawingPanel panel = new DrawingPanel(800, 950);   
    static Graphics g = panel.getGraphics();  
    static Scanner playerInput = new Scanner(System.in);
    
    static Set<MemoryCard> playerOneSet = new HashSet<MemoryCard>();
    static Set<MemoryCard> playerTwoSet = new HashSet<MemoryCard>();
    static TreeMap<Integer, MemoryCard> gameBoard = new TreeMap<Integer, MemoryCard>();
    static ArrayList<MemoryCard> cardDeck = new ArrayList<MemoryCard>();
    static TreeMap<Integer, List<Integer>> coordMap = new TreeMap<Integer, List<Integer>>();    
    
    //creates Custom Colors
    static Color TEAL = new Color(15, 103, 158);
    static Color LILAC = new Color(108, 102, 155);
    static Color SPRUCE = new Color(78, 112, 73);
    static Color BURNTRED = new Color(145, 33, 33);
    

    public static void main(String[] args) throws Exception {  
        fillBoard();
        drawBoard();
        
        String evenPlayer = "Player One";
        String oddPlayer = "Player Two";
        
        //initializes a counter to keep track of which player is up for their turn. 
        int counter = 0;
        //Alternates players until someone wins or there is a draw. 
        while (gameNotOver(playerOneSet, playerTwoSet)) {
            if (counter % 2 == 0) {
                takeTurn(evenPlayer, playerOneSet);
                counter++;
            }
            else {
                takeTurn(oddPlayer, playerTwoSet);  
                counter++;
            }
        }


    }
    
    public static void takeTurn(String player, Set<MemoryCard> playerSet) throws Exception {
            System.out.println("It's " + player + "'s turn. Select the first card index to uncover: ");
            int firstCardIndex = playerInput.nextInt();
            
            if (firstCardIndex >= 0 && firstCardIndex <= 63 && !cardDeck.get(firstCardIndex).getIsUncovered()) {
                cardDeck.get(firstCardIndex).setIsUncovered(true);
                drawBoard();
            }
            else {
                System.out.println("Try again. Please type an integer between 1- 63.");
                takeTurn(player, playerSet);
            }
            
            System.out.println(player + " select the second card to uncover.");
            int secondCardIndex = playerInput.nextInt();
            
            if (secondCardIndex >= 0 && secondCardIndex <= 63 && !cardDeck.get(secondCardIndex).getIsUncovered()) {
                cardDeck.get(secondCardIndex).setIsUncovered(true);
                drawBoard();           
            }
            else {
                System.out.println("Try again. Please type an integer between 1- 63.");
                cardDeck.get(firstCardIndex).setIsUncovered(false);
                takeTurn(player, playerSet);
            }
            
            String firstShape = cardDeck.get(firstCardIndex).getShape();
            String secondShape = cardDeck.get(secondCardIndex).getShape();
            String firstColor = cardDeck.get(firstCardIndex).getColorString();
            String secondColor = cardDeck.get(secondCardIndex).getColorString();
            
            if (firstShape.equals(secondShape) && firstColor.equals(secondColor)) {
                MemoryCard temp = new MemoryCard(firstShape, firstColor);
                playerSet.add(temp);
                System.out.println("You've found a match. Go Again.");
                //only lets player keep taking turns so long as there isn't a winner 
                if (gameNotOver(playerOneSet, playerTwoSet)) {
                    takeTurn(player, playerSet);
                }
            }
            
            else {
                System.out.println("No match.");
                cardDeck.get(firstCardIndex).setIsUncovered(false);
                cardDeck.get(secondCardIndex).setIsUncovered(false);
                
            }
            
            
    }
    
    public static boolean gameNotOver(Set<MemoryCard> playerOne, Set<MemoryCard> playerTwo) {
        int total = playerOne.size() +playerTwo.size();
        //sets only store one of the matching cards, hence the 32 card condition
        if ( total < 32) {
            return true;
        }
        else if (total == 32 && playerOne.size() > playerTwo.size()) {
            System.out.println("Player One is the Winner! \n List of winning cards: \n");
            printWinningCards(playerOne);
            return false;
        }
        else if (total == 32 && playerTwo.size() > playerOne.size()) {
            System.out.println("Player Two is the Winner! \n List of winning cards: \n");
            printWinningCards(playerTwo);
            return false;        
        } 
        return true;
    }
    
    public static void printWinningCards (Set<MemoryCard> player) {
        Iterator<MemoryCard> mC = player.iterator();
        while (mC.hasNext()) {
            System.out.println(mC.next().toString());
        } 
    }
    
    public static void fillBoard() {
        Color[] colorRoller = new Color[] {TEAL, LILAC, SPRUCE, BURNTRED};
        String[] colorStringRoller = new String[] {"Blue", "Purple", "Green", "Red"};
        String[] shapeRoller = new String[] {"Moon", "Star", "Mage", "Sword", "Potion", "Tower", "Bow", "Fortune"};
        
        //makes 32 unique pairs of cards, rolling through color and shape types 
        for (int i = 0; i < colorRoller.length; i++) {
            for (int j = 0; j < shapeRoller.length; j++) {
                MemoryCard card = new MemoryCard(shapeRoller[j], colorRoller[i], colorStringRoller[i], false);
                cardDeck.add(card);  
                MemoryCard cardTwo = new MemoryCard(shapeRoller[j], colorRoller[i], colorStringRoller[i], false);
                cardDeck.add(cardTwo);   
            }
        }  
        
        Collections.shuffle(cardDeck);
        
        int indexCounter = 0;
        for (MemoryCard card: cardDeck) {
            gameBoard.put(indexCounter, card);
            indexCounter++;
        }
        
    }
    
    public static void drawBoard() {
        //clear board
        g.setColor(Color.WHITE);   
        g.fillRect(1, 1, 800, 950);             
        g.setColor(Color.BLACK);
        
        int coordIndex =0;
        
        //Draws Blank Cards
        for (int i = 0;  i < 8; i++) {
            for (int j = 0; j < 8 ; j++) {
                //stores coords for drawCard in a coordinate map,
                //index position in gameBoard will determine where drawn on board              
                int xC = (95 * j);
                int yC = (100 * i);    
                coordMap.put(coordIndex, new ArrayList<Integer>(2));
                coordMap.get(coordIndex).add(xC);
                coordMap.get(coordIndex).add(yC);
                coordIndex ++;
                g.drawRoundRect(37 + xC, 37 + yC, 61, 86, 10,10);
                g.fillRoundRect(40 + xC, 40 + yC, 55, 80, 10,10);
                
            }        
        }
        
        //Draws Cards
        for (int cardNum : gameBoard.keySet()) {
            MemoryCard temp = gameBoard.get(cardNum);
            List<Integer> tempXY = coordMap.get(cardNum);
            String type = temp.getShape();
            Color c = temp.getColor();
            boolean isU = temp.getIsUncovered();
            if (isU) {
                drawShape(cardNum, type, c, tempXY.get(0), tempXY.get(1));
            }
            else {
                drawNumber(cardNum, tempXY.get(0), tempXY.get(1));
            }
                        
        }
             

        
    }
    
    public static void drawNumber(Integer cardN, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 30)); 
        if (cardN < 10) {
            g.drawString(cardN.toString(), (58 + x), (88 + y)); 
        }
        else 
            g.drawString(cardN.toString(), (49 + x), (89 + y)); 
    }
    
    public static void drawShape (int cardNum, String cardType, Color custC, int x, int y) {
        
        switch (cardType) {
            case "Moon":
                g.setColor(custC);
                g.fillOval(45 + x, 45 + y, 45, 65);
                g.setColor(Color.BLACK);
                g.fillOval(60 + x, 53 + y, 33, 50);
                break;
                
            case "Star":
                g.setColor(custC);
                g.fillRect(55 + x, 55 + y, 28, 28);
                g.setColor(Color.BLACK);
                g.fillOval(45 + x, 45 + y, 24, 24);
                g.fillOval(70+ x, 70 + y, 24, 24);
                g.fillOval(45 + x, 70+ y, 24, 24);
                g.fillOval(70 + x,45 + y, 24, 24);    
                g.setColor(custC);
                g.fillRect(48 + x, 86 + y, 24, 24);
                g.setColor(Color.BLACK);
                g.fillOval(40 + x, 80 + y, 19, 19);
                g.fillOval(61 + x, 101 + y, 19, 19);
                g.fillOval(40 + x, 101+ y, 19, 19);
                g.fillOval(61 + x ,80 + y, 19, 19);    
                break;
                
            case "Mage":
                g.setColor(custC);
                g.fillOval(48 + x, 85 + y, 40, 18);
                int[] hatX = new int[] {58 + x, 65 + x, 80 + x, 74 + x, 80 +x};
                int[] hatY = new int[] {87 + y, 60 + y, 50 + y, 62 + y, 87 + y};
                int n = hatX.length;
                g.fillPolygon(hatX, hatY, n);
                break;
                
            case "Sword":
                g.setColor(custC);
                int[] swordX = new int[] {70 + x, 75 + x, 75 + x, 72 + x, 80 + x, 80 + x, 72 + x, 
                        72 + x, 68 + x, 68 + x, 60 + x, 60 + x, 68 + x, 65 + x, 65 + x};
                int[] swordY = new int[] {45 + y, 60 + y, 90 + y, 98 + y, 98 + y, 102 + y, 102 + y,
                        112 + y, 112 + y, 102 + y, 102 + y, 98 + y, 98 + y, 90 + y, 60 + y};
                int o = swordX.length;
                g.fillPolygon(swordX, swordY, o);       
                break;
                
            case "Potion":
                g.setColor(custC);
                g.fillRoundRect(58 + x, 55 + y,  20, 5, 2, 2);
                g.fillRect(62 + x, 60 + y, 12, 12);
                int[] potX = new int[] {62 + x, 74 + x, 90 + x,46 + x};
                int[] potY = new int[] {72 + y, 72 + y, 100 + y, 100 + y};
                int p = potX.length;
                g.fillPolygon(potX, potY, p);
                break;
                
            case "Tower":
                g.setColor(custC);
                int[] topX = new int[] {60 + x, 77 + x, 70 + x};
                int[] topY = new int[] {70 + y, 70 + y, 55 + y};
                int t = topX.length;
                g.fillPolygon(topX, topY, t);
                int[] airX = new int[] {61 + x, 75 + x, 77 + x, 59 + x};
                int[] airY = new int[] {70 + y, 70 + y, 110 + y, 110 + y};
                ((Graphics2D) g).setStroke(new BasicStroke(2));
                int a = airX.length;
                g.drawPolygon(airX, airY, a);
                g.fillRoundRect(65 + x, 77 + y, 6, 8, 2, 2);
                ((Graphics2D) g).setStroke(new BasicStroke(1));
                break;
                
            case "Bow":
                g.setColor(custC);
                ((Graphics2D) g).setStroke(new BasicStroke(3));
                g.drawArc(20 + x, 50 + y, 60, 58, -80, 160);
                ((Graphics2D) g).setStroke(new BasicStroke(1));
                g.drawLine(55 + x, 80 + y, 69 + x, 56 + y);
                g.drawLine(55 + x, 80 + y, 69 + x, 98 + y);
                g.drawLine(47 + x, 80 + y, 86 + x, 80 + y);
                int[] triX = new int[] {86 + x, 93 + x, 86 + x};
                int[] triY = new int[] {76 + y, 80 + y, 84 + y};
                int tri = triX.length;
                g.fillPolygon(triX, triY, tri);
                break;
                
            case "Fortune":
                g.setColor(Color.WHITE);
                g.fillOval(52 + x, 60 + y, 30, 30);
                g.setColor(custC);
                g.fillRoundRect(54 + x, 88 + y, 26, 5, 2, 2);
                int[] magX = new int[] {56 + x, 77 + x, 86 + x, 45 + x};
                int[] magY = new int[] {93 + y, 93 + y, 98 + y, 98 + y};
                int mag = magX.length;
                g.fillPolygon(magX, magY, mag);
                break;
                
                default:
                    System.out.println("Piece not recognized.");
                    break;
        }
      
         
         }
         
           
        
    }
    


