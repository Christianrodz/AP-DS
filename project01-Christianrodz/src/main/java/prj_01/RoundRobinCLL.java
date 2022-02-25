// Christian Rodriguez Christian.rodriguez73@upr.edu
package prj_01;
import java.util.concurrent.ThreadLocalRandom;


class Node {
    public int id;
    public Node next;
    public Node previous;
    public Boolean proccessed_flag;
    /**
     * Constructor for node with id parameter
     * <p>
     * Constructs a Node with the given parameter id and marks it as proccessed by default
     * @param id - number that corresponds to its slot
     */
    public Node (int id) {
        this.id = id;
        proccessed_flag = true;
    }
}

interface RoundRobinCLLInterface {
    abstract void findEmptySlot();
    abstract void findFilledSlot();
}

public class RoundRobinCLL implements RoundRobinCLLInterface {
    private int num_nodes = 5;
    public Node head = null;
    public Node tail = null;
    public Boolean stopLoop = false;
    private int termination_limit;
    /**
     * Puts thread to sleep for a random amount of time
     * @throws if something went wrong when putting thread to sleep
     */
    private void holdon() {
        try{
            Thread.currentThread().sleep(ThreadLocalRandom.current().nextInt(500, 3000));//500,3000
        }
        catch(Exception e){
            System.out.println("Something went wrong.");
        }
    }
    /**
     * Writes the circular linked list's slots alongside their processed_flag as a string
     * <p>
     * Start by declaring the string and writing the current thread's name
	 * Use a reference node, starting from the head, to iterate through the list
	 * Add the indication that this is the first node and add the processed flag and add an arrow to point to the following node
	 * Utilizing a for loop, keep adding the rest of the nodes until we reach the end
	 * @return s - String describing the contents of the circular linked list alongside the slot's processing_flag
     */
    @Override
    public String toString () {
        String s = new String(""+ Thread.currentThread().getName() + " ");
        Node node = head;
        s+= "(Node-1: " + node.proccessed_flag + ")";
        s+= " ==> ";

        for (int i=1; i<num_nodes; i++) {
            node = node.next;
            s+= "(Node-"+(i+1)+": "+node.proccessed_flag + ")";
            if (i<num_nodes-1)
                s+= " ==> ";
        }
        return s;
    }
    /**
     * Synchronized methods that allow threads to change the status of the slot to either processed or not processed
     * <p>
     * Print out which thread is holding resources
     * Change the processed flag to set_slot
     * Print out which thread is releasing its resources
     * if we changed the slot to true, we put the thread to sleep with holdon()
     * @param node - slot that will have its status changed
     * @param set_slot - set the slot to either processed (false) or not processed (true)
     */
    private synchronized void holdRR(Node node, Boolean set_slot) {
        System.out.println("Thread " + Thread.currentThread().getName() + " Holding Resources");
        node.proccessed_flag = set_slot ;
        System.out.println("Thread " + Thread.currentThread().getName() + " Releasing Resources");
        if (set_slot) holdon();
    }
    /**
     * Worker process that finds processed slots and marks them as unprocessed
     * <p>
     * Use holdon() to put the threads to sleep before they can start to work
     * Starting from the head in the linked list enter a loop
     * <p>
     * While stopLoop is false
     * Move through the linked list
     * If we find a processed slot
     * Change it to unprocessed using holdRR() and move the reference of the node to the head again
     * Otherwise continue moving through list.
     */
    public void findEmptySlot() {
        holdon();
        /* PUT YOUR CODE HERE TO FIND AN EMPTY SLOT */
        /* STARTING FROM THE FIRST NODE IN THE LINKED LIST */
        /*** IMPORTANT:: USE THE holdRR() METHODE TO ACCESS THE LINKED LIST ***/
        /*** TO AVOID RACE CONDITION ***/
        Node curNode = head;
        while (!stopLoop) {
        	if(curNode.proccessed_flag) {
        		holdRR(curNode, false);
        		curNode = head;
        	}else {
        		curNode = curNode.next;	
        	}
        	
        }
        
    }
    
