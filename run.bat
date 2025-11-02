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
