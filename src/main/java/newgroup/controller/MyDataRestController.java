package newgroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import newgroup.bean.MySampleBean;
import newgroup.entity.MyDataEntity;
import newgroup.services.MyDataService;

@RestController
public class MyDataRestController {

	@Autowired
	private MyDataService service;

	@Autowired
	MySampleBean bean;

	@RequestMapping("/rest")
	public List<MyDataEntity> restAll() {
		return service.getAll();
	}

	@RequestMapping("/rest/{num}")
	public MyDataEntity restBy(@PathVariable int num) {
		return service.getId(num);
	}

	@RequestMapping("/count")
	public int count() {
		return bean.count();
	}

}