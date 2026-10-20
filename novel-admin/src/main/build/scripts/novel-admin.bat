@echo off
cd /d "%~dp0.."
java -jar -Dspring.profiles.active=prod novel-admin.jar
pause
