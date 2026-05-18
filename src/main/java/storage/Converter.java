package storage;

import java.nio.ByteBuffer;

public interface Converter {
	byte[] serialize(DataRecord dataRecord);
	DataRecord deserialize(ByteBuffer sourceBuffer);
}
