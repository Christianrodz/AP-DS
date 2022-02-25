//Christian Rodriguez Christian.rodriguez.73@upr.edu
package prj_01;

public class RRScheduler {
	/**
	 * The main that initiates the run of the program
	 * <p>
	 * Initialize default values for args
	 * Loop through list of args, if it finds an arg for the termination limit, number of threads and the project step it changes it accordingly
	 * If the inputed project step is not 1 or 2, tell the user the correct values and exit back into the terminal
	 * <p>
	 * Start the program
	 * Create RoundRobinCLL object as a null
	 * if project step is 2 we use the constructor with the number of threads and the termination limit
	 * Create a threadRunnable with the roundRobine
	 * Create the threads and make them runnable with threadRunnable
	 * Use a loop to start every created thread
	 * If roundRobine is not null, start the main findFilledSlot() method in RoundRobinCLL
	 * Once the main is finished, print out the indicator that it is done
	 * Change stopLoop to true so the program finishes.
	 * @param args
	 */
    public static void main(String[] args){
        int termination_limit = 100;
        int no_threads = 5;
        int project_step = 1;
        for (int i=0; i<args.length; i++) {
            if (args[i].equals("-t") || args[i].equals("--termination")) {
                termination_limit = Integer.valueOf(args[++i]);
            }
            else if (args[i].equals("-p") || args[i].equals("--processes")) {
                no_threads = Integer.valueOf(args[++i]);
            }
            else if (args[i].equals("-s") || args[i].equals("--prjstep")) {
                project_step = Integer.valueOf(args[++i]);
                if (project_step!=1 && project_step!=2) {
                    System.out.println("Project Step value is 1 or 2 (" + project_step + " given).");
                    System.exit(1);
                }
            }
        }

        System.out.println("Starting Program...");


        RoundRobinCLL roundRobine = null;
        if (project_step==2) {
            roundRobine =  new RoundRobinCLL(12, termination_limit);
        }

        ThreadRunnable rrRunnable = new ThreadRunnable(roundRobine);
        Threads threads = new Threads(no_threads, rrRunnable);

        for (int i=0; i<threads.threads.size(); i++) {
            threads.threads.get(i).start();
        }

        if (roundRobine!=null) roundRobine.findFilledSlot() ;

        System.out.println("Main Finished ... Bye Bye");

        if (roundRobine!=null) roundRobine.stopLoop = true;

    }
}
