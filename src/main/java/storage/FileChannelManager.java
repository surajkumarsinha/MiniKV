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
	<T> T execute(DiskTask<T> diskTask) throws IOException;
}
