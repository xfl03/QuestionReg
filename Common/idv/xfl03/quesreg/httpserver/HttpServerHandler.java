package idv.xfl03.quesreg.httpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import idv.xfl03.quesreg.MainPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	private MainPool mainPool;
    public HttpServerHandler(MainPool mainPool) {
		this.mainPool=mainPool;
	}

	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,HttpObject msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            HttpResponseStatus hrs=HttpResponseStatus.OK;
            File file=null;
            String pa=req.getUri().toLowerCase();
            //System.out.println(pa);
            if(pa.equalsIgnoreCase("/")){
            	//System.out.println("e");
            	pa="index.html";
            }
            pa=pa.replaceFirst("/", "");
            //System.out.println(pa);
            String token="";
            Set<Cookie> cookies;
            String value = req.headers().get(HttpHeaders.Names.COOKIE);
            if (value == null) {
                cookies = Collections.emptySet();
            } else {
                cookies = CookieDecoder.decode(value);
            }
            if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equalsIgnoreCase("token"))
                    	token=cookie.getValue();
                }
            }
            StringBuilder apiRes=null;
            String clientIP = req.headers().get("X-Forwarded-For");
			if (clientIP == null) {
				InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
						.remoteAddress();
				clientIP = insocket.getAddress().getHostAddress();
			}
            if(pa.startsWith("api/")){
            	//API
            	apiRes=new StringBuilder();
            	String target=pa.replace("api/", "");
            	QueryStringDecoder decoderQuery = new QueryStringDecoder(req.getUri());
                Map<String, List<String>> uriAttributes = decoderQuery.parameters();
                /*
                for (Entry<String, List<String>> attr: uriAttributes.entrySet()) {
                    for (String attrVal: attr.getValue()) {
                    	apiRes.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
                    }
                }*/  
                APIHandler apih=new APIHandler(mainPool, uriAttributes, target, token, clientIP);
                apiRes.append(apih.handle());
                token=apih.token;
            }else{
            	file=new File(mainPool.majorConfig.webFolder,pa);
            }
            if(file!=null){
            	if(!file.getAbsolutePath().startsWith(mainPool.majorConfig.webFolder.getPath())
            			||file.isDirectory()){
            		hrs=HttpResponseStatus.FORBIDDEN;
            		file=null;
            	}
            }
            if(file!=null){
            	if(!file.exists()){
            		file=null;
            		hrs=HttpResponseStatus.NOT_FOUND;
            	}
            }
            
            
            
            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            boolean keepAlive = HttpHeaders.isKeepAlive(req);
            //FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, hrs);
            
            if(file!=null){
            	FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					int fileLen = fis.available();
					byte[] buf = new byte[102400];
					int numofBlock = fileLen / buf.length;
					int lastSize = fileLen % buf.length;
					
					//Send Full Blocks
					for (int i = 0; i < numofBlock; i++) {
						fis.read(buf, 0, buf.length);
						response.content().writeBytes(buf);
					}
					
					//Send Last
					buf = new byte[lastSize];
					fis.read(buf, 0,lastSize);
					response.content().writeBytes(buf);
					
				} catch (IOException e) {
					e.printStackTrace();
					hrs=HttpResponseStatus.INTERNAL_SERVER_ERROR;
					
					try {
						//End
						fis.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
            }else{
            	ByteBuf buffer;
            	if(apiRes==null){
            	buffer = Unpooled.copiedBuffer(hrs.code()+"", CharsetUtil.UTF_8);
            	response.content().writeBytes(buffer);
            	}else{
            		buffer = Unpooled.copiedBuffer(apiRes, CharsetUtil.UTF_8);
                	response.content().writeBytes(buffer);
            	}
            }
            
            //buffer.release();
            
            //Get Header
            System.out.println();
            System.out.println("---A Client Connected---");
            System.out.println("Accept:"+ req.headers().get(HttpHeaders.Names.ACCEPT));
            System.out.println("User-Agent:"+ req.headers().get(HttpHeaders.Names.USER_AGENT));
            //System.out.println("Host:"+ req.headers().get(HttpHeaders.Names.HOST));
            //System.out.println("Server:"+ req.headers().get(HttpHeaders.Names.SERVER));
            //System.out.println("POST:"+ req.getDecoderResult().toString());
            System.out.println("Geturi:"+ req.getUri());
            System.out.println("DNT:"+ req.headers().get("DNT"));
            //System.out.println("Method:"+ req.getMethod());
            //System.out.println("Version:"+ req.getProtocolVersion());
			
			System.out.println("Client IP:"+ clientIP);

			  
			
			String mime="text/html";
            //Set Header
			if(file!=null){
				String fileName=file.getName();
				if(fileName.endsWith(".ico"))
					mime="image/x-icon";
				if(fileName.endsWith(".txt"))
					mime="text/plain";
				if(fileName.endsWith(".css"))
					mime="text/css";
				if(fileName.endsWith(".gif"))
					mime="image/gif";
				if(fileName.endsWith(".js"))
					mime="application/x-javascript";
				if(fileName.endsWith(".jpg")||fileName.endsWith(".jpeg")||fileName.endsWith(".jpe"))
					mime="image/jpeg";
				if(fileName.endsWith(".png"))
					mime="image/png";
				if(fileName.endsWith(".svg"))
					mime="image/svg+xml";
				if(fileName.endsWith(".htc"))
					mime="text/x-component";
			}
			if(apiRes!=null)
				mime="text/plain";
            response.headers().set(CONTENT_TYPE, mime+"; charset=utf-8");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.SERVER, "QuestionReg / 1.0.0");
            
            if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                	if(!cookie.getName().equalsIgnoreCase("token"))
                		response.headers().add(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.encode(cookie));
                }
            }
            Cookie tokenCookie = new DefaultCookie("token", token);
            tokenCookie.setPath("/");
            response.headers().add(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.encode(tokenCookie));
            
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
