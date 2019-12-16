package com.magic.interview.service.dynamic_datasource;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Supplier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-12-13 14:00
 **/
public class ReplaceService {

    /**
     * 指定一个目录a，遍历目录下所有指定后缀名的文件(vm / html / js 等等)，将关键字 itougu.jrj.com.cn 替换为 www.itougu.com。
     * 目录遍历需包含目录a下的所有子目录.
     * <p>
     * result:输出一个执行结果文件，包含更新文件名、行号
     *
     * @param path:          目录
     * @param suffix         后缀：,分割
     * @param beReplacedWord :被替换文本
     * @param newWord        : 新文本
     */
    public static void scanFils(String path, String suffix, String beReplacedWord, String newWord) throws IOException {

        final Path[] currentPath = new Path[1];
        final Set<Integer>[] modifyLines = new Set[1];
        final int[] count = {0};
        final StringBuffer[] stringBuffer = new StringBuffer[1];
        final long[] total = {0};

        Map<String, Set<Integer>> result = new HashMap<>();
        Map<Path, StringBuffer> data = new HashMap<>();
        Files.walk(Paths.get(path)).filter(p -> !Files.isDirectory(p))
                .filter(p -> {
                    boolean matches = FileSystems.getDefault().getPathMatcher("glob:**.{" + suffix + "}").matches(p);
                    if (matches) {
                        stringBuffer[0] = new StringBuffer();
                        currentPath[0] = p;
                        count[0] = 0;
                        modifyLines[0] = new HashSet<>();
                    }
                    return matches;
                })
                .flatMap(p -> {
                    Supplier<Stream<String>> streams = () -> {
                        try {
                            return Files.lines(p, Charset.forName("UTF-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return Stream.empty();
                    };
                    total[0] = streams.get().count();
                    return streams.get();
                })
                .forEach(s -> {
                    if (!StringUtils.contains(s, beReplacedWord)) {
                        stringBuffer[0].append(s).append(System.lineSeparator());
                        count[0]++;
                        if (count[0] == total[0] && CollectionUtils.isNotEmpty(modifyLines[0]) && ArrayUtils.isNotEmpty(stringBuffer)) {
                            result.put(currentPath[0].getFileName().toString(), modifyLines[0]);
                            data.put(currentPath[0], stringBuffer[0]);
                        }
                        return;
                    }
                    String replace = StringUtils.replace(s, beReplacedWord, newWord);
                    stringBuffer[0].append(replace).append(System.lineSeparator());
                    count[0]++;
                    modifyLines[0].add(count[0]);

                    if (count[0] == total[0]) {
                        result.put(currentPath[0].getFileName().toString(), modifyLines[0]);
                        data.put(currentPath[0], stringBuffer[0]);
                    }
                });
        if (MapUtils.isEmpty(data)) {
            return;
        }
        //回写文件
        data.forEach((k, v) -> {
            try {
                Files.write(k, v.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //输出执行结果文件
        FileUtils.writeStringToFile(new File("D:/test/result.txt"), JSON.toJSONString(result), Charset.forName("UTF-8"));
    }

    public static void main(String[] args) throws IOException {
        scanFils("D:/test", "js,html,txt", "itougu.jrj.com.cn", "www.itougu.com");
    }
}
