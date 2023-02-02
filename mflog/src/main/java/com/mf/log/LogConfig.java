package com.mf.log;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志配置
 */
public class LogConfig {

    /**
     * 日志tag
     */
    public final String tag;

    /**
     * 日志输出根目录
     */
    public final String logBasePath;

    /**
     * 日志输出目录
     */
    public final String logDir;

    /**
     * 是否打印线程信息
     */
    public final boolean isEnableThreadInfo;

    /**
     * 是否打印边框
     */
    public final boolean isEnableBorder;

    /**
     * 是否打印调用栈
     */
    public final boolean isEnableStackTrace;

    /**
     * 是否输出日志到控制台
     */
    public final boolean isLogToConsole;

    /**
     * 是否通过xlog保存日志文件
     */
    public final boolean isLogToFileByXLog;

    /**
     * 是否通过mars保存日志文件
     */
    public final boolean isLogToFileByMars;

    /**
     * 是否上传日志文件
     */
    public final boolean isUploadLogFile;

    public final BackupConfig backupConfig;
    public final UploadConfig uploadConfig;
    public final List<CleanConfig> cleanConfigs;

    /*package*/ LogConfig(final Builder builder) {
        this.tag = builder.tag;
        this.logBasePath = builder.logBasePath;
        this.logDir = builder.logDir;

        this.isEnableThreadInfo = builder.isEnableThreadInfo;
        this.isEnableBorder = builder.isEnableBorder;
        this.isEnableStackTrace = builder.isEnableStackTrace;
        this.isLogToConsole = builder.isLogToConsole;
        this.isLogToFileByXLog = builder.isLogToFileByXLog;
        this.isLogToFileByMars = builder.isLogToFileByMars;
        this.isUploadLogFile = builder.isUploadLogFile;

        this.backupConfig = builder.backupConfig;
        this.uploadConfig = builder.uploadConfig;
        this.cleanConfigs = builder.cleanConfigs;
    }

    public static class Builder {
        private String tag = "mflog";
        private String logBasePath = LogUtils.getSdPath() + "/mflog";
        private String logDir = "main";
        private boolean isEnableThreadInfo = false;
        private boolean isEnableBorder = false;
        private boolean isEnableStackTrace = false;
        private boolean isLogToConsole = true;
        private boolean isLogToFileByXLog = false;
        private boolean isLogToFileByMars = false;
        private boolean isUploadLogFile = false;
        private BackupConfig backupConfig;
        private UploadConfig uploadConfig;
        private List<CleanConfig> cleanConfigs;

        public Builder() {

        }

