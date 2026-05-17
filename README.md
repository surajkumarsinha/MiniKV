## Data Flow

### PUT

Application layer 
    -> converts to a DataRecord
    -> serialize to bytes
    -> flush bytes to disk
    -> add key to memory

Data Record:
- Key size
- Value size 
- Timestamp
- Key 
- Value
    
