package control;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import model.Constants;
import model.GuessResult;
import view.WordleGUI;


public class WordleControl {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> descs = new ArrayList<>();
    ArrayList<String> validWords = new ArrayList<>();
    int index;
    String word;
    String desc;
    WordleGUI game;
    int totalRows;

    public void initialize(){
        try {
            Scanner w4 = new Scanner(new File("words4.txt"));
            Scanner w5 = new Scanner(new File("words5.txt"));
            Scanner w6 = new Scanner(new File("words6.txt"));
            Scanner w7 = new Scanner(new File("words7.txt"));
            Scanner w8 = new Scanner(new File("words8.txt"));
            Scanner w9 = new Scanner(new File("words9.txt"));

            while (w4.hasNext()) {
                Constants.validGuesses.add(w4.nextLine());
            }
            while (w5.hasNext()) {
                Constants.validGuesses.add(w5.nextLine());
            }
            while (w6.hasNext()) {
                Constants.validGuesses.add(w6.nextLine());
            }
            while (w7.hasNext()) {
                Constants.validGuesses.add(w7.nextLine());
            }
            while (w8.hasNext()) {
                Constants.validGuesses.add(w8.nextLine());
            }
            while (w9.hasNext()) {
                Constants.validGuesses.add(w9.nextLine());
            }
        } catch (Exception e) {
//            System.out.println("file not found");
        }
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        words = new ArrayList<>();
        descs = new ArrayList<>();

        //read in all 5 letter words from wordlist
        try {
            File wordFile = new File("wordlist.txt");
            Scanner scFile = new Scanner(wordFile);
            
            while (scFile.hasNextLine()) {
                words.add(scFile.nextLine());
            }
            scFile.close();

        } 
        catch (FileNotFoundException e) {
//            System.out.println("File not found");
        }
        catch (Exception e) {
//            System.out.println("File error");
        }

        try {
            File wordFile = new File("worddesc.txt");
            Scanner scFile = new Scanner(wordFile);
            
            while (scFile.hasNextLine()) {
                descs.add(scFile.nextLine());
            }
            scFile.close();

        } 
        catch (FileNotFoundException e) {
//            System.out.println("File not found");
        }
        catch (Exception e) {
//            System.out.println("File error");
        }
        //choose random word


        index = rand.nextInt(words.size()); 

        word = words.get(index).toUpperCase();
        desc = descs.get(index);

        totalRows = word.length()+1;
        game = new WordleGUI(word.length());

    }

    public void mainLoop(){
        int guessCount=0;
        boolean guessed = false;

        while (!guessed && guessCount < totalRows){
            String guessedWord = game.getGuessedWord();
//            System.out.println(guessedWord);
            if (game.checkGuess(guessedWord)) {
//                System.out.println(guessedWord + " is valid");
                GuessResult result = checkWord(word, guessedWord);
                checkAllGuessed(result);
                game.refresh(result, guessCount);
                guessed=result.isAllGuessed();
                guessCount++;
            }
        }

        if(guessed){
            game.playGameSound("you won");
            game.congratulate(desc, guessCount);
            //use jconfirmpane
        }
        else{
            game.playGameSound("you lost");
            game.sorry(word,desc, guessCount);
        }
        game.dispose();
    }

    GuessResult checkWord(String word, String guessedWord){
//        System.out.println("word is "+ word + " length is " + word.length());
        GuessResult result = new GuessResult(word.length());

        HashMap<Character, Integer> seen =new HashMap<>();
        for (int i = 0; i<word.length(); i++){
            seen.put(word.charAt(i), countChar(word, word.charAt(i)));
        }

        for(int i = 0; i < word.length(); i++){
            char current = guessedWord.charAt(i);
            char wordCurrent = word.charAt(i);

            if(current==wordCurrent){
                result.getIndividualResult().set(i, 2);
                seen.replace(current, seen.get(current)-1);
            }else{

                result.getIndividualResult().set(i, 0);

            }

        }

        for(int i = 0; i <word.length(); i++){
            char current = guessedWord.charAt(i);
            String cur = "" + current;
            String wordCur = "" + word.charAt(i);
            if(cur.equals(wordCur)){
                continue;
            }
            if (word.indexOf(cur) != -1 && seen.get(current) > 0) {
                result.getIndividualResult().set(i, 1);
                seen.replace(current, seen.get(current) - 1);
            } else {
                result.getIndividualResult().set(i,0);
            }
        }

        for (int i = 0; i < guessedWord.length(); i++) {
            if (result.getIndividualResult().get(i) == 2) {
                Constants.letterStates[guessedWord.charAt(i) - 65] = 2;
            } else if (result.getIndividualResult().get(i) == 1) {
                Constants.letterStates[guessedWord.charAt(i) - 65] = 1;
            }
        }
        System.out.println(word);

        // logic here
        return result;
    }

    void checkAllGuessed(GuessResult g){
        boolean allGuessed = true;
        for(Integer i : g.getIndividualResult()){
            if(i!=2){
                allGuessed = false;
            }
        }
        
        g.setAllGuessed(allGuessed);
    }


    public static int countChar(String str, char c){
        int count = 0;

        for(int i=0; i < str.length(); i++){
            if(str.charAt(i) == c){
                count++;
            }
        }

        return count;
    }



    


}
