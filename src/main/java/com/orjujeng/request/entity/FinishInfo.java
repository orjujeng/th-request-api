package com.orjujeng.request.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinishInfo {
	private Integer id;
	private String emailAddress;
	private String nameZh;
	private Date startDate;
	private Date endDate;
	private String bankHolidayDate;
	private String bankHolidayDays;
	private String actWorkDays;  
	private String annualLeavingDays;
	private String sickLeavingDays;
	private String projectCode;
	private Integer proportion;
	
}
