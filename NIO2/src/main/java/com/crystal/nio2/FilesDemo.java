package com.crystal.nio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * Created by xiaoou on 17/12/10 14:56.
 * NIO2DEMO类 , 测试一些Paths, Files等在java7中新的类
 *
 * @version 1.0.0
 */
public class FilesDemo {
    public static void main(String[] args) throws IOException {
        // 遍历Linux根目录/下的子目录
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("/"));
        Iterator<Path> iterator = directoryStream.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            System.out.println(path);
        }

        // 递归创建文件目录
        Path path = Files.createDirectories(Paths.get("/Users/baidu/test/test"));
        System.out.println(path.getFileName());

        // 创建文件test.txt
        Path file = Files.createFile(Paths.get("/Users/baidu/test/test/test.txt"));

        // 使用缓冲字符流 写入文件内容
        Charset charset = Charset.forName("UTF-8");
        String text = "Hello, java NIO2";
        BufferedWriter bufferedWriter = Files.newBufferedWriter(file, charset, StandardOpenOption.APPEND);
        bufferedWriter.write(text);
        bufferedWriter.close();

        // 使用缓冲字符流 读取文件内容
        BufferedReader bufferedReader = Files.newBufferedReader(file, charset);
        String line = null;
        while ( (line = bufferedReader.readLine()) != null ) {
            System.out.println(line);
        }
        bufferedReader.close();
    }
}
