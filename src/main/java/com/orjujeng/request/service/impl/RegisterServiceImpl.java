package com.orjujeng.request.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orjujeng.request.entity.ChangePendingRequestStatusVo;
import com.orjujeng.request.entity.FinishInfo;
import com.orjujeng.request.entity.MailInfo;
import com.orjujeng.request.entity.MemberInfo;
import com.orjujeng.request.entity.RequestInfo;
import com.orjujeng.request.mapper.MemberInfoMapper;
import com.orjujeng.request.mapper.RequestInfoMapper;
import com.orjujeng.request.service.RegisterService;
import com.orjujeng.request.utils.MailSendUtil;
import com.orjujeng.request.utils.Result;
import com.orjujeng.request.utils.ResultCode;
import com.rabbitmq.client.Channel;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	RequestInfoMapper requestInfoMapper;
	@Autowired
	MemberInfoMapper memberInfoMapper;
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	MailSendUtil mailSendUtil;

	@Override
	@Transactional
	public Result addNewRequest(MemberInfo memberInfo, String requestType) {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setAccountNum(memberInfo.getAccountNum());
		requestInfo.setMemberId(memberInfo.getId());
		requestInfo.setMemberName(memberInfo.getNameZh());
		requestInfo.setRequestType(requestType);
		requestInfo.setRequestStatus("P");
		List<RequestInfo> checkRequestInfoStatusList = requestInfoMapper.checkRequestInfoStatus(requestInfo);
		if (checkRequestInfoStatusList.size() > 0) {
			return Result.error(ResultCode.FAIL.code, "You Have a Pending Request", null);
		}
		requestInfoMapper.addRequestInfo(requestInfo);
		return Result.success(null);
	}

	@Override
	public Result checkRequstInfo(String status) {
		List<RequestInfo> result = requestInfoMapper.getRequestInfoList(status);
		return Result.success(result);
	}

	@Override
	@Transactional
	public Result changeRequestStatus(ChangePendingRequestStatusVo changePendingRequestStatusVo) {
		if (changePendingRequestStatusVo.getStatus().equals("D")) {
			requestInfoMapper.updateRequestInfo(changePendingRequestStatusVo.getRequestId(), "D");
		} else if (changePendingRequestStatusVo.getStatus().equals("A")
				&& changePendingRequestStatusVo.getType().equals("Timesheet Backend Auth")) {
			requestInfoMapper.updateRequestInfo(changePendingRequestStatusVo.getRequestId(), "A");
			memberInfoMapper.updateBdAuth(changePendingRequestStatusVo.getMemberId());
		} else {
			return Result.error(ResultCode.FAIL.code, "Request Info With Error", null);
		}
		return Result.success(null);
	}

	@RabbitListener(queues = "newTimesheetQueue")
	public void reciveNewTimesheetAndBookMailQueue(Message message, String context, Channel channel) {
		try {
			String seqId = context;
			List<MailInfo> mailInfoList = requestInfoMapper.getMailInfoBySeqId(seqId);
			for (MailInfo mailInfo : mailInfoList) {
				rabbitTemplate.convertAndSend("newTimesheetSendMailExchange", "newTimesheetSendMail", mailInfo);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@RabbitListener(queues = "newTimesheetSendMailQueue")
	public void handleNewTimesheetMail(Message message, MailInfo context, Channel channel)  {
		try {
			try {
				
				mailSendUtil.newThHtmlEmail("Notification - New TimeSheet Need You Submit", "newTh", context);
			}catch(MessagingException e) {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
				log.error(e.getMessage(), e);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@RabbitListener(queues = "finishTimesheetQueue")
	public void recivefinishTimesheetQueueAndBookMailQueue(Message message, String context, Channel channel) {
		try {
			String seqId = context;
			List<FinishInfo> finishInfoList = requestInfoMapper.getFinishInfoBySeqId(seqId);
			
			rabbitTemplate.convertAndSend("finishTimesheetSendMailExchange", "finishTimesheetSendMail", finishInfoList);
			
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@RabbitListener(queues = "finishTimesheetSendMailQueue")
	public void handleFinishTimesheetMail(Message message, List<FinishInfo> context, Channel channel)  {
		try {
			try {
				mailSendUtil.finishThHtmlEmail("Notification - TimeSheet Has Been Complated", "finishTh", context);
			}catch(MessagingException e) {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
				log.error(e.getMessage(), e);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
