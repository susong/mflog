package com.mf.log.upload;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mf.log.LogUtils;
import com.mf.log.upload.entity.ResultVo;
import com.mf.log.upload.entity.UploadTokenRespVo;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UploadFileToQiniu extends UploadFileBase {

    private final UploadManager uploadManager = new UploadManager();

    @Override
    public void uploadFile(File file, String filename, FileType fileType) {
        getToken(file, filename, fileType);
    }

    private void getToken(File file, String filename, FileType fileType) {
        if (url == null || url.length() == 0) {
            LogUtils.w("tokenUrl is null");
            return;
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("fileType", fileType.name());
        urlBuilder.addQueryParameter("project", project);
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.w("获取七牛token异常", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String resp = body.string();
                        LogUtils.i("resp:" + resp);
                        ResultVo<UploadTokenRespVo> resultVo = JSON.parseObject(resp, new TypeReference<ResultVo<UploadTokenRespVo>>() {
                        });
                        if (resultVo.getCode()  == 0 && resultVo.getData() != null) {
                            realUploadFile(file, filename, resultVo.getData().getHost(), resultVo.getData().getToken());
                        } else {
                            LogUtils.w("获取七牛token失败 response:" + resp);
                        }
                    } else {
                        LogUtils.w("获取七牛token失败 response body is null");
                    }
                } catch (Exception e) {
                    LogUtils.w("获取七牛token异常", e);
                }
            }
        });
    }

    private void realUploadFile(File file, String fileName, String baseUrl, String token) {
        uploadManager.put(file, fileName, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    String url = baseUrl + "/" + key;
                    LogUtils.i("上传七牛成功:" + url);
                    file.delete();
                } else {
                    LogUtils.w("上传七牛失败 " + info.message);
                }
            }
        }, null);
    }
}
