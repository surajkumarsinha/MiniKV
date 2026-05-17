package storage;

import java.nio.file.Path;

public record RecordMetadata(long offset, int size, Path path, long timestamp) {}
