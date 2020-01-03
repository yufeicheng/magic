package com.magic.interview.service.scanfile_replace;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
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
     * <p>
     * 压缩包-时间戳
     *
     * @param path:          目录
     * @param suffix         后缀：,分割
     * @param beReplacedWord :被替换文本
     * @param newWord        : 新文本
     */
    public static void scanFils(String path, String suffix, String beReplacedWord, String newWord, String outPath) throws IOException, InterruptedException {

        final Path[] currentPath = new Path[1];
        final Set<Integer>[] modifyLines = new Set[1];
        final int[] count = {0};
        final StringBuffer[] stringBuffer = new StringBuffer[1];
        final long[] total = {0};

        Map<String, Set<Integer>> result = new HashMap<>();
        Map<Path, StringBuffer> data = new HashMap<>();

        //不以 / 开头，说明此时输入的是相对路径，pwd 取绝对路径
        if (!path.startsWith("/")) {
            Process pwd = Runtime.getRuntime().exec("pwd");
            String line;
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(pwd.getInputStream(), "UTF-8"));
            while ((line = br.readLine()) != null) {
                path = line;
            }
        }

        //替换前先进行打包
        String gzName = StringUtils.substring(path, path.lastIndexOf("/") + 1);
        String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());

        String command = "tar -zcvf " + gzName + "_" + time + ".tar.gz ../" + gzName;
        Process process = Runtime.getRuntime().exec(command);
        System.out.println("打包执行中...：" + command);
        if (process.waitFor() == 0) {
            System.out.println("打包完成，开始替换。。。");
        }

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
            System.out.println("无可替换文件");
            return;
        }
        //回写文件
        data.forEach((k, v) -> {
            try {
                System.out.println("处理 " + k + " 文件");
                Files.write(k, v.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //输出执行结果文件
        FileUtils.writeStringToFile(new File(outPath + "/" + time + "_result.txt"), JSON.toJSONString(result), Charset.forName("UTF-8"));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        scanFils(args[0], args[1], args[2], args[3], args[4]);
    }
}
