
package view;
import control.WordleControl;
import model.GuessResult;
import javax.sound.sampled.*;
import javax.swing.*;

import java.io.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import model.Constants;
import java.util.ArrayList;
import java.util.Scanner;


public class WordleGUI extends JFrame implements KeyListener, ActionListener {

    JPanel guessesPanel; // panel for guessed letters
    JLabel[][] guessesPanelLetters; // 2d array for the guessed letters interface
    JPanel keyboardPanel1;
    JPanel keyboardPanel2;
    JPanel keyboardPanel3;
    JButton[] keyboardRow1; // qwertyuiop
    JButton[] keyboardRow2; // asdfghjkl
    JButton[] keyboardRow3; // (enter)zxcvbnm(delete)
    JTextField typing;
    char[][] letterBoard;
    int[] cursor;
    int wordSize;
    String guess, guessedWord=null;
    boolean guessIsRed = false;


    public WordleGUI(int wordSize) {
        Constants.letterStates = new int[26];
        this.wordSize = wordSize;
        guessesPanel = new JPanel();
        guessesPanelLetters = new JLabel[wordSize + 1][wordSize];
        letterBoard = new char[wordSize + 1][wordSize];
        cursor = new int[]{0, 0};
        for (int r = 0; r < wordSize + 1; r++) {
            for (int c = 0; c < wordSize; c++) {
                guessesPanelLetters[r][c] = new JLabel("", SwingConstants.CENTER);
                guessesPanelLetters[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                guessesPanel.add(guessesPanelLetters[r][c]);
                guessesPanel.setBackground(new Color(18, 18, 19));
                guessesPanel.setOpaque(true);
                guessesPanelLetters[r][c].setBackground(new Color(129, 131, 132));
                guessesPanelLetters[r][c].setOpaque(true);
                guessesPanelLetters[r][c].setForeground(Color.white);
                guessesPanelLetters[r][c].setFont(new Font("Clear Sans", Font.BOLD, 18 - wordSize));
            }
        }
        guessesPanel.setLayout(new GridLayout(wordSize + 1, wordSize));

        keyboardPanel1 = new JPanel();
        keyboardPanel2 = new JPanel();
        keyboardPanel3 = new JPanel();
        String[] keyboardCharacters = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H",
                "J", "K", "L", "ENTER", "Z", "X", "C", "V", "B", "N", "M", "DLT"};
        keyboardRow1 = new JButton[10];
        keyboardRow2 = new JButton[9];
        keyboardRow3 = new JButton[9];
        for (int i = 0; i < 28; i++) {
            if (i < 10) {
                keyboardRow1[i] = new JButton();
                keyboardRow1[i].setText(keyboardCharacters[i]);
                keyboardPanel1.add(keyboardRow1[i]);
                keyboardRow1[i].setBorder(BorderFactory.createLineBorder(new Color(18, 18, 19), 2));
                keyboardRow1[i].setBackground(new Color(129, 131, 132));
                keyboardRow1[i].setOpaque(true);
                keyboardRow1[i].setForeground(Color.white);
                keyboardRow1[i].setFont(new Font("Clear Sans", Font.BOLD, 13));
                keyboardRow1[i].addActionListener(this);
            } else if (i < 19) {
                keyboardRow2[i - 10] = new JButton();
                keyboardRow2[i - 10].setText(keyboardCharacters[i]);
                keyboardPanel2.add(keyboardRow2[i - 10]);
                keyboardRow2[i - 10].setBorder(BorderFactory.createLineBorder(new Color(18, 18, 19), 2));
                keyboardRow2[i - 10].setBackground(new Color(129, 131, 132));
                keyboardRow2[i - 10].setOpaque(true);
                keyboardRow2[i - 10].setForeground(Color.white);
                keyboardRow2[i - 10].setFont(new Font("Clear Sans", Font.BOLD, 13));
                keyboardRow2[i - 10].addActionListener(this);
            } else {
                keyboardRow3[i - 19] = new JButton();
                keyboardRow3[i - 19].setText(keyboardCharacters[i]);
                keyboardPanel3.add(keyboardRow3[i - 19]);
                keyboardRow3[i - 19].setBorder(BorderFactory.createLineBorder(new Color(18, 18, 19), 2));
                keyboardRow3[i - 19].setBackground(new Color(129, 131, 132));
                keyboardRow3[i - 19].setOpaque(true);
                keyboardRow3[i - 19].setForeground(Color.white);
                keyboardRow3[i - 19].setFont(new Font("Clear Sans", Font.BOLD, 13));
                if (i == 19) {
                    keyboardRow3[i - 19].setFont(new Font("Clear Sans", Font.BOLD, 11));
                }
                keyboardRow3[i - 19].addActionListener(this);
            }
        }
        keyboardPanel1.setLayout(new GridLayout(1, 10));
        keyboardPanel2.setLayout(new GridLayout(1, 9));
        keyboardPanel3.setLayout(new GridLayout(1, 9));
        typing = new JTextField();
        typing.setText(" ");
        typing.addKeyListener(this);

        add(guessesPanel);
        add(keyboardPanel1);
        add(keyboardPanel2);
        add(keyboardPanel3);
        add(typing);

        guessesPanel.setSize(50 * wordSize, 300);
        guessesPanel.setLocation((9 - wordSize) * 25, 50);
        keyboardPanel1.setLocation(37, 400);
        keyboardPanel1.setSize(375, 50);
        keyboardPanel1.setBackground(new Color(18, 18, 19));
        keyboardPanel2.setLocation(55, 450);
        keyboardPanel2.setSize(340, 50);
        keyboardPanel2.setBackground(new Color(18, 18, 19));
        keyboardPanel3.setLocation(37, 500);
        keyboardPanel3.setSize(375, 50);
        keyboardPanel3.setBackground(new Color(18, 18, 19));
        typing.setLocation(0, 0);
        typing.setSize(465, 600);
//        typing.setBackground(new Color(18, 18, 19));
        typing.setOpaque(false);
        typing.setBorder(BorderFactory.createEmptyBorder());
        typing.setForeground(new Color(18, 18, 19));
        typing.setBackground(new Color(18, 18, 19));


        setLayout(new BorderLayout());
        setTitle("Wordle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(465, 600);
        getContentPane().setBackground(new Color(18, 18, 19));
        setVisible(true);
    }

    public void refresh(GuessResult individualResult, int guessCount) {
        for (int i = 0; i < this.wordSize; i++) {
            guessesPanelLetters[guessCount][i].setText(Character.toString(letterBoard[guessCount][i]));

            Color bkg = new Color(58, 58, 60);
            switch (individualResult.getIndividualResult().get(i)) {
                case 1:
                    bkg = new Color(181, 159, 59);
                    break;
                case 2:
                    bkg = new Color(83, 141, 78);
                    break;
            }
            guessesPanelLetters[guessCount][i].setBackground(bkg);
            guessesPanelLetters[guessCount][i].setOpaque(true);
        }

        for (int i = 0; i < 26; i++) {
            int[] index = {0, 0};
            switch (i) {
                case 0:
                    index = new int[]{1, 0};
                    break;
                case 1:
                    index = new int[]{2, 5};
                    break;
                case 2:
                    index = new int[]{2, 3};
                    break;
                case 3:
                    index = new int[]{1, 2};
                    break;
                case 4:
                    index = new int[]{0, 2};
                    break;
                case 5:
                    index = new int[]{1, 3};
                    break;
                case 6: // g
                    index = new int[]{1, 4};
                    break;
                case 7:
                    index = new int[]{1, 5};
                    break;
                case 8: // i
                    index = new int[]{0, 7};
                    break;
                case 9:
                    index = new int[]{1, 6};
                    break;
                case 10:
                    index = new int[]{1, 7};
                    break;
                case 11:
                    index = new int[]{1, 8};
                    break;
                case 12: // m
                    index = new int[]{2, 7};
                    break;
                case 13:
                    index = new int[]{2, 6};
                    break;
                case 14:
                    index = new int[]{0, 8};
                    break;
                case 15: // p
                    index = new int[]{0, 9};
                    break;
                case 16: // q
                    index = new int[]{0, 0};
                    break;
                case 17: // r
                    index = new int[]{0, 3};
                    break;
                case 18: // s
                    index = new int[]{1, 1};
                    break;
                case 19: // t
                    index = new int[]{0, 4};
                    break;
                case 20: // u
                    index = new int[]{0, 6};
                    break;
                case 21: // v
                    index = new int[]{2, 4};
                    break;
                case 22: // w
                    index = new int[]{0, 1};
                    break;
                case 23: // x
                    index = new int[]{2, 2};
                    break;
                case 24: // y
                    index = new int[]{0, 5};
                    break;
                case 25: // z
                    index = new int[]{2, 1};
                    break;
            }
            if (Constants.letterStates[i] == 2) {
                if (index[0] == 0) {
                    keyboardRow1[index[1]].setBackground(new Color(83, 141, 78));
                    keyboardRow1[index[1]].setOpaque(true);
                } else if (index[0] == 1) {
                    keyboardRow2[index[1]].setBackground(new Color(83, 141, 78));
                    keyboardRow2[index[1]].setOpaque(true);
                } else {
                    keyboardRow3[index[1]].setBackground(new Color(83, 141, 78));
                    keyboardRow3[index[1]].setOpaque(true);
                }
            } else if (Constants.letterStates[i] == 1) {
                if (index[0] == 0) {
                    if (!keyboardRow1[index[1]].getBackground().equals(new Color(83, 141, 78))) {
                        keyboardRow1[index[1]].setBackground(new Color(181, 159, 59));
                        keyboardRow1[index[1]].setOpaque(true);
                    }
                } else if (index[0] == 1) {
                    if (!keyboardRow2[index[1]].getBackground().equals(new Color(83, 141, 78))) {
                        keyboardRow2[index[1]].setBackground(new Color(181, 159, 59));
                        keyboardRow2[index[1]].setOpaque(true);
                    }
                } else {
                    if (!keyboardRow3[index[1]].getBackground().equals(new Color(83, 141, 78))) {
                        keyboardRow3[index[1]].setBackground(new Color(181, 159, 59));
                        keyboardRow3[index[1]].setOpaque(true);
                    }
                }
            }
        }
    }

    public void updateBoard() {
        for (int i = 0; i < this.wordSize; i++) {
            guessesPanelLetters[cursor[0]][i].setText(Character.toString(letterBoard[cursor[0]][i]));
            if (guessIsRed) {
                guessesPanelLetters[cursor[0]][i].setForeground(new Color(255, 0, 0));
                guessesPanelLetters[cursor[0]][i].setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
            } else {
                guessesPanelLetters[cursor[0]][i].setForeground(Color.WHITE);
                guessesPanelLetters[cursor[0]][i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        typing.setText("");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 8) { //backspace
            if (cursor[1] != 0) {
                letterBoard[cursor[0]][cursor[1] - 1] = ' ';
                cursor[1]--;
                if (guessIsRed) {
                    guessIsRed = false;
                }
            }
        } else if (e.getKeyCode() == 10) { //enter
            if (cursor[1] == this.wordSize && !guessIsRed) { //the row is filled
                guess = "";
                for (char c : letterBoard[cursor[0]]) {
                    guess += c; //fill guess string with letters in current row
                }
                guessedWord=guess;
                cursor[0]++;
                cursor[1] = 0;
            } else {
//                System.out.println("invalid guess"); //row not filled
            }
        } else if (e.getKeyCode() >= 65 && e.getKeyCode() <= 90) { //any other letter char
            if (cursor[1] < this.wordSize - 1) { //row not filled yet
                letterBoard[cursor[0]][cursor[1]] = Character.toTitleCase((char) e.getKeyCode());
                cursor[1]++;
            } else if (cursor[1] == this.wordSize - 1) {
                letterBoard[cursor[0]][cursor[1]] = Character.toTitleCase((char) e.getKeyCode());
                cursor[1]++;
                String guess = "";
                for (int i = 0; i < letterBoard[cursor[0]].length - 1; i++) {
                    guess += letterBoard[cursor[0]][i];
                }
                guess += (char) e.getKeyCode();
                if (!checkGuess(guess)) {
                    guessIsRed = true;
                }
            } else {
//                System.out.println("full characters");
            }
        } else {
//            System.out.println("invalid character"); //character can be typed
        }
        updateBoard(); //set text inside buttons
        typing.setText(" ");
    }

    void sleep(long milliSec){
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getGuessedWord() {
        while (guessedWord==null)
            sleep(100);
        String result = guessedWord;
        guessedWord=null;
        return result;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        typing.setText(" ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!e.getActionCommand().equals("DLT") && !e.getActionCommand().equals("ENTER")) {
            if (cursor[1] < this.wordSize - 1) { //row not filled yet
                letterBoard[cursor[0]][cursor[1]] = Character.toTitleCase(e.getActionCommand().charAt(0));
                cursor[1]++;
            } else if (cursor[1] == this.wordSize - 1) {
                letterBoard[cursor[0]][cursor[1]] = Character.toTitleCase(e.getActionCommand().charAt(0));
                cursor[1]++;
                String guess = "";
                for (int i = 0; i < letterBoard[cursor[0]].length - 1; i++) {
                    guess += letterBoard[cursor[0]][i];
                }
                guess += e.getActionCommand().charAt(0);
                if (!checkGuess(guess)) {
                    guessIsRed = true;
                }
            } else {
//                System.out.println("full characters");
            }
        } else if (e.getActionCommand().equals("DLT")) {
            if (cursor[1] != 0) {
                letterBoard[cursor[0]][cursor[1] - 1] = ' ';
                cursor[1]--;
                if (guessIsRed) {
                    guessIsRed = false;
                }
            }
        } else {
            if (cursor[1] == this.wordSize && !guessIsRed) { //the row is filled
                guess = "";
                for (char c : letterBoard[cursor[0]]) {
                    guess += c; //fill guess string with letters in current row
                }
                guessedWord=guess;
                cursor[0]++;
                cursor[1] = 0;
            } else {
//                System.out.println("invalid guess"); //row not filled
            }
        }
        updateBoard();
    }

    

    public void congratulate(String desc, int tries){

        JTextArea textArea = new JTextArea("You guessed it in " + tries + " tries!\n"+desc);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "Congratulations!",
                                       JOptionPane.DEFAULT_OPTION);

    }

    public void sorry(String word, String desc, int tries){

        JTextArea textArea = new JTextArea("The word was " + word + ".\n" + desc);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "Better luck next time!",
                                       JOptionPane.DEFAULT_OPTION);
    }

    /**
	 * Plays a different .wav clip depending on the mode given.
	 * @param mode the context(shoot, hit, miss, game over)
	 */
	public void playGameSound(String mode){
		String YOUWINFILE = "youwin.wav";
		String YOULOSTFILE = "youlose.wav";
		String audioFile;
		Clip clip;
		AudioInputStream audioInputStream;

		if(mode.equals("you won")){
			audioFile = YOUWINFILE;
		}else{
			audioFile = YOULOSTFILE;
        }	


		try {
			// create AudioInputStream object
			audioInputStream = AudioSystem.getAudioInputStream(new File(audioFile).getAbsoluteFile());
			// create clip reference
			clip = AudioSystem.getClip();
			// open audioInputStream to the clip
			clip.open(audioInputStream);
			clip.start();
			Thread.sleep(3000);
			clip.close();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) { 
			((Throwable) e).printStackTrace();
		}
		
	}
    public boolean checkGuess(String guess) {
//        System.out.println("guess is " + guess);
        return Constants.validGuesses.contains(guess.toUpperCase());
    }
}
//gaming