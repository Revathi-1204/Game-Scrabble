
package com.boardui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Dictionary 
{
    private ArrayList<String> wordList;

    public Dictionary()
    {
        wordList = new ArrayList<>();
        try
        {
            addFileToDictionary();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void addFileToDictionary() throws FileNotFoundException
    {
        File file = new File("C:\\Users\\anush.a\\Documents\\scrabble-javafx\\sowpods.txt");
        try (Scanner sc = new Scanner(file))
        {
            while (sc.hasNextLine())
            {
                String word = sc.nextLine();
                word = word.toLowerCase();
                wordList.add(word);
            }
        }
    }

    public boolean verifyWord(String word)
    {
        word = word.toLowerCase();
        return wordList.contains(word);
    }

	public static void main (String[] args){
		Dictionary Dict = new Dictionary();
		try{
		Dict.addFileToDictionary();
		}
		catch(Exception e){
			System.out.println("Not Found");
		}
	}
}
