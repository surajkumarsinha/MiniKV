package storage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.ActiveSegment;
import storage.Converter;
import storage.DataRecord;
import storage.RecordMetadata;
import storage.SegmentManager;
import storage.WALWriter;

import java.io.IOException;
import java.nio.ByteBuffer;

final class DefaultWalWriter implements WALWriter {
	private static final Logger logger = LogManager.getLogger(DefaultWalWriter.class);

	private final Converter converter;
	private final SegmentManager segmentManager;


	public DefaultWalWriter(
		SegmentManager segmentManager,
		Converter converter
	) {
		this.converter = converter;
		this.segmentManager = segmentManager;
	}


	@Override
	public synchronized RecordMetadata write(String key, String value) throws IOException {
		long currentTime = System.currentTimeMillis();
		DataRecord dataRecord = new DataRecord(key, value, currentTime);
		byte[] byteArray = converter.serialize(dataRecord);
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

		RecordMetadata metadata = segmentManager.withActiveSegment(
			byteArray.length, 
			activeSegment -> activeSegment.append(byteBuffer, currentTime)
		);
		
		logger.info("Write and fsync completed successfully for key {}. Segment: {}", key, metadata.path());
		return metadata;
	}
}

