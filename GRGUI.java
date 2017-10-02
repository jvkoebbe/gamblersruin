import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
//
// immediate modifications
// -----------------------
// need the stop button to force the thing to quit
// need an exit button for the entire window
// need to take into account the window events
// finally - do the applet thing again to make things a bit cleaner
//
// this class provides a gui for investigating the gambler's ruin problem
// ----------------------------------------------------------------------
//
public class GRGUI extends JFrame implements ActionListener,
                                             ItemListener,
                                             Runnable,
                                             WindowListener,
                                             WindowFocusListener,
                                             WindowStateListener {
  //
  // constructor to build the gui for each and every user on the web
  // ---------------------------------------------------------------
  //
  public GRGUI(String lbl) {
    //
    // call the applet object
    // ----------------------
    //
    super(lbl);
    //
    // set the layout for the applet
    // -----------------------------
    //
    setLayout(new GridLayout(0,1));
    //
    // build a panel with all of the input and output
    // ----------------------------------------------
    //
    buildPanel();
    add(mainPanel);
    //
    // add the panels to the applet to make a gui
    // ------------------------------------------
    // fill in the help information
    // ----------------------------
    //
    setHelpInformation();
    //
    // setup the frame and show it
    // ---------------------------
    //
    setSize(WIDTH, HEIGHT);
    //
    // add the window event listeners
    // ------------------------------
    //
    addWindowListener(this);
    addWindowFocusListener(this);
    addWindowStateListener(this);
    //
  }
  //
  // this method will build the gui into a panel
  // -------------------------------------------
  //
  public boolean buildPanel() {
    mainPanel = new Panel();
    mainPanel.setLayout(new BorderLayout());
    //
    // the main panel definition
    // -------------------------
    //
    mainLabelPanel = new Panel();
    mainLabelPanel.setLayout(new GridLayout(1,0));
      mainLabel = new Label("Gambler's Ruin Application");
    mainLabelPanel.add(new Label(""));
    mainLabelPanel.add(mainLabel);
    mainLabelPanel.add(new Label(""));
    //
    // put in a panel for the parameter values
    // ---------------------------------------
    //
    parameterPanel = new Panel();
    parameterPanel.setLayout(new GridLayout(0,1));
      parameterLabel = new Label("Parameters");
      startingAmountLabel = new Label("Starting Amount");
      startingAmountTextField
           = new TextField(new Integer(startingCash).toString(), 20);
      goalAmountLabel = new Label("Goal");
      goalAmountTextField = new TextField(new Integer(goal).toString(), 20);
      probabilityLabel = new Label("Change of Winning a Game");
      probabilityTextField = new TextField(new Double(p).toString(), 20);
      noOfGamblersLabel = new Label("Number of Gamblers");
      noOfGamblersTextField
           = new TextField(new Integer(noOfGamblers).toString(), 20);
      maxNoOfPlaysLabel = new Label("Maximum Number of Plays");
      maxNoOfPlaysTextField
           = new TextField(new Integer(maxNoOfPlays).toString(), 20);
    parameterPanel.add(parameterLabel);
    parameterPanel.add(startingAmountLabel);
    parameterPanel.add(startingAmountTextField);
    parameterPanel.add(goalAmountLabel);
    parameterPanel.add(goalAmountTextField);
    parameterPanel.add(probabilityLabel);
    parameterPanel.add(probabilityTextField);
    parameterPanel.add(noOfGamblersLabel);
    parameterPanel.add(noOfGamblersTextField);
    parameterPanel.add(maxNoOfPlaysLabel);
    parameterPanel.add(maxNoOfPlaysTextField);

    outputPanel = new Panel();
    outputPanel.setLayout(new BorderLayout(0,1));
      outputTextArea = new TextArea(200,100);
      successPanel = new Panel();
      successPanel.setLayout(new GridLayout(2,0));
        winsLabel = new Label("Winners");
        winsTextField = new TextField("",10);
        lossLabel = new Label("# Ruined");
        lossTextField = new TextField("",10);
        chanceLabel = new Label("Chances");
        chanceTextField = new TextField("",10);
        tooLongLabel = new Label("Not Finished");
        tooLongTextField = new TextField("",10);
        exactLabel = new Label("Exact Value");
        exactTextField = new TextField("",10);
      successPanel.add(winsLabel);
      successPanel.add(winsTextField);
      successPanel.add(lossLabel);
      successPanel.add(lossTextField);
      successPanel.add(chanceLabel);
      successPanel.add(chanceTextField);
      successPanel.add(tooLongLabel);
      successPanel.add(tooLongTextField);
      successPanel.add(new Label(""));
      successPanel.add(new Label(""));
      successPanel.add(exactLabel);
      successPanel.add(exactTextField);
    outputPanel.add("Center", outputTextArea);
    outputPanel.add("South", successPanel);

    buttonPanel = new Panel();
    buttonPanel.setLayout(new GridLayout(1,0));
      startButton = new Button("Simulate");
      startButton.addActionListener(this);
      stopButton = new Button("Stop");
      stopButton.addActionListener(this);
      toggleStartButton();
      exitButton = new Button("Exit");
      exitButton.addActionListener(this);
    buttonPanel.add(startButton);
    buttonPanel.add(stopButton);
    buttonPanel.add(exitButton);
    //
    mainPanel.add("North", mainLabelPanel);
    mainPanel.add("West", parameterPanel);
    mainPanel.add("Center", outputPanel);
    mainPanel.add("South", buttonPanel);
    //
    return true;
  }
  //
  // this method starts each new gambler into the process with a new thread
  // ----------------------------------------------------------------------
  //
  public void startGambling() {
    if(gamblingThread != null) return;
    gamblingThread = new Thread(this);
    gamblingThread.start();
  }
  //
  // start the gambler to running
  // ----------------------------
  //
  public void run() {
    //
    // build an index
    // --------------
    //
    int index = 0;
    //
    // get the parameters from the text fields
    // ---------------------------------------
    //
    getInfo();
    //
    // check the parameters
    // --------------------
    //
    if(!checkInfo()) {
      System.out.println("Something Wrong in the Input Data!");
      return;
    }
    //
    // stop the thread for the current gambler
    // ---------------------------------------
    //
    stopGamblers = false;
    //
    // initialize the output generated
    // -------------------------------
    initOutput();
    toggleStartButton();
    //
    // set up some generic gamblers matching the maximum number of threads
    // allowed
    // -------
    //
    gamblers = new Gambler[noOfThreads];
    maskVector = new boolean[noOfThreads];
    for(int i=0; i<noOfThreads; i++) maskVector[i] = true;
    //
    // compute the exact result from the simple gambler's ruin problem
    // ---------------------------------------------------------------
    //
    computeExactResult();

    int nt = 0;
    winners = 0;
    losers = 0;
    unknown = 0;

    // loop over all the gamblers
    for(int l=0; l<noOfGamblers; l++) {
      if(stopGamblers) {
        stopGambling();
        return;
      }

      // get the next available thread location
      for(int ii=0; ii<noOfThreads; ii++) {
        if(maskVector[ii]) {
          index = ii;
          maskVector[ii] = false;
          break;
        }
      }

      // set up the available gambler to go
      gamblers[index] = new Gambler(l+1, startingCash, goal, p, maxNoOfPlays);
      gamblers[index].startGambling();

      nt = nt + 1;

      // sleep the process if the slots are all filled up
      while(nt >= noOfThreads) {
        try {
          Thread.sleep(100);
          for(int ii=0; ii<noOfThreads; ii++) {
            if(gamblers[ii].isGamblerDone()) {
              if(gamblers[ii].wasGamblerThrownOut()) {
                unknown = unknown + 1;
              } else {
                if(gamblers[ii].didGamblerWin()) {
                  winners = winners + 1; 
                } else {
                  losers = losers + 1; 
                }
              }
              outputTextArea.appendText(gamblers[ii].toString() + "\n");
              setOutput();
              maskVector[ii] = true;
              gamblers[ii] = null;
              nt = nt - 1;
            }
          }
        } catch(InterruptedException ie) {
        }
      }

    }

    // get the rest of the information
    while(nt > 0) {
      try {
        Thread.sleep(100);
        for(int ii=0; ii<noOfThreads; ii++) {
          if(gamblers[ii] != null) {
            if(gamblers[ii].isGamblerDone()) {
              if(gamblers[ii].didGamblerWin()) {
                winners = winners + 1; 
              } else {
                losers = losers + 1; 
              }
              outputTextArea.appendText(gamblers[ii].toString());
              if(gamblers[ii].wasGamblerThrownOut()) unknown = unknown + 1;
              setOutput();
              maskVector[ii] = true;
              gamblers[ii] = null;
              nt = nt - 1;
            }
          }
        }
      } catch(InterruptedException ie) {
      }
    }

    stopGambling();

  }

  public void computeExactResult() {
    double q = 1.0 - p;
    double ratio = q/p;
    double ratc = Math.pow(ratio, goal);
    double rata = Math.pow(ratio, startingCash);
    double exactVal = ( 1.0 - rata )/( 1.0 - ratc );
    exactTextField.setText(digitFilter(exactVal,7));
  }

  public void setOutput() {
    winsTextField.setText(new Integer(winners).toString());
    lossTextField.setText(new Integer(losers).toString());
    tooLongTextField.setText(new Integer(unknown).toString());
    double denom = ( (double) ( winners + losers ) );
    if(denom == 0.0) denom = 1.0;
    double val = ((double) winners) / denom;
    chanceTextField.setText(digitFilter(val,7));
  }

  public void stopGambling() {
    stopGamblers = true;
    gamblingThread = null;
    toggleStartButton();
  }

  public void initOutput() {
    String s = " Gambling Results:\n\n";
    outputTextArea.setText(s);
  }
   
  public void setHelpInformation() {
    String s = "How to Use the Gambler's Ruin Application\n\n";
    s = s + "Fill in the parameter values in the text fields on the left side";
    s = s + " of the applet. The values must satisfy:";
    s = s + " resident of the U.S. you must supply a TOEFL score before we can";
    s = s + " process application materials for you. If possible, fill in the";
    s = s + " GRE scores. \n\n";
    s = s + "If something changes color it means that you need to supply or";
    s = s + "modify the information in that area of the preapplication.\n\n";
    s = s + "When all the information is valid, clicking on the submit button";
    s = s + "will send the information to the appropriate people in the Dept.";
    helpString = s;
  }
   
  public void toggleStartButton() {
    Color bg = Color.black;
    Color fg = Color.white;
    if(stopGamblers) {
      startButton.setBackground(fg);
      startButton.setForeground(bg);
      stopButton.setBackground(bg);
      stopButton.setForeground(fg);
    } else {
      startButton.setBackground(bg);
      startButton.setForeground(fg);
      stopButton.setBackground(fg);
      stopButton.setForeground(bg);
    }
  }
   
  public void toggleColor(Component c) {
    Color bg = c.getBackground();
    Color fg = c.getForeground();
    c.setBackground(fg);
    c.setForeground(bg);
  }
   
  // the following gets the simulation information from the text fields
  public void getInfo() {
    String s;
    int ival;
    double dval;

    // set the data problem flag
    dataProblem = false;

    s = "dummy";
    if(startingAmountTextField != null) s = startingAmountTextField.getText();
    try {
      ival = Integer.valueOf(getValue(s)).intValue();
    } catch(NumberFormatException nfe) {
      ival = 0;
      dataProblem = true;
    }
    startingCash = ival;

    s = "dummy";
    if(goalAmountTextField != null) s = goalAmountTextField.getText();
    try {
      ival = Integer.valueOf(getValue(s)).intValue();
    } catch(NumberFormatException nfe) {
      ival = 0;
      dataProblem = true;
    }
    goal = ival;

    s = "dummy";
    if(probabilityTextField != null) s = probabilityTextField.getText();
    try {
      dval = Double.valueOf(getValue(s)).doubleValue();
    } catch(NumberFormatException nfe) {
      dval = DEFAULTPROBABILITY;
      dataProblem = true;
    }
    p = dval;
    if(p < 0.0) p = 0.0;
    if(p > 1.0) p = 1.0;
    q = 1.0 - p;

    s = "dummy";
    if(noOfGamblersTextField != null) s = noOfGamblersTextField.getText();
    try {
      ival = Integer.valueOf(getValue(s)).intValue();
    } catch(NumberFormatException nfe) {
      ival = 0;
      dataProblem = true;
    }
    noOfGamblers = ival;
    if(noOfGamblers < 1) noOfGamblers = 1;
    noOfThreads = noOfGamblers;
    if(noOfThreads > MAXIMUM_THREADS) noOfThreads = MAXIMUM_THREADS;

    s = "dummy";
    if(maxNoOfPlaysTextField != null) s = maxNoOfPlaysTextField.getText();
    try {
      ival = Integer.valueOf(getValue(s)).intValue();
    } catch(NumberFormatException nfe) {
      ival = 0;
      dataProblem = true;
    }
    maxNoOfPlays = ival;
    if(maxNoOfPlays < 40) maxNoOfPlays = 40;

  }

  public String getValue(String line) {
    return line.trim();
  }
   
  public boolean checkInfo() {
    if(dataProblem) return false;
    return true;
  }

  public void windowDeactivated(WindowEvent evt) {
    System.out.println(this.getAppName() + " App Deactivated:  window event");
    System.exit(0);
  }
  public void windowActivated(WindowEvent evt) {
    System.out.println("WindowListener event:  windowActivated() called");
  }
  public void windowDeiconified(WindowEvent evt) {
    System.out.println("WindowListener event:  windowDeiconified() called");
  }
  public void windowIconified(WindowEvent event) {
    System.out.println("WindowListener event:  windowIconified() called");
  }
  public void windowClosed(WindowEvent event) {
    System.out.println("WindowListener event:  windowClosed() called");
    System.exit(0);
  }
  public void windowClosing(WindowEvent event) {
    System.out.println("WindowListener method called: windowClosing.");
    ActionListener task = new ActionListener() {
      boolean alreadyDisposed = false;
      public void actionPerformed(ActionEvent e) {
        if(isDisplayable()) {
          alreadyDisposed = true;
          dispose();
        }
      }
    };
    Timer timer = new Timer(500, task); //fire every half second
    timer.setInitialDelay(2000);        //first delay 2 seconds
    timer.setRepeats(false);
    timer.start();
  }
  public void windowOpened(WindowEvent event) {
    System.out.println("WindowListener event:  windowOpened() called");
  }
  public void windowLostFocus(WindowEvent event) {
    System.out.println("WindowFocusListener event: windowLostFocus() called");
  }
  public void windowGainedFocus(WindowEvent event) {
    System.out.println("WindowFocusListener event: windowGainedFocus() called");
  }
  public void windowStateChanged(WindowEvent event) {
    System.out.println("WindowStateListener event: windowStateChanged() called");
  }
   
  public void itemStateChanged(ItemEvent event) {
    Object src = event.getSource();
  }
   
  public void actionPerformed (ActionEvent event) {
    Object source = event.getSource();
    if(source.equals(startButton)) startGambling();
    if(source.equals(stopButton)) stopGambling();
    if(source.equals(exitButton)) {
      System.out.println(getAppName() + ": exiting....");
      System.exit(0);
    }
  }

  public String getAppName() {
    return applicationName;
  }

  public String digitFilter(double value, int n) {
    String integerPart = "0";
    String dot = ".";
    String fractionPart = "0";

    String s = Double.toString(value);
    StringTokenizer st = new StringTokenizer(s, " .");
    if(st.hasMoreTokens()) integerPart = st.nextToken();
    if(st.hasMoreTokens()) {
      String t = st.nextToken();
      if(t.length() <= n) fractionPart = t;
      if(t.length() > n) fractionPart = t.substring(0,n);
    }
    return integerPart + dot + fractionPart;
  }

  public static void main(String args[]) {
    // pretty simple test
    GRGUI grgui = new GRGUI("Gamblers Ruin Application Test");
    grgui.setSize(800,400);
    grgui.show();
  }

  // local variables
  private static String applicationName = "GRGUI";
  private int WIDTH = 800;
  private int HEIGHT = 400;
    private Panel mainPanel = null; 
      private Panel mainLabelPanel;
        private Label mainLabel;

    private Panel parameterPanel;
      private Label parameterLabel;
      private Label startingAmountLabel;
      private TextField startingAmountTextField;
      private Label goalAmountLabel;
      private TextField goalAmountTextField;
      private Label probabilityLabel;
      private TextField probabilityTextField;
      private Label noOfGamblersLabel;
      private TextField noOfGamblersTextField;
      private Label maxNoOfPlaysLabel;
      private TextField maxNoOfPlaysTextField;
    private Panel outputPanel;
      private TextArea outputTextArea;
      private Panel successPanel;
        private Label winsLabel;
        private TextField winsTextField;
        private Label lossLabel;
        private TextField lossTextField;
        private Label chanceLabel;
        private TextField chanceTextField;
        private Label tooLongLabel;
        private TextField tooLongTextField;
        private Label exactLabel;
        private TextField exactTextField;
    private Panel buttonPanel;
      private Button startButton;
      private Button stopButton;
      private Button exitButton;
  //
  // variables for the calculations
  // ------------------------------
  //
  private int startingCash = 5;
  private int currentCash = 5;
  private int goal = 10;
  private double p = 0.49;
  private double q = 0.51;
  private int noOfGamblers = 100;
  private int maxNoOfPlays = 100;
  //
  private boolean dataProblem = false;
  private static double DEFAULTPROBABILITY = 0.49;
  //
  // thread variables for the gamblers to play
  // -----------------------------------------
  //
  private Gambler [] gamblers = null;
  private Thread gamblingThread = null;
  private boolean stopGamblers = true;
  private boolean [] maskVector = null;
  private static int MAXIMUM_THREADS = 5;
  private int noOfThreads = 5;
  //
  // variables to take care of the totaling of winners and losers
  // ------------------------------------------------------------
  //
  private int winners = 0;
  private int losers = 0;
  private int unknown = 0;
  //
  // a help string
  // -------------
  //
  private String helpString;
  //
}
