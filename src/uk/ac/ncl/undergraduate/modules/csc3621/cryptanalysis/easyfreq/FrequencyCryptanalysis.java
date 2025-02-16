package uk.ac.ncl.undergraduate.modules.csc3621.cryptanalysis.easyfreq;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * This class is for frequency cryptanalysis of ciphertext when the key is an
 * integer.
 *
 * @author Changyu Dong
 * @author Roberto Metere
 * @author Yathartha Sharma
 */
public class FrequencyCryptanalysis {

	/**
	 * The ciphertext (encryption of the plaintext).
	 */
	private String ciphertext;

	/**
	 * The plaintext (readable content).
	 */
	private String plaintext;

	/**
	 * The key such that the encryption of the plaintext with such key gives the
	 * ciphertext.
	 */
	private int key;

	/**
	 * Frequency table (of the ciphertext).
	 */
	private FrequencyTable table;

	/**
	 * INTERACTIVE means that you can manually tune the analysis and/or the
	 * result.
	 */
	public static final boolean INTERACTIVE = true;

	/**
	 * AUTOMATIC means that the analysis will not ask any further information.
	 */
	public static final boolean AUTOMATIC = false;

	/**
	 * This variable is just to run the script interactive, that is with manual
	 * tunes.
	 */
	private boolean interactive = AUTOMATIC;

	/**
	 * Set the ciphertext to analyse.
	 *
	 * @param text
	 *            the text to set as ciphertext
	 */
	public void setCiphertext(String text) {
		this.ciphertext = text;
	}

	/**
	 * Create an new class to cryptanalyze texts.
	 */
	public FrequencyCryptanalysis() {
	}

	/**
	 * Constructor with interactive choice.
	 *
	 * @param interactive
	 *            whether it should ask for manual tuning or not
	 */
	public FrequencyCryptanalysis(boolean interactive) {
		this.interactive = interactive;
	}

	/**
	 * This method is to allow you to manually set the key can be used as a
	 * subroutine in your cryptanalysis for manual adjustment
	 */
	private void manualAdjustment() {
		int i;

		System.out.println("Enter the key (0-25): ");
		i = Util.reader.nextInt(); // Scans the next token of the input as an
									// int.
		if (i >= 0 && i <= 25) {
			this.key = i;
			System.out.println("The key is set to " + this.key);
		} else {
			System.out.println("The key is invalid (must be an integer between 0 and 25 included).");
		}
	}

	/**
	 * This method conducts cryptanalysis of the frequency of letters in the
	 * ciphertext to retrieve the encryption key.
	 *
	 * <p>
	 * TODO:
	 * <ul>
	 * <li>Conduct a frequency analysis of the internal buffer.
	 * <li>Find the key. You should try your best to find the key based on your
	 * analysis.
	 * <li>Store the key in the class variable <code>this.key</code>.
	 * </ul>
	 *
	 * <p>
	 * Manual adjustment in the method is allowed but needs to be justified in
	 * your report. You can create methods as you like.
	 *
	 * @return the key as the result of the cryptanalysis
	 */
	public int cryptanalysis() {
		// Please, do not remove the editor-fold comments.
		// <editor-fold defaultstate="collapsed" desc="Write your code here
		// below!">
		char maxChar = AnalyseText.maxCharByFilePath("/res/pg1661.txt");		// getting char with max frequency for english by doing frequency analysis on pg1661.txt file
		char maxCharForCiphertext = AnalyseText.maxCharByFilePath("/res/Exercise1Ciphertext.txt");  // getting char with max frequency for ciphertext file by doing frequency analysis.txt file
		int key = (int) maxCharForCiphertext - (int) maxChar; 	// getting the shift
		this.key = key;											// returning the key
		// </editor-fold> // END OF YOUR CODE
		// The following code allows you to manually adjust your result.
		if (this.interactive) {
			String answer;
			do {

				do {
					System.out.println("Do you want to see the plaintext (Y/N)? ");
					answer = Util.reader.next().toUpperCase();
				} while (!(answer.equals("Y") || answer.equals("N")));

				if (answer.equals("Y")) {
					this.decrypt();
					System.out.println(this.plaintext);
				}

				do {
					System.out.println("Do you want to see the key (Y/N)? ");
					answer = Util.reader.next().toUpperCase();
				} while (!(answer.equals("Y") || answer.equals("N")));

				if (answer.equals("Y")) {
					System.out.println(this.key);
				}

				do {
					System.out.println("Do you want to change the key (Y/N)? ");
					answer = Util.reader.next().toUpperCase();
				} while (!(answer.equals("Y") || answer.equals("N")));

				if (answer.equals("Y")) {
					this.manualAdjustment();
				}

				do {
					System.out.println("Do you want to stop (Y/N)? ");
					answer = Util.reader.next().toUpperCase();
				} while (!(answer.equals("Y") || answer.equals("N")));

			} while (!answer.equals("Y"));
		}

		return this.key;
	}

