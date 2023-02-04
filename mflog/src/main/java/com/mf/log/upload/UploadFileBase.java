package com.mf.log.upload;

import androidx.annotation.NonNull;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.OkHttpClient;

public abstract class UploadFileBase implements IUploadFile {

    protected String url = "";
    protected String project = "parking";
    protected String deviceId = "0";
    protected final OkHttpClient httpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(3, TimeUnit.SECONDS) // 连接超时
            .readTimeout(3, TimeUnit.SECONDS) // 读取超时
            .writeTimeout(3, TimeUnit.SECONDS) //  写超时
            .dns(new XDns(3, TimeUnit.SECONDS)) // DNS超时
            .build();

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void uploadFile(String filePath, String filename, FileType fileType) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        File file = new File(filePath);
        if (filename == null || filename.length() == 0) {
            filename = file.getName();
        }
        if (fileType == null) {
            fileType = FileType.BIN;
        }
        uploadFile(file, filename, fileType);
    }


    public static class XDns implements Dns {
        private final long timeout;
        private final TimeUnit unit;

        public XDns(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
        }

        @NonNull
        @Override
        public List<InetAddress> lookup(@NonNull final String hostname) throws UnknownHostException {
            try {
                FutureTask<List<InetAddress>> task = new FutureTask<>(() ->
                        Arrays.asList(InetAddress.getAllByName(hostname)));
                new Thread(task).start();
                return task.get(timeout, unit);
            } catch (Exception e) {
                UnknownHostException unknownHostException =
                        new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                unknownHostException.initCause(e);
                throw unknownHostException;
            }
        }
    }
}
