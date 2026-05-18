package engine;

import storage.RecordMetadata;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryKeyDirectory implements KeyDirectory {
    private final ConcurrentMap<String, RecordMetadata> index = new ConcurrentHashMap<>();

    @Override
    public void add(String key, RecordMetadata recordMetadata) {
        index.put(key, recordMetadata);
    }
}
