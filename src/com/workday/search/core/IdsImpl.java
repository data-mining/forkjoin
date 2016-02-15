package com.workday.search.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import com.workday.search.api.Ids;
import com.workday.search.exceptions.InvalidIdListException;

public final class IdsImpl implements Ids {
	
	/**
	 * 
	 * ConcurrentSkipListSet are Fail-safe and Thread-safe
	 */
	private ConcurrentSkipListSet<Short> idList = null;
	
	private Iterator<Short> idIterator = null;


	IdsImpl(Collection<Short> inputs) throws InvalidIdListException {
		if(inputs == null) throw new InvalidIdListException("Input Ids can not be Null");
		this.idList = new ConcurrentSkipListSet<Short>(inputs);
		this.idIterator = idList.iterator();
	}

	@Override
	public short nextId() {
		if (idIterator.hasNext()) {
			return idIterator.next();
		} else {
			return -1;
		}
	}

}
