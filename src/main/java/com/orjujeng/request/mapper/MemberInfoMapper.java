package com.orjujeng.request.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberInfoMapper {

	void updateBdAuth(String memberId);

}
