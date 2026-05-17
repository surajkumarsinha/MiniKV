# Mini KV

## Decision Logs

### Phase 0 
- Initially we will start with very basic implementation without concurrency, compaction, network partitioning assumptions
- Only string types as key and value for now
- assumption is keys are small to persist in memory

---

## Entities

### Data Record:
- Key size
- Value size
- Timestamp
- Key
- Value

### Record Metadata:
- offset
- size
- timestamp
- file path

---

## Data Flow

### PUT
- Application layer 
- converts to a DataRecord
- serialize to bytes
- flush bytes to disk
- Add key to memory

### GET
- Check in memory map (KeyDirectory)
- Find data offset
- Disk seek
- Deserialize data
- return
    
