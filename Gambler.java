import java.io.*;
//
// this class does the work od one gambler
//
public class Gambler extends Object implements Runnable {
  //
  // the main instantiation of a gambler
  // -----------------------------------
  //
  public Gambler(int id, int a, int c, double p, int mp) {
    //
    // this is a gambler id to keep track of which gambler this is
    // -----------------------------------------------------------
    //
    gamblerID = id;
    done = setParameters(a, c, p, mp);
  }
  //
  // this method is used to set the parameters for the gambler
  // ---------------------------------------------------------
  //
  public boolean setParameters(int a, int c, double p, int mp) {
    //
    // set the starting amount
    // -----------------------
    //
    startingCash = a;
    //
    // check the starting amount to see if there is anything to gamble with
    // --------------------------------------------------------------------
    //
    if(startingCash < 0) startingCash = 0;
    //
    // set the goal amount of cash to end up with
    // ------------------------------------------
    //
    goal = c;
    //
    // make sure the goal is greater than zero
    // ---------------------------------------
    //
    if(goal < 0) goal = 0;
    //
    // test the goal amount to make sure it is greater than the starting amount
    // ------------------------------------------------------------------------
    //
    if(goal <= startingCash) return true;
    //
    // set the probablity of winning and losing
    // ----------------------------------------
    //
    winningProbability = p;
    if(winningProbability < 0.0) winningProbability = 0.0;
    if(winningProbability > 1.0) winningProbability = 1.0;
    q = 1.0 - p;
    //
    // set a maximum number of plays to avoid infinite sequences
    // ---------------------------------------------------------
    //
    maximumNoOfPlays = mp;
    if(maximumNoOfPlays > 1000) maximumNoOfPlays = 1000;
    //
    return true;
    //
  }
  //
  // this method starts the gambling up for this instantiation
  // ---------------------------------------------------------
  //
  public void startGambling() {
    //
    // instantiate a thread to allow the gambling to begin
    // ---------------------------------------------------
    //
    Thread gamblingThread = new Thread(this);
    //
    // start the thread
    // ----------------
    gamblingThread.start();
    //
  }
  //
  // set up the running of the gambler
  // ---------------------------------
  //
  public void run() {
    //
    // set a variable to keep track of the current amount of cash
    // ----------------------------------------------------------
    //
    currentCash = startingCash;
    //
    // set the number of plays to 0
    // ----------------------------
    //
    noOfPlays = 0;
    //
    // loop over the number of plays - note that the loop could go on forever
    // so a while loop is used to avoid too large a number of plays
    // ------------------------------------------------------------
    //
    while(0<currentCash && currentCash<goal
                        && noOfPlays < maximumNoOfPlays) {
      //
      // compute a random number as if the gambler has been played once
      // --------------------------------------------------------------
      //
      double ranval = Math.random();
      //
      // test for a win or loss
      // ----------------------
      //
      if(ranval<winningProbability) {
        currentCash = currentCash + 1;
      } else {
        currentCash = currentCash - 1;
      }
      //
      // increment the number of plays
      // -----------------------------
      //
      noOfPlays++;
      //
    }
    //
    // when done, determine if the gambler achieved the goal amount
    // ------------------------------------------------------------
    //
    if(currentCash == goal) {
      won = true;
    } else {
      won = false;
    }
    //
    // test to see if the number of plays exceeded the maximum number of plays
    // allowed
    // -------
    //
    thrownOut = false;
    if(noOfPlays >= maximumNoOfPlays) thrownOut = true;
    //
    // be done
    // -------
    //
    done = true;
  }
  //
  // need a method to set the gambler id
  // -----------------------------------
  //
  public void setGamblerID(int id) { gamblerID = id; }
  public int getGamblerID() { return gamblerID; }
  //
  // let the object instantiating the gambler know when the process is done
  // ----------------------------------------------------------------------
  //
  public boolean isGamblerDone() {
    return done;
  }
  //
  // let the object instantiating the gambler know if the gambler won
  // ----------------------------------------------------------------
  //
  public boolean didGamblerWin() {
    if(won) return true;
    return false;
  }
  //
  // let the object instantiating the gambler know the gambler was thrown out
  // ------------------------------------------------------------------------
  //
  public boolean wasGamblerThrownOut() {
    return thrownOut;
  }
  //
  // this method will create a string of what the object is about
  // ------------------------------------------------------------
  //
  public String toString() {
    String s = "Gambler Information - ";
    s = s + " ID:  " + getGamblerID() + "  Amount: " + currentCash
          + "  No. of Plays: " + noOfPlays;
    if(won) {
      s = s + "  WON";
    } else {
      if(thrownOut) {
        s = s + "  THROWNOUT";
      } else {
        s = s + "  LOST";
      }
    }
    return s;
  }
  //
  // this method will test the object to make sure things work
  // ---------------------------------------------------------
  //
  public static void main(String args[]) {
    //
    // instantiate a gambler
    // ---------------------
    //
    Gambler g = new Gambler(101, 10, 20, 0.49, 200);
    //
    // set a sum variable to keep track of winners and losers
    // ------------------------------------------------------
    //
    int sumw = 0;
    int suml = 0;
    //
    // a variable to keep track of the number of gamblers thrown out before
    // the result known
    // ----------------
    //
    int sumto = 0;
    //
    // set the number of gamblers for the test run
    // -------------------------------------------
    //
    int ng = 100;
    //
    // loop over the number of gamblers in the test
    // --------------------------------------------
    //
    for(int i=0; i<ng; i++) {
      g.setGamblerID(i+1);
      g.run();
      System.out.println(g.toString());
      if(g.didGamblerWin()) {
        sumw++;
      } else {
        if(g.wasGamblerThrownOut()) {
          sumto++;
        } else {
          suml++;
        }
      }
    }
    //
    // output the results
    // ------------------
    //
    double succ = ((double) sumw) / ( ng - sumto );
    double lose = ((double) suml) / ( ng - sumto );
    System.out.println("\n Chances to succeed:  " + succ);
    System.out.println(" Chances to fail:     " + lose);
    System.out.println(" Thrown out:          " + sumto);
    //
  }
  //
  // local variables
  // ---------------
  //
  private int gamblerID;
  private int startingCash = 1;
  private int currentCash = 1;
  private int goal = 10;
  private double winningProbability;
  private double q;
  private int noOfPlays = 0;
  private int maximumNoOfPlays = 100;
  private boolean won;
  private boolean done = false;
  private boolean thrownOut = false;

  private Thread gamblingThread;

}
