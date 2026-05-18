package storage.impl;

import storage.DiskTask;
import storage.FileChannelManager;
import storage.PathGenerator;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class DefaultFileChannelManager implements FileChannelManager {
	private FileChannel currentChannel;
	private Path currentFilePath;

	public DefaultFileChannelManager(PathGenerator pathGenerator) throws IOException {
		initializeChannel(pathGenerator.getPath());
	}

	void initializeChannel(Path path) throws IOException {
		if(currentChannel != null && currentChannel.isOpen()) return;
		currentFilePath = path;
		currentChannel = FileChannel.open(
			path,
			StandardOpenOption.CREATE,
			StandardOpenOption.WRITE,
			StandardOpenOption.APPEND
		);

	}

	@Override
	public <T> T execute(DiskTask<T> diskTask) throws IOException {
		return diskTask.execute(currentChannel, currentFilePath);
	}
}
