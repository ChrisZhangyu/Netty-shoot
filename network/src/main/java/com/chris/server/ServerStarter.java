package com.chris.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

public class ServerStarter {
    private final int serverProviderPort;
    private final int requestPort = 80;

    public ServerStarter(int serverProviderPort) {
        this.serverProviderPort = serverProviderPort;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerGetHTTPHandlerFactory serverGetHTTPHandlerFactory = new ServerGetHTTPHandlerFactory();
        ServerProviderHandlerFactory serverProviderHandlerFactory = new ServerProviderHandlerFactory();

        ServerBootstrap sbs = new ServerBootstrap();
        sbs.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (ch.localAddress().getPort() == requestPort) {
                            System.out.println("创建远程请求端channel:" + ch.id());
                            System.out.println("父channel:" + ch.parent().id());

//                            pipeline.addLast("parseHttp", new HttpServerCodec());
//                            pipeline.addLast(new HttpRequestEncoder());
//                            pipeline.addLast(new HttpObjectAggregator(65536));
                            serverProviderHandlerFactory.channel = ch;
                            pipeline.addLast("transferToServer", serverGetHTTPHandlerFactory.getHTTPHandler());
                        } else if (ch.localAddress().getPort() == 8888){
                            System.out.println("创建服务端channel:" + ch.id());
                            System.out.println("父channel:" + ch.parent().id());
//                            pipeline.addLast("parseHttp", new HttpServerCodec());
//                            pipeline.addLast(new HttpRequestEncoder());
//                            pipeline.addLast(new HttpObjectAggregator(65536));
                                serverGetHTTPHandlerFactory.channel = ch;
                            pipeline.addLast("transferToServer", serverProviderHandlerFactory.getHTTPHandler());

                        }
                    }
                });
        ChannelFuture requestChannel = null;
        ChannelFuture serverChannel = null;

        try {
            serverChannel = sbs.bind(8888).sync();
            System.out.println("监听服务端：" + serverChannel.channel().localAddress());
            System.out.println(serverChannel.channel().id());
            requestChannel = sbs.bind(requestPort).sync();
            System.out.println("监听请求端：" + requestChannel.channel().localAddress());
            System.out.println(requestChannel.channel().id());


            requestChannel.channel().closeFuture().sync();
//            serverChannel.channel().closeFuture().sync();
        } finally {
            System.out.println("关闭请求端channel");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            requestChannel.channel().close();
        }
//        in this code how many channels was created when two connections were created

    }

    public static void main(String[] args) throws InterruptedException {
        new ServerStarter(8888).run();
    }
}
