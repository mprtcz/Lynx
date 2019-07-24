# Lynx

A simple java application listing all links and sublinks within a web page. 

## Running

To run the app following components are required:
* maven
* java `11` or higher
* internet connection
* git

1. Clone the repository
2. Using a command line (with JAVA_HOME and Path environment variables in place):
  
  * run `mvn package`
  * enter the created folder: **target**: `cd target`
  * run following command: `java -jar Lynx-1.0-SNAPSHOT.jar` with appropriate arguments
  
## App arguments 

In order to run the app properly, a url argument must be provided: 
 `--URL=` with a value being the page the user wants to have processed, ex:
 
 `java -jar Lynx-1.0-SNAPSHOT.jar --URL=google.com`
 
The app will throw if this argument is not provided.
 
 Another argument that can be passed in is `--OUTPUT`. ONce it is set as `COMMAND`, the app will not generate an output file which brings this to next step:
 
## Output

An output is a simple text file with links scrapped from the page and all of the subpages.
Indentations signify that links are children of the less indented link.

Static links are prefixed with `>` and external links are prefixed with `+`

## Tradeoffs and potential improvements:
 * First of all, the app has an extremely low test coverage and the author is very well aware of this fact.
 A few example tests are provided which can indicate how further tests would be written.
 
 * There is no retry mechanism. When a page is not loaded, it will stay that way and the app is not going to attempt next try. Adding a simple loop and increasing number of tries might be of handy in this case.
  
 * Static links are determined in a very naive way. More research and better defining what is a static link will help here. A name matcher checking if the link points to a well known static format (.css, .png etc.) could be used.
 
 * Logger can be configured in a way that allows the user to enable/disable console debug output.
 
  
  
 