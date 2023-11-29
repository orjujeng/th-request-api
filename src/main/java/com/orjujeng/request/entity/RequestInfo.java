package com.orjujeng.request.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {
	private Integer id;
	private String accountNum;
	private Integer memberId;
	private String memberName;
	private String requestType;
	private Date requestDate;
	private String requestStatus;
}
