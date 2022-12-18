package one.microproject.filescompare;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getFileChecksum(Path file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file.toFile());

        byte[] byteArray = new byte[10240];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };
        fis.close();

        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer
                    .toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

    public static Path getComparePath(String baseSrcPath, Path dstPath, Path currentPath) {
        String delta = currentPath.toString().substring(baseSrcPath.length());
        return Path.of(dstPath.toString(), delta).normalize();
    }

    public static String getHumanReadableSize(Float size) {
        if (size <= 1024f) {
            return size + " B";
        } else {
            int counter = 0;
            float result = size;
            while (result > 1024f) {
                counter = counter + 1;
                result = result / 1024;
            }
            String unit = "??";
            if (counter == 1) {
                unit = "KB";
            }
            if (counter == 2) {
                unit = "MB";
            }
            if (counter == 3) {
                unit = "GB";
            }
            if (counter == 4) {
                unit = "TB";
            }
            if (counter == 5) {
                unit = "PB";
            }
            String formattedResult = String.format("%.03f", result);
            return formattedResult + " " + unit;
        }

    }

    public static String getDuration(float durationSec) {
        if (durationSec < 60) {
            String formattedResult = String.format("%.03f", durationSec);
            return formattedResult + " s";
        } else if (durationSec < 3600) {
            int minutes = (int)(durationSec / 60);
            float reminder = durationSec - (minutes * 60);
            String formattedReminder = String.format("%.03f", reminder);
            return minutes + ":" + formattedReminder;
        } else if (durationSec < 86400) {
            int hours = (int)(durationSec / 3600);
            int minutes = (int)(durationSec - (hours * 3600)) / 60;
            float reminder = durationSec - (hours * 3600) - (minutes * 60);
            String formattedReminder = String.format("%.03f", reminder);
            return hours + ":" + minutes + ":" + formattedReminder;
        } else {
            int days = (int)(durationSec / 86400);
            int hours = (int)(durationSec - (days * 86400)) / 3600;
            int minutes = (int)(durationSec - (days * 86400) - (hours * 3600)) / 60;
            float reminder = durationSec - (days * 86400) - (hours * 3600) - (minutes * 60);
            String formattedReminder = String.format("%.03f", reminder);
            return days + " days " + hours + ":" + minutes + ":" + formattedReminder;
        }
    }

}
