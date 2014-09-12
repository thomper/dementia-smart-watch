dementia-smart-watch
====================

INB372 Team7 project.

[![Build Status](http://106.186.31.86:9090/buildStatus/icon?job=dementia-smart-watch)](http://106.186.31.86:9090/job/dementia-smart-watch/)

Android Build Instructions
--------------------------
In the repo root directory run:

android update project -p ./smart-watch-android/  
android update test-project -m ../smart-watch-android/ -p ./smart-watch-android-tests/

Then in the smart-watch-android-tests/ directory run:

ant clean debug  
ant installt test

You will need to have a phone connected in debugging mode or an Android Virtual Device running for the tests to run.

Web Build Instructions
----------------------
Run ant from the web/WEB-INF directory.
