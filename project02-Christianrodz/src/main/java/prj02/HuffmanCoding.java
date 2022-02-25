package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Christian G Rodriguez Berrios <802-18-1892> (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	public static void main(String[] args) {
			HuffmanEncodedResult();
		
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("stringData6.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * Receives a String and computes each unique UTF-8 character's frequency inside the String,
	 * places the unique character as the Key with its frequency being its Value,
	 * and returns the Map
	 *
	 * @param inputString given string encoded in UTF-8 to have its frequency distribution computed
	 * @return Map containing the frequency distribution of every unique UTF-8 character in the String
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		HashTableSC<String, Integer> FreqD = new HashTableSC<String, Integer>(inputString.length(), new SimpleHashFunction<String>());
		/*Loop through the inputString
		 * if the frequency distribution map does not have that key we add it to the map alongside
		 * its value, which is the key's frequency obtained with an auxiliary method*/
		for(int i = 0; i < inputString.length();i++) {
			if(!FreqD.containsKey(inputString.substring(i,i+1))) {
				FreqD.put(inputString.substring(i,i+1), countFreq(inputString.charAt(i),inputString));
			}
		}
		return FreqD;
	}

	/**
	 * Receives a map with the frequency distribution,
	 * Builds the huffman tree starting from the leaves up to the root using a SortedLinkedList
	 * returns the root node of the corresponding huffman tree.
	 *
	 * @param fD a map with the frequency distribution
	 * @return root node of the corresponding huffman tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
		BTNode<Integer,String> rootNode = null;
		SortedLinkedList<BTNode<Integer,String>> BTList = new SortedLinkedList<>();
		/*Loop through all the keys in the frequency distribution map
		 * add a new binary tree node with the current character's frequency as the Key 
		 * and the character as the Value to the SortedLinkedList
		 */
		for(String Keys: fD.getKeys()) {
			BTList.add(new BTNode<Integer,String>(fD.get(Keys), Keys));
		}
		/*Loop through SortedLinkedList to build Binary Tree starting from leaves until it leaves one element
		 * Make a empty Binary Tree Node which will be the father of the two leaves x and y
		 * x and y are the left and right child of the BTN respectively
		 * set the father's key and value as the sum of his children's keys and values
		 * add the father to the SortedLinkedList
		 */
		for(int i = 0; i < BTList.size()-1;) {
		    rootNode = new BTNode<Integer,String>();
			BTNode<Integer,String> x = BTList.removeIndex(i);
			BTNode<Integer,String> y = BTList.removeIndex(i);
			rootNode.setLeftChild(x);
			rootNode.setRightChild(y);
			rootNode.setKey(x.getKey()+y.getKey());
			rootNode.setValue(x.getValue()+y.getValue());
			BTList.add(rootNode);
		}
		//the only element left in the SortedLinkedList is the root
		return BTList.removeIndex(0);
	}

	/**
	 * Receives the root of a huffman tree and returns a mapping
	 * of every symbol to its corresponding huffman code
	 * with help from an auxiliary method
	 * 
	 * @param huffmanRoot is the root of a huffman tree
	 * @return codeMap a mapping of every symbol with its corresponding huffman code
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		/* TODO Construct Prefix Codes */
		HashTableSC<String, String> codeMap = new HashTableSC<String, String>(new SimpleHashFunction<String>());
		codeMapper("", codeMap,huffmanRoot);
		return codeMap;
	}

	/**
	 * Receives the huffman code map and the input string,
	 * and returns the huffman encoded string
	 *
	 * @param encodingMap a mapping of every symbol with its corresponding huffman code
	 * @param inputString Original String that will get encoded
	 * @return Encoded string
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */
		String result = "";
		/*Looping through the given string 
		 * Make a string utilizing the huffman prefix code map 
		 * to change every letter to its corresponding prefix code and adding it */
		for(int i = 0; i < inputString.length(); i++) {
			result = result + encodingMap.get(inputString.substring(i,i+1));
		}
		

		return result; 
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	/**
	 * Receives a character and the input String
	 * and returns the frequency of the given character in the String
	 * 
	 * @param toCount character whose frequency will get counted
	 * @param inputString String to count frequency of given character
	 * @return frequency of given character
	 */
	
	private static int countFreq(char toCount, String inputString) {
		//TODO finish implementation
		int counter = 0;
		/*Looping through the String
		 increase counter every time the Character shows up in it. */
		for(int i = 0; i < inputString.length(); i++) {
			if(inputString.charAt(i) == toCount) {
				counter++;
			}
		}
		return counter;
	}
	
			
	/**
	 * Receives a String with a Code, 
	 * the current mapping of every symbol with its corresponding Huffman code,
	 * and the root of the Huffman tree and with recursion it completes the mapping.
	 * @param Code current string of moves required to traverse the tree to get to a Node
	 * @param codeMap current mapping of every symbol with its corresponding Huffman code
	 * @param huffmanRoot root of the Huffman tree
	 */
	private static void codeMapper(String Code, HashTableSC<String,String> codeMap,BTNode<Integer,String> huffmanRoot) {
		if(huffmanRoot.getRightChild() == null && huffmanRoot.getLeftChild() == null) {
			//If we're a leaf we put the value and the traversal Code we've written, then return
			codeMap.put(huffmanRoot.getValue(),Code);
			return;
		}else {
			//Recursive Call to move to the left of the tree, adding a 0 to the traversal code
			codeMapper(Code + "0", codeMap,huffmanRoot.getLeftChild());
			//Recursive Call to move to the right of the tree, adding a 1 to the traversal code
			codeMapper(Code + "1", codeMap, huffmanRoot.getRightChild());
		}
		
	}

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable Prefix Code Map
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
