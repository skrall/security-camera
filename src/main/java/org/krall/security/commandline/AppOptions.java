package org.krall.security.commandline;

import ch.qos.logback.classic.Level;
import org.kohsuke.args4j.Option;

import java.io.File;

public class AppOptions {

    private static AppOptions appOptions;

    @Option(name = "--width", aliases = {"-w"}, depends = "--height", usage = "Width of the image captured")
    int imageWidth = 800;

    @Option(name = "--height", aliases = {"-h"}, depends = "--width", usage = "Height of the image captured")
    int imageHeight = 600;

    @Option(name = "--directory", aliases = {"-d"}, usage = "Directory to save images")
    File imageDirectory = new File("/home/pi/images");

    @Option(name = "--verticalRegions", aliases = {"-vr"}, usage = "Number of vertical Regions")
    int verticalRegions = 8;

    @Option(name = "--horizontalRegions", aliases = {"-hr"}, usage = "Number of horizontal Regions")
    int horizontalRegions = 6;

    @Option(name = "--sensitivity", aliases = {"-s"}, usage = "Sensitivity level for motion detection")
    int sensitivity = 5;

    @Option(name = "--stabilizer", aliases = {"-st"}, usage = "Stabilizer level for motion detection")
    int stabilizer = 10;

    @Option(name = "--loglevel", aliases = {"-l"}, usage = "Log level", handler = LogLevelHandler.class)
    Level logLevel = Level.INFO;

    @Option(name = "--useAwt", aliases = {"-a"}, usage = "Use AWT instead of JavaFX to process image")
    boolean useAwt = false;

    static {
        appOptions = new AppOptions();
    }

    private AppOptions() {
    }

    public static AppOptions getInstance() {
        return appOptions;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public int getVerticalRegions() {
        return verticalRegions;
    }

    public int getHorizontalRegions() {
        return horizontalRegions;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public int getStabilizer() {
        return stabilizer;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isUseAwt() {
        return useAwt;
    }
}
