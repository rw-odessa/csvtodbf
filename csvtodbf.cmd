@ECHO OFF
setlocal

REM ��������� 䠩��� csv � dbf �ଠ�.
REM V 1.00 - ��ࢠ� �ᯫ��樮���� �����.
REM ��� ᮧ����� 11:44 15.10.2014

REM==================================================
REM ��⠭���� ��६�����
SET RUNDIR=%~dp0
SET FILES_PATH=%RUNDIR%

ECHO .
ECHO ==================================================
ECHO %date% %time% - START CONVERSION FILES

REM==================================================
REM ���� 䠩��� �믮������
for /f %%a IN ('dir /o:n "%FILES_PATH%*.csv" /b') do (
REM �����⨬ ���������
ECHO java.exe -jar %RUNDIR%csvtodbf.jar %FILES_PATH%%%a %RUNDIR%OP4.ini
"C:\Program Files\Java\jdk1.7.0_21\bin\java.exe" -jar "%RUNDIR%csvtodbf.jar" "%FILES_PATH%%%a" "%RUNDIR%OP.ini"|| (
ECHO %date% %time% - ERROR CONVERSION FILE %FILES_PATH%%%a
)
ECHO %date% %time% - END CONVERSION FILES
ECHO ==================================================
)

EXIT /B 0