package storage;

public final class DataRecord {
	private final String key;
	private final String value;
	private final long timestampEpochMillis;
	private boolean isDeleted;

	public DataRecord(String key, String value, long timestampEpochMillis) {
		this.key = key;
		this.value = value;
		this.timestampEpochMillis = timestampEpochMillis;
	}
}
