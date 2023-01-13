package com.mf.log.upload;

import java.io.File;

public interface IUploadFile {
    enum FileType {
        IMAGE,
        AUDIO,
        VIDEO,
        LOG,
        TEXT,
        PACKAGE,
        BIN
    }

    void setUrl(String url);

    void setProject(String project);

    void setDeviceId(String deviceId);

    void uploadFile(String filePath, String filename, FileType fileType);

    void uploadFile(File file, String filename, FileType fileType);
}
