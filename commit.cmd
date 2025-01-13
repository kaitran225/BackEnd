@echo off
setlocal enabledelayedexpansion

REM Get the current directory path
set "currentDir=%CD%"
if exist "%currentDir%\.git" (
    cd .auto 
    powershell.exe -ExecutionPolicy Bypass -File "auto-commit.ps1"
) else (
    echo .git folder not found in the current directory.
)