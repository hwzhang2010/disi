package com.hywx.sirs.net;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;


/**
 * @program: drone
 * @description: 通过长度来获取需要解析的数据
 * @author: ld
 * @create: 2019-06-06 16:33
 */
public class MessagePacketByLengthDecoder extends LengthFieldBasedFrameDecoder
//public class MessagePacketByLengthDecoder extends MessageToMessageDecoder<ByteBuf>
{

    // 消息头长度
    //private static final int HEADER_SIZE = 32;
    // private DataMsgFactory dataMsgFactory = new DataMsgFactory();
    /**
     *
     * @param maxFrameLength  帧的最大长度
     * @param lengthFieldOffset length字段偏移的地址
     * @param lengthFieldLength length字段所占的字节长
     * @param lengthAdjustment 修改帧数据长度字段中定义的值，可以为负数 因为有时候我们习惯把头部记入长度,若为负数,则说明要推后多少个字段
     * @param initialBytesToStrip 解析时候跳过多少个长度
     * @param failFast 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异
     */
    public MessagePacketByLengthDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset,
                                        int lengthFieldLength, int lengthAdjustment,
                                        int initialBytesToStrip, boolean failFast)
    {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

//    @Override
//    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
//    {
//
//        //在这里调用父类的方法,实现指得到想要的部分（自己实现）,我在这里全部都要,也可以只要body部分
//        ByteBuf buf = (ByteBuf) super.decode(ctx,in);
//
//        if(buf == null)
//        {
//            return null;
//        }
//        //判断包整体长度是否正确
//        if(buf.readableBytes()<=HEADER_SIZE)
//        {
//            throw new Exception("字节数不足");
//        }
//
//        byte data[] = new byte[buf.readableBytes()];
//        buf.readBytes(data);
//
//        String proData1 = StringUtils.bytesToHexString(data);
//        System.out.println("Decoder 原始数据内容hex：" + proData1);
//
//
//        /*进行拆包*/
////        in.resetReaderIndex();
////
////        // 30个字节的标记位置
////        byte[] msgHeadMarkArray = new  byte[30];
////        in.readBytes(msgHeadMarkArray);
////
////        // 小端读取2个字节的消息体长度
////        short msgBodyLength = in.readShortLE();
////
////        int readableBytes = in.readableBytes();
////
////        if(readableBytes< msgBodyLength)
////        {
////            throw new Exception("标记的长度不符合实际长度");
////        }
////
////
////        // 把以上读到的数据拷贝到一个数组里，进行数据解析
////
////
////        in.resetReaderIndex();
////
////        ByteBuf buf = in.readBytes(HEADER_SIZE +msgBodyLength);
////        byte data[] = new byte[buf.readableBytes()];
////        buf.readBytes(data);
////
////        String proData1 = StringUtils.bytesToHexString(data);
////        System.out.println("Decoder 原始数据内容hex：" + proData1);
//
//
//        return data;
//
//
//        //client 端 ip
//        /*SocketAddress clientIp = ctx.channel().remoteAddress();
//        DecoderFileLog decoderFileLog = new DecoderFileLog(clientIp,proData1);
//        LOGGERTXT.info("Decoder 原始数据内容hex：" + decoderFileLog);
//        // 以上把原始数据读出来了，读标记已经增长了，实际读取数据时候，需要把读标记复位
//        // 读标记
//        in.resetReaderIndex();
//
//        // 11个字节的信息类别前数据
//        byte[] msgHeadMarkArray = new  byte[11];
//        in.readBytes(msgHeadMarkArray);
//
//        // 小端读取4个字节的消息类别
//        int bid = in.readIntLE();
//        String bidstr = ByteUtil.toHex(bid);
//
//        // 读取消息体长度前数据
//        byte[] msgHeadMarkArray1 = new  byte[15];
//        in.readBytes(msgHeadMarkArray1);
//
//        // 小端读取2个字节的消息体长度
//        short msgBodyLength = in.readShortLE();
//
//        if(in.readableBytes()!= msgBodyLength)
//        {
//            throw new Exception("标记的长度不符合实际长度");
//        }
//        //读取body
//        byte[] bytesBody = new byte[in.readableBytes()];
//        in.readBytes(bytesBody);
//
//        System.out.println("Decoder 进入解析数据内容hex：" + bidstr);
//
//        //这之间可以进行报文的解析处理
//        dataMsgFactory.sendMsgToKafka(bidstr,proData1,receiveData);*/
////        return  proData1;
//
//    }
}