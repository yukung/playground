package org.yukung.sandbox;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * @author Yusuke Ikeda
 */
public class EchoClient {
    public static void main(String[] args) {
        NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool()
        );

        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            // Downstream
            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
            pipeline.addLast("stringEncoder", new StringEncoder());
            // Upstream
            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
            pipeline.addLast("stringDecoder", new StringDecoder());
            // Handler
            pipeline.addLast("handler", new EchoClientHandler());   // Client

            return pipeline;
        });

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 9999));// connect to 9999 port.
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
}
