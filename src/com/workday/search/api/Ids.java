package com.workday.search.api;

public interface Ids {
	/**
	 * 
	 * return the next id in sequence, -1 if at end of data.
	 * 
	 * The ids should be in sorted order (from lower to higher) to facilitate
	 * 
	 * the query distribution into multiple containers.
	 * 
	 * In the "PayrollResult" example, if the query is
	 * 
	 * Find PayrollResult where net >= 3000 and net < 5000
	 * 
	 * and employees #345, #23, #987 meet the query condition, this interface
	 * 
	 * should iterate through 23, 345, 987.
	 */

	short nextId();
}
