package idv.xfl03.quesreg.httpserver;

import idv.xfl03.quesreg.MainPool;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends
		ChannelInitializer<SocketChannel> {
	private MainPool mainPool;
    public HttpServerInitializer(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	@Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpContentCompressor());
        pipeline.addLast(new HttpServerHandler(mainPool));  
    }
}