        public Builder(LogConfig logConfig) {
            tag = logConfig.tag;
            logBasePath = logConfig.logBasePath;
            logDir = logConfig.logDir;

            isEnableThreadInfo = logConfig.isEnableThreadInfo;
            isEnableBorder = logConfig.isEnableBorder;
            isEnableStackTrace = logConfig.isEnableStackTrace;
            isLogToConsole = logConfig.isLogToConsole;
            isLogToFileByXLog = logConfig.isLogToFileByXLog;
            isUploadLogFile = logConfig.isUploadLogFile;

            backupConfig = logConfig.backupConfig;
            uploadConfig = logConfig.uploadConfig;
            if (logConfig.cleanConfigs != null) {
                cleanConfigs = new ArrayList<>(logConfig.cleanConfigs);
            }
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder logBasePath(String logBasePath) {
            this.logBasePath = logBasePath;
            return this;
        }

        public Builder logDir(String logDir) {
            this.logDir = logDir;
            return this;
        }

        public Builder isEnableThreadInfo(boolean isEnableThreadInfo) {
            this.isEnableThreadInfo = isEnableThreadInfo;
            return this;
        }

        public Builder isEnableBorder(boolean isEnableBorder) {
            this.isEnableBorder = isEnableBorder;
            return this;
        }

        public Builder isEnableStackTrace(boolean isEnableStackTrace) {
            this.isEnableStackTrace = isEnableStackTrace;
            return this;
        }

        public Builder isLogToConsole(boolean isLogToConsole) {
            this.isLogToConsole = isLogToConsole;
            return this;
        }

        public Builder isLogToFileByXLog(boolean isLogToFileByXLog) {
            this.isLogToFileByXLog = isLogToFileByXLog;
            return this;
        }

        public Builder isLogToFileByMars(boolean isLogToFileByMars) {
            this.isLogToFileByMars = isLogToFileByMars;
            return this;
        }

        public Builder isUploadLogFile(boolean isUploadLogFile) {
            this.isUploadLogFile = isUploadLogFile;
            return this;
        }

        public Builder backupConfig(BackupConfig backupConfig) {
            this.backupConfig = backupConfig;
            return this;
        }

        public Builder uploadConfig(UploadConfig uploadConfig) {
            this.uploadConfig = uploadConfig;
            return this;
        }

        public Builder addCleanConfig(CleanConfig cleanConfig) {
            if (cleanConfigs == null) {
                cleanConfigs = new ArrayList<>();
            }
            cleanConfigs.add(cleanConfig);
            return this;
        }

        public LogConfig build() {
            initEmptyFieldsWithDefaultValues();
            return new LogConfig(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (backupConfig == null) {
                backupConfig = new BackupConfig.Builder().build();
            }
            if (uploadConfig == null) {
                uploadConfig = new UploadConfig.Builder().build();
            }
            if (cleanConfigs == null) {
                cleanConfigs = new ArrayList<>();
            }
        }
    }

    /**
     * 日志备份配置
     */
    public static class BackupConfig {
        /**
         * 单个日志大小
         */
        public final long singleLogMaxSize;
        /**
         * 日志总大小
         */
        public final long totalLogMaxSize;
        /**
         * 日志保留时间
         */
        public final long logRetentionTime;
        /**
         * 日志检测轮询时间
         */
        public final int logCheckInterval;
        /**
         * 最大备份索引
         */
        public final int maxBackupIndex;

        /*package*/ BackupConfig(final Builder builder) {
            this.singleLogMaxSize = builder.singleLogMaxSize;
            this.totalLogMaxSize = builder.totalLogMaxSize;
            this.logRetentionTime = builder.logRetentionTime;
            this.logCheckInterval = builder.logCheckInterval;
            this.maxBackupIndex = builder.maxBackupIndex;
        }

        public static class Builder {
            private long singleLogMaxSize = 10 * 1024 * 1024;/*10MB*/
            private long totalLogMaxSize = 500 * 1024 * 1024;/*500MB*/
            private long logRetentionTime = 5 * 24 * 60 * 60 * 1000;/*5天*/
            private int logCheckInterval = 10 * 60 * 1000;/*10分钟*/
            private int maxBackupIndex = 100;

            public Builder() {
            }

            public Builder(BackupConfig backupConfig) {
                this.singleLogMaxSize = backupConfig.singleLogMaxSize;
                this.totalLogMaxSize = backupConfig.totalLogMaxSize;
                this.logRetentionTime = backupConfig.logRetentionTime;
                this.logCheckInterval = backupConfig.logCheckInterval;
                this.maxBackupIndex = backupConfig.maxBackupIndex;
            }

            public Builder singleLogMaxSize(long singleLogMaxSize) {
                this.singleLogMaxSize = singleLogMaxSize;
                return this;
            }

            public Builder totalLogMaxSize(long totalLogMaxSize) {
                this.totalLogMaxSize = totalLogMaxSize;
                return this;
            }

            public Builder logRetentionTime(long logRetentionTime) {
                this.logRetentionTime = logRetentionTime;
                return this;
            }

            public Builder logCheckInterval(int logCheckInterval) {
                this.logCheckInterval = logCheckInterval;
                return this;
            }

            public Builder maxBackupIndex(int maxBackupIndex) {
                this.maxBackupIndex = maxBackupIndex;
                return this;
            }

            public BackupConfig build() {
                return new BackupConfig(this);
            }
        }
    }

    /**
     * 日志上传配置
     */
    public static class UploadConfig {

        public enum Target {
            MEC,
            CLOUD,
        }

        /**
         * 上传日志目标
         */
        public final Target target;
        /**
         * 上传路径
         */
        public final String url;
        /**
         * 项目
         */
        public final String project;
        /**
         * 设备id
         */
        public final String deviceId;

        /*package*/ UploadConfig(Builder builder) {
            this.target = builder.target;
            this.url = builder.url;
            this.project = builder.project;
            this.deviceId = builder.deviceId;
        }

        public static class Builder {
            private Target target = Target.MEC;
            private String url = "";
            private String project = "PARKING";
            private String deviceId = "0";

            public Builder() {
            }

            public Builder(UploadConfig uploadConfig) {
                this.target = uploadConfig.target;
                this.url = uploadConfig.url;
                this.project = uploadConfig.project;
                this.deviceId = uploadConfig.deviceId;
            }

            public Builder target(Target target) {
                this.target = target;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }

            public Builder project(String project) {
                this.project = project;
                return this;
            }

            public Builder deviceId(String deviceId) {
                this.deviceId = deviceId;
                return this;
            }

            public UploadConfig build() {
                return new UploadConfig(this);
            }
        }
    }

    public static class CleanConfig {
        /**
         * 日志目录
         */
        public final String logDir;
        /**
         * 大小限制
         */
        public final long sizeLimit;
        /**
         * 数量限制
         */
        public final int countLimit;
        /**
         * 检测轮询时间
         */
        public final int checkInterval;

        /*package*/ CleanConfig(Builder builder) {
            this.logDir = builder.logDir;
            this.sizeLimit = builder.sizeLimit;
            this.countLimit = builder.countLimit;
            this.checkInterval = builder.checkInterval;
        }

        public static class Builder {
            private String logDir = "";
            private long sizeLimit = 500 * 1024 * 1024;/*500MB*/
            private int countLimit = 100;
            private int checkInterval = 10 * 60 * 1000;/*10分钟*/

            public Builder() {
            }

            public Builder(CleanConfig cleanConfig) {
                this.logDir = cleanConfig.logDir;
                this.sizeLimit = cleanConfig.sizeLimit;
                this.countLimit = cleanConfig.countLimit;
                this.checkInterval = cleanConfig.checkInterval;
            }

            public Builder logDir(String logDir) {
                this.logDir = logDir;
                return this;
            }

            public Builder sizeLimit(long sizeLimit) {
                this.sizeLimit = sizeLimit;
                return this;
            }

            public Builder countLimit(int countLimit) {
                this.countLimit = countLimit;
                return this;
            }

            public Builder checkInterval(int checkInterval) {
                this.checkInterval = checkInterval;
                return this;
            }

            public CleanConfig build() {
                return new CleanConfig(this);
            }
        }
    }
}
