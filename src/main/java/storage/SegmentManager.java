package storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * Manages the lifecycle of WAL segments, including rotation and resource pooling.
 */
public interface SegmentManager extends AutoCloseable {
    <T> T withActiveSegment(int upcomingWriteSize, WriteAction<T> action) throws IOException;
    <T> T withReadChannel(Path path, ReadAction<T> action) throws IOException;
}
