@ECHO OFF
setlocal

REM Конвертация файлов csv в dbf формат.
REM V 1.00 - Первая эксплуатационная версия.
REM Дата создания 11:44 15.10.2014

REM==================================================
REM Установка переменных
SET RUNDIR=%~dp0
SET FILES_PATH=%RUNDIR%

ECHO .
ECHO ==================================================
ECHO %date% %time% - START CONVERSION FILES

REM==================================================
REM Поиск файлов выполнения
for /f %%a IN ('dir /o:n "%FILES_PATH%*.csv" /b') do (
REM Запустим конвертацию
ECHO java.exe -jar %RUNDIR%csvtodbf.jar %FILES_PATH%%%a %RUNDIR%OP4.ini
"C:\Program Files\Java\jdk1.7.0_21\bin\java.exe" -jar "%RUNDIR%csvtodbf.jar" "%FILES_PATH%%%a" "%RUNDIR%OP.ini"|| (
ECHO %date% %time% - ERROR CONVERSION FILE %FILES_PATH%%%a
)
ECHO %date% %time% - END CONVERSION FILES
ECHO ==================================================
)

EXIT /B 0