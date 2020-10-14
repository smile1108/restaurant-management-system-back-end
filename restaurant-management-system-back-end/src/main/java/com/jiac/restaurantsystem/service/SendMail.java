package com.jiac.restaurantsystem.service;

/**
 * FileName: SendMail
 * Author: Jiac
 * Date: 2020/10/14 20:59
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 测试邮件发送功能
 */
@Service
public class SendMail {
    private final Logger logger = LoggerFactory.getLogger(SendMail.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送纯文本邮件
     * @param to            邮件接收方
     * @param subject       邮件主题
     * @param text          邮件内容
     */
    public void sendTextMail(String to,String subject,String text){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        try{
            javaMailSender.send(simpleMailMessage);
            logger.info("邮件已发送。");
        }catch (Exception e){
            logger.error("邮件发送失败。" + e.getMessage());
        }
    }

    /**
     * 发送带附件的邮件
     * @param to            邮件接收方
     * @param subject       邮件主题
     * @param text          邮件内容
     * @param path          附件所在的文件路径
     */
    public void sendAttachmentMail(String to,String subject,String text,String path){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            // 创建一个multipart格式的message
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text,true);
            // 添加附件信息
            FileSystemResource file = new FileSystemResource(new File(path));
            String fileName = path.substring(path.lastIndexOf(File.separator));
            messageHelper.addAttachment(fileName,file);
            // 发送带附件的邮件
            javaMailSender.send(mimeMessage);
            logger.info("邮件发送成功");
        }catch (Exception e){
            logger.error("带有附件的邮件发送失败。" + e.getMessage());
        }
    }
}
