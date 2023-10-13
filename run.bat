@echo off
ren H:\Program\Mytools\MeTools.jar Mytools_"%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%".jar
copy   G:\MWeb\MeTools\out\artifacts\MeTools_jar\MeTools.jar H:\Program\Mytools\  /y