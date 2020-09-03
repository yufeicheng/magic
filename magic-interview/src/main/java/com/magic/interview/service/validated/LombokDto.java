package com.magic.interview.service.validated;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.acl.Group;
import java.util.Date;
import java.util.List;

/**
 * //@NonNull  @CleanUp  @Synchronized
 *
 * @author Cheng Yufei
 * @create 2019-11-05 10:32
 **/
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
//自定义时间校验
@MyCheck(startTime = "beginTime", endTime = "overTime", message = "结束时间不能早于开始时间")
public class LombokDto implements Serializable {


	private static final long serialVersionUID = -5442835454091291491L;

	public LombokDto() {
	}

	//set属性时会做null的校验
	@NotNull(groups = GroupA.class, message = "cid不能为空")
	Integer cid;
	@Size(min = 2, max = 10, groups = GroupA.class)
	String name;

	String location;

	/**
	 * 使用jackjson时
	 */
    /*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date time;*/

	/**
	 * 使用fastjson时
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	Date time;

	String beginTime;

	String overTime;

	/**
	 * 嵌套校验, 会对集合中的每个元素进行校验
	 * http 发送数据时 Job须时静态内部类，否则无法接受数据
	 */
	@Valid
	List<Job> job;

	@Data
	public static class Job implements Serializable {


		private static final long serialVersionUID = 6280731005984075425L;

		public Job() {
		}

		@Range(min = 2, max = 5,groups = GroupA.class)
		@Digits(integer = 1, fraction = 0,groups = GroupA.class)
		private BigDecimal jobId;

		@Size(min = 2, max = 10,groups = GroupA.class)
		private String jobName;
	}


	/**
	 * 校验分组
	 */
	public interface GroupA {
	}
}
