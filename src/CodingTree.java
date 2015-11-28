//Derek Moore, Heather Pedersen
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.PriorityQueue;

public class CodingTree {
	static final int MAP_SIZE = 32768;
	String text;
	MyHashTable<String, String> codes;
	MyHashTableQuadratic<String, String> codesQuad;
	private MyHashTable<String, Integer> frequencies;
	byte[] bits;
	private Node finishedTree;
	String bitString;
	

	public CodingTree(String fullText) {
		text = fullText;		
		codes = new MyHashTable<String, String>(MAP_SIZE);
		codesQuad = new MyHashTableQuadratic<String, String>(MAP_SIZE);
		frequencies = new MyHashTable<String, Integer>(MAP_SIZE);		
		bitString = "";
		doStuff();
	}

	private void doStuff() {
		countWordFrequency();		
		generateTree();
		generateCode();		
		encode();		
	}
	
	public static String decode(String theBitString, MyHashTable<String, String> theCodes) {
		StringBuilder result = new StringBuilder(), currentBits = new StringBuilder();		
		int len = theBitString.length(), curPos = 0;		
		
		MyHashTable<String, String> reverseCodesMap = new MyHashTable<String, String>(MAP_SIZE); 
		for(MyEntry<String, String> e : theCodes.toList()){
			String tempKey =  e.value, tempValue = e.key;			
			reverseCodesMap.put(tempKey, tempValue);			
		}		
		
		while(len-- > 1) {			
			currentBits.append(theBitString.charAt(curPos++));
			if(reverseCodesMap.contains(currentBits.toString())) {				
				result.append(reverseCodesMap.get(currentBits.toString()));
				currentBits.delete(0, currentBits.length());
				}
			}		
		return result.toString();
	}

	private void countWordFrequency() {
		StringBuilder temp = new StringBuilder();		
		for(char c : text.toCharArray()) {	
			if((c + "").matches("[a-zA-Z0-9]") || c == '\'' || (c == '-' ))  {
				temp.append(c);				
			} else {
				if (temp.length() > 0) {					
					if (!frequencies.contains(temp.toString()))	frequencies.put(temp.toString(), 1);
					else frequencies.put(temp.toString(), frequencies.get(temp.toString()) + 1);
				}
				if(!frequencies.contains("" + c)) frequencies.put("" + c, 1); 
				else frequencies.put(""+c, (frequencies.get(""+c) + 1));
				temp.delete(0, temp.length());
			}			
		}
		if (temp.length() > 0) {
			if (!frequencies.contains(temp.toString()))	frequencies.put(temp.toString(), 1);
			else frequencies.put(temp.toString(), frequencies.get(temp.toString()) + 1);
		}		
	}
	
	private void generateTree() {		
		ArrayList<MyEntry<String, Integer>> freqList = frequencies.toList();
		PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
		while(!freqList.isEmpty()) {			
			nodeQueue.add( new Node(freqList.remove(0)));
		}
		Node leftNode, rightNode, parentNode;

		while(nodeQueue.size() > 1) {
			parentNode = new Node(null);			
			leftNode = nodeQueue.poll();
			rightNode = nodeQueue.poll();
			parentNode.addleft(leftNode);
			parentNode.addRight(rightNode);
			nodeQueue.add(parentNode);
		}
		finishedTree = nodeQueue.poll();	
	}
	
	private void generateCode() {
		traverseTree(finishedTree, "");
	}
	
	private void traverseTree(Node n, String s) {				
		if(n.isLeaf()) {
			codes.put(n.data.key, s);
		}else {
		traverseTree(n.left, s += "0");		
		traverseTree(n.right, s = s.substring(0, s.length()-1) + "1");
		}
	}
	
	public void generateCodeQuad() {
		for(MyEntry<String, String> m : codes.toList())codesQuad.put(m.key, m.value);
	}

	
	
	
	private void encode() {
		int len = text.length(), curPos = 0, lastSeperator = 0 ; 
		StringBuilder sbBits = new StringBuilder("");		
		char curChar;
		while(len-- > 1) {
			curChar = text.charAt(curPos);
			if(!((curChar + "").matches("[a-zA-Z0-9]") || curChar == '\'' || (curChar == '-' )))  {				
				if(lastSeperator == 0 ) {								
					sbBits.append(codes.get(text.substring(lastSeperator, curPos)));					
				}else if(lastSeperator !=(curPos-1) ) {					
					sbBits.append(codes.get(text.substring(lastSeperator+1, curPos)));					
				}
				sbBits.append(codes.get(curChar+""));				
				lastSeperator = curPos;
			}
			curPos++;			
		}
		bitString = sbBits.toString().substring(4);		
		int index = 0, currentByte = 0;
		bits = new byte[bitString.length()/8 +1];
		Arrays.fill(bits, (byte)0);
		while(index <  bitString.length() - 8) {			
			if (bitString.length() - index > 7) {
				bits[currentByte] = ((byte) Integer.parseInt(bitString.substring(index, index + 8), 2));
				index += 8;
			} else {
				byte tempB = 0;
				for(int i =0; i<8; i++) {
					byte temp = (byte) Integer.parseInt(bitString.substring(index));
					tempB += temp << 7-i;
					index+=8;
				}
				bits[currentByte] = (tempB);
			}
			currentByte++;
		}
	}	

private class Node implements Comparable<Node>{
		
		Node left;
		Node right;
		MyEntry<String, Integer> data;
		int weight;

		Node(MyEntry<String, Integer> d){
			data = d;
			left = null;
			right = null;
			if(d==null) weight = 0;
			else weight = ((Integer)data.value);
		}

		void addleft(Node n) {
			this.left = n;
			weight += n.weight;
		}

		void addRight(Node n) {
			this.right = n;
			weight += n.weight;
		}

		boolean isLeaf() {
			return Objects.isNull(left) && Objects.isNull(right);
		}

		public int compareTo(Node other) {
			return weight - other.weight;
		}

		public String toString() {
			return "" + weight;
		}
	}
}
