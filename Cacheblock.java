//package cache_simulator;

public class Cacheblock {
	public String validity;
	public long address;
	public long tag;
	public int ranking;

	public Cacheblock() {
		this.validity = "Invalid";
		this.address = -1;
		this.tag = -1;
		this.ranking = -1;
	}

	public final void setValidity(String validity) {
		this.validity = validity;
	}

	public final void setAddress(long address) {
		this.address = address;
	}

	public final void setTag(long tag) {
		this.tag = tag;
	}

	public final void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public final String ValidityINFO() {
		return this.validity;
	}

	public final long getAddress() {
		return this.address;
	}

	public final long TagINFO() {
		return this.tag;
	}

	public final int getRanking() {
		return this.ranking;
	}
}