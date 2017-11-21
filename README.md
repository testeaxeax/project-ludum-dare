[![Build Status](https://travis-ci.org/testeaxeax/project-ludum-dare.svg?branch=master)](https://travis-ci.org/testeaxeax/project-ludum-dare)

**Installation instructions:**
1. Clone repository using "git clone https://github.com/testeaxeax/project-ludum-dare"
2. Open Eclipse
3. Import the project using File > Import... > Gradle > Existing Gradle Project, then select the project's root folder and click on "Finish"
4.1 Now you need to select *-desktop, click on "Properties", select "Java Build Path", switch to the "Source" tab, click on "Select Folder" and finally tick "assets"
OR
4.2 Run the gradle task "afterEclipseImport"
5. Now you can build the project for desktop by using the "Run as..." option in the context menu and selecting the "DesktopLauncher" class

**NOTICE**: You will need to redo step 4.1 or 4.2 every time you refresh your gradle configuration

[Documentation](https://testeaxeax.github.io/project-ludum-dare/)
