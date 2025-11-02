@echo off
setlocal enabledelayedexpansion

rem Exit immediately if a command fails
set ERRLEVEL=0

set TEST_FLAG=false
set OUT_DIR=out

rem Parse command line arguments
:parse_args
if "%~1"=="" goto after_parse
if "%~1"=="--test" (
    set TEST_FLAG=true
    shift
    goto parse_args
)
if "%~1"=="-t" (
    set TEST_FLAG=true
    shift
    goto parse_args
)
echo Unknown option: %~1
exit /b 1
:after_parse

rem Ensure out directory exists
if not exist "%OUT_DIR%" (
    mkdir "%OUT_DIR%"
)

rem Check if directory is empty
dir /b "%OUT_DIR%" >nul 2>&1
if %errorlevel% neq 0 (
    echo Compiling Java files into %OUT_DIR%...
    for /r %%f in (*.java) do (
        if not defined JAVA_FILES (
            set "JAVA_FILES=%%f"
        ) else (
            set "JAVA_FILES=!JAVA_FILES! %%f"
        )
    )
    if not defined JAVA_FILES (
        echo No Java files found to compile.
        exit /b 1
    )
    javac -d "%OUT_DIR%" -cp "libs/*" !JAVA_FILES!
    echo Compilation complete.
) else (
    echo Using existing compiled classes in %OUT_DIR%.
)

echo.

if "%TEST_FLAG%"=="true" (
    echo Running Tests
    java -cp "%OUT_DIR%;libs/*" Impl.Program true
) else (
    echo Running Program
    java -cp "%OUT_DIR%;libs/*" Impl.Program
)

echo.
endlocal
