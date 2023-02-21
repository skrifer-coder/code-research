package skrifer.github.com.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static final long TRANSFER_ACK = 3840L;
    public static final long COMMAND_FILE_ACK = 3384L;
    public static final long COMMAND_TRANSFER_FILE = 3841L;
    public static final long COMMAND_RESPONSE_OK = 3844L;
    public static final long COMMAND_ACK = 3842L;
    public static final long COMMAND_FINGERPRONT_IMAGE = 3872L;
    public static final long GET_THE_SERIAL_NUMBER = 3896L;

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());

        try {
            ChannelFuture future = bootstrap.connect("192.168.9.200", 8888).sync();
            future.channel().write(GET_THE_SERIAL_NUMBER);
            future.channel().write(0x18);
            future.channel().write(0);
            future.channel().flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
    }
}
