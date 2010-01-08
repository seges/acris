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

REM @set CLASSPATH=%CLASSPATH%;src\main\java;src\main\resources;..\client\target\classes;..\client\src\main\java;..\client\src\main\resources;target\classes;
@echo on
set CLASSPATH=%CLASSPATH%;%~dp0src\main\java;%~dp0src\main\resources;%~dp0..\client\target\classes;%~dp0..\client\src\main\java;%~dp0..\client\src\main\resources;%~dp0target\classes\;

@REM start java -classpath %CLASSPATH% -Dgen="www-gen -out www-test" -Djava.rmi.server.codebase="%~dp0/%RMI_INTERFACE%" sk.seges.web.generator.service.ContentGeneratorService
start java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,address=8001,suspend=y -Xmx512m -classpath "%CLASSPATH%" -Djava.rmi.default=true sk.seges.web.generator.service.ContentGeneratorService