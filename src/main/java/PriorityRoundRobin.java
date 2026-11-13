package main.java;

/*
Assume that PA, PB, and PC are three distinguished programs. 
Assume that processes executing PA have a priority of 2, 
processes executing PB have a priority of 3 and processes 
executing PC have a priority of 1. In order to complete 
their execution, PA needs 10 ticks, PB needs 7 ticks and PC 
needs 5 ticks. Assume that processes running PA and PB will 
fork a new process every 3 ticks of execution. The process 
running PA forks new processes which execute PB while the 
process running forks new processes which execute PC. Assume 
that 2 processes P1 and P2 have arrived at t=0. P1 and P2 
are going to execute programs PA and PB respectively. 
Furthermore, there is some hardware failure in this computer 
system that generates periodic signals and sends them to the 
currently running process every 3 ticks. The first signal 
arrives at time t=3. Implement this scenario illustrating 
the scheduling of these processes if a priority with a 
round-robin scheduling algorithm is used. Assume that a time 
quantum of 4 is used. Also assume that a smaller priority 
number means a higher priority.
*/

// Abstract Class to be extended by Processes
abstract class Process {
	private final int processID;
	private final int priority;
	private int burst;
	private int signalCount= 0;
	
	// Process Constructor
	Process(int processID, int priority, int burst) {
		this.processID = processID;
		this.priority = priority;
		this.burst = burst;
	}
	
	// Return this Process' ProcessID
	protected int getProcessID() {
		return this.processID;
	}
	
	// Return this Process' Priority
	protected int getPriority() {
		return this.priority;
	}
	
	// Return this Process' Remaining Burst
	protected int getBurst() {
		return this.burst;
	}
	
	// TODO: Implement decreaseBurst
	// Should this return a variable? Should the program check it first?
	protected void decreaseBurst() {
		// TODO
	}
	
	// Return this Process' Current Signal Count
	protected int getSignalCount() {
		return this.signalCount;
	}
	
	// Signal this Process, Incrementing signalCount by 1
	protected void Signal() {
		this.signalCount++;
	}
	
	// TODO FORKS
}

// Process Type A
class ProcessA extends Process {
	
	// ProcessA Constructor with priority 2 and burst time of 10
	ProcessA(int processID) {
		super(processID, 2, 10);
	}
}

// Process Type B
class ProcessB extends Process {
	
	// ProcessB Constructor with priority 3 and burst time of 7
	ProcessB(int processID) {
		super(processID, 3, 7);
	}
}

// Process Type C
class ProcessC extends Process {
	
	// ProcessC Constructor with priority 1 and burst time of 5
	ProcessC(int processID) {
		super(processID, 1, 5);
	}
}

public class PriorityRoundRobin {
	// Method for printing a thing
	// Parameters is an array of arrays ((Process, Arrive Time)(Process, Arrive Time))
	static void ganttChart() {
		// TODO Auto-generated method stub
		System.out.println("I am a Geek");
	}
	
	public static void main(String[] args) {
		ganttChart();
	}

}
