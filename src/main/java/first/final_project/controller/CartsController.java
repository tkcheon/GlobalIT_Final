package first.final_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import first.final_project.dao.CartsMapper;
import first.final_project.vo.CartsVo;
import first.final_project.vo.MemberVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carts/")
public class CartsController {

	@Autowired
	CartsMapper carts_mapper;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpSession session;

	@Autowired
	ServletContext application;

	// /menu/list.do
	// /menu/list.do?page=2
	@RequestMapping("list.do")
	public String list(@RequestParam(name = "page", defaultValue = "1") int nowPage,
			Model model) {

		List<CartsVo> list = carts_mapper.selectList();
		System.out.println(list);

		// request binding
		model.addAttribute("list", list);

		return "carts/carts_list";
	}

	// 장바구니에 메뉴 추가
	// 요청 Parameter이름과 받는 변수명이 동일하면 @RequestParam(name="")의 name속성은 생략가능
	@RequestMapping("insert.do")
	public String insert(
			@RequestParam("menu_id") int menu_id,
			@RequestParam("shop_id") int shop_id,
			@RequestParam("carts_quantity") int carts_quantity) {

		MemberVo user = (MemberVo) session.getAttribute("user");
		int member_id = user.getMember_id();

		CartsVo existingItem = carts_mapper.findByMenuId(member_id, shop_id, menu_id);

		if (existingItem != null) {

			// 동일 메뉴가 장바구니에 존재하면 수량만 업데이트
			existingItem.setCarts_quantity(existingItem.getCarts_quantity() + carts_quantity);

			carts_mapper.update(existingItem.getCarts_id(), existingItem.getCarts_quantity());

		} else {
			CartsVo vo = new CartsVo();
			vo.setCarts_quantity(carts_quantity);

			vo.setMember_id(member_id);
			vo.setMenu_id(menu_id);
			vo.setShop_id(shop_id);

			carts_mapper.insert(vo);
		}

		return "redirect:list.do";
	}

	// 수정
	// /carts/modify.do?carts_id=9&carts_quantity=5
	@PostMapping("modify.do")
	public String modify(int carts_id, int carts_quantity, RedirectAttributes ra) {
		carts_mapper.update(carts_id, carts_quantity);

		return "redirect:list.do";
	}

	// 삭제
	// /carts/delete.do?carts_id=5
	// /carts/delete.do?carts_id=5&page=2
	@RequestMapping("delete.do")
	public String delete(int carts_id, RedirectAttributes ra) {

		// CartsVo 정보 얻어온다
		CartsVo vo = carts_mapper.selectOne(carts_id);

		carts_mapper.delete(carts_id);

		return "redirect:list.do";

	}
}