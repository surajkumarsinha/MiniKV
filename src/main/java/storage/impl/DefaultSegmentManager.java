package storage.impl;

import storage.ActiveSegment;
import storage.PathGenerator;
import storage.ReadAction;
import storage.SegmentManager;
import storage.WriteAction;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static utility.Constants.MAX_WAL_SEGMENT_SIZE_KB;

public final class DefaultSegmentManager implements SegmentManager {
    private final PathGenerator pathGenerator;
    private final Map<Path, FileChannel> readChannels = new ConcurrentHashMap<>();
    
    private ActiveSegment activeSegment;
    private final long maxSegmentSize;

    public DefaultSegmentManager(PathGenerator pathGenerator) throws IOException {
        this.pathGenerator = pathGenerator;
        this.maxSegmentSize = MAX_WAL_SEGMENT_SIZE_KB * 1024L;
        rotate(); // Initialize first segment
    }

    @Override
    public synchronized <T> T withActiveSegment(int upcomingWriteSize, WriteAction<T> action) throws IOException {
        if (activeSegment.size() + upcomingWriteSize > maxSegmentSize) {
            rotate();
        }
        return action.perform(activeSegment);
    }

    @Override
    public <T> T withReadChannel(Path path, ReadAction<T> action) throws IOException {
        FileChannel channel = readChannels.computeIfAbsent(path, p -> {
            try {
                return FileChannel.open(p, StandardOpenOption.READ);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open read channel for: " + p, e);
            }
        });
        return action.perform(channel);
    }

    private synchronized void rotate() throws IOException {
        if (activeSegment != null) {
            activeSegment.close();
        }
        Path nextPath = pathGenerator.nextPath();
        activeSegment = new ActiveSegment(nextPath);
    }

    @Override
    public synchronized void close() throws IOException {
        if (activeSegment != null) {
            activeSegment.close();
        }
        for (FileChannel channel : readChannels.values()) {
            if (channel.isOpen()) {
                channel.close();
            }
        }
        readChannels.clear();
    }
}
