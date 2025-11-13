import javax.swing.*;

public class App {

    public static void main(String[] args) {

        int boardWidth = 1500;
        int boardHeight = 500;

        JFrame frame = new JFrame("Chrome Dinosaur");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        ChromeDinosaur chromeDinosaur = new ChromeDinosaur();
        frame.add(chromeDinosaur);
        frame.pack();
        chromeDinosaur.requestFocus();
        
        frame.setVisible(true);
    }
 }