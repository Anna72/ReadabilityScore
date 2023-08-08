package readability;

import javax.imageio.IIOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static float countScore(int sentences, int words, int letters) {
        float score = (float) (4.71*((float) letters / (float) words) +
                        0.5 * ((float) words / (float) sentences) - 21.43);
        return score;
    }

    public static float fleschKincaid(int sentences, int words, int syllables){
        float score = (float) (0.39 *((float) words / (float) sentences) +
                11.8 * ((float) syllables / (float) words) - 15.59);
        return score;
    }

    public static float sMOG(int sentences, int poly){
        float score = (float) (1.043 * Math.sqrt((poly * (30/sentences))) + 3.1291);
        return score;
    }

    public static float colemanLiau(int sentences, int words, int letters){
        float score =(float) (0.0588 *((float) letters / (float) words * 100.0) -
                0.296 * ((float) sentences / (float) words * 100.0) - 15.8);
        return score;
    }

    public static boolean isVowel(char c){
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y';
    }

    public static int countSyllables(String word) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (isVowel(c)){
                if (i == 0) {
                    count++;
                } else {
                    if (!isVowel(word.charAt(i-1)) && !(i == word.length() - 1 && c =='e')){
                        count++;
                    }
                }
            }
        }
        return count == 0 ? 1 : count;
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = " ";
        try {
            System.out.println(input = readFileAsString(args[0]));
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }


        System.out.println("The text is: \n" + input);
        String[] inputs = input.split("[!.?] ");
        int sentences = inputs.length;
        int words = 0;
        int letters = 0;
        int syllables = 0;
        int polysyllables = 0;
        for (char c : input.toCharArray()) {
            if (c != ' ') letters++;
        }

        for(String s: inputs){
            String[] w = s.split(" ");
            words += w.length;
            for ( String word : w) {
                int wordSyllables = countSyllables(word);
                syllables += wordSyllables;
                if (wordSyllables >2){
                    polysyllables++;
                }
            }
        }

        System.out.println("Words: " + words + "\nSentences: " + sentences
            + "\nCharacters: " + letters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        String system = scan.nextLine();

        float score = countScore(sentences, words, letters);
        float fk = fleschKincaid(sentences, words, syllables);
        float smog = sMOG(sentences, polysyllables);
        float cl = colemanLiau(sentences, words, letters);
        int age = (int) Math.ceil(score) + 5;
        int ageFK = (int) Math.ceil(fk) + 5;
        int ageSMOG = (int) Math.ceil(smog) + 5;
        int ageCL = (int) Math.ceil(cl) + 5;
        float avAge = (float) ((float)age + (float)ageCL + (float)ageSMOG + (float)ageFK)/4;
        if (system.equals("all")) {


            System.out.printf("Automated Readability Index: %f (about %d-year-olds).\n", score, age + 5);
            System.out.printf("Flesch–Kincaid readability tests: %f (about %d-year-olds).\n", fk, ageFK);
            System.out.printf("Simple Measure of Gobbledygook: %f (about %d-year-olds).\n", smog, ageSMOG);
            System.out.printf("Coleman–Liau index: %f (about %d-year-olds).\n", cl, ageCL);


            System.out.printf("This text should be understood in average by %f-year-olds.", avAge);
        }

    }
}
