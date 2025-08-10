import sys
import os
import urllib.parse
import ctypes
import tkinter as tk
from tkinter import simpledialog

root = tk.Tk()
root.withdraw()

bandwidth_limit = simpledialog.askinteger(
    "Bandwidth Limit",
    "Enter bandwidth limit in KB/s (0 = unlimited):",
    minvalue=0
)

if bandwidth_limit:
    bandwidth_limit *= 1024

if getattr(sys, 'frozen', False):
    base_path = sys._MEIPASS
else:
    base_path = os.path.dirname(__file__)

dll_path = os.path.join(base_path, "download_mng.dll")    
download_manager = ctypes.CDLL(dll_path)

download_manager.download_file.argtypes = [ctypes.c_char_p, ctypes.c_long]
download_manager.download_file.restype = ctypes.c_int
download_manager.show_notification.argtypes = [ctypes.c_char_p]
download_manager.show_notification.restype = None

def parse_ntpr_url(ntpr_url):
    parsed = urllib.parse.urlparse(ntpr_url)
    query = urllib.parse.parse_qs(parsed.query)

    url = query.get('url', [None])[0]

    if url is None:
        raise ValueError("Missing 'url' parameter")
    
    return url

def main():
    if len(sys.argv) < 2:
        download_manager.show_notification("Invalid usage!\nUsage: ntpr://download?url=...".encode('utf-8'))
        sys.exit(1)

    ntpr_url = sys.argv[1]
    try:
        url = parse_ntpr_url(ntpr_url)
    except Exception as e:
        download_manager.show_notification(f"Error parsing parameters: {e}".encode('utf-8'))
        sys.exit(1)

    result = download_manager.download_file(url.encode('utf-8'), bandwidth_limit)

    if result == 0:
        download_manager.show_notification("Download successful!".encode('utf-8'))
    else:
        download_manager.show_notification("Download failed!".encode('utf-8'))

if __name__ == "__main__":
    main()
