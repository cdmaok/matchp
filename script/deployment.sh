#!/bin/bash
## this script is to deploy the matchp program
MP_HOME=/home/guanxinjun/matchp
TOMCAT_HOME=/usr/local/apache-tomcat-7.0.27

cd $MP_HOME
git pull origin master
mvn clean package
sudo cp ./matchp-web/target/matchp-web.war $TOMCAT_HOME/webapps/
sudo rm -rf $TOMCAT_HOME/webapps/matchp-web
cd $TOMCAT_HOME/bin
## need to test the tomcat if is ready or not use curl or something.
sudo sh shutdown.sh
sudo sh startup.sh
echo 'finish update'

