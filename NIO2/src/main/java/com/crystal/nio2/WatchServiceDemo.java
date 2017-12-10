package com.crystal.nio2;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * Created by xiaoou on 17/12/10 15:11.
 *
 * @version 1.0.0
 */
public class WatchServiceDemo {
    public void watchDir(Path path) throws IOException, InterruptedException {
        // 创建WatchService实例
        WatchService watchService = FileSystems.getDefault().newWatchService();
        // 注册WatchService所监听的事件(目录创建、目录修改、目录删除)
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        // 无限循环获取监听到的事件
        while (true) {
            final WatchKey key = watchService.take();
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                final WatchEvent.Kind<?> kind = watchEvent.kind();
                // 忽略OVERFLOW事件
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                final Path fileName = watchEventPath.context();
                // 打印事件类型及发生事件的文件名称
                System.out.println(kind + ": " + fileName );
            }

            // 重置key
            boolean valid = key.reset();
            // 如果key无效（比如监听的文件被删除）, 则退出
            if (!valid) {
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Path path = Paths.get("/Users/baidu/test/test");
        WatchServiceDemo demo = new WatchServiceDemo();
        demo.watchDir(path);
    }
}
