package application;

public interface KVStore {
	void put(String key, String value);
	String get(String key);
	boolean delete(String key);
}
