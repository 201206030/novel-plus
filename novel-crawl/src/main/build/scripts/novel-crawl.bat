@echo off
cd /d "%~dp0.."
java -jar -Dspring.profiles.active=prod novel-crawl.jar
pause
