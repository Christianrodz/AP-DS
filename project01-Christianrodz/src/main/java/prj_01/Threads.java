//Christian Rodriguez Christian.rodriguez.73@upr.edu
package prj_01;
import java.util.*;
public class Threads {
    public ArrayList<Thread> threads = new ArrayList<Thread>();
    /**
     * Constructs an ArrayList of threads given the number of threads
     * @param noThreads - number of threads given by the user
     */
    public Threads(int noThreads){
       for (int i=0; i<noThreads; i++){
           ThreadRunnable runnable = new ThreadRunnable();
           System.out.println("Creating Thread " + (i+1));
           threads.add(new Thread(runnable, ""+i));
       }
    }
    /**
     * Constructs ArrayList of threads given number of threads and a ThreadRunnable
     * @param noThreads - number of threads given by the user
     * @param runnable - object that runs the threads
     */
    public Threads(int noThreads, ThreadRunnable runnable){
        for (int i=0; i<noThreads; i++){
            System.out.println("Creating Thread " + (i+1));
            threads.add(new Thread(runnable, ""+i));
        }
    }
}
