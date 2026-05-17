package storage.impl;

import engine.KeyDirectory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.*;

import java.io.IOException;
import java.nio.ByteBuffer;

final class DefaultWalWriter implements WALWriter {
	private static final Logger logger = LogManager.getLogger(DefaultWalWriter.class);

	private final Converter converter;
	private final KeyDirectory keyDirectory;
	private final FileChannelManager fileChannelManager;


	public DefaultWalWriter(
		FileChannelManager fileChannelManager,
		Converter converter,
		KeyDirectory keyDirectory
	) {
		this.converter = converter;
		this.keyDirectory = keyDirectory;
		this.fileChannelManager = fileChannelManager;
	}


	@Override
	public void write(String key, String value) {
		try {
			long currentTime = System.currentTimeMillis();
			DataRecord dataRecord = new DataRecord(key, value, currentTime);
			byte[] byteArray = converter.serialize(dataRecord);
			ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
			fileChannelManager.writeTo((fileChannel, filePath) -> {
				long currentOffset = fileChannel.position();
				int totalBytesWritten = fileChannel.write(byteBuffer);
				fileChannel.force(true);
				keyDirectory.add(
					key,
					new RecordMetadata(currentOffset, totalBytesWritten, filePath, currentOffset)
				);
			});
			logger.info("Write and fsync completed successfully.");
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
