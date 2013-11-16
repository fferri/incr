package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiHashMap<K, V> extends HashMap<K, Set<V>> {
	private static final long serialVersionUID = 1L;

	public MultiHashMap() {
		super();
	}
	
	public MultiHashMap(int initialCapacity) {
		super(initialCapacity);
	}
	
	public MultiHashMap(Map<? extends K, ? extends Set<V>> m) {
		super(m);
	}
	
	public MultiHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	public void add(K k, V v) {
		Set<V> s = get(k);
		if(s == null) {
			s = new HashSet<>();
			s.add(v);
			put(k, s);
		} else {
			s.add(v);
		}
	}
	
	public void remove(K k, V v) {
		if(containsKey(k))
			get(k).remove(v);
	}
}
