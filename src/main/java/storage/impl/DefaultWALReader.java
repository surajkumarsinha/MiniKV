package storage.impl;

import storage.Converter;
import storage.DataRecord;
import storage.RecordMetadata;
import storage.SegmentManager;
import storage.WALReader;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DefaultWALReader implements WALReader {

	private final SegmentManager segmentManager;
	private final Converter converter;

	public DefaultWALReader(
		SegmentManager segmentManager,
		Converter converter
	) {
		this.segmentManager = segmentManager;
		this.converter = converter;
	}

	@Override
	public DataRecord read(RecordMetadata recordMetadata) throws IOException {
		return segmentManager.withReadChannel(recordMetadata.path(), channel -> {
			ByteBuffer buffer = ByteBuffer.allocate(recordMetadata.size());
			int bytesRead = channel.read(buffer, recordMetadata.offset());
			if (bytesRead != recordMetadata.size()) {
				throw new IOException("Failed to read expected number of bytes. Expected: " 
					+ recordMetadata.size() + ", Read: " + bytesRead);
			}
			buffer.flip();
			return converter.deserialize(buffer);
		});
	}
}
