## Project
This is a project created by Alex Huseu as a code challenge for Vorto company. 
### Project structure
The project consists from few modules and a Main class which is responsible for running the application.
`utils` package contains helpers methods
`files` package responsible for reading files with loads
`model` package contains files with required business models
`analyzer` package contains files with main business logic which construct drivers routes. 
Any other algorithm for routes creation could be easily added by implementing `Analyzer` interface. 
Unit tests weren't added because they are not considered during the project evaluation.
## Setup
### Java setup
To run this application java 11 or later have to be installed on your machine.
You can check which version of java is currently active on your machine by executing 
the command in your terminal `java --version`
To install java 11 run `brew install openjdk@11` in terminal. 
Follow the instructions in the terminal and make the required configuration
### Github
clone the project from the public Github repo `https://github.com/XcomXX/vorto_project.git`
### Compile the project
Go to `vorto_project/src`
Run the command in a terminal window `javac -d ../out *.java`
### Run the project
Run the command in a terminal window `java -cp pathToOutDir Main pathToFolderWithTests/test.txt`
For example `java -cp /Users/joe/projects/vorto_project/out Main /Users/joe/vorto_tests/problem8.txt`
