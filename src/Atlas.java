//package GameOn.AtlasGame.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
public class Atlas {
    final int LIVES = 3;
    private int lives;
    private int numPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private ArrayList<String> usedWords;
    private ArrayList<Player> players;
    //public ArrayList<String> remainingWords;
    private char startingLetter;
    private String myWord;
    Random rand;
    Random rand2;
    URL wikiLink;
    URL googleLink;
    Map<Character, ArrayList<String>> dictionary;
    Map<Character, ArrayList<String>> accentDictionary;
    private class Player {
        public int remLives;
        public String name;
        public boolean guessed;
        public Player(String name) {
            this.name = name;
            this.remLives = LIVES;
            this.guessed = false;
        }
        public void reduceLife() {
            remLives--;
        }
    }
    public Atlas() throws FileNotFoundException, MalformedURLException {
        lives = LIVES;
        usedWords = new ArrayList<String>();
        players = new ArrayList<>();
        currentPlayer = new Player("Hacker");
        //remainingWords = new ArrayList<String>();
        startingLetter = 's';
        myWord = "switzerland";
        dictionary = new HashMap<Character,ArrayList<String>>();
        accentDictionary = new HashMap<Character,ArrayList<String>>();
        rand = new Random();
        rand2 = new Random();
        wikiLink = new URL("https","en.wikipedia.org", "/wiki/Switzerland" );
        googleLink = new URL("https", "google.com", "/search?q=Switzerland");
        makeRemainingWords("world.csv", "worldCities.csv", "states.txt");


    }

    public void makeRemainingWords(String filenameWorld, String filenameCities, String filenameStates) throws FileNotFoundException {
        Scanner readFileWorld = new Scanner(new File(filenameWorld));
        Scanner readFileCities = new Scanner(new File(filenameCities));
        Scanner readFileStates = new Scanner(new File(filenameStates));
        readFileCities.useDelimiter("\\n|\\r");
        readFileWorld.useDelimiter("\\n|\\r");
        while (readFileCities.hasNext()) {
            String place = readFileCities.next().toLowerCase();
            String cityAccent = new String(place);
            char start = place.charAt(0);
            place = Normalizer.normalize(place, Normalizer.Form.NFD);
            place = place.replaceAll("[^A-Za-z\s]", "");
            if (!(dictionary.containsKey(start))){
                dictionary.put(start, new ArrayList<>());
            }
            if (!(accentDictionary.containsKey(start))){
                accentDictionary.put(start, new ArrayList<>());
            }
            if (!(dictionary.get(start).contains(place))) {
                dictionary.get(start).add(place);
            }
            if (!(accentDictionary.get(start).contains(cityAccent))) {
                accentDictionary.get(start).add(cityAccent);
            }
            //remainingWords.add(place);
            if (readFileCities.hasNextLine()) {
                readFileCities.nextLine();
            }
        }
        readFileWorld.nextLine();
        while (readFileWorld.hasNext()) {

            //if (readFileWorld.hasNext()) {
                String world = readFileWorld.nextLine().toLowerCase();
                String worldAccent = new String(world);
                world = Normalizer.normalize(world, Normalizer.Form.NFD);
                world = world.replaceAll("[^\\p{ASCII}]", "");
                char start = world.charAt(0);
                if(!(dictionary.containsKey(start))){
                    dictionary.put(start, new ArrayList<String>());
                }
                if (!(dictionary.get(start).contains(world))) {
                    //System.out.println(world);
                    dictionary.get(start).add(world);
                }
                if(!(accentDictionary.containsKey(start))){
                    accentDictionary.put(start, new ArrayList<String>());
                }
                if (!(accentDictionary.get(start).contains(worldAccent))) {
                    accentDictionary.get(start).add(worldAccent);
                }
                //remainingWords.add(world);
            //}

        }
        readFileStates.nextLine();
        while (readFileStates.hasNextLine()) {
            String state = readFileStates.nextLine().toLowerCase().replaceAll("[^A-Za-z\s]","");
            char start = state.charAt(0);
            if(!(dictionary.containsKey(start))){
                dictionary.put(start, new ArrayList<String>());
            }
            if (!(dictionary.get(start).contains(state))) {
                dictionary.get(start).add(state);
            }
            if(!(accentDictionary.containsKey(start))){
                accentDictionary.put(start, new ArrayList<String>());
            }
            if (!(accentDictionary.get(start).contains(state))) {
                accentDictionary.get(start).add(state);

            }
            //remainingWords.add(state);
        }

    }
    public String pickWord(){
        int num = rand2.nextInt(dictionary.get(startingLetter).size());
        //System.out.println( dictionary.get(start).get(num));
        String word = accentDictionary.get(startingLetter).get(num);
        word = Normalizer.normalize(word,Normalizer.Form.NFD);
        word = word.replaceAll("[^\\p{ASCII}]", "");
        return word;
    }
    /*public String pickWord() {
        //Fill out to pick a random word from the list of remaining Words;
        String word = remainingWords.get(rand.nextInt(remainingWords.size()));
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
        word = word.replaceAll("[^\\p{ASCII}]", "");
        //System.out.println("Picked: " + word);
        if (word.charAt(0) == startingLetter) {
            return word;
        } else {
            return pickWord();
        }
    }*/
    public boolean isValid(String input) {
        if (/*remainingWords*/dictionary.get(input.charAt(0)).contains(input) || usedWords.contains(input) ) {
            return true;
        }
        return false;
    }

