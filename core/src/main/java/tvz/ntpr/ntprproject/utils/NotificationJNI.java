package tvz.ntpr.ntprproject.utils;

public class NotificationJNI {
    static {
        String dllPath = NotificationJNI.class.getClassLoader().getResource("other/native.dll").getPath();
        System.load(dllPath);
    }

    public static native void showNotification(String message);
    public static native void playSystemSound();
}
