@echo off
ren D:\app\Mytools\Mytools.jar Mytools_"%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%".jar
copy   D:\01code\web\Mytools\out\artifacts\Mytools_jar\Mytools.jar D:\app\Mytools\  /y