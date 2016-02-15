/**
 * 
 */
package com.workday.search.core;

import com.workday.search.api.RangeContainer;
import com.workday.search.api.Strategy.SearchStrategy;
import com.workday.search.api.RangeContainerFactory;
import com.workday.search.exceptions.IdOutOfRangeException;


/**
 * @author kaniska_mac
 * 
 */
public class RangeContainerFactoryImpl implements RangeContainerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workday.analytics.tests.containers.RangeContainerFactory#createContainer
	 * (long[])
	 */

	public static RangeContainerFactory INSTANCE = new RangeContainerFactoryImpl();

	private RangeContainerFactoryImpl() {

	}

	@Override
	public RangeContainer createContainer(long[] data) throws IdOutOfRangeException {

		RangeContainer rangeContainer = RangeContainerImpl.buildContainer(data);

		return rangeContainer;
	}

	/**
	 * 
	 * @param plan
	 * @param rangeContainer
	 * @throws Exception
	 */
	public void setCotainerSearchStrategy(SearchStrategy plan, RangeContainer rangeContainer) throws Exception {
		if(rangeContainer == null){
			throw new Exception("Container Not Found");
		}
		((RangeContainerImpl)rangeContainer).setStrategy(plan);
	}

}
