package storage;

public interface WALWriter {
	void write(String key, String value);
}
