package engine;

import storage.RecordMetadata;

public interface KeyDirectory {
	void add(String key, RecordMetadata recordMetadata);
}
