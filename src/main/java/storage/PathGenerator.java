package storage;

import java.nio.file.Path;

public interface PathGenerator {
	Path nextPath();

	// Logic to generate paths is encompassed here
	final class PathGeneratorImpl implements PathGenerator {
		@Override
		public Path nextPath() {
			return null;
		}
	}
}
