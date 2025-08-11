import sys
import os
import urllib.parse
import ctypes
import tkinter as tk
from tkinter import simpledialog, filedialog, messagebox

ntpr = None
root = tk.Tk()
root.withdraw()

def load_ntpr_library():
	if getattr(sys, 'frozen', False):
		base_path = sys._MEIPASS
	else:
		base_path = os.path.dirname(__file__)

	dll_path = os.path.join(base_path, "ntpr_library.dll")	
	return ctypes.CDLL(dll_path)

def get_bandwidth_limit():
	bandwidth_limit = simpledialog.askinteger(
		"Bandwidth Limit",
		"Enter bandwidth limit in KB/s (0 = unlimited):",
		minvalue=0
	)
	if bandwidth_limit:
		return bandwidth_limit * 1024
	return 0

def get_file_paths():
	data_path = filedialog.askopenfilename(
		title="Select PDF to verify",
		filetypes=[("PDF Files", "*.pdf")]
	)

	sig_path = filedialog.askopenfilename(
		title="Select signarture file to verify against",
		filetypes=[("P7S Files", "*.p7s")]
	)

	if getattr(sys, 'frozen', False):
		base_path = os.path.dirname(sys.executable)
	else:
		base_path = os.path.dirname(os.path.abspath(__file__))

	cer_path = os.path.join(base_path, "cert.pem")

	if not data_path or not sig_path:
		messagebox.showinfo("Verification", "No file(s) selected.")
		return None, None, None

	return (
		os.path.normpath(data_path),
		os.path.normpath(sig_path),
		os.path.normpath(cer_path)
	)

def parse_ntpr_url(ntpr_url):
	parsed = urllib.parse.urlparse(ntpr_url)
	query = urllib.parse.parse_qs(parsed.query)

	url = query.get('url', [None])[0]

	if url is None:
		raise ValueError("Missing 'url' parameter")

	return url

def download_file(ntpr_url, bandwidth_limit):
	global ntpr

	ntpr.download_file.argtypes = [ctypes.c_char_p, ctypes.c_long]
	ntpr.download_file.restype = ctypes.c_int

	try:
		url = parse_ntpr_url(ntpr_url)
	except Exception as e:
		ntpr.show_notification(f"Error parsing parameters: {e}".encode('utf-8'))
		return 1

	result = ntpr.download_file(url.encode('utf-8'), bandwidth_limit)

	if result == 0:
		ntpr.show_notification("Download successful!".encode('utf-8'))
	else:
		ntpr.show_notification("Download failed!".encode('utf-8'))

	return result

def verify_digital_signature(file_path, sig_path, cer_path):
	global ntpr

	if file_path == None or sig_path == None or cer_path == None:
		return 2

	ntpr.verify_signature.argtypes = [ctypes.c_char_p, ctypes.c_char_p, ctypes.c_char_p]
	ntpr.verify_signature.restype = ctypes.c_int

	result = ntpr.verify_signature(file_path.encode('utf-8'), sig_path.encode('utf-8'), cer_path.encode('utf-8'))

	if result == 0:
		ntpr.show_notification("Signature valid!".encode('utf-8'))
	elif result == 1:
		ntpr.show_notification("Signature invalid!".encode('utf-8'))
	else:
		ntpr.show_notification(f"Error validating signature! (Error code: {result})".encode('utf-8'))

	return result

def main():
	global ntpr
	ntpr = load_ntpr_library()
	ntpr.show_notification.argtypes = [ctypes.c_char_p]
	ntpr.show_notification.restype = None

	if len(sys.argv) == 2:
		ntpr_url, bandwidth_limit = sys.argv[1], get_bandwidth_limit()
		exit_code = download_file(ntpr_url, bandwidth_limit)
		sys.exit(exit_code)
	elif len(sys.argv) == 1:
		data_path, sig_path, cer_path = get_file_paths()
		exit_code = verify_digital_signature(data_path, sig_path, cer_path)
		sys.exit(exit_code)

	ntpr.show_notification("Invalid usage!\nUsage: ntpr://download?url=...".encode('utf-8'))
	sys.exit(1)

if __name__ == "__main__":
	main()
