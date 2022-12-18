package one.microproject.filescompare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class CompareDirContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompareDirContext.class);

    private long fileCounter;
    private long directoryCounter;
    private long otherCounter;
    private long dataBytes;
    private Map<String, Integer> extensions;
    private Queue<Path> paths;
    private long startTimeStamp;
    private long durationMs;

    private List<String> errors;

    public CompareDirContext() {
        this.fileCounter = 0;
        this.directoryCounter = 0;
        this.dataBytes = 0;
        this.otherCounter = 0;
        this.extensions = new HashMap<>();
        this.paths = new LinkedList<>();
        this.errors = new ArrayList<>();
        this.startTimeStamp = System.currentTimeMillis();
        this.durationMs = 0;
    }

    public void addPaths(File[] files) {
        LOGGER.info("ADD: {} files", files.length);
        for (int i=0; i<files.length; i++) {
            this.paths.add(files[i].toPath());
        }
    }

    public void addDirectory() {
        this.directoryCounter = this.directoryCounter + 1;
    }

    public void addOther() {
        this.otherCounter = this.otherCounter + 1;
    }

    public void addFile(long size, String extension) {
        this.fileCounter = this.fileCounter + 1;
        this.dataBytes = this.dataBytes + size;
        if (extensions.containsKey(extension)) {
            Integer counter = extensions.get(extension);
            extensions.put(extension, Integer.valueOf(counter + 1));
        } else {
            extensions.put(extension, Integer.valueOf(1));
        }
    }

    public boolean hasPaths() {
        return !this.paths.isEmpty();
    }

    public Path getNext() {
        return this.paths.remove();
    }

    public long getFileCounter() {
        return fileCounter;
    }

    public long getDirectoryCounter() {
        return directoryCounter;
    }

    public long getOtherCounter() {
        return otherCounter;
    }

    public long getDataBytes() {
        return dataBytes;
    }

    public Map<String, Integer> getExtensions() {
        return extensions;
    }

    public void logError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void close() {
        this.durationMs = System.currentTimeMillis() - this.startTimeStamp;
    }

    public float getDurationSec() {
        return this.durationMs / 1000f;
    }

}
