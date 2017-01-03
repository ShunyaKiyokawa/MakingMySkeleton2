//コントローラー。マッピングなどのアプリケーション制御を行う

package newgroup.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import newgroup.bean.MyDataBean;
import newgroup.entity.MyDataEntity;
import newgroup.repository.MyDataRepository;
import newgroup.services.MyDataService;


@Controller
public class MySkeletonController {
	//final String ROLE_ADMIN = "ROLE_ADMIN"; のちのち定数化した方が修正楽そう
	public enum Authority {ROLE_USER, ROLE_ADMIN};

	//MyDataEntityDaoImpl dao;

	//リレーショナルDBにしてからDAO動かなくなってますね。Service認証への切り替え
	@Autowired
	private MyDataService service;

	//Autowiredを使い、MyDataRepositoryフィールドにインスタンスを自動設定
	@Autowired
	MyDataRepository repository;

	@PersistenceContext
	EntityManager entityManager;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(
		@ModelAttribute("formModel") MyDataEntity mydata,
			ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg","this is sample content.");
		mav.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mav.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー
		mav.addObject("formModel",mydata);
		List<MyDataEntity> list = service.getAll();
		//Iterable<MyDataEntity> list = repository.findAll();
		//Iterable<MyDataEntity> list = dao.getAll();
		//Iterable<MyDataEntity> list = dao.findByAge(11, 100);
		//Iterable<MyDataEntity> list = repository.findByAge(11, 100);
		//list = repository.findAllOrderByUsername();
		//Relationにしてからdaoが動かない。repositoryは動く。原因究明中。persistanceContext?
		//MsgController側は動く
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel")
			@Validated MyDataEntity mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("index");
			mov.addObject("msg","sorry, error is occured...");
			Iterable<MyDataEntity> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		mov.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mov.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー

/*       // パスワードを暗号化する
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordHash = encoder.encode(password.getPasswordHash());
        // System.out.println("エンコード前:\t" + account.getPasswordHash());
        account.setPasswordHash(passwordHash);
        // System.out.println("エンコード後:\t" + account.getPasswordHash());
        accountService.saveAccount(account);*/

		return res;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(@ModelAttribute MyDataEntity mydata,
			@PathVariable int id,ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title","edit mydata.");
		MyDataEntity data = repository.findById((long)id);
		mav.addObject("formModel",data);
		mav.addObject("role_user","ROLE_USER");
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView update(@ModelAttribute MyDataEntity mydata,
			ModelAndView mav) {
		repository.saveAndFlush(mydata);
		mav.addObject("role_user","ROLE_USER");
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable int id,
			ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title","delete mydata.");
		MyDataEntity data = repository.findById((long)id);
		mav.addObject("formModel",data);
		return mav;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(@RequestParam long id,
			ModelAndView mav) {
		repository.delete(id);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ModelAndView find(ModelAndView mav) {
		mav.setViewName("find");
		mav.addObject("title","Find Page");
		mav.addObject("msg","MyDataのサンプルです。");
		mav.addObject("value","");
		List<MyDataEntity> list = service.getAll();
		//Iterable<MyDataEntity> list = repository.findAll();
		//Iterable<MyDataEntity> list = dao.getAll();
		//List<MyDataEntity> list = dao.getAll();
		//Relationにしてからdaoが動かない。repositoryは動く。原因究明中。
		//ImplがListでもIterableは並び替え必要なら使う
		mav.addObject("datalist", list);
		return mav;
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public ModelAndView search(HttpServletRequest request,
			ModelAndView mav) {
		mav.setViewName("find");
		String param = request.getParameter("fstr");
		if (param == ""){
			mav = new ModelAndView("redirect:/find");
		} else {
			mav.addObject("title","Find result");
			mav.addObject("msg","「" + param + "」の検索結果");
			mav.addObject("value",param);
			List<MyDataEntity> list = service.findUsername(param);
			//List<MyDataEntity> list = dao.find(param);
			//Iterableはコレクションなどの要素を順番に処理する場合に使用する
			mav.addObject("datalist", list);
		}
		return mav;
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	  public String login(Model model) {
	    return "security/login";
	  }

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView remove(	ModelAndView mav) {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/Ajax", method = RequestMethod.GET)
	public ModelAndView Ajax(
		@ModelAttribute("formModel") MyDataEntity mydata,
			ModelAndView mav) {
		mav.setViewName("Ajax");
		mav.addObject("msg","this is sample content.");
		mav.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mav.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー
		mav.addObject("formModel",mydata);
		List<MyDataEntity> list = service.getAll();
		//Iterable<MyDataEntity> list = repository.findAll();
		//Iterable<MyDataEntity> list = dao.getAll();
		//Iterable<MyDataEntity> list = dao.findByAge(11, 100);
		//Iterable<MyDataEntity> list = repository.findByAge(11, 100);
		//list = repository.findAllOrderByUsername();
		//Relationにしてからdaoが動かない。repositoryは動く。原因究明中。persistanceContext?
		//MsgController側は動く
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/Ajax", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView Ajax(
			@ModelAttribute("formModel")
			@Validated MyDataEntity mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("Ajax");
			mov.addObject("msg","sorry, error is occured...");
			Iterable<MyDataEntity> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		mov.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mov.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー

/*       // パスワードを暗号化する
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordHash = encoder.encode(password.getPasswordHash());
        // System.out.println("エンコード前:\t" + account.getPasswordHash());
        account.setPasswordHash(passwordHash);
        // System.out.println("エンコード後:\t" + account.getPasswordHash());
        accountService.saveAccount(account);*/

		return res;
	}

	@RequestMapping(value = "/BootStrap", method = RequestMethod.GET)
	public ModelAndView BootStrap(
		@ModelAttribute("formModel") MyDataEntity mydata,
			ModelAndView mav) {
		mav.setViewName("BootStrap");
		mav.addObject("msg","this is sample content.");
		mav.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mav.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー
		mav.addObject("formModel",mydata);
		List<MyDataEntity> list = service.getAll();
		//Iterable<MyDataEntity> list = repository.findAll();
		//Iterable<MyDataEntity> list = dao.getAll();
		//Iterable<MyDataEntity> list = dao.findByAge(11, 100);
		//Iterable<MyDataEntity> list = repository.findByAge(11, 100);
		//list = repository.findAllOrderByUsername();
		//Relationにしてからdaoが動かない。repositoryは動く。原因究明中。persistanceContext?
		//MsgController側は動く
		mav.addObject("datalist",list);
		return mav;
	}

	@RequestMapping(value = "/BootStrap", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView BootStrap(
			@ModelAttribute("formModel")
			@Validated MyDataEntity mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("BootStrap");
			mov.addObject("msg","sorry, error is occured...");
			Iterable<MyDataEntity> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		mov.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mov.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー

/*       // パスワードを暗号化する
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordHash = encoder.encode(password.getPasswordHash());
        // System.out.println("エンコード前:\t" + account.getPasswordHash());
        account.setPasswordHash(passwordHash);
        // System.out.println("エンコード後:\t" + account.getPasswordHash());
        accountService.saveAccount(account);*/

		return res;
	}

	@RequestMapping(value = "/BootStrapOriginal", method = RequestMethod.GET)
	  public String BootStrapOriginal(Model model) {
	    return "BootStrapOriginal";
	  }

	@RequestMapping(value = "/nosecurity", method = RequestMethod.GET)
	  public String nosecurity(Model model) {
	    return "security/nosecurity";
	  }
	@RequestMapping(value = "/IPAddress", method = RequestMethod.GET)
	  public String IPAddress(Model model) {
	    return "security/IPAddress";
	  }
//modelはtemplateのhtml情報がないのでそのままreturnできない。returnしたかったらModelAndViewを使う。
//ちなみにリターンで指定しているのはhtmlのファイル構造（templatesフォルダ下から）
	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
	public ModelAndView adminindex(
		@ModelAttribute("formModel") MyDataEntity mydata,
			ModelAndView mav) {
		mav.setViewName("adminsindex");
		mav.addObject("msg","this is sample content.");
		mav.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mav.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー
		mav.addObject("formModel",mydata);
		Iterable<MyDataEntity> list = repository.findAll();
		mav.addObject("datalist",list);
		return mav;
	}
	@RequestMapping(value = "/admin/index", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView adminform(
			@ModelAttribute("formModel")
			@Validated MyDataEntity mydata,
			BindingResult result,
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()){
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/");
		} else {
			mov.setViewName("adminsindex");
			mov.addObject("msg","sorry, error is occured...");
			Iterable<MyDataEntity> list = repository.findAll();
			mov.addObject("datalist",list);
			res = mov;
		}
		mov.addObject("role_admin","ROLE_ADMIN"); //管理者権限のロールを指定する
		mov.addObject("role_user","ROLE_USER"); //一般ユーザーロールを指定する。このページで作成するのは一般ユーザー
		return res;
	}

	@Autowired
	MyDataBean myDataBean;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView indexById(@PathVariable long id,
			ModelAndView mav) {
		mav.setViewName("pickup");
		mav.addObject("title","Pickup Page");
		String table = "<table>"
				+ myDataBean.getTableTagById(id)
				+ "</table>";
		//getTableTagByIdはMyDataBeanの中のクラスを呼び出し、dataに設定している
		mav.addObject("msg","pickup data id = " + id);
		mav.addObject("data",table);
		return mav;
	}

	//ページネーションのリクエストハンドラ。/{num}にページ番号を付ける
	@RequestMapping(value = "/page/{num}",
			method = RequestMethod.GET)
	public ModelAndView page(@PathVariable Integer num,
			ModelAndView mav) {
		//@PathVariableはパスで値が指定されるもの
		Page<MyDataEntity> page = service.getMyDataInPage(num);
		mav.setViewName("page");
	    mav.addObject("title","Find Page");
		mav.addObject("msg","MyDataのサンプルです。");
		mav.addObject("pagenumber", num);
		mav.addObject("datalist", page);
		//getmyDataInPageを呼び出しPageインスタンスを取り出す。
		//これをdatalistという名前で保管し、テンプレートで表示
	    return mav;
	}


/*	@PostConstruct
	//初期化（コンストラクト）を行う。mysqlに追加され続けて邪魔なので削除。メモリにデータを保存するタイプならコメントアウトははずすべき。ちなみにこれ使うと現在エラーが発生中
	public void init(){
		dao = new MyDataEntityDaoImpl(entityManager);
		MyDataEntity d1 = new MyDataEntity();
		d1.setUsername("oreore");
		d1.setAge(10);
		d1.setRole("ROLE_ADMIN");
		d1.setPassword("password");
		d1.setMail("ore@oreeore");
		d1.setMemo("090999999");
		repository.saveAndFlush(d1);

	}
*/

}