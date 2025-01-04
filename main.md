# Gamblers Ruin Webpage

This repository contains a version of the Gambler's Ruin Java Applet from a long time ago. This object oriented class was written
by [Joe Koebbe](http://www.math.usu.edu/~koebbe) as part of a teaching unit for a course entitled Math 4620 Computer Aided
Mathematics for Secondary Mathematics Teachers. The idea was to present an example of a mathematical modeling problem with a
closed form analytic solution for the problem. Note that the solution obtained is for a specific case of the problem. The
assumptions required to compute the analytic solution are given in the materials for this repository.

The code is self-contained, written in Java. All you need is a relatively new version of a Java compiler. The graphical user
interface displays textfields for input and output. This webpage documents how a user can interact with the code once you have
it compiled and running. There are also some basic instructions on how to download the code, compile the code (if necessary),
and run the GUI.

### Gambler's Ruin codes written in Java

The original code was written in Java due to the desire to create an Applet that could be run from inside most browsers. Since
the main people involved with Java development have decided not to support Java plugins anymore, the easiest way to obtain and
use the code is by downloading the jar file in this repository. If you are interested, you can download the software and use it
as is - meaning that I won't fix problems you create in any of the code. If you just want the code up and running you should
stick with the jar file approach.

### Downloading the code:

The code can be downloaded by saving the targets of the following two links to your computer. The files are text files. So, if
you are receiving something else (like binaries, malware, or adware) you are not downloading the code that I have written. The
code is also very well documented with descriptions of most things that are going on in the code. If problems start with people
misusing the code making a mess of the code, I will remove the code from this site. So, please be responsible when you use this
or anyone's code for that matter.

1. [GRGUI.java](https://jvkoebbe.github.io/gamblersruin/GRGUI.java).
2. [Gambler.java](https://jvkoebbe.github.io/gamblersruin/Gambler.java).

After you have downloaded the codes, put these files in the same folder (there are no packages involved in these codes) and
compile the codes using the following command in a **cmd** window:

    javac GRGUI.java

Note that this assumes the binary for the java compiler (javac) is in the path for your computer. If this is not the case,
the process for setting up the path variable can be found at a number of sites related to working on Linux, Cygwin, and/or
Windows sites.

### Jar File Download

A java archive (jar) file can be downloaded by saving the target for the following link:

[GRGUI.jar](https://jvkoebbe.github.io/gamblersruin/GRGUI.jar)

To use this option the only other step in the process is to use the **java** command to run the code directly out of the jar
file. The command is:

    java -jar GRGUI.jar

Keep in mind that you may still need to change the system path variable to include the **java** command.



1. [Oracle Instructions:](https://www.java.com/EN/DOWNLOAD/HELP/PATH.XML)
2. [Stack Overflow Instructions:](https://stackoverflow.com/questions/31925437/javac-doesnt-work-correctly-on-windows-10)

Note that you need to make sure that you include both the **javac** and **java** commands in this process. So, you should
include the path to the Java Developer Kit (JDK) folder instead of the Java Runtime Environment folder (JRE). The former
includes both the compiler and runtime commands. Once you have the code compiled, you can run the application that will
build a graphical user interface on your screen. The command is:

    java GRGUI

You should see a small window show up in the upper left hand corner of your screen. The textfields can be used to play around
with the Gambler's Ruin problem. To see how the code works, just hit the Simulate button. Change the parameters and see what
happens.

### Opening a CMD Window to Run the Gamblers Ruin Application

To get at all of the Java commands on a Windows system, you will need to open a CMD window. This can be done by running the
command in Windows. Hold down the meta key (the one with the Microsoft logo) and type an 'r'. A window will pop up. Then
type the command "cmd" and enter to start a CMD-Window. The **javac** and **java** commands can be run from inside this
window to execute the commands and start up the Graphical User Interface (GUI).
