package storage.impl;

import storage.Converter;
import storage.DataRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataRecordConverter implements Converter {
	@Override
	public byte[] serialize(DataRecord dataRecord) {
		byte[] keyBytes = dataRecord.getKey().getBytes(StandardCharsets.UTF_8);
		byte[] valueBytes = dataRecord.getValue().getBytes(StandardCharsets.UTF_8);

		ByteBuffer buffer = ByteBuffer.allocate(
			Integer.BYTES +     // key size
							Integer.BYTES +     // value size
							Long.BYTES +        // timestamp
							keyBytes.length +
							valueBytes.length
		);

		buffer.putInt(keyBytes.length);
		buffer.putInt(valueBytes.length);
		buffer.putLong(dataRecord.getTimestampEpochMillis());

		buffer.put(keyBytes);
		buffer.put(valueBytes);

		return buffer.array();
	}

	@Override
	public DataRecord deserialize(ByteBuffer sourceBuffer) {
		int keySize = sourceBuffer.getInt();
		int valueSize = sourceBuffer.getInt();
		long timestamp = sourceBuffer.getLong();

		if (keySize < 0 || valueSize < 0) {
			throw new IllegalArgumentException("Negative key/value size detected");
		}

		int expectedSize = 2 * Integer.BYTES + Long.BYTES + keySize + valueSize;
		if (sourceBuffer.capacity() < expectedSize) {
			throw new IllegalArgumentException("Corrupted record: insufficient bytes");
		}
		byte[] keyBytes = new byte[keySize];
		byte[] valueBytes = new byte[valueSize];

		sourceBuffer.get(keyBytes);
		sourceBuffer.get(valueBytes);

		String key = new String(keyBytes, StandardCharsets.UTF_8);
		String value = new String(valueBytes, StandardCharsets.UTF_8);

		return new DataRecord(key, value, timestamp);
	}
}

// Persistable record should look like:
// key_size value_size key value timestamp
