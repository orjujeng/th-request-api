package com.orjujeng.request.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {
	private String emailAddress;
	private String nameZh;
	private Date startDate;
	private Date endDate;
	private Date exceptFinishDate;
	private String bankHolidayDate;
}
