

import java.util.*;
import java.io.*;

//throws FileNotFoundException

public class sim_cache {

	
//	void displaySets(cache L1, cache L2) {
//	  System.out.println("===== L1 contents =====\n");
//
//	  int i = 0;
//	  for (int j = 0; j < L1.cache.size(); j++) {
//	    String val;
//	    if (i == 0) {
//	      System.out.println("Set " + j / L1.assoc + ": ");
//	    }
//	    if (i == L1.assoc) {
//	      i = 0;
//	      System.out.println("\nSet " + j / L1.assoc + ": ");
//	    }
//	    val = L1.cache.get(j).getValidity();
//	    if (val == "Dirty") {
//	      val = " D ";
//	    } else {
//	      val = " ";
//	    }
//	    System.out.println(hex + L1.cache.get(j).getTag() << val + " ");
//	    System.out.println(dec);
//	    i = i + 1;
//	  }
//	  System.out.println << endl;
//
//	  if (L1.nextlevelcache != null) {
//	    String val;
//	    System.out.println("===== L2 contents =====");
//	    int i = 0;
//	    for (int j = 0; j < L2.cache.size(); j++) {
//	      if (i == 0) {
//	        System.out.println("Set " + j / L2.assoc + ": ");
//	      }
//	      if (i == L2.assoc) {
//	        i = 0;
//	        System.out.println("\nSet " + j / L2.assoc + ": ");
//	      }
//	      val = L2.cache.get(j).getValidity();
//	      if (val == "Dirty") {
//	        val = " D ";
//	      } else {
//	        val = " ";
//	      }
//	      System.out.println << hex << L2.cache.get(j).getTag() << val << " ";
//	      System.out.println << dec;
//	      i = i + 1;
//	    }
//	    System.out.println << endl;
//	  }
//	}
//
//	void displayResults(cache L1, cache L2)
//	{
//	  System.out.println("===== Simulation results (raw) =====\n");
//	  System.out.println("a. number of L1 reads:             "+ L1.reads);
//	  System.out.println("b. number of L1 read misses:       " + L1.rdMiss);
//	  System.out.println("c. number of L1 writes:            " + L1.writes);
//	  System.out.println("d. number of L1 write misses:      " + L1.wtMiss);
//	  float miss_rate;
//	  miss_rate = (float)(L1.rdMiss + L1.wtMiss) / (L1.reads + L1.writes);
//	  System.out.println("e. L1 miss rate:                   " + miss_rate);
//	  System.out.println("f. number of L1 writebacks:        " + L1.writebacks);
//
//
//	  if(L1.nextlevelcache == null)
//	  {
//	    System.out.println("g. number of L2 reads:             " + 0);
//	    System.out.println("h. number of L2 read misses:       " + 0);
//	    System.out.println("i. number of L2 writes:            " + 0);
//	    System.out.println("j. number of L2 write misses:      " + 0);
//	    System.out.println("k. L2 miss rate:                   " + 0);
//	    System.out.println("l. number of L2 writebacks:        " + 0);
//	    System.out.println("m. total memory traffic:           " + L1.memtraffic);
//	  }
//	  else
//	  {
//	    System.out.println("g. number of L2 reads:             " + L2.reads);
//	    System.out.println("h. number of L2 read misses:       " + L2.rdMiss);
//	    System.out.println("i. number of L2 writes:            " + L2.writes);
//	    System.out.println("j. number of L2 write misses:      " + L2.wtMiss);
//	    float l2_miss_rate;
//	    l2_miss_rate = (float) L2.rdMiss / L2.reads;
//	    System.out.println("k. L2 miss rate:                   " + l2_miss_rate);
//	    System.out.println("l. number of L2 writebacks:        " + L2.writebacks);
//	    System.out.println("m. total memory traffic:           " + L1.backinvalidation_wb + L2.memtraffic);
//	    //System.out.println();
//	  }
//	}

		public static int stringToInteger(String s)
	{
		int output = 0;
		for (int i = 0; i < s.length(); i++)
		{
			output = (output * 10 + s.charAt(i) - '0');
		}
		return output;
	}

