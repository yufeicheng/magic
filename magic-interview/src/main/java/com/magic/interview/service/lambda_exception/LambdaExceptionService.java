package com.magic.interview.service.lambda_exception;

import lombok.SneakyThrows;
import lombok.var;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * lambda表达式中异常处理
 *
 * @author Cheng Yufei
 * @create 2019-10-31 15:51
 **/
public class LambdaExceptionService {

    /**
     * Files.lines(p)：受检查异常处理，需进行try.catch 处理，对于lambda来说比较。。。。
     * 所以自定义Function来将异常进行处理，是lambda看起来简洁
     *
     * @throws IOException
     */
    @SneakyThrows
    public static void files()  {
        long count =
                //获取目录下的文件夹及文件
                Files.walk(Paths.get("D:/test"))
                        //筛选出文件
                        .filter(p -> !Files.isDirectory(p))
                        //以xml结尾的文件
                        .filter(p -> {
                           /* System.out.println(p.endsWith("tools.xml"));//true
                            System.out.println(p.endsWith(".xml"));//false
                            System.out.println(p.toString().endsWith(".xml"));*///true
                            System.out.println(pathsMatch(p));//true
                            //return p.toString().endsWith(".xml");
                            return pathsMatch(p);
                        })
                        //按行获得文本

                        /*.flatMap(p -> {
                            try {
                                return Files.lines(p);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return Stream.empty();
                            }
                        })*/
                        //自定义Handle来处理异常
                        .flatMap(Handle.handle(p -> Files.lines(p, Charset.forName("utf-8"))))
                        //过滤空行
                        .filter(line -> !line.trim().isEmpty())
                        //文件有效行数
                        .count();
        System.out.println(count);
    }

    public static void main(String[] args) throws IOException {
        files();
    }

    private static boolean pathsMatch(Path p) {
        //文件编码需要转为utf-8，否则在matches时候会报：MalformedInputException: Input length = 1 错误
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.{xml,txt}");
        return pathMatcher.matches(p);
    }
}
