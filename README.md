# matchp
This is a picture matching program.  
User can input the text and get matching picture both through UI interface and restful interface.  

## About the Program
1. It's developed by using Spring Framework and Elastic Search.


## How to deploy
1. mvn package
2. put the war package into the tomcat.
3. reconfig the property
4. start the tomcat service.

## Simple Note  
The project is based on spring framework and elastic Search, which is organized as several modules.
matchp-core: code about elastic search api
matchp-web: program web interface
matchp-api: program's rest api.
