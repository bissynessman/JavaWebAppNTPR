pyinstaller.exe --onefile --noconsole ^
--add-binary "..\bin\ntpr_handler embedded dlls\ntpr_library.dll;." ^
--add-binary "..\bin\ntpr_handler embedded dlls\libcurl.dll;." ^
--workpath "..\build artifacts\PyInstaller" ^
--distpath "..\bin" ^
"..\source\ntpr_handler.py"