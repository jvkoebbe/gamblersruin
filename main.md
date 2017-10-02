# Gamblers Ruin Webpage

This repository contains a version of the Gambler's Ruin Java Applet from a long time ago. This object oriented class was written
by [Joe Koebbe](http://www.math.usu.edu/~koebbe) as part of a teaching unit for a course entitled Math 4620 Computer Aided
Mathematics for Secondary Mathematics Teachers. The idea was to present an example of a mathematical modeling problem with a
closed form analytic solution for the problem.

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

After you have downloaded the codes, but these in the same folder (there are no packages involved in these codes) and compile
the codes using the following command:

    javac GRGUI.java

Note that this assumes the binary for the java compiler (javac) is in the path for your computer. If this is not the case,
the process for setting up the path variable can be found at a number of sites related to working on Linux, Cygwin, and/or
Windows sites.
