1. Data Integrity & Reliability (Critical)
* Missing CRC/Checksums: DataRecordConverter does not include a checksum. Without this, there is no way to detect bit rot or partial writes (torn pages) during recovery. Bitcask
  typically uses a CRC32 at the start of every record.
* Tombstone Serialization: The isDeleted flag in DataRecord is ignored during serialization. Deletions currently only exist in memory; upon restart (once recovery is implemented), all
  "deleted" records would reappear because the WAL doesn't persist the deletion status.
* Missing Exception Safety: In KVStoreImpl.put, if walWriter.write succeeds but keyDirectory.add fails (e.g., due to OOM), the system is in an inconsistent state. The WAL has the data,
  but the index doesn't. While ConcurrentHashMap.put is unlikely to fail, the architecture should explicitly handle index/log synchronization.

2. Concurrency & Performance
* The synchronized Bottleneck: DefaultWalWriter.write is synchronized and includes a call to fileChannel.force(true) (fsync). This means every write blocks every other writer until the
  physical disk head has moved and the data is durable.
    * Recommendation: Decouple the fsync policy. Consider a "group commit" or a background flusher if absolute durability on every single write isn't required by the user.
* GC Pressure: DataRecordConverter allocates new byte[] arrays and a ByteBuffer for every write. In a high-throughput scenario, this will trigger frequent Stop-The-World GCs.
    * Recommendation: Use a thread-local or pooled ByteBuffer and avoid intermediate byte[] if possible (write directly to the channel or a reusable buffer).

3. Storage Management
* Infinite Log Growth: There is no logic for file rotation or segment management. A production Bitcask implementation limits segment size (e.g., 2GB) and opens a new one, allowing
  older segments to be merged/compacted.
* File Channel Leakage: DefaultFileChannelManager opens a channel but doesn't seem to have a lifecycle management plan for closing it gracefully.

4. Implementation Gaps
* get is unimplemented: The InMemoryKeyDirectory is missing a lookup method, and KVStoreImpl.get is a stub.
* No Recovery Logic: WALReader is an empty interface. The engine currently cannot start from an existing log file.

Suggested Immediate Priorities:
1. Add a CRC32 field to the beginning of the DataRecord on-disk format.
2. Fix Deletions: Persist the isDeleted flag (usually by writing a special tombstone value or a flag byte).
3. Implement Recovery: Write the logic to scan the WAL and populate the KeyDirectory on startup. This is the "moment of truth" for your file format.