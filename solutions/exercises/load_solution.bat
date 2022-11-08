@echo off
:: delayed expansion is needed to enable the !variable! syntax. https://ss64.com/nt/delayedexpansion.html
setlocal EnableDelayedExpansion

set EXERCISE=%1

if [%EXERCISE%]==[] (
  echo This command will copy the provided solutions into your exercises directory.
  echo USAGE: load_solution.sh "search string"
  echo search string   A string that makes up all, or part, of the exercise name.
  echo Exercise numbers, e.g. 01 are the simplest form of search.
  echo:
  echo WARNING:          RUNNING THIS COMMAND WILL OVERWRITE YOUR CODE. MAKE SURE YOU ACTUALLY WANT TO DO THAT.
  echo:
  exit /B 0
)

set SUB_FOLDERS=movr\rides movr\vehicles movr\users movr\ui_gateway

set SOLUTION_FOLDER=..\solutions

for /f %%i in ('dir %SOLUTION_FOLDER%\* /b ^| findstr %EXERCISE%') do (
    set EXERCISE_FOLDER=%SOLUTION_FOLDER%\%%i
)

if [!EXERCISE_FOLDER!]==[] (
  echo Unable to find a solution for the requested exercise: %EXERCISE%
  echo USAGE: load_solution.bat search_string
  exit /B 0
)

for %%f in (%SUB_FOLDERS%) do (
    set SOLUTION=!EXERCISE_FOLDER!\%%f
    set EXERCISE=%%f

    echo Loading solution from !SOLUTION!
    echo                  to !EXERCISE!

    if not exist !SOLUTION!\ (
      echo WARNING: Unable to find solution for in the requested folder: !SOLUTION!... skipping
    )

    rmdir /s/q !EXERCISE!
    if not exist !EXERCISE! mkdir !EXERCISE!
    xcopy /s/q/i !SOLUTION! !EXERCISE! 1>NUL
)
