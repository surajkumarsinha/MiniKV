package storage;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface WALReader {
	DataRecord read(RecordMetadata recordMetadata) throws IOException;
}
