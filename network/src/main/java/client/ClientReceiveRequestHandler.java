package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;

@ChannelHandler.Sharable
public class ClientReceiveRequestHandler extends ChannelInboundHandlerAdapter {




    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println(o);
//        给本地网关发送请求
        System.out.println("给网关发送请求");
        String host = "127.0.0.1";
        int port = 8333;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(channelHandlerContext.channel().eventLoop())
                .channel(channelHandlerContext.channel().getClass())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline ch =  socketChannel.pipeline();
                        ch.addLast(new HttpClientCodec());
                        ch.addLast(new SendGatewayHandler(channelHandlerContext.channel()));
                    }
                });
        System.out.println("向网关发送请求");
        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()){
                f.channel().writeAndFlush((FullHttpRequest) o);
            }else {
                f.channel().close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }

    private class SendGatewayHandler extends ChannelInboundHandlerAdapter{
        private final Channel clientChannel;

        private SendGatewayHandler(Channel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object fullHttpResponse) throws Exception {
            clientChannel.writeAndFlush((FullHttpResponse) fullHttpResponse);
        }
    }
}
