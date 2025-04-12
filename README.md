Javadoc Links:
 - https://github.com/mjl87/CSE360HW4/blob/master/BroadcastMessagePage.pdf
 - https://github.com/mjl87/CSE360HW4/blob/master/StaffActivityReportPage.pdf
 - https://github.com/mjl87/CSE360HW4/blob/master/StaffDashboardPage.pdf
 - https://github.com/mjl87/CSE360HW4/blob/master/SuspendUser.pdf
 - https://github.com/mjl87/CSE360HW4/blob/master/HW4JUnitTest.pdf

Junit Test Class:
 - https://github.com/mjl87/CSE360HW4/blob/master/src/application/HW4JUnitTest.java
 - https://github.com/mjl87/CSE360HW4/blob/master/HW4JUnitTest.pdf

Screencast Link:
 - https://youtu.be/nYAScqTKcVw

Getting Started (run instructions):

 - Download as a zip file under the Code button
 - Place and extract zip file in the Eclipse Workspace
 - Open the extracted zip folder as a project directory under Import from Existing Project File in the file tab
 - Right Click on the Project Folder, then navigate to Properties -> Java Build Path -> Libraries, and add the Javafx-sdk-23.01 User Library, JavaFX SDK Library, and JRE System Library to Modulepath
 - Then add the H2 User Library to the Classpath.
 - Apply and Close
 - Right click on the Project folder, then navigate to Run as -> Run Configurations. Under Java Application, open the source code main program in the Main tab.
 - Add the following under VM Arguments: --module-path "C:\Users\YOURUSERNAME\javafx-sdk-23.0.1\lib” --add-modules javafx.controls,javafx.fxml
- Apply and Close
