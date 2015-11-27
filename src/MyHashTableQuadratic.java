import java.util.Objects;


public class MyHashTableQuadratic <K, V> extends MyHashTable<K, V> {
	static final int QUAD_STEP = 7;
	public MyHashTableQuadratic(int c) {
		super(c);		
	}

	@Override
	public void put(K searchKey, V newValue) {
		int index = hash(searchKey), steps = 0;
		MyEntry<K, V> temp = new MyEntry<K, V>(searchKey, newValue);
		while(Objects.nonNull(data.get(index)) && !searchKey.equals(data.get(index).key)) {
			data.get(index).probedOver();
			temp.probe();
			index+=QUAD_STEP*steps++;	
			index%=capacity;			
		}
		if(!contains(searchKey)){			
			numEnts++;
			probeHGram[temp.probeCount]++;			
		}
		data.set(index, temp);		
		maxProbe = Math.max(temp.probeCount, maxProbe);
	}

	@Override
	V get(K searchKey) {
		int index = hash(searchKey), steps = 0;
		MyEntry<K, V> result = data.get(index);
		while(result != null && result.probed && !searchKey.equals(result.key)) {
			index+=QUAD_STEP*steps++;			
			index%=capacity;
			result = data.get(index);
			
		}
		if(!searchKey.equals(result.key)) result = null;	
		
		V returnVal = null;
		if(Objects.nonNull(result)) returnVal = result.value;
		
		return returnVal;
	}

	@Override
	public boolean contains(K searchKey) {
		int index = hash(searchKey), steps = 0;
		boolean result = false;
		MyEntry<K, V> checkedEntry = data.get(index);
		while(checkedEntry != null && checkedEntry.probed && !searchKey.equals(checkedEntry.key)) {
			index+=QUAD_STEP*steps++;			
			index%=capacity;
			checkedEntry = data.get(index);
			
		}
		if(checkedEntry != null && searchKey.equals(checkedEntry.key))result = true;
		return result;
	}

}
