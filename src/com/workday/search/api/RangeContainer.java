package com.workday.search.api;


public interface RangeContainer {
	/**
	 * 
	 * @return the Ids of all instances found in the container that
	 * 
	 *         have data value between fromValue and toValue with optional
	 * 
	 *         inclusivity.
	 * @throws Exception 
	 */

	Ids findIdsInRange(long fromValue,

		long toValue,
		
		boolean fromInclusive,
		
		boolean toInclusive) throws Exception;
	

	
}
