

import java.util.*;

//boolean non_exclusive_cache_access = true;

public class Cache {
	public static boolean non_exclusive_cache_access = true;
	public int trace_num;
	public Cache nextlevelcache;
	public boolean evicted;
	public long evictedAddress;
	public boolean hit;
	public int way;
	public boolean writeBack;
	public int size;
	public int blocksize;
	public int assoc;
	public String repl_Policy;
	public String inclusion;
	public int sets;
	public int Cacheblocks;
	public ArrayList<Cacheblock> cache = new ArrayList<Cacheblock>();
	public int offset_Width;
	public int index_Width;
	public int tag_Width;
	//public int offset_Mask;
	//public int index_Mask;
	//public int tag_Mask;

	public int reads;
	public int writes;
	public int rdMiss;
	public int wtMiss;
	public int rdHits;
	public int wtHits;
	public int writebacks;
	public int memtraffic;
	public int backinvalidation_wb;

	public Cache(int blocksize, int size, int assoc, String repl_Policy, String inclusion) {
		this.trace_num = 0;
		this.nextlevelcache = null;
		this.evicted = false;
		this.evictedAddress = -1;
		this.hit = false;
		this.way = -1;
		this.writeBack = false;

		this.size = size;
		this.blocksize = blocksize;
		this.assoc = assoc;
		this.repl_Policy = repl_Policy;
		this.inclusion = inclusion;

		// Define Cache
		this.sets = (this.size) / (this.assoc * this.blocksize);
		this.Cacheblocks = this.sets * this.assoc;
		ArrayList<Cacheblock> cache_array = new ArrayList<Cacheblock>();
		for (int i = 0; i < Cacheblocks; i++) {
			cache_array.add(new Cacheblock());
		}
		this.cache = new ArrayList<Cacheblock>(cache_array);
		this.offset_Width = (int) (Math.log(this.blocksize) / Math.log(2));
		this.index_Width = (int) (Math.log(this.sets) / Math.log(2));
		this.tag_Width = (int) (64 - this.offset_Width - this.index_Width);

		this.reads = 0;
		this.writes = 0;
		this.rdMiss = 0;
		this.wtMiss = 0;
		this.rdHits = 0;
		this.wtHits = 0;
		this.writebacks = 0;
		this.memtraffic = 0;
		this.backinvalidation_wb = 0;
	}

	public int calcIndex(long address) {

		return (int)((address << tag_Width) >> (tag_Width + offset_Width));
	}

	public long calcTag(long address) {
		return (address >>> (64 - tag_Width));
	}

	public void blockAccess(long address, char op){
		  this.evicted = false;
		  this.hit = false;
		  this.way = -1;
		  this.writeBack = false;

		  this.trace_num = this.trace_num + 1;

		  if (op == 'r') {
			  this.reads = this.reads + 1;
		  }
		  else {
			  this.writes = this.writes + 1;
		  }
		  Cacheblock cache_cell = this.findBlock(address);
		  if (cache_cell == null) {
			  this.hit = false;
			  if (op == 'r') {
				  this.rdMiss = this.rdMiss + 1;
			  }
			  else{
				  this.wtMiss = this.wtMiss + 1;
			  }
		
			  this.memtraffic = this.memtraffic + 1;
			  if (non_exclusive_cache_access == true){
				  cache_cell = this.replaceCell(address);
			  }
		  }
		  else{
			  this.hit = true;
			  if (op == 'r'){
				  this.rdHits = this.rdHits + 1;
			  }
			  else{
				  this.wtHits = this.wtHits + 1;
			  }
		  if (this.repl_Policy.equals("LRU")){
			  cache_cell.setRanking(this.trace_num);
		  	}
		  if (this.repl_Policy.equals("FIFO")){
			  if (this.hit == false){
				  cache_cell.setRanking(this.trace_num);
			  }
		  }
		  if (op == 'w') {
		      cache_cell.setValidity("Dirty");
		    }
		
		}
	}
	//ArrayList<Cacheblock> getSet(ArrayList<Cacheblock> list, int index) {

//    ArrayList<Cacheblock> out = new ArrayList<Cacheblock>();
  //  for(int i = (this.assoc * index); i < (index + 1) * this.assoc; i++) {

//      out.add(list.get(i));

//    }

//    return out;
  //}


	public Cacheblock findBlock(Long address) {
		long tag = this.calcTag(address);
		int index = this.calcIndex(address);
		ArrayList<Cacheblock> cacheSet = new ArrayList<Cacheblock>();
		for (int i = (this.assoc * index); i < (index + 1) * this.assoc; i++) {
			cacheSet.add(this.cache.get(i));
		}
		//ArrayList<Cacheblock> cacheSet = this.getSet(this.cache, index);

		for (int way = 0; way < this.assoc; way++) {
			// cout << cacheSet[0] << " " << tag << endl;
			if ((cacheSet.get(way).TagINFO() == tag)) {
				if (!cacheSet.get(way).ValidityINFO().equals("Invalid")) {
					// cout << "hi " << endl;
					this.way = way;
					return cacheSet.get(way);
				}
			}
		}
		return null;
	}

	public Cacheblock replaceCell(long address) {
		long tag = this.calcTag(address);
		int index = this.calcIndex(address);
		Cacheblock evicted_Block = new Cacheblock();
		int flag = 0;
		ArrayList<Cacheblock> cacheSet1 = new ArrayList<Cacheblock>();
		for (int i = (this.assoc * index); i < (index + 1) * this.assoc; i++) {
			cacheSet1.add(this.cache.get(i));
		}
		for (int way = 0; way < this.assoc; way++) {
			if (cacheSet1.get(way).ValidityINFO().equals("Invalid")) {
				this.way = way;
				flag = 1;
				evicted_Block = cacheSet1.get(way);
			}
		}
		if (flag == 0) {
			int way = 0;
			int minimum = this.trace_num;
			ArrayList<Cacheblock> cacheSet2 = new ArrayList<Cacheblock>();
			for (int i = (this.assoc * index); i < (index + 1) * this.assoc; i++) {
				cacheSet2.add(this.cache.get(i));
			}
			if (this.repl_Policy.equals("LRU") || this.repl_Policy.equals("FIFO")) {
				for (int i = 0; i < this.assoc; i++) {
					if (cacheSet2.get(i).getRanking() < minimum) {
						minimum = cacheSet2.get(i).getRanking();
						way = i;
					}
				}
			}
			evicted_Block = this.cache.get(this.assoc * index + way);
			this.evicted = true;
			this.evictedAddress = evicted_Block.getAddress();
		}
		if (evicted_Block.ValidityINFO().equals("Dirty")) {
			this.writeBack = true;
			this.writebacks = this.writebacks + 1;
			this.memtraffic = this.memtraffic + 1;
		}
		evicted_Block.setTag(tag);
		evicted_Block.setAddress(address);
		evicted_Block.setValidity("Valid");

		if (this.repl_Policy.equals("LRU")) {

			evicted_Block.setRanking(this.trace_num);
		}
		if (this.repl_Policy.equals("FIFO")) {
			if (this.hit == false) {
				evicted_Block.setRanking(this.trace_num);
			}
		}

		return evicted_Block;
	}

	public void invalidateCache(Long address) {

		Cacheblock Cacheblock = this.findBlock(address);
		if (Cacheblock != null) {
			if (this.inclusion.equals("inclusive")) {
				if (Cacheblock.ValidityINFO().equals("Dirty")) {
					this.writeBack = true;
					this.backinvalidation_wb = this.backinvalidation_wb + 1;
				}
			}
			Cacheblock.setValidity("Invalid");
		}
	}

}