    /**
     * This is the main process that looks for slots that are marked as not_processed, when it has found such a slot it changes it to processed.
     * <p>
     * Put the main thread to sleep using holdon(). Otherwise the thread finishes before the others can work
     * Use an initial loop to be able to terminate when we've hit the termination limit.
     * A inner loop moves indefinitely through the circular linked list for the slots.
     * If the inner loop finds a valid slot, it changes it using holdRR().
     * Once it reaches the tail it exits the inner loop and increases counter, changing the node to the head.
     * Once it increases the counter, if we've hit the termination limit we break from the outer loop
     * Otherwise we print out the circular linked list to see its current status.
     */
    public void findFilledSlot() {
        /* PUT YOUR CODE HERE TO FIND THE FILLED SLOTS */
        /* FOR THE MAIN PROCESS                        */
        /*** IMPORTANT:: USE THE holdRR() METHODE TO ACCESS THE LINKED LIST ***/
    	holdon();
    	/** counts how many times we've iterated through the circular linked list*/
        int count = 0; //Start counter at 0
        /** Node reference to move through the circular linked list*/
        Node curNode = head;
        while (!stopLoop) { 
            /* PUT YOUR CODE HERE TO FIND THE FILLED SLOTS */
        	while(!curNode.equals(tail)) {
        		if(!curNode.proccessed_flag) {
        			holdRR(curNode, true);
        		}
        		curNode = curNode.next;
        	}
        	curNode = head;
        	count++;
            if (count>termination_limit) break;
            System.out.println("Main Move No.: " + count%num_nodes + "\t" + toString());
        
        }
        
    }
    /**
     * This method initializes the circular linked list for use in the program
     * <p>
     * Start by checking if num_nodes is valid
     * if valid, continue with the program, otherwise throw an exception
     * At the start of the method create a node and mark it as the head. Making its id 1 since its the first node
     * loop from 1 to num_nodes, increasing i by 1
     * <p>
     * Inside the loop we create a new node with id of i+1 and make the previous node reference it
     * Afterwards move the node forwards to the newly created node for the next run of the loop
     * <p>
     * Exiting the loop means we are at the tail of the Node
     * Indicate that the tail is the current node
     * Make the linked list circular by making the tail hold the reference for the head.
     * @throws If number of nodes is less than 0
     */

    private void fillRoundRubin () {
        /* PUT YOUR CODE HERE INITIATE THE CIRCULAR LINKED LIST */
        /* WITH DESIRED NUMBER OF NODES BASED TO THE PROGRAM   */
    	if(num_nodes < 0) { throw new IndexOutOfBoundsException(); }
    	/** reference for the created nodes, used as the iterator.*/
    	Node temp = new Node(1);
    	head = temp;
    	for(int i = 1; i<num_nodes; i++){	
    		temp.next = new Node(i+1);
    		temp = temp.next;
    	}
		tail = temp;
		tail.next = head;
    }
    //Constructors for the RoundRobinCLL class
    
    /**
     * Constructs the RoundRobinCLL given a number of nodes and termination limit
     * @param num_nodes - number of nodes for the CLL
     * @param termination_limit - Indicates when the main method should stop
     */
    public RoundRobinCLL(int num_nodes, int termination_limit) {
        this.num_nodes = num_nodes;
        this.termination_limit = termination_limit;
        fillRoundRubin();
    }
    /**
     * Constructs RoundRobinCLL given number of nodes
     * @param num_nodes - number of nodes for the CLL
     */
    public RoundRobinCLL(int num_nodes) {
        this.num_nodes = num_nodes;
        fillRoundRubin();
    }
    /**
     * Constructs RoundRobinCLL utilizing default values
     */
    public RoundRobinCLL() {
        fillRoundRubin();
    }

}
