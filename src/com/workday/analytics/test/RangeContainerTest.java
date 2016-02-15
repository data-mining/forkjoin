/**
 * 
 */
package com.workday.analytics.test;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;

import com.workday.search.api.Ids;
import com.workday.search.api.RangeContainer;
import com.workday.search.api.Strategy.SearchStrategy;
import com.workday.search.core.RangeContainerFactoryImpl;

/**
 * @author kaniska_mac
 * 
 */

public class RangeContainerTest {
	static int N = 32000;
	static long[] data = new long[N];

	@Before
	public void setup() {
		for (int i = 0; i < N; i++) {
			data[i] = ThreadLocalRandom.current().nextLong(5000, 200000);
		}
	}

	@Test
	public void testSearch() {
		testSequentialSearch();
		testParallelSearch();
	}

	public void testSequentialSearch() {
		long startTime = System.currentTimeMillis();

		try {

			RangeContainer rangeContainer = RangeContainerFactoryImpl.INSTANCE
					.createContainer(data);
			((RangeContainerFactoryImpl) RangeContainerFactoryImpl.INSTANCE)
					.setCotainerSearchStrategy(SearchStrategy.SEQ,
							rangeContainer);
			Ids idList = rangeContainer.findIdsInRange(10000, 100000, true,
					true);

			System.out.print("Sequential Search : Total Latency : ");
			System.out.print(System.currentTimeMillis() - startTime);

			int count = 0;
			while (true) {
				int idx = idList.nextId();
				if (idx == -1) {
					break;
				} else {
					count++;
					//System.out.print(idx+" ,");
				}
			}
			System.out.println(" Result Size :" + count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testParallelSearch() {
		long startTime = System.currentTimeMillis();

		try {

			RangeContainer rangeContainer = RangeContainerFactoryImpl.INSTANCE
					.createContainer(data);

			((RangeContainerFactoryImpl) RangeContainerFactoryImpl.INSTANCE)
					.setCotainerSearchStrategy(SearchStrategy.PAR,
							rangeContainer);

			Ids idList = rangeContainer.findIdsInRange(10000, 100000, true,
					true);

			System.out.print("Parallel Search : Total Latency : ");
			System.out.print(System.currentTimeMillis() - startTime);

			int count = 0;
			while (true) {
				int idx = idList.nextId();
				if (idx == -1) {
					break;
				} else {
					count++;
					//System.out.print(idx+" ,");
				}
			}
			System.out.println(" Result Size :" + count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
