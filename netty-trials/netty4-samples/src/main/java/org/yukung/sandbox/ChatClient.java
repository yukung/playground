package org.yukung.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author yukung
 */
public class ChatClient {
    private final String host;
    private final int port;

    private ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void run() throws InterruptedException, IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast("encoder", new StringEncoder());

                    pipeline.addLast("handler", new ChatClientHandler());
                }
            });

            Channel channel = bootstrap.connect(host, port).sync().channel();
            ChannelFuture lastWriteFuture;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = in.readLine();
                lastWriteFuture = channel.writeAndFlush(line + '\n');
                if (line == null) {
                    break;
                }
                lastWriteFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        Channel ch = future.channel();
//                        ch.close();
                    }
                });
//                lastWriteFuture.channel().closeFuture().sync();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new ChatClient("localhost", 8080).run();
    }
}
