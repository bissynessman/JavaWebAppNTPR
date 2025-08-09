#define WINVER 0x0602
#define _WIN32_WINNT 0x0602
#include <windows.h>
#include <shlobj.h>
#include <jni.h>
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
long gSpeedLimit = 0;
std::chrono::steady_clock::time_point gStartTime;
std::string gFilename;

std::wstring utf8_to_wstring(const std::string& str) {
	if (str.empty()) return std::wstring();
    int wstring_size = MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), NULL, 0);
    std::wstring wStr(wstring_size, 0);
    MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), &wStr[0], wstring_size);
    return wStr;
}

std::string getDownloadsFolder() {
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

// Write callback
size_t write_data(void* ptr, size_t size, size_t nmemb, void* stream) {
    std::ofstream* out = static_cast<std::ofstream*>(stream);
    out->write(static_cast<char*>(ptr), size * nmemb);
    return size * nmemb;
}

std::string formatSpeed(double bytesPerSec) {
    std::ostringstream oss;
    if (bytesPerSec > 1024 * 1024) {
        oss << std::fixed << std::setprecision(2) << (bytesPerSec / (1024 * 1024)) << " MB/s";
    } else {
        oss << std::fixed << std::setprecision(1) << (bytesPerSec / 1024) << " KB/s";
    }
    return oss.str();
}

std::string formatEta(double seconds) {
    int sec = (int)seconds;
    int mins = sec / 60;
    sec = sec % 60;
    std::ostringstream oss;
    oss << mins << "m " << sec << "s";
    return oss.str();
}

// Progress callback
int progress_callback(void* clientp, curl_off_t dltotal, curl_off_t dlnow, curl_off_t, curl_off_t) {
    if (dltotal > 0) {
        int percent = static_cast<int>((dlnow * 100) / dltotal);
        SendMessage(hProgressBar, PBM_SETPOS, percent, 0);

        auto now = std::chrono::steady_clock::now();
        double elapsedSec = std::chrono::duration<double>(now - gStartTime).count();
        double speed = (elapsedSec > 0) ? (double)dlnow / elapsedSec : 0;
        double remainingSec = (speed > 0) ? ((double)dltotal - dlnow) / speed : 0;

        std::string stats = formatSpeed(speed) + " - ETA: " + formatEta(remainingSec);
        SetWindowTextW(hLabelStats, utf8_to_wstring(stats).c_str());
    }
    return 0;
}

// Window procedure
LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
    switch (msg) {
        case WM_DESTROY:
            PostQuitMessage(0);
            return 0;
    }
    return DefWindowProc(hwnd, msg, wParam, lParam);
}

void download(std::string url) {
    std::string downloadsPath = getDownloadsFolder();
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

        if (gSpeedLimit > 0) {
            curl_easy_setopt(curl, CURLOPT_MAX_RECV_SPEED_LARGE, (curl_off_t)gSpeedLimit);
        }

        curl_easy_perform(curl);
        curl_easy_cleanup(curl);
    }

    PostMessage(hWnd, WM_CLOSE, 0, 0);
}

extern "C" JNIEXPORT void JNICALL Java_tvz_ntpr_core_utils_DownloadManagerJni_downloadFile(JNIEnv* env, jobject, jstring jUrl, jlong speedLimitBytesPerSec) {
    const char* cUrl = env->GetStringUTFChars(jUrl, nullptr);
    std::string url = cUrl;

    gSpeedLimit = (long) speedLimitBytesPerSec;

    size_t pos = url.find_last_of('/');
    gFilename = (pos != std::string::npos) ? url.substr(pos + 1) : "downloaded_file";

    // Register window class
    const char CLASS_NAME[] = "ProgressWindow";
    WNDCLASSW wc = {};
    wc.lpfnWndProc = WndProc;
    wc.hInstance = GetModuleHandle(NULL);
    wc.lpszClassName = utf8_to_wstring(CLASS_NAME).c_str();
    RegisterClassW(&wc);

    // Create window
    hWnd = CreateWindowW(utf8_to_wstring(CLASS_NAME).c_str(), utf8_to_wstring("Downloading File").c_str(),
                        WS_OVERLAPPEDWINDOW & ~WS_MAXIMIZEBOX & ~WS_SIZEBOX,
                        CW_USEDEFAULT, CW_USEDEFAULT, 320, 150,
                        NULL, NULL, GetModuleHandle(NULL), NULL);

    InitCommonControls();

    // File label
    hLabelFile = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring(gFilename).c_str(),
                              WS_CHILD | WS_VISIBLE,
                              10, 10, 280, 20,
                              hWnd, NULL, GetModuleHandle(NULL), NULL);

    // Progress bar
    hProgressBar = CreateWindowEx(0, PROGRESS_CLASS, NULL,
                                  WS_CHILD | WS_VISIBLE,
                                  10, 40, 280, 20,
                                  hWnd, NULL, GetModuleHandle(NULL), NULL);
    SendMessage(hProgressBar, PBM_SETRANGE, 0, MAKELPARAM(0, 100));

    // Stats label
    hLabelStats = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring("0 KB/s - ETA: 0m 0s").c_str(),
                               WS_CHILD | WS_VISIBLE,
                               10, 70, 280, 20,
                               hWnd, NULL, GetModuleHandle(NULL), NULL);

    ShowWindow(hWnd, SW_SHOW);

    std::thread dlThread(download, url);
    dlThread.detach();

    MSG message;
    while (GetMessage(&message, NULL, 0, 0) > 0) {
        TranslateMessage(&message);
        DispatchMessage(&message);
    }

    env->ReleaseStringUTFChars(jUrl, cUrl);
}
