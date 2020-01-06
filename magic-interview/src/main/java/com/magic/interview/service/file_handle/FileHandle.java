package com.magic.interview.service.file_handle;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作
 *
 * @author Cheng Yufei
 * @create 2020-01-03 17:01
 **/
public class FileHandle {


    /**
     * 文件列表，中文排序
     */
    @Test
    public void sortFile() throws IOException {
        Path path = Paths.get("D:/system");

        Comparator<Path> comparator = (p1, p2) -> Collator.getInstance(Locale.CHINESE).compare(p1.getFileName().toString(), p2.getFileName().toString());

        List<Path> list = Files.list(path).sorted(comparator).collect(Collectors.toList());
        System.out.println(list);
    }


    /**
     * 文件路径描述： path paths
     */
    @Test
    public void paths() {

        Path path = Paths.get("D:/test/result.txt");

        //文件名 result.txt
        Path fileName = path.getFileName();
        System.out.println(fileName);

        //父路经 D:\test
        Path parent = path.getParent();
        System.out.println(parent);

        //文件层级 2
        int nameCount = path.getNameCount();
        System.out.println(nameCount);

        //根据层级获取文件（夹）名 test
        Path name = path.getName(0);
        System.out.println(name);

        //获取同级目录下的文件
        Path path1 = path.resolveSibling("b.txt");
        Path path2 = path.resolveSibling("js/detail.js");
        System.out.println(path1 + "---" + path2);

        //获取子集目录下文件（夹）
        Path pathResolve = Paths.get("D:/test/velocity");
        Path resolve = pathResolve.resolve("html/top.html");
        System.out.println(resolve);


        //D:\
        Path root = path.getRoot();
        System.out.println(root);

        //绝对路径 D:\test\result.txt
        Path path3 = path.toAbsolutePath();
        System.out.println(path3);

        //转为 File
        File file = path.toFile();
    }


    @Test
    public void files() throws IOException {
        Path path = Paths.get("D:/test/result.txt");

        //是否是文件夹
        boolean directory = Files.isDirectory(path);
        System.out.println(directory);

        //校验文件存在/不存在
        boolean exists = Files.exists(path);
        boolean notExists = Files.notExists(path);
        System.out.println(exists + "-----" + notExists);

        //校验是否是文件
        boolean regularFile = Files.isRegularFile(path);
        System.out.println(regularFile);

        //在同级目录下创建文件/文件夹
        Path file = Files.createFile(path.resolveSibling("files_create.txt"));
        System.out.println(file);

        Path new_file = Files.createDirectory(path.resolveSibling("new_file"));
        System.out.println(new_file);

        //删除文件（夹）
        boolean b = Files.deleteIfExists(Paths.get("D:/test/files_create.txt"));
        System.out.println(b);

        //复制
        Path copyRes = Files.copy(path, Paths.get("D:/result.txt"));
        System.out.println(copyRes);

        //移动
        Path moveRes = Files.move(path, Paths.get("D:/res.txt"), StandardCopyOption.REPLACE_EXISTING);
        System.out.println(moveRes);
    }

    @Test
    public void files_write_read() throws IOException {

        Path path = Paths.get("D:/test/tools.xml");

        //读取出来为一行一行逗号拼接的字符串
        List<String> readAllLines = Files.readAllLines(path, Charset.forName("utf-8"));
        System.out.println(readAllLines);

        //读取出来为原有格式布局
        Stream<String> lines = Files.lines(path);
        lines.forEach(s-> System.out.println(s));

        //读成字节数组
        byte[] bytes = Files.readAllBytes(path);

        //写入时指定拼接方式，否则会覆盖；集合中一个元素为一行
        Path write = Files.write(path, Lists.newArrayList("测试", "BBB"),Charset.forName("utf-8"), StandardOpenOption.APPEND);
        System.out.println(write);


        //获取 BufferedWriter 进行写入
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path, Charset.forName("utf-8"), StandardOpenOption.APPEND);


    }

}
