package cn.mvc.service.impl;

import cn.mvc.dao.EmailDao;
import cn.mvc.pojo.Email;
import cn.mvc.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailDao emailDao;


    @Override
    public String Send(String[] SendTo, String MessageTitle, String MessageBody, String fileName) {
        MimeMessage mMessage = javaMailSender.createMimeMessage();//创建邮件对象
        MimeMessageHelper mMessageHelper;
        try {
            mMessageHelper = new MimeMessageHelper(mMessage, true);
            mMessageHelper.setFrom("php_mis@phihong.com.tw");//发件人邮箱
            mMessageHelper.setTo(SendTo);//收件人邮箱
            //mMessageHelper.setCc("");//抄送人
            mMessageHelper.setSubject(MessageTitle);//邮件的主题
            mMessageHelper.setText(MessageBody, true);//邮件的文本内容，true表示文本以html格式打开
            File file = new File(fileName);
            mMessageHelper.addAttachment(fileName.substring(fileName.lastIndexOf("/") + 1), file);//在邮件中添加一个附件
            javaMailSender.send(mMessage);//发送邮件
        } catch (MessagingException e) {
            e.printStackTrace();
            return "发送失败";
        }
        return "发送成功";
    }

    @Override
    public List<Email> GetSendToList(int taskId) {
        return emailDao.queryDataByTask(taskId);
    }


}
