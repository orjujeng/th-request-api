package com.orjujeng.request.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.orjujeng.request.entity.FinishInfo;
import com.orjujeng.request.entity.MailInfo;
import com.orjujeng.request.entity.RequestInfo;

@Mapper
public interface RequestInfoMapper {

	List<RequestInfo> checkRequestInfoStatus(RequestInfo requestInfo);

	void addRequestInfo(RequestInfo requestInfo);

	List<RequestInfo> getRequestInfoList(String status);

	void updateRequestInfo(@Param("requestId") String requestId, @Param("status") String status);

	List<MailInfo> getMailInfoBySeqId(String seqId);

	List<FinishInfo> getFinishInfoBySeqId(String seqId);

}
