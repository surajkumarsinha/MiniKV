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
		this.isDeleted = false;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public long getTimestampEpochMillis() {
		return timestampEpochMillis;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}
}
