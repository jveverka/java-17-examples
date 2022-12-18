package one.microproject.filescompare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        LOGGER.info("Starting file compare");
        Path srcPath = Path.of(args[0]).normalize();
        Path dstPath = Path.of(args[1]).normalize();
        if (srcPath.equals(dstPath)) {
            LOGGER.error("ERROR: source and destination directory must be different directories !");
            return;
        }
        LOGGER.info("SRC: {}", srcPath);
        LOGGER.info("DST: {}", dstPath);
        LOGGER.info("Compare directories: {} {}", srcPath.toString(), dstPath.toString());

        File src = srcPath.toFile();
        File dst = dstPath.toFile();

        if (!src.isDirectory()) {
            LOGGER.error("ERROR: source must be directory !");
            return;
        }

        if (!dst.isDirectory()) {
            LOGGER.error("ERROR: destination must be directory !");
            return;
        }

        CompareDirContext context = new CompareDirContext();
        context.addPaths(src.listFiles());
        String baseSrcPath = srcPath.toString();
        while (context.hasPaths()) {
            Path currentPath = context.getNext();
            Path comparePath = FileUtils.getComparePath(baseSrcPath, dstPath, currentPath);
            File currentFile = currentPath.toFile();
            File compareFile = comparePath.toFile();
            LOGGER.info("PATH: {} {}", currentPath, comparePath);
            if (currentFile.isDirectory()) {
                context.addDirectory();
                context.addPaths(currentFile.listFiles());
                if (!compareFile.isDirectory()) {
                    context.logError("DST: Directory NOT found: " + comparePath);
                }
            } else if (currentFile.isFile()) {
                String path = currentFile.getPath().toString();
                String extension = path.substring(path.lastIndexOf(".") + 1);
                context.addFile(currentFile.length(), extension);
                if (!compareFile.isFile()) {
                    context.logError("DST: File NOT found: " + comparePath);
                } else {
                    String currentChecksum = FileUtils.getFileChecksum(currentPath);
                    String compareChecksum = FileUtils.getFileChecksum(comparePath);
                    if (!currentChecksum.equals(compareChecksum)) {
                        context.logError("Files differ: " + currentPath + " " + comparePath);
                    }
                    Long currentFileSize = currentFile.length();
                    Long compareFileSize = compareFile.length();
                    if (!currentFileSize.equals(compareFileSize)) {
                        context.logError("File sizes differ: " + currentPath + " " + comparePath);
                    }
                }
            } else {
                context.addOther();
                if (!compareFile.exists()) {
                    context.logError("DST: NOT found: " + comparePath);
                }
            }
        }
        context.close();
        Float duration = context.getDurationSec();
        Float bytesPerSec = context.getDataBytes() / duration;

        LOGGER.info("#**************************************************");
        LOGGER.info("File extensions: {}", context.getExtensions().size());
        long cumulativeCounter = 0;
        for (String key: context.getExtensions().keySet()) {
            if (key.length() <= 4) {
                LOGGER.info(" {}: {}", key, context.getExtensions().get(key));
            } else {
                cumulativeCounter = cumulativeCounter + context.getExtensions().get(key);
            }
        }
        LOGGER.info(" others: {}", cumulativeCounter);

        LOGGER.info("#**************************************************");
        LOGGER.info("# SRC: {}", srcPath);
        LOGGER.info("# DST: {}", dstPath);
        LOGGER.info("# Dirs    scanned: {}", context.getDirectoryCounter());
        LOGGER.info("# Files   scanned: {}", context.getFileCounter());
        LOGGER.info("# Others  scanned: {}", context.getOtherCounter());
        LOGGER.info("# Bytes   scanned: {} Bytes", context.getDataBytes());
        LOGGER.info("# Bytes   scanned: {}", FileUtils.getHumanReadableSize(context.getDataBytes() / 1f));
        LOGGER.info("# Scan speed     : {}/s", FileUtils.getHumanReadableSize(bytesPerSec));
        LOGGER.info("# Duration       : {} s", duration);
        LOGGER.info("# Duration       : {}", FileUtils.getDuration(duration));
        LOGGER.info("#**************************************************");

        if (context.hasErrors()) {
            LOGGER.error("ERRORS found: {}", context.getErrors().size());
            for (String error : context.getErrors()) {
                LOGGER.error("ERROR: {}", error);
            }
        } else {
            LOGGER.info("ALL OK: DST directory contains all files in SRC directory !");
        }

    }

}