	public static void main(String[] args) throws FileNotFoundException{
		// TODO Auto-generated method stub
		int BlockSize = Integer.parseInt(args[0]);
		int l1_Size = Integer.parseInt(args[1]);
		int l1_Assoc = Integer.parseInt(args[2]);
		int l2_Size = Integer.parseInt(args[3]);
		int l2_Assoc = Integer.parseInt(args[4]);
		int repl = Integer.parseInt(args[5]);
		int inclusion = Integer.parseInt(args[6]);
		String trace_File = args[7];
		String repl_policies[] = { "LRU", "FIFO", "Pseudo" };
		String incl_policies[] = { "non-inclusive", "inclusive", "exclusive" };
		String replacement_Policy = repl_policies[repl];
		String inclusion_Policy = incl_policies[inclusion];

		Cache L1 = new Cache(BlockSize, l1_Size, l1_Assoc, replacement_Policy, inclusion_Policy);
		Cache L2 = new Cache(BlockSize, l2_Size, l2_Assoc, replacement_Policy, inclusion_Policy);
		L1.nextlevelcache = L2;
		//if (l2_Size != 0) {
		//	Cache L2 = new Cache(BlockSize, l2_Size, l2_Assoc, replacement_Policy, inclusion_Policy);
		//	L1.nextlevelcache = L2;
		//}

		ArrayList<Long> DecimalAddress = new ArrayList<Long>();
		ArrayList<Character> Instruction = new ArrayList<Character>();

		File file = new File(trace_File);
		Scanner input = new Scanner(file);
		int a = 0;
		while (input.hasNext()) {
			String word = input.next();
			// inputfile >> input;
			if (word.length() == 1) {
				// System.out.println(word);
				Instruction.add(word.charAt(0));
			} else {
				// System.out.println(word);
				DecimalAddress.add(getDecimal(word));
			}
			a++;
		}
		int n = DecimalAddress.size();
		for (int i = 0; i < n; i++) {
			System.out.println(Instruction.size());
			L1.blockAccess(DecimalAddress.get(i), Instruction.get(i));

			if (l2_Size != 0) {

				if (inclusion_Policy == "non-inclusive") {
					if (L1.writeBack == true) {
						L2.blockAccess(L1.evictedAddress, 'w');
					}
					if (L1.hit == false) {
						System.out.println(DecimalAddress.get(i));
						L2.blockAccess(DecimalAddress.get(i), 'r');
					}
				}

				if (inclusion_Policy == "inclusive") {
					if (L1.writeBack == true) {
						L2.blockAccess(L1.evictedAddress, 'w');
						if (L2.evicted == true) {
							L1.invalidateCache(L2.evictedAddress);
						}
					}
					if (L1.hit == false) {
						L2.blockAccess(DecimalAddress.get(i), 'r');

						if (L2.evicted == true) {
							L1.invalidateCache(L2.evictedAddress);
						}
					}
				}

				if (inclusion_Policy == "exclusive") {
					if (L1.hit == true) {
						L2.invalidateCache(DecimalAddress.get(i));
					}
					if (L1.evicted == true) {
						L2.blockAccess(L1.evictedAddress, 'w');
						if (L1.writeBack == false) {
							L2.findBlock(L1.evictedAddress).setValidity("Valid");
						}
					}
					if (L1.hit == false) {
						//non_exclusive_cache_access = false;
						L2.blockAccess(DecimalAddress.get(i), 'r');
						//non_exclusive_cache_access = true;
						if (L2.hit == true) {
							if (L2.findBlock(DecimalAddress.get(i)).ValidityINFO() == "Dirty") {
								L1.findBlock(DecimalAddress.get(i)).setValidity("Dirty");
							}
							L2.invalidateCache(DecimalAddress.get(i));
						}
					}
				}
			}
		}
		displayConfiguration(BlockSize, l1_Size, l1_Assoc, l2_Size, l2_Assoc, replacement_Policy, inclusion_Policy,
				trace_File);
		// displaySets(L1, L2);
		// displayResults(L1, L2);

	}

	public static void displayConfiguration(int block, int size1, int assoc1, int size2, int assoc2, String rep,
			String incl, String input_file) {
		System.out.println("===== Simulator configuration =====\n");
		System.out.println("BLOCKSIZE:              " + block);
		System.out.println("L1_SIZE:                " + size1);
		System.out.println("L1_ASSOC:               " + assoc1);
		System.out.println("L2_SIZE:                " + size2);
		System.out.println("L2_ASSOC:               " + assoc2);
		System.out.println("REPLACEMENT POLICY: " + rep);
		System.out.println("INCLUSION PROPERTY:  " + incl);
		System.out.println("trace_File:   " + input_file);
	}

	public static long getDecimal(String hex) {
		String digits = "0123456789ABCDEF";
		hex = hex.toUpperCase();
		long val = 0;
		for (int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			long d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

}