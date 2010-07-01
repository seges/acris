@set RMI_INTERFACE=..\rmi\target\rmi-1.0.0-SNAPSHOT.jar
@set M2_REPO=%USERPROFILE%\.m2\repository

@echo off
setlocal enabledelayedexpansion
set SEPARATOR=/
set filecontent=
for /f "delims=" %%a in (cp.txt) do (
  set currentline=%%a
  set filecontent=!filecontent!%SEPARATOR%!currentline!
)

@set CLASSPATH=%filecontent%
@set CLASSPATH=%CLASSPATH%;%~dp0\src\main\java;%~dp0\src\main\resources;%~dp0\..\client\target\classes;%~dp0\..\client\src\main\java;%~dp0\..\client\src\main\resources;%~dp0\target\classes;
@echo on

@REM start java -classpath %CLASSPATH% -Dgen="www-gen -out www-test" -Djava.rmi.server.codebase="%~dp0/%RMI_INTERFACE%" sk.seges.web.generator.service.ContentGeneratorService
@start java -Xmx512m -classpath "%CLASSPATH%" -XstartOnFirstThread -Djava.rmi.default=true sk.seges.web.generator.service.ContentGeneratorService