package storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Encapsulates the currently active WAL segment for writing.
 * Handles the physical append operation and durability (fsync).
 */
public final class ActiveSegment implements AutoCloseable {
    private final FileChannel channel;
    private final Path path;

    public ActiveSegment(Path path) throws IOException {
        this.path = path;
        this.channel = FileChannel.open(
            path,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
        );
    }

    /**
     * Appends a buffer to the segment and forces it to disk.
     * @return Metadata about the written record.
     */
    public RecordMetadata append(ByteBuffer buffer, long timestamp) throws IOException {
        synchronized (this) {
            long offset = channel.position();
            int size = channel.write(buffer);
            channel.force(true); // Ensure durability
            return new RecordMetadata(offset, size, path, timestamp);
        }
    }

    public long size() throws IOException {
        return channel.size();
    }

    public Path getPath() {
        return path;
    }

    @Override
    public void close() throws IOException {
        if (channel.isOpen()) {
            channel.close();
        }
    }
}
