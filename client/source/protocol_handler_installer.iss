[Setup]
AppName=NTPR Protocol Handler
AppVersion=1.0
DefaultDirName={commonpf}\NtprProtocolHandler
DefaultGroupName=NtprProtocolHandler
OutputBaseFilename=Ntpr-Protocol-Handler Installer
Compression=lzma
SolidCompression=yes

[Files]
Source: "..\bin\protocol_handler.exe"; DestDir: "{app}"; Flags: ignoreversion

[Registry]
Root: HKCR; Subkey: "ntpr"; ValueType: string; ValueName: ""; ValueData: "URL:NTPR Protocol"; Flags: uninsdeletekey
Root: HKCR; Subkey: "ntpr"; ValueType: string; ValueName: "URL Protocol"; ValueData: ""; Flags: uninsdeletekey
Root: HKCR; Subkey: "ntpr\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\protocol_handler.exe"" ""%1"""; Flags: uninsdeletekey

[UninstallDelete]
Type: files; Name: "{app}\protocol_handler.exe"
