package com.orjujeng.request.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.orjujeng.request.entity.FinishInfo;
import com.orjujeng.request.entity.MailInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
public class MailSendUtil {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	public void newThHtmlEmail(String subject, String content, MailInfo mailInfo) throws MessagingException {
		if (mailInfo.getEmailAddress() != null && !mailInfo.getEmailAddress().equals("")) {
			// 创建一个MINE消息
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
			// 谁发
			minehelper.setFrom("orjujeng@foxmail.com");
			// 谁要接收
			minehelper.setTo(mailInfo.getEmailAddress());
			// 邮件主题
			minehelper.setSubject(subject);
			// 邮件内容 true 表示带有附件或html
			Context ctx = new Context();
			// 给模板的参数的上下文
			ctx.setVariable("mailInfo", mailInfo);
			// 执行模板引擎，执行模板引擎需要传入模板名、上下文对象
			// Thymeleaf的默认配置期望所有HTML文件都放在 **resources/templates ** 目录下，以.html扩展名结尾。
			// String emailText = templateEngine.process("email/templates", ctx);
			String emailText = templateEngine.process(content, ctx);
			minehelper.setText(emailText, true);
			mailSender.send(message);
		} else {
			log.error("Mail Address Wrong");
		}
	}

	public void finishThHtmlEmail(String subject, String content, List<FinishInfo> finishInfoList) throws MessagingException {
		for (FinishInfo finishInfo : finishInfoList) {
			if (finishInfo.getEmailAddress() != null && !finishInfo.getEmailAddress().equals("")) {
				// 创建一个MINE消息
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
				// 谁发
				minehelper.setFrom("orjujeng@foxmail.com");
				// 谁要接收
				minehelper.setTo(finishInfo.getEmailAddress());
				// 邮件主题
				minehelper.setSubject(subject);
				// 邮件内容 true 表示带有附件或html
				Context ctx = new Context();
				// 给模板的参数的上下文
				ctx.setVariable("finishInfoList", finishInfoList);
				ctx.setVariable("finishInfo", finishInfo);
				// 执行模板引擎，执行模板引擎需要传入模板名、上下文对象
				// Thymeleaf的默认配置期望所有HTML文件都放在 **resources/templates ** 目录下，以.html扩展名结尾。
				// String emailText = templateEngine.process("email/templates", ctx);
				String emailText = templateEngine.process(content, ctx);
				minehelper.setText(emailText, true);
				mailSender.send(message);
			} else {
				log.error("Mail Address Wrong");
			}
		}
	}
}
