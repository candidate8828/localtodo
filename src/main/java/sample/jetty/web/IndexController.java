/*
 * Copyright 2012-2016 the original author or authors.
 *
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
 */

package sample.jetty.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sample.jetty.service.WorklogService;

@Controller
public class IndexController {

	@Autowired
	private WorklogService helloWorldService;

	@RequestMapping("/")
	public ModelAndView toIndex(HttpServletRequest request, ModelMap model) {
		return new ModelAndView("index", model);
	}
	
	@RequestMapping("/helloWorld")
	public String helloWorld(@RequestParam(value="name", required=false, defaultValue="World") String name, HttpServletRequest request, Model model) {
		//System.out.println(request.getClass());
		model.addAttribute("name", name);
		model.addAttribute("hellomessage", this.helloWorldService.getHelloMessage());
		return "greeting";
	}
	
	@RequestMapping("/doUpadte")
	public String doUpadte(@RequestParam(value="name", required=false, defaultValue="World") String name, HttpServletRequest request, Model model) {
		model.addAttribute("name", name);
		model.addAttribute("hellomessage", this.helloWorldService.updateCityById(1L));
		return "index";
	}
	
	@RequestMapping("/testPage")
	public ModelAndView testPage(HttpServletRequest request, ModelMap model) {
		String contextPath = request.getContextPath();
		model.put("contextPath", contextPath);
		model.addAttribute("name", "yangsong");
		return new ModelAndView("testpage/testpage", model);
	}
	
}