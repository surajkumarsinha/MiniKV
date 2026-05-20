package application;

import engine.KeyDirectory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.DataRecord;
import storage.RecordMetadata;
import storage.WALReader;
import storage.WALWriter;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.NoSuchElementException;

public class KVStoreImpl implements KVStore {
    private static final Logger logger = LogManager.getLogger(KVStoreImpl.class);

    private final WALWriter walWriter;
    private final WALReader walReader;
    private final KeyDirectory keyDirectory;

    public KVStoreImpl(
        WALWriter walWriter,
        WALReader walReader,
        KeyDirectory keyDirectory
    ) {
        this.walWriter = walWriter;
        this.walReader = walReader;
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
        try {
            RecordMetadata recordMetadata = keyDirectory.get(key);
            if (recordMetadata == null) throw new NoSuchElementException("No value found against the key");
            DataRecord dataRecord = walReader.read(recordMetadata);
            return dataRecord.getValue();
        } catch (Exception e) {
            logger.error("Failed to get record for key: " + key, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String key) {
        throw new UnsupportedOperationException("Method yet to be implemented");
    }
}
