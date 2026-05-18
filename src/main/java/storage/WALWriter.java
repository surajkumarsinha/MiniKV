package storage;

import java.io.IOException;

public interface WALWriter {
	RecordMetadata write(String key, String value) throws IOException;
}
