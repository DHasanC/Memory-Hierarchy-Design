# Memory-Hierarchy-Design
 Implementation of a flexible cache and memory hierarchy simuator using a subset of the SPEC-2000 benchmark suite.
 
 3. Specification of Memory Hierarchy
Design a generic cache module that can be used at any level in a memory hierarchy. For example,
this cache module can be “instantiated” as an L1 cache, an L2 cache, an L3 cache, and so on.
Since it can be used at any level of the memory hierarchy, it will be referred to generically as
CACHE throughout this specification.
3.1. Configurable parameters
CACHE should be configurable in terms of supporting any cache size, associativity, and block
size, specified at the beginning of simulation:
o SIZE: Total bytes of data storage.
o ASSOC: The associativity of the cache (ASSOC = 1 is a direct-mapped cache).
o BLOCKSIZE: The number of bytes in a block.
There are a few constraints on the above parameters: 1) BLOCKSIZE is a power of two and 2) the
number of sets is a power of two. Note that ASSOC (and, therefore, SIZE) need not be a power of
two. As you know, the number of sets is determined by the following equation:
#𝑠𝑒𝑡𝑠 =
𝑆𝐼𝑍𝐸
𝐴𝑆𝑆𝑂𝐶 × 𝐵𝐿𝑂𝐶𝐾𝑆𝐼𝑍𝐸
2
3.2. Replacement policy
All students need to implement three replacement policies: LRU (least-recently-used), PseudoLRU policy and optimal policy. Replacement policy will be a configurable parameter for the CACHE
simulator.
3.2.1 LRU policy
Replace the block that was least recently touched (updated on hits and misses).
3.2.2 Pseudo-LRU policy (Tree-PLRU)
Replace the block that was least recently touched by using a binary tree, for example, a 4-way set
associative cache needs three bits to keep track of most recently used block. Following is an
example with diagrams.
3.2.3 Optimal policy
Replace the block that will be needed farthest in the future. Note that this is the most difficult
replacement policy and it is impossible to implement in a real system. This will need preprocessing
the trace to determine reuse distance for each memory reference (i.e. how many accesses later
we will need this cache block). You can then run the actual cache simulation on the output of the
preprocessing stage.
3
Note: If there is more than one block (in a set) that’s not going to be reused again in the trace,
replace the leftmost one that comes up from the search.
3.3. Write Policy
CACHE should use the WBWA (write-back + write-allocate) write policy.
o Write-allocate: A write that misses in CACHE will cause a block to be allocated in CACHE.
Therefore, both write misses and read misses cause blocks to be allocated in CACHE.
o Write-back: A write updates the corresponding block in CACHE, making the block dirty. It
does not update the next level in the memory hierarchy (next level of cache or memory). If a
dirty block is evicted from CACHE, a writeback (i.e., a write of the entire block) will be sent to
the next level in the memory hierarchy.
3.4. Allocating a block: Sending requests to next level in the memory hierarchy
Your simulator must be capable of modeling one or more instances of CACHE to form an overall
memory hierarchy, as shown in Fig. 1.
CACHE receives a read or write request from whatever is above it in the memory hierarchy (either
the CPU or another cache). The only situation where CACHE must interact with the next level
below it (either another CACHE or main memory) is when the read or write request misses in
CACHE. When the read or write request misses in CACHE, CACHE must “allocate” the requested
block so that the read or write can be performed.
Thus, let us think in terms of allocating a requested block X in CACHE. The allocation of requested
block X is actually a two-step process. The two steps must be performed in the following order.
1. Make space for the requested block X. If there is at least one invalid block in the set,
then there is already space for the requested block X and no further action is required
(go to step 2). On the other hand, if all blocks in the set are valid, then a victim block V
must be singled out for eviction, according to the replacement policy (Section 3.2). If
this victim block V is dirty, then a write of the victim block V must be issued to the next
level of the memory hierarchy.
2. Bring in the requested block X. Issue a read of the requested block X to the next level
of the memory hierarchy and put the requested block X in the appropriate place in the
set (as per step 1).
To summarize, when allocating a block, CACHE issues a write request (only if there is a victim
block and it is dirty) followed by a read request, both to the next level of the memory hierarchy.
Note that each of these two requests could themselves miss in the next level of the memory
hierarchy (if the next level is another CACHE), causing a cascade of requests in subsequent
levels. Fortunately, you only need to correctly implement the two steps for an allocation locally
within CACHE. If an allocation is correctly implemented locally (steps 1 and 2, above), the memory
hierarchy as a whole will automatically handle cascaded requests globally.
4
`
Fig. 1: Your simulator must be capable of modeling one or more instances of CACHE to form an overall
memory hierarchy.
3.5. Updating state
After servicing a read or write request, whether the corresponding block was in the cache already
(hit) or had just been allocated (miss), remember to update other state. This state includes
LRU/Pseudo-LRU/optimal counters affiliated with the set as well as the valid and dirty bits affiliated
with the requested block.
3.6. Inclusion Property
Now, implement another inclusion property (inclusive property) for CACHE. Inclusion property will
be a configurable parameter for the CACHE simulator.
3.6.1 Non-inclusive cache
Non-inclusive property is the default property used in this machine problem. It is simply what you’ll
get if you follow the directions listed above. There is no enforcement of either the cache inclusion
nor the cache exclusion property. A cache block in an inner cache may or may not be in an outer
cache.
3.6.2 Inclusive cache
According to the inclusive property, an outer cache should be a superset of all inner caches it
surrounds. i.e. any reference in L1 cache must also hit in the L2 cache. For homogeneous caches,
such as the ones we shall be testing, the only difference between inclusive and non-inclusive
cache is on L2 eviction (happens when read or write request misses at the L2 cache and the
requested block needs to be allocated). When a victim block in the L2 cache needs to be evicted,
the L2 cache must invalidate the corresponding block in L1 as well (assuming it exists there). If the
L1 block that needs to be invalidated is dirty, a write of the block will be issued to the main
memory directly.
Read or WriteRequest
CACHE
From CPU
CACHE
Read or WriteRequest
Read or WriteRequest
Read or WriteRequest
Main Memory
5
4. Memory Hierarchies to be Explored in this Machine Problem
While Fig. 1 illustrates an arbitrary memory hierarchy, you will only need to study the memory
hierarchy configurations shown in Fig. 2a and Fig. 2b. Also, these are the only configurations the
TAs will test.
For this machine problem, all CACHEs in the memory hierarchy will have the same BLOCKSIZE.
\
Fig. 2a: Configurations to be studied.
Fig. 2b: Configurations to be studied.
