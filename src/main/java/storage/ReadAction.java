package storage;

import java.io.IOException;
import java.nio.channels.FileChannel;

@FunctionalInterface
public interface ReadAction<T> {
    T perform(FileChannel channel) throws IOException;
}