    public boolean isUnused(String input) {
        if (/*remainingWords*/dictionary.get(input.charAt(0)).contains(input)) {
            return true;
        }
        return false;
    }
    public char changeStartingLetter(){
        char letter = startingLetter;
        while (letter == startingLetter){
            letter = (char)(rand.nextInt(26)+'a');
        }
        return letter;
    }
    //public String
    public void run() throws MalformedURLException {
        Scanner read = new Scanner(System.in);
        System.out.println("Enter the number of players ... (Must be greater than 0)");
        String in = read.next();
        if (!in.matches("[0-9]+") || (in.matches("[0]"))) {
            run();
        }
        int numPlayers = Integer.valueOf(in);
        read.nextLine();
        if (numPlayers != 1) {
            for (int i = 0; i < numPlayers; i++) {
                System.out.println("Enter player " + (i + 1) + "'s name:");
                String name = read.nextLine();
                //System.out.println(name);
                //read.next();
                players.add(new Player(name));
            }
        }
        if (numPlayers == 1) {
            while (lives > 0) {
                System.out.println("Enter a country or city or state that starts with: \'" + startingLetter + "\' or enter p to pass.");
                String input = read.nextLine();
                if (!(input.matches("[a-zA-Z\s.]+"))) {
                    System.out.println("Enter a country/city/state. Only letters are allowed!");
                } else {
                    input = input.toLowerCase();
                    if (input.equals("p")) {
                        lives--;
                        String possible1 = pickWord();
                        //remainingWords.remove(possible1);
                        dictionary.get(startingLetter).remove(possible1);
                        System.out.println("You have lost a life:( You now have " + lives + " lives remaining.");
                        System.out.println("You could have guessed " + possible1 + " or " + pickWord());
                        startingLetter = changeStartingLetter();

                    } else if (isValid(input)) {
                        if (!(isUnused(input))) {
                            System.out.println("This country/city/state has been used already! You have lost a life :(  Enter p to pass.");
                            lives--;
                            System.out.println("You have " + lives + " lives remaining.");
                        } else {
                            if (input.charAt(0) == startingLetter) {
                                System.out.println("OK.");
                                dictionary.get(startingLetter).remove(input);
                                //remainingWords.remove(input);
                                usedWords.add(input);
                                startingLetter = input.charAt(input.length() - 1);
                                myWord = pickWord();
                                //remainingWords.remove(myWord);
                                //if (myWord.contains(" ")){
                                //    String file = "/wiki/"
                                //}
                                String duplicateWord = new String(myWord);
                                String newWord = Pattern.compile("\\b(.)(.*?)\\b")
                                        .matcher(duplicateWord)
                                        .replaceAll(match -> match.group(1).toUpperCase() + match.group(2));
                                String file = "/wiki/" + newWord;
                                String file2 = "/search?q=" + newWord;
                                file = file.replaceAll("[\\s]+", "_");
                                file2 = file2.replaceAll("[\\s]+", "+");
                                wikiLink = new URL("https", "en.wikipedia.org", file);
                                googleLink = new URL("https", "google.com", file2);
                                dictionary.get(startingLetter).remove(myWord);
                                usedWords.add(myWord);
                                System.out.println("I've chosen: " + myWord + " Wikipedia link: " + wikiLink + " Google link: " + googleLink);
                                System.out.println("Your turn");
                                startingLetter = myWord.charAt(myWord.length() - 1);
                            } else {
                                lives--;
                                System.out.println("You have lost a life. You have " + lives + " lives remaining.");
                            }
                        }
                    } else {
                        System.out.println("Enter a valid country/city/state! Or enter p to pass.");
                        //lives--;
                    }
                }
            }
            System.out.println("You lasted " + (int) Math.floor(usedWords.size()) / 2 + " rounds against the CPU.");
        } else if (numPlayers > 1){
            currentPlayer = players.get(0);
            currentPlayerIndex = 0;
            while (numPlayers > 1) {
                System.out.println("Player: " + currentPlayer.name + ", Lives remaining: " + currentPlayer.remLives);
                System.out.println("Enter a country or city or state that starts with: \'" + startingLetter + "\' or enter p to pass.");
                String input = read.nextLine();
                if (!(input.matches("[a-zA-Z\s.]+"))) {
                    System.out.println("Enter a country/city/state. Only letters are allowed!");
                } else {
                    input = input.toLowerCase();
                    if (input.equals("p")) {
                        currentPlayer.remLives--;
                        String possible1 = pickWord();
                        //remainingWords.remove(possible1);
                        dictionary.get(startingLetter).remove(possible1);
                        System.out.println("You have lost a life:( You now have " + currentPlayer.remLives + " lives remaining.");
                        System.out.println("You could have guessed " + possible1 + " or " + pickWord());
                        startingLetter = changeStartingLetter();

                    } else if (isValid(input)) {
                        if (!(isUnused(input))) {
                            System.out.println("This country/city/state has been used already! You have lost a life :(  Enter p to pass.");
                            currentPlayer.reduceLife();
                            System.out.println("You have " + currentPlayer.remLives + " lives remaining.");
                        } else {
                            if (input.charAt(0) == startingLetter) {
                                System.out.println("OK.");
                                dictionary.get(startingLetter).remove(input);
                                usedWords.add(input);
                                startingLetter = input.charAt(input.length() - 1);
                                currentPlayerIndex = ((currentPlayerIndex + 1) % numPlayers);
                                currentPlayer = players.get(currentPlayerIndex);
                            } else {
                                currentPlayer.reduceLife();
                                System.out.println("You have lost a life. You have " + currentPlayer.remLives + " lives remaining.");
                            }
                        }
                    } else {
                        System.out.println("Enter a valid country/city/state! Or enter p to pass.");
                        //lives--;
                    }
                    if (currentPlayer.remLives == 0) {
                        numPlayers--;
                        System.out.println("Player " + currentPlayer.name + " has lost!\n There are " + numPlayers + " players remaining in the game.");
                        players.remove(currentPlayer);
                        if (numPlayers != 1) {
                            currentPlayer = players.get(currentPlayerIndex);
                        }
                    }

                }
            }
            currentPlayer = players.get(0);
            System.out.println("The winner is " + currentPlayer.name + " with " + currentPlayer.remLives + " lives remaining.");
        }
    }
}