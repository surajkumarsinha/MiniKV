package storage;

public interface Converter {
	byte[] serialize(DataRecord dataRecord);
	DataRecord deserialize(byte[] bytes);
}
