#include <windows.h>
#include <jni.h>
#include <fstream>

HMODULE g_hMod;

extern "C" __declspec(dllexport) void ShowNotification(const char* message) {
    HWND hwnd = GetForegroundWindow();

    MessageBoxA(hwnd, message, "Notification", MB_OK | MB_ICONINFORMATION);
}

extern "C" __declspec(dllexport) void PlaySystemSound() {
    MessageBeep(MB_OK);
}

extern "C" __declspec(dllexport) void JNICALL Java_tvz_ntpr_core_utils_NotificationJNI_showNotification(
        JNIEnv* env, jobject obj, jstring message) {
    const char* nativeMessage = env->GetStringUTFChars(message, 0);
    ShowNotification(nativeMessage);
    env->ReleaseStringUTFChars(message, nativeMessage);
}

extern "C" __declspec(dllexport) void JNICALL Java_tvz_ntpr_core_utils_NotificationJNI_playSystemSound(
        JNIEnv* env, jobject obj) {
    PlaySystemSound();
}

BOOL APIENTRY DllMain(HMODULE hModule, DWORD ul_reason_for_call, LPVOID lpReserved) {
    switch (ul_reason_for_call) {
        case DLL_PROCESS_ATTACH:
        case DLL_THREAD_ATTACH:
        case DLL_THREAD_DETACH:
        case DLL_PROCESS_DETACH:
            break;
    }
    return TRUE;
}