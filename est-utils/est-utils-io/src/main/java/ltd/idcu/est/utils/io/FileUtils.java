package ltd.idcu.est.utils.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.List;

public final class FileUtils {

    private FileUtils() {
    }

    public static File getFile(String... names) {
        return FilePathUtils.getFile(names);
    }

    public static File getFile(File directory, String... names) {
        return FilePathUtils.getFile(directory, names);
    }

    public static String getTempDirectoryPath() {
        return FilePathUtils.getTempDirectoryPath();
    }

    public static File getTempDirectory() {
        return FilePathUtils.getTempDirectory();
    }

    public static String getUserDirectoryPath() {
        return FilePathUtils.getUserDirectoryPath();
    }

    public static File getUserDirectory() {
        return FilePathUtils.getUserDirectory();
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        return FileReadWriteUtils.openInputStream(file);
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return FileReadWriteUtils.openOutputStream(file);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        return FileReadWriteUtils.openOutputStream(file, append);
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        return FileReadWriteUtils.readFileToByteArray(file);
    }

    public static String readFileToString(File file) throws IOException {
        return FileReadWriteUtils.readFileToString(file);
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        return FileReadWriteUtils.readFileToString(file, charset);
    }

    public static List<String> readLines(File file) throws IOException {
        return FileReadWriteUtils.readLines(file);
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return FileReadWriteUtils.readLines(file, charset);
    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        FileReadWriteUtils.writeByteArrayToFile(file, data);
    }

    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        FileReadWriteUtils.writeByteArrayToFile(file, data, append);
    }

    public static void writeStringToFile(File file, String data) throws IOException {
        FileReadWriteUtils.writeStringToFile(file, data);
    }

    public static void writeStringToFile(File file, String data, Charset charset) throws IOException {
        FileReadWriteUtils.writeStringToFile(file, data, charset);
    }

    public static void writeStringToFile(File file, String data, Charset charset, boolean append) throws IOException {
        FileReadWriteUtils.writeStringToFile(file, data, charset, append);
    }

    public static void writeLines(File file, Collection<?> lines) throws IOException {
        FileReadWriteUtils.writeLines(file, lines);
    }

    public static void writeLines(File file, String lineEnding, Collection<?> lines) throws IOException {
        FileReadWriteUtils.writeLines(file, lineEnding, lines);
    }

    public static void writeLines(File file, String lineEnding, Collection<?> lines, boolean append) throws IOException {
        FileReadWriteUtils.writeLines(file, lineEnding, lines, append);
    }

    public static void write(File file, CharSequence data) throws IOException {
        FileReadWriteUtils.write(file, data);
    }

    public static void write(File file, CharSequence data, Charset charset) throws IOException {
        FileReadWriteUtils.write(file, data, charset);
    }

    public static void write(File file, CharSequence data, Charset charset, boolean append) throws IOException {
        FileReadWriteUtils.write(file, data, charset, append);
    }

    public static void forceMkdir(File directory) throws IOException {
        FileDirectoryUtils.forceMkdir(directory);
    }

    public static void forceMkdirParent(File file) throws IOException {
        FileDirectoryUtils.forceMkdirParent(file);
    }

    public static boolean deleteQuietly(File file) {
        return FileDirectoryUtils.deleteQuietly(file);
    }

    public static void forceDelete(File file) throws IOException {
        FileDirectoryUtils.forceDelete(file);
    }

    public static void deleteDirectory(File directory) throws IOException {
        FileDirectoryUtils.deleteDirectory(directory);
    }

    public static void cleanDirectory(File directory) throws IOException {
        FileDirectoryUtils.cleanDirectory(directory);
    }

    public static boolean isSymlink(File file) {
        return FileDirectoryUtils.isSymlink(file);
    }

    public static boolean isDirectory(File file) {
        return FileDirectoryUtils.isDirectory(file);
    }

    public static boolean isFile(File file) {
        return FileDirectoryUtils.isFile(file);
    }

    public static boolean exists(File file) {
        return FileDirectoryUtils.exists(file);
    }

    public static boolean isReadable(File file) {
        return FileAttributeUtils.isReadable(file);
    }

    public static boolean isWritable(File file) {
        return FileAttributeUtils.isWritable(file);
    }

    public static boolean isHidden(File file) {
        return FileAttributeUtils.isHidden(file);
    }

    public static long sizeOf(File file) {
        return FileAttributeUtils.sizeOf(file);
    }

    public static long sizeOfDirectory(File directory) {
        return FileAttributeUtils.sizeOfDirectory(directory);
    }

    public static int countFiles(File directory) {
        return FileAttributeUtils.countFiles(directory);
    }

    public static int countDirectories(File directory) {
        return FileAttributeUtils.countDirectories(directory);
    }

    public static String byteCountToDisplaySize(long size) {
        return FileAttributeUtils.byteCountToDisplaySize(size);
    }

    public static String getExtension(String filename) {
        return FilePathUtils.getExtension(filename);
    }

    public static String getExtension(File file) {
        return FilePathUtils.getExtension(file);
    }

    public static String removeExtension(String filename) {
        return FilePathUtils.removeExtension(filename);
    }

    public static String removeExtension(File file) {
        return FilePathUtils.removeExtension(file);
    }

    public static String getBaseName(String filename) {
        return FilePathUtils.getBaseName(filename);
    }

    public static String getBaseName(File file) {
        return FilePathUtils.getBaseName(file);
    }

    public static String getName(String filename) {
        return FilePathUtils.getName(filename);
    }

    public static String getName(File file) {
        return FilePathUtils.getName(file);
    }

    public static File getParent(File file) {
        return FilePathUtils.getParent(file);
    }

    public static String getParentPath(File file) {
        return FilePathUtils.getParentPath(file);
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return FileTempUtils.createTempFile(prefix, suffix);
    }

    public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
        return FileTempUtils.createTempFile(prefix, suffix, directory);
    }

    public static File createTempDirectory() throws IOException {
        return FileTempUtils.createTempDirectory();
    }

    public static File createTempDirectory(String prefix) throws IOException {
        return FileTempUtils.createTempDirectory(prefix);
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        FileCopyMoveUtils.copyFile(srcFile, destFile);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        FileCopyMoveUtils.copyFile(srcFile, destFile, preserveFileDate);
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        FileCopyMoveUtils.copyFileToDirectory(srcFile, destDir);
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        FileCopyMoveUtils.copyFileToDirectory(srcFile, destDir, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        FileCopyMoveUtils.copyDirectory(srcDir, destDir);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        FileCopyMoveUtils.copyDirectory(srcDir, destDir, preserveFileDate);
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        FileCopyMoveUtils.moveFile(srcFile, destFile);
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        FileCopyMoveUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        FileCopyMoveUtils.moveDirectory(srcDir, destDir);
    }

    public static void touch(File file) throws IOException {
        FileAttributeUtils.touch(file);
    }

    public static FileTime getLastModifiedTime(File file) throws IOException {
        return FileAttributeUtils.getLastModifiedTime(file);
    }

    public static FileTime getCreationTime(File file) throws IOException {
        return FileAttributeUtils.getCreationTime(file);
    }

    public static FileTime getLastAccessTime(File file) throws IOException {
        return FileAttributeUtils.getLastAccessTime(file);
    }

    public static List<File> listFiles(File directory) {
        return FileDirectoryUtils.listFiles(directory);
    }

    public static List<File> listFiles(File directory, String[] extensions, boolean recursive) {
        return FileDirectoryUtils.listFiles(directory, extensions, recursive);
    }

    public static List<File> listDirectories(File directory) {
        return FileDirectoryUtils.listDirectories(directory);
    }

    public static String getCanonicalPath(File file) throws IOException {
        return FilePathUtils.getCanonicalPath(file);
    }

    public static String getAbsolutePath(File file) {
        return FilePathUtils.getAbsolutePath(file);
    }

    public static String getPath(File file) {
        return FilePathUtils.getPath(file);
    }

    public static void setReadOnly(File file) throws IOException {
        FileAttributeUtils.setReadOnly(file);
    }

    public static void setWritable(File file, boolean writable) throws IOException {
        FileAttributeUtils.setWritable(file, writable);
    }

    public static void setReadable(File file, boolean readable) throws IOException {
        FileAttributeUtils.setReadable(file, readable);
    }

    public static void setExecutable(File file, boolean executable) throws IOException {
        FileAttributeUtils.setExecutable(file, executable);
    }
}
