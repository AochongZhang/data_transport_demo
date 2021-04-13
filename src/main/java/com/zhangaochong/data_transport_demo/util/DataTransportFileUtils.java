package com.zhangaochong.data_transport_demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 11:24
 */
@Slf4j
public abstract class DataTransportFileUtils {
    public static String exportFile(String path, String fileName, String content, boolean append) {
        File file = new File(path, fileName);
        if (file.getParentFile().mkdirs()) {
            log.info("创建目录 {}", path);
        }
        try (FileWriter fileWriter = new FileWriter(file, append)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static String exportFile(String path, String fileName, String content) {
        return exportFile(path, fileName, content, false);
    }
}
