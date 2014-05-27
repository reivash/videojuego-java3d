@echo off
Echo Launches NWM Viewer
Echo Make sure you have Java3D correctly installed.
Echo Show models available in Path.

java.exe -showversion -cp j3dfly.jar;j3dedit.jar;NWM.jar  net.sf.nwn.viewer.Main 

echo Thanks for using NWM model viewer !
pause