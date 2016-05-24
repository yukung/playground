package org.yukung.sandbox;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * @author Yusuke Ikeda
 */
public class EchoServer {
    public static void main(String[] args) {
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool()
        );

        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            // Downstream
            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
            pipeline.addLast("stringEncoder", new StringEncoder());
            // Upstream
            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
            pipeline.addLast("stringDecorder", new StringDecoder());
            // Handler
            pipeline.addLast("handler", new EchoServerHandler());   // Server

            return pipeline;
        });

        bootstrap.bind(new InetSocketAddress(9999));    // listen on 9999 port.
    }
}
