//Derek Moore, Heather Pedersen
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MyHashTable <K, V> {
	static final int LINEAR_STEP = 1;
	List<MyEntry<K, V>> data;
	int capacity, numEnts, maxProbe;
	int[] probeHGram;
	
	
	public MyHashTable(final int c) {
		capacity = c;
		data = new ArrayList<MyEntry<K, V>>(capacity);
		populateEmptyArray();
		numEnts = 0;
		probeHGram = new int[capacity];
		Arrays.fill(probeHGram, 0);
		maxProbe = 0;		
	}
	
	

	public void put(K searchKey, V newValue) {		
		int index = hash(searchKey);
		MyEntry<K, V> temp = new MyEntry<K, V>(searchKey, newValue);
		while(Objects.nonNull(data.get(index)) && !searchKey.equals(data.get(index).key)) {
			data.get(index).probedOver();
			temp.probe();
			index+=LINEAR_STEP;
			index%=capacity;			
		}
		if(!contains(searchKey)){			
			numEnts++;
			probeHGram[temp.probeCount]++;			
		}
		data.set(index, temp);		
		maxProbe = Math.max(temp.probeCount, maxProbe);		
	}
	
	V get(K searchKey) {		
		int index = hash(searchKey);
		MyEntry<K, V> result = data.get(index);
		while(result != null && result.probed && !searchKey.equals(result.key)) {
			index+=LINEAR_STEP;			
			index%=capacity;
			result = data.get(index);			
		}		
		V returnVal = null;
		if(Objects.nonNull(result) && searchKey.equals(result.key)) returnVal = result.value;		
		return returnVal;
	}
	
	public boolean contains(K searchKey) {
		int index = hash(searchKey);
		boolean result = false;
		MyEntry<K, V> checkedEntry = data.get(index);
		while(checkedEntry != null && checkedEntry.probed && !searchKey.equals(checkedEntry.key)) {
			index+=LINEAR_STEP;			
			index%=capacity;
			checkedEntry = data.get(index);
			
		}
		if(checkedEntry != null && searchKey.equals(checkedEntry.key))result = true;
		return result;		
	}
	
	public void stats() {
		StringBuilder result = new StringBuilder();
		result.append("Hash Table Stats\n= = = = = = = = = = = = = = = = = = = =\nNumber of Entries: ");
		result.append(numEnts + "\nNumber of Buckets: " + capacity +"\nHistogram of Probes: ");
		result.append(histogramToString() + "\nFill Percentage: " + (numEnts/(double)capacity)*100 + "%");
		result.append("\nMax Probe: " + maxProbe + "\nAverage Linear Prob: " + calcAvgProbe() );
		System.out.println(result.toString()+"\n\n\n");
	}

	public ArrayList<MyEntry<K, V>> toList() {
		ArrayList<MyEntry<K, V>>  result= new ArrayList<MyEntry<K, V>>();
		for(int i = 0; i<data.size(); i++)if(data.get(i) !=null)result.add(data.get(i));		
		return result;
	}
	
	 double calcAvgProbe() {
		int result = 0;
		for(int i = 0; i < maxProbe; i++) {
			result+=i*probeHGram[i];
		}
		return result/(double)numEnts;
	}

	 String histogramToString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for(int i = 0; i < maxProbe; i++)result.append(probeHGram[i] + ", ");
		if(maxProbe > 0) result.delete(result.length()-2, result.length()).append(']');
		else result.append(']');
		return result.toString();
	}	

	int hash(K searchKey) {					
		return ((searchKey).hashCode()&0x7FFF)%capacity;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();		
		result.append('[');
		for(int i = 0; i < capacity; i++){
			if(data.get(i) != null){				
				result.append("(" + data.get(i).key + ", " + data.get(i).value + "), ");
			}
		}
		return result.toString().substring(0, result.length() -2 ) +"]" ;
	}
	
	 void populateEmptyArray() {
		for(int i = 0; i < capacity; i++)data.add(null);		
	}
	
	 int iterativeNumberElements() {
		int result=0;
		for(int i = 0; i<data.size(); i++)if(data.get(i) !=null)result++;	
		return result;
	}	
}
