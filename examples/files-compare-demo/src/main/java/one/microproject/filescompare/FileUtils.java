package one.microproject.filescompare;

import java.io.File;
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

    public static String getHumanReadableSize(Long size) {
        if (size <= 1024) {
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

}
