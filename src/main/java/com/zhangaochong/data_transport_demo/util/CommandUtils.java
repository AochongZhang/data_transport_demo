package com.zhangaochong.data_transport_demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author Aochong Zhang
 * @date 2021-04-14 10:39
 */
@Slf4j
public class CommandUtils {
    /**
     * 执行命令
     *
     * @param command 命令
     */
    public static void execCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int waitFor = process.waitFor();
            if (waitFor == 0) {
                log.info("[执行命令] 成功, 标准输出: {}", getResult(process.getInputStream()));
            } else {
                String result = getResult(process.getErrorStream());
                log.error("[执行命令] 失败, 错误输出: {}", result);
                throw new RuntimeException("[执行命令] 失败, 错误输出: " + result);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("命令执行出错");
        }
    }

    /**
     * 执行命令
     *
     * @param cmdarray 命令
     */
    public static void execCommand(String[] cmdarray) {
        try {
            Process process = Runtime.getRuntime().exec(cmdarray);
            int waitFor = process.waitFor();
            if (waitFor == 0) {
                log.info("[执行命令] 成功, 标准输出: {}", getResult(process.getInputStream()));
            } else {
                String result = getResult(process.getErrorStream());
                log.error("[执行命令] 失败, 错误输出: {}", result);
                throw new RuntimeException("[执行命令] 失败, 错误输出: " + result);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("命令执行出错");
        }
    }

    private static String getResult(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
