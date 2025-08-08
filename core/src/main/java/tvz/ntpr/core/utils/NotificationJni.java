package tvz.ntpr.core.utils;

public class NotificationJni {
    static {
        String dllPath = NotificationJni.class.getClassLoader().getResource("other/native.dll").getPath();
        System.load(dllPath);
    }

    public static native void showNotification(String message);
    public static native void playSystemSound();
}
