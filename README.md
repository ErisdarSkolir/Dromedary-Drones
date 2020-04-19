# Team Orange - Dromedary Drones Comp. 350 A

## This is an Eclipse project built with JDK 1.8 u241 and JavaFX 8

To run this project make sure Eclipse is installed, if not it can be downloaded from [here](https://www.eclipse.org). In addition e(fx)clipse needs to be installed for the JavaFX library. To do this when eclipse is open go to help > eclipse marketplace, search "efxclipse", and install the efxclipse plugin.

Next, download the master branch of this repostiory as a zip file. Then in eclipse go to file > import, then general > projects from folder or archive. In the next window click "Archive" and select the zip folder that was just downloaded. You only need to select the second folder which has "master" at the end of it.

Next right click the project and go to properties > java build path and double click on the entry labeled "JRE System Library" and then select your the JDK installed on your computer. Note that this *must* be JDK 1.8 as newer version of java will not work with the JavaFX 8 library. 

Finally still in the java build path window click "Add Library" and add the JavaFX SDK if it is not already added.

Once all these steps are completed the project should have no compilation errors. If so check that the JDK library and JavaFX library are installed correctly.

To run the program, in the package explorer navigiate to Dromedary Drones > src > edu.gcc.main > Main.java. Right click Main.java and select Run As > Java Application
