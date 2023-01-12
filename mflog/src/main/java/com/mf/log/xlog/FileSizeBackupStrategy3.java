package com.mf.log.xlog;

import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2;

public class FileSizeBackupStrategy3 extends FileSizeBackupStrategy2 {

    private int numOfZero = 0;

    /**
     * Constructor.
     *
     * @param maxSize        the max size the file can reach
     * @param maxBackupIndex the max backup index, or {@link #NO_LIMIT}, see {@link #getMaxBackupIndex()}
     */
    public FileSizeBackupStrategy3(long maxSize, int maxBackupIndex) {
        super(maxSize, maxBackupIndex);
        if (maxBackupIndex > 0) {
            numOfZero = (int) Math.floor(Math.log10(maxBackupIndex));
        }
    }

    @Override
    public String getBackupFileName(String fileName, int backupIndex) {
        if (numOfZero > 0) {
            return fileName + "." + String.format("%0" + numOfZero + "d", backupIndex);
        }
        return fileName + "." + backupIndex;
    }
}
