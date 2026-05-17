package storage;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// we can later add methods to manager to create new log files after max file size is reached
// we can also add other important methods like merging wal log files
public interface FileChannelManager {
	void writeTo(DiskFlush diskFlush) throws IOException;

	final class DefaultFileChannelManager implements FileChannelManager {
		private FileChannel currentChannel;
		private Path currentFilePath;

		public DefaultFileChannelManager(String walDirectoryPath) throws IOException {
			initializeChannel(Path.of(URI.create(walDirectoryPath)));
		}

		void initializeChannel(Path path) throws IOException {
			if(currentChannel != null && currentChannel.isOpen()) return;
			currentFilePath = path;
			currentChannel = FileChannel.open(
				path,
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.APPEND
			);

		}

		@Override
		public void writeTo(DiskFlush diskFlush) throws IOException {
			diskFlush.flush(currentChannel, currentFilePath);
		}
	}
}
