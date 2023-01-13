package com.mf.log.upload;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSON;
import com.mf.log.LogUtils;
import com.mf.log.upload.entity.ResultVo;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UploadFileToMec extends UploadFileBase {
    protected String url = "http://192.168.100.99:8080/v1/private/terminal-api/terminal-log-upload/upload";
    protected String deviceId = "123";

    @Override
    public void uploadFile(File file, String filename, FileType fileType) {
        realUploadFile(file, filename);
    }

    private void realUploadFile(File file, String fileName) {
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("deviceId", deviceId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();

        httpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        LogUtils.w("上传日志文件到MEC失败", e);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            ResponseBody body = response.body();
                            if (body != null) {
                                String resp = body.string();
                                LogUtils.i("resp:" + resp);
                                ResultVo resultVo = JSON.parseObject(resp, ResultVo.class);
                                if (resultVo.getCode() == 200) {
                                    LogUtils.i("上传日志文件到MEC成功");
                                    String path = file.getAbsolutePath();
                                    boolean delete = file.delete();
                                    LogUtils.i("删除日志文件:" + delete + " " + path);
                                } else {
                                    LogUtils.w("上传日志文件到MEC失败 response:" + resp);
                                }
                            } else {
                                LogUtils.w("上传日志文件到MEC失败 response body is null");
                            }
                        } catch (Exception e) {
                            LogUtils.w("上传日志文件到MEC异常", e);
                        }
                    }
                });
    }
}
