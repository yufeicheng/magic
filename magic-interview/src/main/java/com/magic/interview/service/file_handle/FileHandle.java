package com.magic.interview.service.file_handle;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
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
        lines.forEach(s -> System.out.println(s));

        //读成字节数组
        byte[] bytes = Files.readAllBytes(path);

        //写入时指定拼接方式，否则会覆盖；集合中一个元素为一行
        Path write = Files.write(path, Lists.newArrayList("测试", "BBB"), Charset.forName("utf-8"), StandardOpenOption.APPEND);
        System.out.println(write);


        //获取 BufferedWriter 进行写入
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path, Charset.forName("utf-8"), StandardOpenOption.APPEND);

        bufferedWriter.write("接着写");
        bufferedWriter.flush();

        /*InputStream inputStream = Files.newInputStream(path);

        OutputStream outputStream = Files.newOutputStream(path);*/

    }

    @Test
    public void file_list() throws IOException {

        Path path = Paths.get("D:/test");
        //文件列表
        Stream<Path> list = Files.list(path);
        //list.forEach(System.out::println);

        //文件查找
        Stream<Path> finds = Files.find(path, 1, (p, b) -> StringUtils.endsWithAny(p.getFileName().toString(), "txt", "xml"));
        //finds.forEach(System.out::println);

        //文件遍历，查找利用PathMatcher
        Files.walk(path).filter(p -> !Files.isDirectory(p)).forEach(p -> {

            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.{txt,xml,js}");
            boolean matches = pathMatcher.matches(p);
            if (matches) {
                //System.out.println(p);
            }
        });


        Path path2= Paths.get("D:/test");
        //文件遍历
        Files.walkFileTree(path2, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.{txt,xml,js}");
                boolean matches = pathMatcher.matches(file);
                if (matches) {
                    System.out.println(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

    }

    /**
     * glob 规则：
     *
     *      * 匹配零个或多个字符与名称组件，不跨越目录
     *
     *      ** 匹配零个或多个字符与名称组件，跨越目录（含子目录）
     *
     *      ? 匹配一个字符的字符与名称组件
     *
     *      \ 转义字符，例如\{表示匹配左花括号
     *
     *      [] 匹配方括号表达式中的范围，连字符(-)可指定范围。例如[ABC]匹配"A"、"B"和"C"；[a-z]匹配从"a"到"z"；[abce-g]匹配"a"、"b"、"c"、"e"、"f"、"g"；
     *
     *      [!...]匹配范围之外的字符与名称组件，例如[!a-c]匹配除"a"、"b"、"c"之外的任意字符
     *
     *      {}匹配组中的任意子模式，多个子模式用","分隔，不能嵌套
     *
     *
     *
     * SimpleFileVisitor类中方法：
     *
     *      preVisitDirectory 访问一个目录，在进入之前调用。
     *      postVisitDirectory 一个目录的所有节点都被访问后调用。遍历时跳过同级目录或有错误发生，Exception会传递给这个方法
     *      visitFile 文件被访问时被调用。该文件的文件属性被传递给这个方法
     *      visitFileFailed 当文件不能被访问时，此方法被调用。Exception被传递给这个方法
     *
     * FileVisitResult行为结果：
     *
     *      CONTINUE 继续遍历
     *      SKIP_SIBLINGS 继续遍历，但忽略当前节点的所有兄弟节点直接返回上一层继续遍历
     *      SKIP_SUBTREE 继续遍历，但是忽略子目录，但是子文件还是会访问
     *      TERMINATE 终止遍历
     *
     */

}