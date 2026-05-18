package storage;

import java.nio.file.Path;

public interface PathGenerator {
	Path getPath();

	// Logic to generate paths is encompassed here
	final class PathGeneratorImpl implements PathGenerator {
		@Override
		public Path getPath() {
			return null;
		}
	}
}
