package newgroup.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import newgroup.dao.MsgDataDaoImpl;
import newgroup.entity.MsgDataEntity;
import newgroup.repository.MsgDataRepository;

@Controller
public class MsgDataController {

	@Autowired
	MsgDataRepository repository;

	MsgDataDaoImpl dao;

	@PersistenceContext
	EntityManager entityManager;



	@RequestMapping(value = "/msg", method = RequestMethod.GET)
	public ModelAndView msg(ModelAndView mav) {
		mav.setViewName("showMsgData");
		mav.addObject("title","Sample");
		mav.addObject("msg","MsgDataのサンプルです。");
		MsgDataEntity msgdata = new MsgDataEntity();
		mav.addObject("formModel", msgdata);
		List<MsgDataEntity> list = (List<MsgDataEntity>)dao.getAll();
		mav.addObject("datalist", list);
		return mav;
	}

	@RequestMapping(value = "/msg", method = RequestMethod.POST)
	public ModelAndView msgform(
			@Valid @ModelAttribute MsgDataEntity msgdata,
			Errors result,
			ModelAndView mav) {
		if (result.hasErrors()) {
			mav.setViewName("showMsgData");
			mav.addObject("title", "Sample [ERROR]");
			mav.addObject("msg", "値を再チェックしてください!");
			return mav;
		} else {
			repository.save(msgdata);
			return new ModelAndView("redirect:/msg");
		}
	}

	@PostConstruct
	public void init(){
		System.out.println("ok");
		dao = new MsgDataDaoImpl(entityManager);
	}

}
