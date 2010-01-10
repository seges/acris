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
set CLASSPATH=%CLASSPATH%;%~dp0src\main\java;%~dp0src\main\resources;Z:\java\workspaces\Voipac.com\trunk\voipac-client \target\classes;Z:\java\workspaces\Voipac.com\trunk\voipac-client \src\main\java;Z:\java\workspaces\Voipac.com\trunk\voipac-client \src\main\resources;%~dp0target\classes\;

@REM start java -classpath %CLASSPATH% -Dgen="www-gen -out www-test" -Djava.rmi.server.codebase="%~dp0/%RMI_INTERFACE%" sk.seges.web.generator.service.ContentGeneratorService
java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,address=8001,suspend=y -Xmx512m -classpath "%CLASSPATH%" -Djava.rmi.default=true sk.seges.synapso.service.ContentGeneratorService sk.seges.voipac.client.Site
@rem start java -Xmx512m -classpath "%CLASSPATH%" -Djava.rmi.default=true sk.seges.synapso.service.ContentGeneratorService sk.seges.voipac.client.Site