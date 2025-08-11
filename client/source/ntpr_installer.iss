[Setup]
AppName=NTPR Installer
AppVersion=1.0
DefaultDirName={commonpf}\NTPR
DisableDirPage=no
DefaultGroupName=NTPR
OutputBaseFilename=NTPR Installer
Compression=lzma
SolidCompression=yes

[Files]
Source: "..\bin\ntpr_handler.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\bin\cert.pem"; DestDir: "{app}"; Flags: ignoreversion

[Registry]
Root: HKCR; Subkey: "ntpr"; ValueType: string; ValueName: ""; ValueData: "URL:NTPR Protocol"; Flags: uninsdeletekey
Root: HKCR; Subkey: "ntpr"; ValueType: string; ValueName: "URL Protocol"; ValueData: ""; Flags: uninsdeletekey
Root: HKCR; Subkey: "ntpr\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\ntpr_handler.exe"" ""%1"""; Flags: uninsdeletekey

[UninstallDelete]
Type: files; Name: "{app}\ntpr_handler.exe"
