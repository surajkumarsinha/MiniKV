package application;

import engine.KeyDirectory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.RecordMetadata;
import storage.WALWriter;

import java.io.IOException;

public class KVStoreImpl implements KVStore {
    private static final Logger logger = LogManager.getLogger(KVStoreImpl.class);

    private final WALWriter walWriter;
    private final KeyDirectory keyDirectory;

    public KVStoreImpl(WALWriter walWriter, KeyDirectory keyDirectory) {
        this.walWriter = walWriter;
        this.keyDirectory = keyDirectory;
    }

    @Override
    public void put(String key, String value) {
        try {
            RecordMetadata recordMetadata = walWriter.write(key, value);
            keyDirectory.add(key, recordMetadata);
        } catch (IOException e) {
            // In a real application, we might want to have a more specific
            // exception type to indicate a storage failure.
            logger.error("Failed to write record for key: " + key, e);
            // For now, we log and swallow, but the client is not notified of the failure.
            // Let's re-throw a runtime exception.
            throw new RuntimeException("Failed to put key: " + key, e);
        }
    }

    @Override
    public String get(String key) {
        throw new UnsupportedOperationException("Method yet to be implemented");
    }

    @Override
    public boolean delete(String key) {
        throw new UnsupportedOperationException("Method yet to be implemented");
    }
}
