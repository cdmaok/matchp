# matchp
This is a picture matching program.  
User can input the text and get matching picture both through UI interface and restful interface.  

[![Build Status](https://travis-ci.org/cdmaok/matchp.svg?branch=master)](https://travis-ci.org/cdmaok/matchp)

## About the Program
1. It's developed by using Spring Framework and Elastic Search.


## How to deploy
1. mvn package
2. put the war package into the tomcat.
3. reconfig the property
4. start the tomcat service.
5. (optional) you may need to deploy another program named ![matchpService](https://github.com/cdmaok/matchp-service)

## Simple Note  
The project is based on spring framework and elastic Search, which is organized as several modules.
matchp-core: code about elastic search api
matchp-web: program web interface
matchp-api: program's rest api.

## TODOLIST
1. Image de-duplication (Finished)    
2. Learing to Rank (not yet)  
3. Adapting to multiple source data (not yet)  

## Index Field Declaration    
text: just text as you know    
polarity: text's sentiment  
img: image's url  
imgSign: image's signature  
doc_id: text's id  



