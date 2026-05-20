package storage;

import java.io.IOException;

@FunctionalInterface
public interface WriteAction<T> {
    T perform(ActiveSegment segment) throws IOException;
}
