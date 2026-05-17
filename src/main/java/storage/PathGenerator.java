package storage;

import java.nio.file.Path;

public interface PathGenerator {
	Path getPath();

	final class PathGeneratorImpl implements PathGenerator {
		@Override
		public Path getPath() {
			return null;
		}
	}
}
