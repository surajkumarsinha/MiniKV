package storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public interface DiskFlush {
	void flush(FileChannel fileChannel, Path filePath) throws IOException;
}
