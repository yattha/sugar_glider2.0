import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Scanner;
import static java.lang.System.out;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;


public class Main {
	static final String DEFAULT_TXT = "./src/WarAndPeace.txt";
	//static StringBuilder text;
	static CodingTree huffTree;
	
	public static void main(String[] args) {
		//testHashTable();
		readDefaultText();
		huffTree.frequencies.stats();
		//System.out.println(huffTree.codes);
		
		outputCodes();
		outputCompressed();
		//System.out.println(huffTree.codes.size());
		outputDecompressed();
		//decompressonFromFile();
//		for(Entry<String, Integer>m : huffTree.frequencies.entrySet()) {
//			System.out.println(m.getValue() + " : " + (int)m.getKey().charAt(0));
//		}
		//System.out.println(huffTree.frequencies);
	}

	private static void outputDecompressed() {
		
		
		
		try {
			FileWriter out = new FileWriter("./decompressed.txt");
			String decompressed = CodingTree.decode(huffTree.bitString, huffTree.codes);
			//System.out.println(decompressed);
			
			out.write(decompressed);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void outputCompressed() {
		
		try {
			FileOutputStream fos = new FileOutputStream("./compressed.txt");
			//System.out.println(huffTree.bits.length);
			fos.write(huffTree.bits);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void readDefaultText() {
		
		try {
			huffTree = new CodingTree(new String(readAllBytes(get(DEFAULT_TXT))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			String inText = new Scanner(new File(DEFAULT_TXT)).useDelimiter("\\A").next();
//					
//			huffTree = new CodingTree(inText);		
//		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}
			

	private static void testHashTable() {
		MyHashTable<String, String> test = new MyHashTable<String, String>(6);
		//System.out.println(test);
		test.put("0", "a");System.out.println(test);
		test.put("1", "b");System.out.println(test);
		
		test.put("2", "c");System.out.println(test);test.put("2", "d");System.out.println(test);
		test.stats();
		System.out.println(test.get("2"));
		System.out.println(test.get("1"));
		System.out.println(test.get("8"));
		
		
		
	}
	
	
	static String readFile(String path, Charset encoding) {			  
		String result = "";	
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			result = new String(encoded, encoding);
		} catch (IOException e) {				
			e.printStackTrace();
		}
		return result;
	}
	
	static void outputCodes() {
		
		try {
			FileWriter out = new FileWriter("./codes.txt");
			String codeString = huffTree.codes.toString();
			out.write(codeString);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	static void decompressonFromFile() {
		MyHashTable<String, String> inCodes = new MyHashTable<String, String>(CodingTree.MAP_SIZE);
		StringBuilder inBits = new StringBuilder(), tempByte = new StringBuilder();		
		File textFile = new File("./compressed.txt");		
		try {
			//long start = System.currentTimeMillis();
			FileInputStream stream = new FileInputStream(textFile);
			int byteRead = 1;	
			
			while(byteRead >= 0) {
				byteRead = stream.read();				
				tempByte.delete(0, tempByte.length());
				tempByte.append(Integer.toBinaryString(byteRead));
				while(tempByte.length()<8) tempByte.insert(0, '0');				
				inBits.append(tempByte.toString());
				//System.out.println(inBits.length());
			}
			stream.close();				
		} catch (IOException | NullPointerException e1) {
			e1.printStackTrace();
			}
		System.out.println(inBits.length());
		System.out.println(huffTree.bitString.length());
		
		
		
		
		File codeFile = new File("./codes.txt");
		StringBuilder codesSB = new StringBuilder();
		//long start= 0;
		try {			
			//start = System.currentTimeMillis();
			@SuppressWarnings("resource")
			FileInputStream stream = new FileInputStream(codeFile);
			int charRead = 1;			
			while(charRead > 0) {
				charRead = stream.read();
				codesSB.append(((char) charRead));
			}
			
			String codeString = codesSB.substring(1, codesSB.length()-2);
			String[] charWithCodeArray = codeString.split(", ");
			int i = 0;
			while(charWithCodeArray.length> i) {				
				inCodes.put(charWithCodeArray[i++].substring(1), charWithCodeArray[i].substring(0, charWithCodeArray[i++].length()-1));
				//if(i%100 == 0) System.out.println(charWithCodeArray[i].substring(1));
			}	
		} catch (IOException | NullPointerException e1) {}	
		
		PrintStream outputFile;		
		System.out.println(inCodes.get("message"));
		try {			
			outputFile = new PrintStream(new File("./decompressed.txt"));
			outputFile.append(CodingTree.decode(inBits.toString(), inCodes));
			outputFile.close();			
		} catch (FileNotFoundException e1) {}
		
	}	 
	

}
