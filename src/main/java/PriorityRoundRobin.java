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

// Class to implement for Specific Processes
abstract class Process {
	
	// Variable Declaration
	private final int processID;
	private final int priority;
	private int burst;
	
	// Variable Initialization
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
	
	// Decrease this Process' Burst
	protected boolean decreaseBurst() {
		// TODO: Should this return a variable? Should the program check it first?
		return this.burst-- == 0;
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


// Class to implement Priority Round Robin
public class PriorityRoundRobin {

	// Records
	public record ProcessTimes(Process[] processes, int[] processStartTimes) {}
	
	// Input Validation
	static void validateInput(String input) {
		String inputRegex = "^\\([a-zA-Z0-9_-]+,\\s*\\d+\\)(?:,\\s*\\([a-zA-Z0-9_-]+,\\s*\\d+\\))*$";
		if (!input.matches(inputRegex)) {
			System.err.println("Input Error: Please use:\n"
					+ "(Process 1 Type, Process 1 Start Time), (Process 2 Name, Process 2 Start Time)\n"
					+ "e.g. (PA, 0), (PB, 0)");
			System.exit(2);
		}
	}
	
	// Split input into List of ProcessTimes
	static ProcessTimes splitInput(String input) {
		// Remove the first and last parenthesis
        String trimmedInput = input.substring(1, input.length() - 1);
        // Split on "), (" with optional whitespace
        String[] arrays = trimmedInput.split("\\),\\s*\\(");

        // Initialize return arrays
        Process[] processes = new Process[arrays.length];
        int[] processStartTimes = new int[arrays.length];

        // Iterate through the collection of elements, split on "," and populate the arrays
        for (int i = 0; i < arrays.length; i++) {
            String[] elements = arrays[i].split(",\\s*");
            switch (elements[0]) {
	            case "PA":
	            	processes[i] = new ProcessA(i+1);
	            	break;
	            case "PB":
	            	processes[i] = new ProcessB(i+1);
	            	break;
	            case "PC":
	            	processes[i] = new ProcessC(i+1);
	            	break;
	            default:
	            	System.err.println("Input Error: " + elements[0] + " is not a valid process name.");
	            	System.exit(2);
	        };
            processStartTimes[i] = Integer.parseInt(elements[1]);
        }
        
        return new ProcessTimes(processes, processStartTimes);
	}
	
	// Produce a Gantt Chart from a list of processes and their corresponding start times
	static void ganttChart(Process[] processes, int[] processStartTimes) {
		// TODO Auto-generated method stub
		for (int i = 0; i<processes.length; i++) {
			System.out.println("Priority: " + processes[i].getPriority());
			System.out.println("Time: " + processStartTimes[i]);
			
		}
	}
	
	public static void main(String[] args) {
		// Hardcoded string defining the process types and start time
		String input = "(PA, 0), (PB, 0)";
		
		validateInput(input);
		ProcessTimes inputProcessTimes = splitInput(input);
		ganttChart(inputProcessTimes.processes(), inputProcessTimes.processStartTimes());
				
	}

}
