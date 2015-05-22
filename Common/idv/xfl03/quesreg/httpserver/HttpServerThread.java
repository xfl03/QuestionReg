package idv.xfl03.quesreg.httpserver;

import idv.xfl03.quesreg.MainPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServerThread implements Runnable {
	private int PORT;
	private MainPool mainPool;
	public HttpServerThread(MainPool mainPool){
		this.mainPool=mainPool;
		this.PORT=this.mainPool.mainConfig.httpServerPort;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.option(ChannelOption.TCP_NODELAY, true); 
            b.option(ChannelOption.SO_TIMEOUT, 60000);  
            b.option(ChannelOption.SO_SNDBUF, 1048576*200);  
              
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new HttpServerInitializer(mainPool));

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("[QuestionReg] Http Server runs on 'http://127.0.0.1:" + PORT + "/'");

            ch.closeFuture().sync();
        } catch (Exception e){
        	e.printStackTrace();
        } finally {
        	bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}

}
