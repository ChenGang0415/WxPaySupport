package com.lalameow.wxpaysupport.network;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.config.MainConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/18
 * 时间: 15:37
 * 功能：请进行修改
 */
public class HttpServer {

    private int PORT = 8080;
    private int BUFFER_SIZE = 1024;
    private String CHARSET = "utf-8"; //默认编码
    private CharsetDecoder decoder; //解码

    private int port;
    private ServerSocketChannel channel;
    private Selector selector;
    private final ByteBuffer buffer;
    private byte[] img;
    private int stat=0;//非0为停止
    public static HttpServer instans=new HttpServer(MainConfig.httpserverport);
    private HttpServer(int port)  {
        this.port = port;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.decoder = Charset.forName(CHARSET).newDecoder();
    }
    public static HttpServer getInstans(){
        return instans;
    }
    /**
     * 单线程服务，通过单一个线程同时为多路复用IO流服务
     * 1、此方式适合：IO密集型的操作：如代理服务.
     * 2、相信大家写过：使用socket的聊天程序:
     * 即accept()一个socket后，new一个Thread为该socket服务，
     * 此方式适合：CPU密集型的操作，如需要处理大量业务、计算
     *
     * @throws IOException
     */
    public void listen(byte[] img) throws IOException {
        this.selector = Selector.open();//打开选择器
        this.img=img;
        //打开一个服务通道
        this.channel = ServerSocketChannel.open();
        //绑定服务端口
        ServerSocket socket = channel.socket();
        InetSocketAddress address= new InetSocketAddress(port);
        socket.bind(address);
        //使用非阻塞模式，使用多道io操作
        channel.configureBlocking(false);
        stat=0;
         WxPaySupport.plugin.getLogger().info("二维码已经生成，请打开浏览器输入地址：[http://"+address.getHostString()+":"+address.getPort()+"]查看");
        while (stat==0) {
            //非阻塞，没有连接，立即返回null，与socket.accept()方法(阻塞)不同，
            SocketChannel client = channel.accept();
            if (client != null) {
                registerClient(client);
            }
            service();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stoplisten(){
        WxPaySupport.plugin.getLogger().info("扫码成功，关闭WEB服务器");
        stat=1;

    }

    /**
     * 将客户端channel注册到selector上
     * 四个事件：Connect、Accept、Read、Write
     *
     * @param client
     * @throws IOException
     */
    private void registerClient(SocketChannel client) throws IOException {
        //设置非阻塞
        client.configureBlocking(false);
        //将客户端channel注册到selector上
        client.register(selector, SelectionKey.OP_READ);

    }


    /**
     * 遍历各客户端通道
     * select()阻塞到至少有一个通道在你注册的事件上就绪了
     * select(long timeout) 多设置一个阻塞时间(毫秒)
     * selectNow() 不阻塞，有无都返回。
     */
    private void service() throws IOException {
        if (selector.selectNow() > 0) {
            //客户端channel的键集合
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                SocketChannel client = (SocketChannel) key.channel();
                if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                }
            }

        }
    }


    //读信息
    private void read(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        int c = client.read(buffer);
        if (c > 0) {
            //flip方法将Buffer从写模式切换到读模式
            buffer.flip();
            CharBuffer charBuffer = decoder.decode(buffer);
            //接收请求
            if(img!=null){
                String qrimg="data:image/png;base64,"+Base64.getEncoder().encodeToString(img);
                key.attach("<div><img src='"+qrimg+"'/></div>");
            }else {
                key.attach("二维码生成错误，请检查配置");
            }

            // 改变自身关注事件，可以用位或操作|组合时间
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } else {
            client.close();
        }
        buffer.clear();
    }

    //响应信息
    private void write(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        String handle = (String) key.attachment();//取出read方法传递的信息。
        String res = Response.getMsg();
        if (handle != null) {
            res = res + "\r\n" + handle;
        }
        ByteBuffer block = ByteBuffer.wrap(res.getBytes());
        client.write(block);
        client.close();
        // 改变自身关注事件，可以用位或操作|组合时间
        //key.interestOps(SelectionKey.OP_READ);
    }


}
class Response {

    public static String getMsg() {
        return "HTTP/1.1 200 OK" + "\r\n";
    }
}