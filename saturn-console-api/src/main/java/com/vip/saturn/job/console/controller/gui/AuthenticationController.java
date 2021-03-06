/**
 * Copyright 2016 vip.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * </p>
 **/

package com.vip.saturn.job.console.controller.gui;

import com.vip.saturn.job.console.controller.SuccessResponseEntity;
import com.vip.saturn.job.console.domain.RequestResult;
import com.vip.saturn.job.console.exception.SaturnJobConsoleException;
import com.vip.saturn.job.console.mybatis.entity.User;
import com.vip.saturn.job.console.service.AuthenticationService;
import com.vip.saturn.job.console.utils.SaturnConsoleUtils;
import com.vip.saturn.job.console.utils.SessionAttributeKeys;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/console/authentication")
public class AuthenticationController extends AbstractGUIController {

	private static final Logger AUDIT_LOGGER = SaturnConsoleUtils.getAuditLogger();

	@Resource
	private AuthenticationService authenticationService;

	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success/Fail", response = RequestResult.class)})
	@RequestMapping(value = "/login", method = {RequestMethod.POST})
	public SuccessResponseEntity login(@RequestParam String username, @RequestParam String password,
			HttpServletRequest request) throws SaturnJobConsoleException {

		User user = authenticationService.authenticate(username, password);

		request.getSession().setAttribute(SessionAttributeKeys.LOGIN_USER_NAME, user.getUserName());
		request.getSession().setAttribute(SessionAttributeKeys.LOGIN_USER_REAL_NAME, user.getRealName());

		AUDIT_LOGGER
				.info("{}({}) was login where ip={} ", user.getUserName(), user.getRealName(), request.getRemoteAddr());

		return new SuccessResponseEntity();
	}

	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success/Fail", response = RequestResult.class)})
	@RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
	public SuccessResponseEntity logout(HttpServletRequest request) {
		AUDIT_LOGGER.info("{}({}) logout, ip {}. ", getCurrentLoginUserName(), getCurrentLoginUserRealName(),
				request.getRemoteAddr());
		request.getSession().invalidate();
		return new SuccessResponseEntity();
	}
}
