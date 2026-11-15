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
	protected void signal() {
		this.signalCount++;
	}
	
	protected void printProcess() {
		System.out.printf("%d, %s, %d, %d, %d%n", 
				this.processID,
				this.getClass().getSimpleName(),
				this.priority,
				this.burst,
				this.getSignalCount());
	}
	// TODO FORKS
}

// Process Type A
class PA extends Process {
	
	// ProcessA Constructor with priority 2 and burst time of 10
	PA(int processID) {
		super(processID, 2, 10);
	}
}

// Process Type B
class PB extends Process {
	
	// ProcessB Constructor with priority 3 and burst time of 7
	PB(int processID) {
		super(processID, 3, 7);
	}
}

// Process Type C
class PC extends Process {
	
	// ProcessC Constructor with priority 1 and burst time of 5
	PC(int processID) {
		super(processID, 1, 5);
	}
}

// Node Class for queue
class ProcessNode {
    private Process process;
    private ProcessNode next;
    
    // Node Constructor
    ProcessNode(Process process) {
        this.process = process;
        this.next = null;
    }
    
    // Get this ProcessNodes' Process
    protected Process getProcess() {
    	return this.process;
    }
    
    // Set this ProcessNodes' Process
    protected void setProcess(Process process) {
    	this.process = process;
    }
    
    // Get this ProcessNodes' next ProcessNode
    protected ProcessNode getNext() {
    	return this.next;
    }
    
    // Set this ProcessNodes' next ProcessNode
    protected void setNext(ProcessNode next) {
    	this.next = next;
    }
}

// Queue Class
class ProcessQueue {
	private ProcessNode front, rear;
	int length;
	
	// Queue Constructor
	ProcessQueue() {
		this.front = this.rear = null;
		this.length = 0;
	}
	
	// Add a node into the queue
	protected void enqueue(Process process) {
		this.length++;
		ProcessNode tempProcessNode = new ProcessNode(process);
		
		// If rear is null, then list is empty, set front to enqueued node
		if (this.rear == null)
			this.front = tempProcessNode;
		// If rear is not null, set the previous rear's next to the enqueued node
		else
			this.rear.setNext(tempProcessNode);
		// Set the rear to the enqueued node
		this.rear = tempProcessNode;
	}
	
	// Remove process from the top
	protected void dequeue() {
		// If queue is already empty, do nothing
		if (this.front == null)
			return;
		
		// Copy front node
		ProcessNode tempProcessNode = this.front;
		
		// Set front node to front's next
		this.front = this.front.getNext();
		
		// If queue is now empty, set rear to null
		if (this.front == null)
			this.rear = null;
		
		// Prevent removed node from pointing to another node
		tempProcessNode.setNext(null);	
		this.length--;
	}
	
	// Peek on the process from the top
	protected Process peek() {
		// If front is not empty, return process
		if (this.front != null) {
			return this.front.getProcess();
		}
		
		// Return exception
		return null;
	}
	
	// Return size of queue
	protected int size() {
		return this.length;
	}
	
	// Print the processes of the queue
	protected void printQueue() {
		ProcessNode tempProcessNode = this.front;
		
		// Print all column headers
		System.out.println("PID | Type | Priority | Burst | Signals");
		while(tempProcessNode != null) {
			Process tempProcess = tempProcessNode.getProcess();
			
			// Print all variable of process in format
			System.out.printf("%-4d| %-5s| %-9d| %-6d| %d%n", 
					tempProcess.getProcessID(),
					tempProcess.getClass().getSimpleName(),
					tempProcess.getPriority(),
					tempProcess.getBurst(),
					tempProcess.getSignalCount());
			
			// Iterate to next node
			tempProcessNode = tempProcessNode.getNext();
		}
	}
}


// Class to implement Priority Round Robin
public class PriorityRoundRobin {

	// Records
	public record ProcessTimes(Process[] processes, int[] processStartTimes) {}
	
	// Test Function to Print Process Types and their start times
	static void printProcessTimes(ProcessTimes processTimes) {
		// Print Header
		System.out.println("Process\n" + "Type | Time");
		// Iterate through list and print process types and start times
		for (int i = 0; i<processTimes.processes().length; i++) {
			System.out.println(processTimes.processes()[i].getClass().getSimpleName()
					+ "   | " + (processTimes.processStartTimes()[i]));
			
		}
	}
	
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
	            	processes[i] = new PA(i+1);
	            	break;
	            case "PB":
	            	processes[i] = new PB(i+1);
	            	break;
	            case "PC":
	            	processes[i] = new PC(i+1);
	            	break;
	            // Process name is not valid
	            default:
	            	System.err.println("Input Error: " + elements[0] + " is not a valid process name.");
	            	System.exit(2);
	        };
            processStartTimes[i] = Integer.parseInt(elements[1]);
        }
        
        return new ProcessTimes(processes, processStartTimes);
	}
	
	static void testQueue(ProcessQueue processQueue) {
		// Print the queues
		int processQueueSizeMax = processQueue.size();
		for (int i = 0; i < processQueueSizeMax; i++) {
			System.out.println("Size: " + processQueue.size());
			processQueue.printQueue();
			System.out.print("\nRemoved: ");
			processQueue.peek().printProcess();
			processQueue.dequeue();
			System.out.println();
		}

		System.out.println("Size: " + processQueue.size());
		processQueue.printQueue();
		processQueue.peek();
		System.out.println();
		Process temp = processQueue.peek();
		processQueue.dequeue();
		System.out.println("Removed: " + temp + "\n");
		System.out.println("Size: " + processQueue.size());
		processQueue.printQueue();
	}
	
	// Produce a Gantt Chart from a list of processes and their corresponding start times
	static void ganttChart(Process[] processes, int[] processStartTimes) {
		// Error checking if process list and process start time list mismatch
		if (processes.length != processStartTimes.length) {
			System.err.println("Error: list of processes and list process start times are of different length");
			System.exit(0);
		}
		
		// Return if process list is empty
		if (processes.length == 0) {
			System.out.println("Empty process list");
			return;
		}
		
		// Create Queues
		ProcessQueue ReadyQueue = new ProcessQueue();

		// Insert processes into queues
		for (int i = 0; i< processes.length; i++) {
			if (processStartTimes[i] == 0) {
				ReadyQueue.enqueue(processes[i]);
			}
		}
		
		testQueue(ReadyQueue);
		
		// printProcessTimes(new ProcessTimes(processes, processStartTimes));
		
	}
	
	public static void main(String[] args) {
		// Hardcoded string defining the process types and start time
		String input = "(PA, 0), (PB, 0)";
		
		validateInput(input);
		ProcessTimes inputProcessTimes = splitInput(input);
		ganttChart(inputProcessTimes.processes(), inputProcessTimes.processStartTimes());
				
	}

}
