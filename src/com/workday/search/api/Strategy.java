package com.workday.search.api;

public interface Strategy {
	
    public enum SearchStrategy {
		SEQ(1), PAR(2);

		private int value;

		private SearchStrategy(int value) {
			this.value = value;
		}

	};

}
