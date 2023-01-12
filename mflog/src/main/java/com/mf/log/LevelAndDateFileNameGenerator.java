package com.mf.log;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.printer.file.naming.FileNameGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LevelAndDateFileNameGenerator implements FileNameGenerator {

    final String fileName = "%s_%s.log";

    ThreadLocal<SimpleDateFormat> mLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHH", Locale.CHINA);
        }
    };

    @Override
    public boolean isFileNameChangeable() {
        return true;
    }

    @Override
    public String generateFileName(int logLevel, long timestamp) {
        SimpleDateFormat sdf = mLocalDateFormat.get();
        sdf.setTimeZone(TimeZone.getDefault());
        if (logLevel >= LogLevel.ERROR) {
            return String.format(fileName, LogLevel.getLevelName(logLevel), sdf.format(new Date(timestamp)));
        }
        return String.format(fileName, LogLevel.getLevelName(LogLevel.INFO), sdf.format(new Date(timestamp)));
    }
}
