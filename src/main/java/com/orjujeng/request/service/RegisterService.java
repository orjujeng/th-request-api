package com.orjujeng.request.service;

import com.orjujeng.request.entity.ChangePendingRequestStatusVo;
import com.orjujeng.request.entity.MemberInfo;
import com.orjujeng.request.utils.Result;

public interface RegisterService {

	Result addNewRequest(MemberInfo memberInfo, String requestType);

	Result checkRequstInfo(String status);

	Result changeRequestStatus(ChangePendingRequestStatusVo changePendingRequestStatusVo);

}
