public class MyEntry<K, V> {
	K key;
	V value;
	boolean probed;
	int probeCount;		


	public MyEntry(K k, V v) {
		key = k;
		value = v;
		probed = false;
		probeCount = 0;
	}

	void probe(){			
		probeCount++;
	}

	public String toString() {			
		return "[" + key + ", "  +value + ", " + probed +", " + probeCount + "]";
	}

	void probedOver() {
		probed = true;
	}
	
	
	

}