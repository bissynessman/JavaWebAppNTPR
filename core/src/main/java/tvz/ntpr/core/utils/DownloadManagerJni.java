package tvz.ntpr.core.utils;

public class DownloadManagerJni {
    static {
        String dllPath = DownloadManagerJni.class.getClassLoader().getResource("other/download_mng.dll").getPath();
        System.load(dllPath);
    }

    public static native void downloadFile(String downloadUrl);
}
