package com.github.zhuchao941.canal2sql.util;

/**
 * @author 赵威 2025/7/8 11:12
 */
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class DynamicLogger {

    private static final ConcurrentHashMap<String, Logger> loggerCache = new ConcurrentHashMap<>();

    public static Logger getLogger(String fileKey) {
        return loggerCache.computeIfAbsent(fileKey, DynamicLogger::createLogger);
    }

    private static Logger createLogger(String fileKey) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 设置编码器
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%msg%n");
        encoder.start();

        // 设置文件输出路径
        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(context);
        fileAppender.setName("FILE-" + fileKey);
        fileAppender.setFile("logs/" + fileKey + ".log");
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        // 创建 Logger
        ch.qos.logback.classic.Logger logger = context.getLogger("dynamic." + fileKey);
        logger.setAdditive(false);
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
        logger.addAppender(fileAppender);
        return logger;
    }
}

