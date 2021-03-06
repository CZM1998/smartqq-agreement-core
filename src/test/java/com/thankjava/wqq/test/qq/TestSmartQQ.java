package com.thankjava.wqq.test.qq;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.thankjava.wqq.extend.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thankjava.wqq.SmartQQClient;
import com.thankjava.wqq.WQQClient;
import com.thankjava.wqq.extend.CallBackListener;

@Deprecated
public class TestSmartQQ {

    private static final Logger logger = LoggerFactory.getLogger(TestSmartQQ.class);

    // 初始化SmartQQClient
    // 需要指明一个NotifyListener 该接口的实例会在 SmartQQClient 拉取到信息时被执行调用
    static final SmartQQClient SMART_QQ_CLIENT = new WQQClient(new MessageHandler());


    public static void main(String[] args) {

        logger.debug("SmartQQ登录开始");

        // 执行登录
        SMART_QQ_CLIENT.login(true, new CallBackListener() {
            // login 接口在得到登录二维码时会调用CallBackListener
            // 并且二维码byte[] 数据会通过ListenerAction.data返回
            @Override
            public void onListener(ActionListener actionListener) {

                try {
                    // 将返回的byte[]数据io处理成一张png图片
                    // 位于项目log/qrcode.png
                    ImageIO.write((BufferedImage) actionListener.getData(), "png", new File("./log/qrcode.png"));
                    logger.debug("获取登录二维码完成,手机QQ扫描 ./log/qrcode.png 位置的二维码图片");
                } catch (Exception e) {
                    logger.error("将byte[]写为图片失败", e);
                }

            }
        }, new CallBackListener() {

            // 然后通过手机QQ扫描登录二维码,允许登录后smartqq-agreement-core工具就正常接收信息了
            // 可以通过SmartQQClient.sendMsg向讨论组或者好友或者群组发送信息
            // smartqq-agreement-core工具在得到好友|讨论组|群组信息后就会调用上面提到的NotifyListener.handler
            // 自此你自需要拓展自己的回复消息的内容,就可以自定义自己的QQ机器人或者组件服务拉
            // 登录完毕后会返回LoginResult 已反馈当前登录结果
            @Override
            public void onListener(ActionListener actionListener) {
                // 登陆成功
                logger.debug("登录结果: " + actionListener.getData());
            }
        });


    }
}
