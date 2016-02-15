/**
 * 
 */
package com.workday.search.core;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import com.workday.search.api.Ids;
import com.workday.search.api.RangeContainer;
import com.workday.search.api.Strategy;
import com.workday.search.exceptions.IdOutOfRangeException;
import com.workday.search.exceptions.InvalidIdListException;

/**
 * @author kaniska_mac
 * This Class executes Query depending upon the Search Strategy
 * For parallel Execution, it submits a Recursive Query to a ForkJoinPool of <#PROCESSOR> number of threads.
 */
final class RangeContainerImpl implements RangeContainer {

	private static int CHUNK_SIZE;
	private final long[] m_data;
	

	static RangeContainer buildContainer(long[] data) throws IdOutOfRangeException {
		if(data.length > 32000) throw new IdOutOfRangeException("Id Range Out of bounds! Array size must be less than 32k");
		return new RangeContainerImpl(data);
	}

	private RangeContainerImpl(long[] p_data) {
		this.m_data = p_data;
	}

	private Strategy.SearchStrategy PLAN;

	public void setStrategy(Strategy.SearchStrategy p_plan) {
		PLAN = p_plan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.workday.analytics.tests.containers.RangeContainer#findIdsInRange(
	 * long, long, boolean, boolean)
	 */
	@Override
	public Ids findIdsInRange(long RANGE_START, long RANGE_END,
			boolean fromInclusive, boolean toInclusive) throws Exception {

		switch (PLAN) {
		case SEQ:
			return searchSequentially(RANGE_START, RANGE_END);
		default:
			return searchParallelly(RANGE_START, RANGE_END);

		}
	}

	/**
	 * 
	 * @param RANGE_START
	 * @param RANGE_END
	 * @return
	 * @throws InvalidIdListException
	 */
	private Ids searchSequentially(long RANGE_START, long RANGE_END) throws InvalidIdListException {
		short high = (short) (m_data.length - 1);
		short low = 0;
		
		ArrayList<Short> result = new ArrayList<Short>();
		
		for (short i = low; i <= high; i++) {
			if (m_data[i] > RANGE_START && m_data[i] < RANGE_END) {
				result.add(i);
			}
		}

		return new IdsImpl(result);
	}

	/**
	 * 
	 * @param RANGE_START
	 * @param RANGE_END
	 * @return
	 * @throws Exception
	 */
	private Ids searchParallelly(long RANGE_START, long RANGE_END) throws Exception {
		int processors = Runtime.getRuntime().availableProcessors();

		CHUNK_SIZE = m_data.length / 4*processors;

		short high = (short) m_data.length;
		short low = 0;
		
		ArrayList<Short> result = new ArrayList<Short>();

		QueryTask task = new QueryTask(m_data, low, high, RANGE_START,
				RANGE_END, result);
		ForkJoinPool poolInstance = new ForkJoinPool(processors);
		poolInstance.invoke(task);
		poolInstance.shutdown();
		
		return new IdsImpl(task.getResult());

	}

	/**
	 * Simple Query Task is divided into smaller sub-sets of data recursively and once data-size < CHUNK_SIZE 
	 * @author kaniska_mac
	 *
	 */
	private static class QueryTask extends RecursiveAction {

		private static final long serialVersionUID = 67781993370162624L;

		short low;
		short high;
		long[] array;
		long search_range_start;
		long search_range_end;

		private ArrayList<Short> result;

		/**
		 * Recursive Query 
		 * 
		 * @param m_data
		 * @param SRCH_IDX_START
		 * @param SRCH_IDX_END
		 * @param SRCH_VAL_START
		 * @param SRCH_VAL_END
		 * @param p_result
		 */
		private QueryTask(long[] m_data, short SRCH_IDX_START,
				short SRCH_IDX_END, long SRCH_VAL_START, long SRCH_VAL_END,
				ArrayList<Short> p_result) {
			array = m_data;
			low = SRCH_IDX_START;
			high = SRCH_IDX_END;
			result = p_result;

			search_range_start = SRCH_VAL_START;
			search_range_end = SRCH_VAL_END;
		}

		/**
		 * Result - holds the list of employee ids matching the query condition
		 * @return
		 */
		public ArrayList<Short> getResult() {
			return this.result;
		}

		@Override
		protected void compute() {
			if (high - low <= CHUNK_SIZE) {
				for (short i = low; i < high; i++) {
					//Execute the Business Logic - Find the Employee Ids whose salary falls between given range
					if (array[i] > search_range_start
							&& array[i] < search_range_end) {
						result.add(i);
					}
				}
			} else {
				short mid = (short) (low + (high - low) / 2);
				QueryTask left = new QueryTask(array, low, mid,
						search_range_start, search_range_end, result);
				QueryTask right = new QueryTask(array, mid, high,
						search_range_start, search_range_end, result);
				invokeAll(left, right);
			}
		}

	}

}
