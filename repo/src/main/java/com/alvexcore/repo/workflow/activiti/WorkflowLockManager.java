/**
 * Copyright © 2014 ITD Systems
 *
 * This file is part of Alvex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alvexcore.repo.workflow.activiti;

import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.VariableScope;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.repo.workflow.activiti.ActivitiScriptNode;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.lock.LockService;
import org.alfresco.service.cmr.lock.LockStatus;
import org.alfresco.service.cmr.lock.LockType;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.beans.factory.InitializingBean;

class LockFilesWork implements RunAsWork<Void> {

	private NodeRef pkg;
	private String action;
	private ServiceRegistry serviceRegistry;

	public LockFilesWork(ServiceRegistry serviceRegistry, NodeRef pkg, String action) {
		this.action = action;
		this.serviceRegistry = serviceRegistry;
		this.pkg = pkg;
	}

	@Override
	public Void doWork() throws Exception {
		String systemUserName = AuthenticationUtil.getSystemUserName();
		String originalFullyAuthenticatedUserName = AuthenticationUtil.getFullyAuthenticatedUser();
		try {
			// TODO: should we just set uid or save and restore Authentication somehow?
			if(!systemUserName.equals(originalFullyAuthenticatedUserName))
				AuthenticationUtil.setFullyAuthenticatedUser(systemUserName);
			List<ChildAssociationRef> assocs = serviceRegistry.getNodeService()
					.getChildAssocs(pkg);
			LockService lockService = serviceRegistry.getLockService();
			for (ChildAssociationRef assoc : assocs) {
				NodeRef document = assoc.getChildRef();
				// TODO
				// We need additional info for lock - id of the process that holds the lock.
				// Otherwise we can hack the system using small one-man workflow just to operate locks.
				// Additional info should prevent it (yes, we are to check process owner manually,
				// but it's better than nothing).
				// However, Alfresco allows additional info for Lifetime.EPHEMERAL locks only.
				// We are not happy with it, since we need our locks to survive server reboots
				if ("LOCK".equals(action)) {
					LockStatus lockStatus = lockService.getLockStatus(document);
					if (!LockStatus.LOCKED.equals(lockStatus)
							&& !LockStatus.LOCK_OWNER.equals(lockStatus)) {
						lockService.lock(document, LockType.NODE_LOCK);
					}
				} else if ("UNLOCK".equals(action)) {
					lockService.unlock(document);
				}
			}
		} finally {
			if(!systemUserName.equals(originalFullyAuthenticatedUserName))
				AuthenticationUtil.setFullyAuthenticatedUser(originalFullyAuthenticatedUserName);
		}
		return null;
	}
}

public class WorkflowLockManager extends AlvexActivitiListener implements
		TaskListener, InitializingBean, ExecutionListener {

	private static final String PACKAGE_VARIABLE = "bpm_package";
	private String action = "LOCK";
	
	public void setAction(String _action) {
		action = _action.toUpperCase();
	}
	
	public void lockFiles(VariableScope scope) {
		// get reference to workflow package
		NodeRef pkg = ((ActivitiScriptNode) scope.getVariable(PACKAGE_VARIABLE)).getNodeRef();
		// run work to set permissions on all documents in package
		RunAsWork<Void> work = new LockFilesWork(serviceRegistry, pkg, action);
		AuthenticationUtil.runAsSystem(work);
	}
	
	@Override
	public void notify(DelegateTask delegateTask) {
		lockFiles(delegateTask);
	}
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		lockFiles(execution);
	}
}
