package de.form4.alfresco.manualsynctrigger_repo.quartz;

/*
 * #%L
 * Trigger Sync Button - Repository extension
 * %%
 * Copyright (C) 2015 form4 GmbH & Co. KG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.site.SiteService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * Webscript interface to start a quartz jobbean peoplesync manually.
 *
 * @author michael.mrotzek
 */
public class StartPeopleSyncWebScript extends AbstractWebScript implements ApplicationContextAware {

	private final static Logger log = Logger.getLogger(StartPeopleSyncWebScript.class);
	private final static String PARAM_SITE_ID = "siteId";

	JobDetailBean jobdetailbean;
	AuthenticationService authenticationService;
	AuthorityService authorityService;
	SiteService siteService;

	@Override
	public void execute(final WebScriptRequest request, final WebScriptResponse response) throws IOException {

		final Writer w = response.getWriter();
		String siteId = request.getParameter(PARAM_SITE_ID);
		String role = siteService.getMembersRole(siteId, authenticationService.getCurrentUserName());
		if (authorityService.hasAdminAuthority() || SiteModel.SITE_MANAGER.equals(role)) {

			log.debug("Starting job manually. jobdetailbean=" + jobdetailbean);
			SchedulerFactory schedFact = new StdSchedulerFactory();

			// Create a trigger that fires exactly once, ten seconds from now
			final long startTime = System.currentTimeMillis() + 1000L;
			final SimpleTrigger trigger = new SimpleTrigger("manualTrigger", null, new Date(startTime), null, 0, 0L);

			try {
				final Scheduler s = schedFact.getScheduler();
				s.addJobListener(new ManualJobListener());
				log.debug("trigger: " + trigger + ", scheduler: " + s);

				s.scheduleJob(jobdetailbean, trigger);
				s.start();

				String msgStart = "LDAP Sync triggered at " + new Date(startTime);
				log.info(msgStart);
				w.write(msgStart);

			} catch (final Exception e) {
				log.error("Error while executing " + jobdetailbean + " manually");
				throw new AlfrescoRuntimeException("Error while executing " + jobdetailbean + " manually", e);
			}

		} else {
			String msg = "Only Alfresco Administrators or SiteManagers can trigger ldpa sync action manually.";
			log.error(msg);
			throw new AlfrescoRuntimeException(msg);
		}
	}

	@Override
	public void setApplicationContext(final ApplicationContext arg0) throws BeansException {
	}

	public void setJobdetailbean(JobDetailBean jobdetailbean) {
		this.jobdetailbean = jobdetailbean;
	}

	public class ManualJobListener extends JobListenerSupport {

		@Override
		public String getName() {
			return "Manual LDAP People Sync";
		}

		@Override
		public void jobExecutionVetoed(final JobExecutionContext arg0) {
			log.info("jobExecutionVetoed: " + arg0.getJobInstance());
		}

		@Override
		public void jobToBeExecuted(final JobExecutionContext arg0) {
			log.info("jobToBeExecuted: " + arg0.getJobInstance());
		}

		@Override
		public void jobWasExecuted(final JobExecutionContext arg0, final JobExecutionException arg1) {
			log.info("jobWasExecuted: " + arg0.getJobInstance());
			log.info("exception?", arg1);
		}
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

}
