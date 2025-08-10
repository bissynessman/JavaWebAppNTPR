#define WINVER 0x0602
#define _WIN32_WINNT 0x0602
#include <windows.h>
#include <shlobj.h>
#include <commctrl.h>
#include <curl/curl.h>
#include <string>
#include <fstream>
#include <thread>
#include <chrono>
#include <sstream>
#include <iomanip>

HWND hProgressBar;
HWND hWnd;
HWND hLabelFile;
HWND hLabelStats;
long gBandwidthLimit = 0;
std::chrono::steady_clock::time_point gStartTime;
std::string gFilename;

std::string get_downloads_folder() {
    PWSTR pathTmp = NULL;
    std::string pathStr;
    if (SUCCEEDED(SHGetKnownFolderPath(FOLDERID_Downloads, 0, NULL, &pathTmp))) {
        char buffer[MAX_PATH];
        wcstombs(buffer, pathTmp, MAX_PATH);
        pathStr = buffer;
        CoTaskMemFree(pathTmp);
    }
    return pathStr;
}

std::string format_bandwidth(double Bps) {
    std::ostringstream oss;
    if (Bps > 1024 * 1024) {
        oss << std::fixed << std::setprecision(2) << (Bps / (1024 * 1024)) << " MB/s";
    } else {
        oss << std::fixed << std::setprecision(1) << (Bps / 1024) << " KB/s";
    }
    return oss.str();
}

std::string format_eta(double seconds) {
    int sec = (int)seconds;
    int mins = sec / 60;
    sec = sec % 60;
    std::ostringstream oss;
    oss << mins << "m " << sec << "s";
    return oss.str();
}

std::wstring utf8_to_wstring(const std::string& str) {
	if (str.empty()) return std::wstring();
    int wstring_size = MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), NULL, 0);
    std::wstring wStr(wstring_size, 0);
    MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), &wStr[0], wstring_size);
    return wStr;
}

// Write callback
size_t write_data(void* ptr, size_t size, size_t nmemb, void* stream) {
    std::ofstream* out = static_cast<std::ofstream*>(stream);
    out->write(static_cast<char*>(ptr), size * nmemb);
    return size * nmemb;
}

// Progress callback
int progress_callback(void* clientp, curl_off_t dltotal, curl_off_t dlnow, curl_off_t, curl_off_t) {
    if (dltotal > 0) {
        int percent = static_cast<int>((dlnow * 100) / dltotal);
        SendMessage(hProgressBar, PBM_SETPOS, percent, 0);

        auto now = std::chrono::steady_clock::now();
        double elapsedSec = std::chrono::duration<double>(now - gStartTime).count();
        double speed = (elapsedSec > 0) ? (double) dlnow / elapsedSec : 0;
        double remainingSec = (speed > 0) ? ((double) dltotal - dlnow) / speed : 0;

        std::string stats = format_bandwidth(speed) + " - ETA: " + format_eta(remainingSec);
        SetWindowTextW(hLabelStats, utf8_to_wstring(stats).c_str());
    }
    return 0;
}

LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_DESTROY:
            PostQuitMessage(0);
            return 0;
    }
    return DefWindowProc(hwnd, msg, wParam, lParam);
}

void make_window() {
	// Class registration
	const char CLASS_NAME[] = "ProgressWindow";
    WNDCLASSW wc = {};
    wc.lpfnWndProc = WndProc;
    wc.hInstance = GetModuleHandle(NULL);
    wc.lpszClassName = utf8_to_wstring(CLASS_NAME).c_str();
    RegisterClassW(&wc);

	// Window creation
    hWnd = CreateWindowW(utf8_to_wstring(CLASS_NAME).c_str(), utf8_to_wstring("Downloading File").c_str(),
                        WS_OVERLAPPEDWINDOW & ~WS_MAXIMIZEBOX & ~WS_SIZEBOX,
                        CW_USEDEFAULT, CW_USEDEFAULT, 320, 150,
                        NULL, NULL, GetModuleHandle(NULL), NULL);

    InitCommonControls();

    hLabelFile = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring(gFilename).c_str(),
                              WS_CHILD | WS_VISIBLE,
                              10, 10, 280, 20,
                              hWnd, NULL, GetModuleHandle(NULL), NULL);

    hProgressBar = CreateWindowEx(0, PROGRESS_CLASS, NULL,
                                  WS_CHILD | WS_VISIBLE,
                                  10, 40, 280, 20,
                                  hWnd, NULL, GetModuleHandle(NULL), NULL);
	
    SendMessage(hProgressBar, PBM_SETRANGE, 0, MAKELPARAM(0, 100));

    hLabelStats = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring("0 KB/s - ETA: 0m 0s").c_str(),
                               WS_CHILD | WS_VISIBLE,
                               10, 70, 280, 20,
                               hWnd, NULL, GetModuleHandle(NULL), NULL);

    ShowWindow(hWnd, SW_SHOW);
}

// Download thread
void download(std::string url) {
    std::string downloadsPath = get_downloads_folder();
    std::string outputPath = downloadsPath + "\\" + gFilename;

    CURL* curl = curl_easy_init();
    if (curl) {
        std::ofstream outFile(outputPath, std::ios::binary);

        gStartTime = std::chrono::steady_clock::now();

        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &outFile);

        curl_easy_setopt(curl, CURLOPT_NOPROGRESS, 0L);
        curl_easy_setopt(curl, CURLOPT_XFERINFOFUNCTION, progress_callback);

        if (gBandwidthLimit > 0) curl_easy_setopt(curl, CURLOPT_MAX_RECV_SPEED_LARGE, (curl_off_t) gBandwidthLimit);

        curl_easy_perform(curl);
        curl_easy_cleanup(curl);
    }

    PostMessage(hWnd, WM_CLOSE, 0, 0);
}

void showNotification(const char* message) {
    HWND hWndForeground = GetForegroundWindow();

    MessageBoxA(hWndForeground, message, "Notification", MB_OK | MB_ICONINFORMATION);
}

int downloadFile(const char* url, long bandwidthLimitBytesPerSec) {
	try {
		std::string urlStr(url);
		gBandwidthLimit = bandwidthLimitBytesPerSec;

		size_t pos = urlStr.find_last_of('/');
		gFilename = (pos != std::string::npos) ? urlStr.substr(pos + 1) : "downloaded_file";

		make_window();

		std::thread dlThread(download, urlStr);
		dlThread.detach();

		MSG message;
		while (GetMessage(&message, NULL, 0, 0) > 0) {
			TranslateMessage(&message);
			DispatchMessage(&message);
		}

		return 0;
	} catch (...) {
		return 1;
	}
}

extern "C" __declspec(dllexport) void show_notification(const char* msg) {
	showNotification(msg);
}

extern "C" __declspec(dllexport) int download_file(const char* url, long bandwidthLimit) {
    return downloadFile(url, bandwidthLimit);
}