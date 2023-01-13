package com.mf.log.upload.entity;


/**
 * 上传获取token响应VO
 */
public class UploadTokenRespVo {

    /**
     * 上传需要的token
     */
    private String token;

    /**
     * 上传后的域名
     */
    private String host;

    public UploadTokenRespVo() {
    }

    public UploadTokenRespVo(String token, String host) {
        this.token = token;
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "UploadTokenRespVo{" +
                "token='" + token + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
