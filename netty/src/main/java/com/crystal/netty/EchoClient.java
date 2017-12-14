package com.crystal.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by xiaoou on 17/12/10 16:34.
 *
 * @version 1.0.0
 */
public class EchoClient {
    public static void main(String[] args) {
        int port = 8080;
        new EchoClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) {
        // 创建客户端处理I/O读写的NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建客户端辅助启动类
            Bootstrap bootstrap = new Bootstrap();
            // 设置NIO线程组
            bootstrap.group(group)
                    // 设置NioSockectChannel， 对应于JDK NIO类SocketChannel类
                    .channel(NioSocketChannel.class)
                    // 设置TCP参数 TCP_NODELAY
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            // 设置客户端处理网络I/O事件的类
                            nioSocketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture f = bootstrap.connect(host, port).sync();

            for (int i = 0; i < 1000; i++) {
                // 构造客户端发送的数据ByteBuf对象
                byte[] req = "你好,Netty!".getBytes();
                ByteBuf messageBuffer = Unpooled.buffer(req.length);
                messageBuffer.writeBytes(req);

                // 向服务器端发送数据
                ChannelFuture channelFuture = f.channel().writeAndFlush(messageBuffer);
                channelFuture.syncUninterruptibly();
            }


            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
