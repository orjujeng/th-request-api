package com.orjujeng.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orjujeng.request.entity.ChangePendingRequestStatusVo;
import com.orjujeng.request.entity.MemberInfo;
import com.orjujeng.request.service.RegisterService;
import com.orjujeng.request.utils.Result;

@RestController
@RequestMapping("/request")
public class RegisterController {
	
	@Autowired
	RegisterService registerService;
	@RequestMapping("/requestBeAuth")
	public Result requestBeAuth(@RequestBody MemberInfo memberInfo) {
		Result result = registerService.addNewRequest(memberInfo,"Timesheet Backend Auth");
		return result;
	}
	
	
	@GetMapping("/checkRequstInfo")
	public Result checkRequstInfo(@RequestParam("status") String status) {
		Result result = registerService.checkRequstInfo(status);
		return result;
	}
	
	@PostMapping("/changeRequestStatus")
	public Result changeRequestStatus(@RequestBody ChangePendingRequestStatusVo changePendingRequestStatusVo) {
		Result result = registerService.changeRequestStatus(changePendingRequestStatusVo);
		return result;
	}
}
