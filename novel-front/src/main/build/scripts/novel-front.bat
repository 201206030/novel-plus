@echo off
cd /d "%~dp0.."
java -jar -Dspring.profiles.active=prod novel-front.jar
pause
