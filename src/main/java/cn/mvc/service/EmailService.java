package cn.mvc.service;

import cn.mvc.pojo.Email;

import java.util.List;

public interface EmailService {
    String Send(String[] SendTo, String MessageTitle, String MessageBody, String fileName);
    List<Email> GetSendToList(int taskId);

}
