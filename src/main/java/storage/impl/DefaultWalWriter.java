package storage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.Converter;
import storage.DataRecord;
import storage.FileChannelManager;
import storage.RecordMetadata;
import storage.WALWriter;

import java.io.IOException;
import java.nio.ByteBuffer;

final class DefaultWalWriter implements WALWriter {
	private static final Logger logger = LogManager.getLogger(DefaultWalWriter.class);

	private final Converter converter;
	private final FileChannelManager fileChannelManager;


	public DefaultWalWriter(
		FileChannelManager fileChannelManager,
		Converter converter
	) {
		this.converter = converter;
		this.fileChannelManager = fileChannelManager;
	}


	@Override
	public synchronized RecordMetadata write(String key, String value) throws IOException {
		long currentTime = System.currentTimeMillis();
		DataRecord dataRecord = new DataRecord(key, value, currentTime);
		byte[] byteArray = converter.serialize(dataRecord);
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

		return fileChannelManager.execute((fileChannel, filePath) -> {
			long currentOffset = fileChannel.position();
			int totalBytesWritten = fileChannel.write(byteBuffer);
			fileChannel.force(true);
			logger.info("Write and fsync completed successfully for key {}.", key);
			return new RecordMetadata(currentOffset, totalBytesWritten, filePath, currentTime);
		});
	}
}

