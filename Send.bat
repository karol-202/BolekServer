@echo off
title Sending files to server
set /p address=Type server address: 
pscp out\artifacts\BolekServer\BolekServer.jar karol@%address%:/home/karol/bolek/BolekServer.jar
pause