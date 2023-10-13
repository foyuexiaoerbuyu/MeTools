cd /
D:
md pic
SET str="%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%"
adb shell screencap -p /sdcard/%str%.png
adb pull /sdcard/%str%.png D:\pic
adb shell rm /sdcard/*.png
exit

