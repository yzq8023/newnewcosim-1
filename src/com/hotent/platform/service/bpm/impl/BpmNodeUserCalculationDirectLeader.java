package com.hotent.platform.service.bpm.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.hotent.core.model.TaskExecutor;
import com.hotent.platform.auth.ISysUser;
import com.hotent.platform.model.bpm.BpmNodeUser;
import com.hotent.platform.service.bpm.IBpmNodeUserCalculation;
import com.hotent.platform.service.system.SysUserOrgService;

/**
 * 根据节点用户设置为“发起人的直属领导(组织)”，计算执行人员。
 * 
 * @author Raise
 */
public class BpmNodeUserCalculationDirectLeader implements
		IBpmNodeUserCalculation {
	@Resource
	private SysUserOrgService sysUserOrgService;

	@Override
	public String getTitle() {
		return "发起人的直属领导(组织)";
	}

	@Override
	public Set<TaskExecutor> getExecutor(BpmNodeUser bpmNodeUser, CalcVars vars) {
		Set<TaskExecutor> uIdSet = new HashSet<TaskExecutor>();
		Long startUserId = vars.startUserId;
		List<ISysUser> sysUsers = sysUserOrgService
				.getLeaderUserByUserId(startUserId);
		for (ISysUser sysUser : sysUsers) {
			uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),
					sysUser.getFullname()));
		}
		return uIdSet;
	}
}
