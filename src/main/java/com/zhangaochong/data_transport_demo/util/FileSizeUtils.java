package com.zhangaochong.data_transport_demo.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aochong Zhang
 * @date 2021-04-14 11:34
 */
public abstract class FileSizeUtils {
    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("^(\\d+)(B|KB|MB|GB|TB|K|M|G|T)$");

    /**
     * 文件大小字符串转换为Byte值
     * 1B -> 1
     * 1KB -> 1024
     *
     * @param fileSize 文件大小字符串, 单位支持B、KB、MB、GB、TB、K、M、G、T
     * @return Byte值
     */
    public static long parseToByte(String fileSize) {
        if (!StringUtils.hasText(fileSize)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        long size;

        Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSize.toUpperCase().replace(" ", ""));
        if (!matcher.find()) {
            throw new IllegalArgumentException("参数" + fileSize + "格式不正确");
        }

        long inputSize = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "B":
                size = inputSize; break;
            case "KB":
            case "K":
                size = inputSize * 1024; break;
            case "MB":
            case "M":
                size = inputSize * 1024 * 1024; break;
            case "GB":
            case "G":
                size = inputSize * 1024 * 1024 * 1024; break;
            case "TB":
            case "T":
                size = inputSize * 1024 * 1024 * 1024 * 1024; break;
            default:
                throw new IllegalArgumentException("文件大小单位" + unit + "不支持");
        }
        return size;
    }
}
