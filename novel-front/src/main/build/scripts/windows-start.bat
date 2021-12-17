@echo off
setlocal enabledelayedexpansion
set JAVA=java

set OPTS=-XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC
set ENGINE=novel-front.jar
cd ../
java -jar %OPTS%   -Dloader.path=conf,lib %ENGINE%
pause

