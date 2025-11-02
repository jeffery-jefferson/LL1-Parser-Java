@echo off
setlocal enabledelayedexpansion

set "TEST_FLAG=false"

:parse_args
if "%~1"=="" goto args_done

if "%~1"=="--test" (
    set "TEST_FLAG=true"
) else if "%~1"=="-t" (
    set "TEST_FLAG=true"
) else (
    echo Unknown option: %~1
    exit /b 1
)
shift
goto parse_args

:args_done

set "TMP_DIR=%TEMP%\java_tmp_%RANDOM%"
mkdir "%TMP_DIR%"

echo Compiling Java files into %TMP_DIR%...
for /r %%f in (*.java) do (
    javac -d "%TMP_DIR%" -cp "libs/*" "%%f"
)

echo.

if "%TEST_FLAG%"=="true" (
    echo Running Tests
    java -cp "%TMP_DIR%;libs/*" Impl/Program true
) else (
    echo Running Program
    java -cp "%TMP_DIR%;libs/*" Impl/Program
)

echo.

REM we run this at the end because Windows doesn't have trap like bash
rd /s /q "%TMP_DIR%"
endlocal
