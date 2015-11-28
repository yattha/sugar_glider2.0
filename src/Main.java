//Derek Moore, Heather Pedersen
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
	static final String DEFAULT_TXT = "./WarAndPeace.txt", PRIDE_TXT = "./PridePrej.txt", XMAS_TXT = "./Christmas.txt";	
	static CodingTree defaultTree, prideTree, xmasTree;
	
	public static void main(String[] args) {
		//testHashTable();
		readTexts();
		System.out.println("\n\nDefault(WarAndPeace.txt) stats...");
		defaultTree.codes.stats();
		outputCodes();
		outputCompressed();	
		outputDecompressed();
		
		//COMMENT OUT NEXT LINE TO DECOMPRESS FROM FILE (specifically works for DEFAULT_TXT, change path or use default files names in project folder
		//decompressonFromFile();
	}

	private static void outputDecompressed() {		
		try {
			FileWriter out = new FileWriter("./DefaultDecompressed.txt");
			String decompressed = CodingTree.decode(defaultTree.bitString, defaultTree.codes);			
			out.write(decompressed);
			out.flush();
			out.close();
			System.out.println("Output decompressed file: DefaultDecomressed.txt");
			
			
			
			FileWriter out1 = new FileWriter("./PrideDecompressed.txt");
			decompressed = CodingTree.decode(prideTree.bitString, prideTree.codes);			
			out1.write(decompressed);
			out1.flush();
			out1.close();
			System.out.println("Output decompressed file: PrideDecomressed.txt");
			
			
			
			FileWriter out2 = new FileWriter("./XMASDecompressed.txt");
			decompressed = CodingTree.decode(xmasTree.bitString, xmasTree.codes);			
			out2.write(decompressed);
			out2.flush();
			out2.close();
			System.out.println("Output decompressed file: XMASDecomressed.txt");
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}

	private static void outputCompressed() {		
		try {
			FileOutputStream fos = new FileOutputStream("./DefaultCompressed.txt");			
			fos.write(defaultTree.bits);
			fos.close();
			System.out.println("Output compressed file: DefaultComressed.txt");
			
			
			fos = new FileOutputStream("./PrideCompressed.txt");			
			fos.write(prideTree.bits);
			fos.close();
			System.out.println("Output compressed file: DefaultComressed.txt");
			
			
			
			fos = new FileOutputStream("./XMASCompressed.txt");			
			fos.write(xmasTree.bits);
			fos.close();
			System.out.println("Output compressed file: XMASComressed.txt\n");
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}

	private static void readTexts() {
		long start = System.currentTimeMillis();
		defaultTree = new CodingTree(readFile(DEFAULT_TXT, Charset.defaultCharset()));
		System.out.println("Uncompressed file size: " + defaultTree.text.length() + " bytes");
		System.out.println("Compressed file size: " + defaultTree.bitString.length()/8 +" bytes");
		System.out.println("Compression ratio: " + (int)((defaultTree.bitString.length()/8)/(double)defaultTree.text.length()*100) +"%");
		System.out.println("Running Time: "+ (System.currentTimeMillis()-start) + "ms\n");
		start = System.currentTimeMillis();
		prideTree = new CodingTree(readFile(PRIDE_TXT, Charset.defaultCharset()));
		System.out.println("Uncompressed file size: " + prideTree.text.length() + " bytes");
		System.out.println("Compressed file size: " + prideTree.bitString.length()/8 +" bytes");
		System.out.println("Compression ratio: " + (int)((prideTree.bitString.length()/8)/(double)prideTree.text.length()*100) +"%");
		System.out.println("Running Time: "+ (System.currentTimeMillis()-start) + "ms\n");
		start = System.currentTimeMillis();
		xmasTree = new CodingTree(readFile(XMAS_TXT, Charset.defaultCharset()));
		System.out.println("Uncompressed file size: " + xmasTree.text.length() + " bytes");
		System.out.println("Compressed file size: " + xmasTree.bitString.length()/8 +" bytes");
		System.out.println("Compression ratio: " + (int)((xmasTree.bitString.length()/8)/(double)xmasTree.text.length()*100) +"%");
		System.out.println("Running Time: "+ (System.currentTimeMillis()-start) + "ms");
		start = System.currentTimeMillis();
		}
			

	@SuppressWarnings("unused")
	private static void testHashTable() {
		MyHashTable<String, String> test = new MyHashTable<String, String>(6);		
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
		System.out.println(path+" read finshed...");
		return result;
	}
	
	static void outputCodes() {
		
		try {
			FileWriter out = new FileWriter("./codes.txt");
			String codeString = defaultTree.codes.toString();
			out.write(codeString);
			out.flush();
			System.out.println("Output default code file: codes.txt");
			out.close();
			
			out = new FileWriter("./PrideCodes.txt");
			codeString = prideTree.codes.toString();
			out.write(codeString);
			out.flush();
			System.out.println("Output Pride code file: PrideCodes.txt");
			out.close();
			
			out = new FileWriter("./XMASCodes.txt");
			codeString = xmasTree.codes.toString();
			out.write(codeString);
			out.flush();
			System.out.println("Output XMAS code file: XMASCodes.txt\n");
			
			out.close();
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
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
		System.out.println(defaultTree.bitString.length());		
		
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
