import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WordIndex {


	private HashMap<String, HashSet<Integer>> pathsMap;

	
	public WordIndex() {
		this.pathsMap = new HashMap<>();
	}

	
	public HashSet<Integer> get(String path) {
		return pathsMap.get(path);
	}
	
	
	public void put(String path, HashSet<Integer> val) {
		pathsMap.put(path, val);
	}
	
	
	public Set<String> keySet() {
		return pathsMap.keySet();
	}
	
	
	public boolean add(String path, int position) {
		if (pathsMap.containsKey(path)) {
			if (pathsMap.get(path).contains(position)) {
				return false;
			} else {
				pathsMap.get(path).add(position);
				return true;
			}
		} else {
			pathsMap.put(path, new HashSet<Integer>());
			pathsMap.get(path).add(position);
			return true;
		}
	}

	
	public boolean addAll(String[] path) {
		return addAll(path, 1);
	}

	
	public boolean addAll(String[] path, int start) {
		boolean change = false;
		for (int i = 0; i < path.length; i++) {
			if (pathsMap.containsKey(path[i])) {
				if (pathsMap.get(path[i]).contains(start + i)) {
					change = false;
				} else {
					pathsMap.get(path[i]).add(start + i);
					change = true;
				}
			} else {
				pathsMap.put(path[i], new HashSet<Integer>());
				pathsMap.get(path[i]).add(start + i);
				change = true;
			}
		}
		return change;
	}

	
	public int count(String path) {
		if (pathsMap.containsKey(path)) {
			return pathsMap.get(path).size();
		} else {
			return 0;
		}
	}

	
	public int paths() {
		return pathsMap.size();
	}

	
	public boolean contains(String path) {
		return pathsMap.containsKey(path);
	}

	
	public boolean contains(String path, int position) {
		if (pathsMap.containsKey(path)) {
			if (pathsMap.get(path).contains(position)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	
	public ArrayList<String> copyPaths() {
		ArrayList<String> list = new ArrayList<String>();
		for (String key : pathsMap.keySet()) {
			list.add(key);
		}
		
		java.util.Collections.sort(list);
		
		return list;
	}

	
	public ArrayList<Integer> copyPositions(String path) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int pos : pathsMap.get(path)) {
			list.add(pos);
		}
		
		java.util.Collections.sort(list);
		
		return list;
	}

	/**
	 * Returns a string representation of this index.
	 */
	@Override
	public String toString() {
		return this.pathsMap.toString();
	}
}
