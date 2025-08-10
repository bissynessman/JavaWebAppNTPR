C:/msys64/ucrt64/bin/g++.exe -fdiagnostics-color=always ^
-I "C:\Program Files (x86)\vcpkg\installed\x64-windows\include" ^
-L "C:\Program Files (x86)\vcpkg\installed\x64-windows\lib" ^
-g "..\source\download_mng.cpp" ^
-o "..\bin\protocol_handler embedded dlls\download_mng.dll" ^
-lcurl -lole32 -lcomctl32 -lshell32 -luser32 -lgdi32 -ladvapi32 -lws2_32 -luuid ^
-shared