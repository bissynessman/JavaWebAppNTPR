pyinstaller.exe --onefile --noconsole ^
--add-binary "..\bin\protocol_handler embedded dlls\download_mng.dll;." ^
--add-binary "..\bin\protocol_handler embedded dlls\libcurl.dll;." ^
--workpath "..\build artifacts\PyInstaller" ^
--distpath "..\bin" ^
"..\source\protocol_handler.py"