package com.mf.log.xlog;

import com.elvishew.xlog.printer.file.backup.BackupStrategy2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackupUtil {

    /**
     * Shift existed backups if needed, and backup the logging file.
     *
     * @param loggingFile    the logging file
     * @param backupStrategy the strategy should be use when backing up
     */
    public static void backup(File loggingFile, BackupStrategy2 backupStrategy) {
        String loggingFileName = loggingFile.getName();
        String path = loggingFile.getParent();
        File backupFile;

        File dir = new File(path);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(loggingFileName);
            }
        });

        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, (o1, o2) -> (int) (o2.lastModified() - o1.lastModified()));

        int nextIndex = 1;
        if (fileList.size() > 1) {
            String name = fileList.get(1).getName();
            try {
                nextIndex = Integer.parseInt(name.substring(name.lastIndexOf('.') + 1)) + 1;
                if (nextIndex == Integer.MIN_VALUE) {
                    nextIndex = 1;
                }
            } catch (Exception e) {
                nextIndex = fileList.size();
            }
        }
        backupFile = new File(path, backupStrategy.getBackupFileName(loggingFileName, nextIndex));
        if (backupFile.exists()) {
            backupFile.delete();
        }
        loggingFile.renameTo(backupFile);
    }

    /**
     * Check if a {@link BackupStrategy2} is valid, will throw a exception if invalid.
     *
     * @param backupStrategy the backup strategy to be verify
     */
    public static void verifyBackupStrategy(BackupStrategy2 backupStrategy) {
        int maxBackupIndex = backupStrategy.getMaxBackupIndex();
        if (maxBackupIndex < 0) {
            throw new IllegalArgumentException("Max backup index should not be less than 0");
        } else if (maxBackupIndex == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Max backup index too big: " + maxBackupIndex);
        }
    }
}
