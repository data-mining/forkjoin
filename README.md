# Parallel Execution of Query using Fork Join

Overall Query executes faster if we use ForkJoinPool

ForkJoinPool performs much better than Sequential Search when data size is large (> 15k) , otherwise the latency difference not very significant.

#### Test Case 

https://github.com/data-mining/forkjoin/blob/master/src/com/workday/analytics/test/RangeContainerTest.java

#### Todo
(1) update Junit Testcase to assert => 'Parallel Search -  Latency' for larger data size is less than 'latency of Sequential Search'

(2) externalize the calculation of CHUNK_SIZE (min size of sub-array) and THREADS_NUMBERS in ForkJoinPool

(3) externalize the actual RANGE_QUERY and execute dynamically inside the QueryTask

#### Design decisions
(1) added a SearchStrategy Enum to effectively compare performance between Sequential and Parallel Search
 
(2) added ConcurrentSkipListSet while storing Ids so that we don't encounter COncurrentModification Exception.

(3) RangeContainerImpl and IdsImpl are mostly immutable and not visible by clients (class is final, contructor private, scope is default)

#### Results of Query Execution

ForkJoinPool Size = 4

Data Size = 32k


Sequential Search : Total Latency : 197 Result Size :14756

Parallel Search : Total Latency : 10 Result Size :14756


Sequential Search : Total Latency : 110 Result Size :14712

Parallel Search : Total Latency : 8 Result Size :14712

///////////////////////////

ForkJoinPool Size = 4

Data Size = 10k

Sequential Search : Total Latency : 25 Result Size :4626

Parallel Search : Total Latency : 20 Result Size :4626


Sequential Search : Total Latency : 28 Result Size :4608

Parallel Search : Total Latency : 22 Result Size :4608

///////////////////////////

ForkJoinPool Size = 4

Data Size = 5k

Sequential Search : Total Latency : 17 Result Size :2300

Parallel Search : Total Latency : 7 Result Size :2300

Sequential Search : Total Latency : 21 Result Size :2311

Parallel Search : Total Latency : 10 Result Size :2311


///////////////////////////////////////////////////

ForkJoinPool Size=8

N=32k

Sequential Search : Total Latency : 81 Result Size :14666

Parallel Search : Total Latency : 15 Result Size :14666

Sequential Search : Total Latency : 61 Result Size :14805

Parallel Search : Total Latency : 11 Result Size :14805

//////////////////

ForkJoinPool Size=8

N=10k

Sequential Search : Total Latency : 28 Result Size :4591

Parallel Search : Total Latency : 24 Result Size :4591

Sequential Search : Total Latency : 65 Result Size :4646

Parallel Search : Total Latency : 32 Result Size :4646

//////////////////

ForkJoinPool Size=8

N=5K


Sequential Search : Total Latency : 33 Result Size :2280

Parallel Search : Total Latency : 9 Result Size :2280


Sequential Search : Total Latency : 18 Result Size :2266

Parallel Search : Total Latency : 10 Result Size :2266

//////////////////
