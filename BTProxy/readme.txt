***TO BUILD AND RUN IN ECLIPSE***

- The BTProxy/lib directory contains 3 files:
	* swt.jar
	* swt-linux.jar
	* swt-macosx.jar

- By default, swt.jar is the SWT JAR for Windows since this is the most common OS.

- If you are running Linux:
	* rename swt.jar to swt-windows.jar
	* rename swt-linux.jar to swt.jar

- If you are running Mac OSX:
	* rename swt.jar to swt-windows.jar
	* rename swt-macosx.jar to swt.jar

- Refresh the Package Explorer list in Eclipse

- Build and run Main.java in Eclipse



***TO COMPILE AND RUN AS A JAR***

**STEP 1**
- Open Eclipse
- File -> Export
- Select "JAR file" from "Java" and click Next
- Select the BTProxy project and type-in the export destination for the JAR file
- Click Next
- Click Next again
- Select the "Use existing manifest from workspace" option and select MANIFEST.MF from the BTProxy directory
- The JAR file has now been created

**STEP 2**
- Place the proper SWT JAR file for the target OS in the same directory as the project's JAR file you created in step 1
- The SWT JARs for the different OSs can be found in BTProxy/lib
- If necessary, rename the SWT JAR file to "swt.jar"

**STEP 3**
- Make sure the file, icon.png, is in the same directory as the project JAR you created in step 1

**STEP 4**
- Run the project JAR you created in step 1