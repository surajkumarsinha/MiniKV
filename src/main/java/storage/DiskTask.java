package storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

@FunctionalInterface
public interface DiskTask<T> {
	T execute(FileChannel fileChannel, Path filePath) throws IOException;
}
