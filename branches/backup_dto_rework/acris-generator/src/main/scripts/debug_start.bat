rem @call start_rmi.bat || goto error
@call mvn -Dmaven.test.skip clean package || goto error
@call mvn dependency:build-classpath || goto error
@call debug_server.bat
@call del /Q /F cp.txt

echo ** Finished successfully **
exit /B 0

:error

echo ** Building failed **
pause
exit /B 1