	/**
	 * This method reconstructs the plaintext from the ciphertext with the key.
	 *
	 * <p>
	 * TODO:
	 * <ul>
	 * <li>After finding the key, use the key to decrypt the ciphertext
	 * <li>Store the plaintext in the class variable
	 * <code>this.plaintext</code>.
	 * </ul>
	 */
	public void decrypt() {
		// Please, do not remove the editor-fold comments.
		// <editor-fold defaultstate="collapsed" desc="Write your code here
		// below!">
		String mainPath, cipherPlaintextFilePath;
		byte[] bytes;
		try {
			mainPath = Paths.get(FrequencyCryptanalysis.class.getResource("/").toURI()).toString();
			cipherPlaintextFilePath = mainPath + "/res/Exercise1Ciphertext.txt";
			bytes = Files.readAllBytes(Paths.get(cipherPlaintextFilePath));
			String ciphertext = new String(bytes, StandardCharsets.UTF_8);			// getting the ciphertext string data from Exercise1Ciphertext file
			char[] plainText = new char[ciphertext.length()];						// array of chars > plaintext
			for (int i = 0; i < ciphertext.length(); i++) {							// iterate through ciphertext string's chars
				char ch = Character.toUpperCase(ciphertext.charAt(i));				// convert chars to uppercase
				if (ch >= 69 && ch <= 90) {											// if character are in the range E-Z
					ch = (char) ((int) ch - (key % 26));							// shift characters using the key
					plainText[i] = ch;												// add decrypted character to the array of plaintext
				} else if (ch > 64 && ch < 69) {									// if characters are A-D then apply extra measures
					if (ch == 65) {
						ch = 87;
						plainText[i] = ch;
					} else if (ch == 66) {
						ch = 88;
						plainText[i] = ch;
					} else if (ch == 67) {
						ch = 89;
						plainText[i] = ch;
					} else {
						ch = 90;
						plainText[i] = ch;
					}
				} else {														// if characters are punctuation/space, add them without decrypting
					plainText[i] = ch;
				}
			}
			this.plaintext = new String(plainText);								// return array of plaintext as string
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// </editor-fold> // END OF YOUR CODE
	}

	/**
	 * Show the results of the complete analysis.
	 */
	public void showResult() {
		System.out.println("The key is " + this.key);
		this.decrypt();
		System.out.println("The plaintext is:");
		System.out.println(this.plaintext);
	}

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 *             errors reading from files
	 * @throws java.net.URISyntaxException
	 *             Errors in retrieving resources
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		String mainPath, plaintextFilePath, ciphertextFilePath, plaintext, ciphertext;
		FrequencyAnalyser frequencyAnalyser;
		FrequencyTable frequencyTable;
		FrequencyCryptanalysis cryptanalysis;
		File solutionDirectory;
		String solutionFrequencyFilePath, solutionKeyFilePath, solutionPlaintextFilePath;

		// Add argument -i at run to enable interactive mode (and disable
		// automatic mode)
		if (0 < args.length && args[0].equals("-i")) {
			cryptanalysis = new FrequencyCryptanalysis(INTERACTIVE);
		} else {
			cryptanalysis = new FrequencyCryptanalysis(AUTOMATIC);
		}

		// Get resources
		mainPath = Paths.get(FrequencyCryptanalysis.class.getResource("/").toURI()).toString();
		plaintextFilePath = mainPath + "/res/pg1661.txt";
		ciphertextFilePath = mainPath + "/res/Exercise1Ciphertext.txt";
		solutionDirectory = new File(mainPath + "/solution1");
		solutionFrequencyFilePath = solutionDirectory + "/frequency.txt";
		solutionKeyFilePath = solutionDirectory + "/key.txt";
		solutionPlaintextFilePath = solutionDirectory + "/plaintext.txt";

		// Analyse the readable text
		plaintext = Util.readFileToBuffer(plaintextFilePath);
		frequencyAnalyser = new FrequencyAnalyser();
		frequencyAnalyser.setText(plaintext);
		frequencyTable = frequencyAnalyser.analyse();
		frequencyTable.print();

		// Crack the ciphertext
		ciphertext = Util.readFileToBuffer(ciphertextFilePath);
		cryptanalysis.setCiphertext(ciphertext);
		cryptanalysis.cryptanalysis();
		cryptanalysis.showResult();

		// Write solution in res path
		if (!solutionDirectory.exists()) {
			solutionDirectory.mkdir();
		}
		Util.printBufferToFile(frequencyTable.toString(), solutionFrequencyFilePath);
		Util.printBufferToFile(Integer.toString(cryptanalysis.key), solutionKeyFilePath);
		Util.printBufferToFile(cryptanalysis.plaintext, solutionPlaintextFilePath);
	}
}