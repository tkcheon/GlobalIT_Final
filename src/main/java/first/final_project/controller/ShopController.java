package first.final_project.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.net.SyslogOutputStream;
import first.final_project.dao.MenuMapper;
import first.final_project.service.AddrService;
import first.final_project.service.KakaoMapService;
import first.final_project.service.ShopService;
import first.final_project.vo.AddrVo;
import first.final_project.vo.MemberVo;
import first.final_project.vo.MenuVo;
import first.final_project.vo.OwnerVo;
import first.final_project.vo.ShopVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {

    @Autowired
    ShopService shop_Service;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpSession session;

    @Autowired
    ServletContext application;

    public BigDecimal formatRating(BigDecimal rating){
        return rating.setScale(1, RoundingMode.HALF_UP);
    }

    public BigDecimal formatRatingInt(BigDecimal rating){
        return rating.setScale(0, RoundingMode.FLOOR);
    }

    @Autowired
    KakaoMapService kakaoMapService;

    @Autowired
    AddrService addrService;

    // @GetMapping("/shops")


    @RequestMapping("/shop/list.do")
    public String shop_list(
        @RequestParam(name="food_category",required = false) String food_category,
        @RequestParam(name="order_addr", required = false) String order_addr, Model model, RedirectAttributes ra) {

        if(order_addr == null) { 
            ra.addAttribute("reason", "not find address");
			return "redirect:../main/display.do";
        }

        List<ShopVo> list;
        double radius = 3000;  // 3km 반경

        System.out.println("1 : " + order_addr);

        System.out.println(food_category);
        // 가게 리스트 필터링
        List<ShopVo> allShops = shop_Service.selectList(food_category);

        // 가게 영업유무 업데이트
        int res = shop_Service.updateStatus();
        
        list = new ArrayList<>();

        try {
            // 고객 주소의 좌표 가져오기
            double[] customerCoordinates = kakaoMapService.getCoordinates(order_addr);

            // 모든 가게에 대해 좌표 계산 후 반경 내 가게 필터링
            for (ShopVo shop : allShops) {
                System.out.println(shop.getShop_addr1());
                double[] shopCoordinates = kakaoMapService.getCoordinates(shop.getShop_addr1());
                double distance = kakaoMapService.calculateDistance(customerCoordinates[0], customerCoordinates[1],
                                                                    shopCoordinates[0], shopCoordinates[1]);
                if (distance <= radius) {
                    list.add(shop);
                    //System.out.println("등록완료");
                }
                if(shop.getShop_rating() !=null){
                    BigDecimal shop_rating = shop.getShop_rating().setScale(1, RoundingMode.HALF_UP);
                    shop.setShop_rating(shop_rating);
                }
            }
            model.addAttribute("list", list);
            model.addAttribute("order_addr", order_addr);
            model.addAttribute("food_category", food_category);
        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 전체 스택 트레이스를 출력
            System.out.println("예외 발생: " + e.getMessage());  // 구체적인 예외 메시지 출력
            model.addAttribute("error", "가게 정보를 불러오는 중 오류가 발생했습니다.");
        }
    
        return "shop/shop_list";
    }


    @RequestMapping("/shop/food_list.do")
    public String shop_food_list(
    @RequestParam(name="food_category", defaultValue = "all") String food_category,
    @RequestParam(name="order_addr", required = false) String order_addr, 
    @RequestParam(name="selectValue", required = false) String selectValue,
    @RequestParam(name="searchValue", required = false)String searchValue,
     Model model, RedirectAttributes ra) throws UnsupportedEncodingException {

 
        // System.out.println(selectValue);
        System.out.println(food_category);
        System.out.println(order_addr);

        if(order_addr == null) { 
            ra.addAttribute("reason", "not find address");
			return "redirect:../main/display.do";
        }

        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("food_category", food_category);
        if(selectValue!=null){
            selectMap.put("selectValue", selectValue); 
        }
        if(searchValue!=null){
            selectMap.put("searchValue", searchValue);
        }

        List<ShopVo> list;
        double radius = 3000;  // 10km 반경

        System.out.println("1 : " + order_addr);

        // 가게 리스트 필터링
        List<ShopVo> allShops = shop_Service.selectListValue(selectMap);
        // List<ShopVo> allShops = shop_Service.selectList(food_category);
        int res = shop_Service.updateStatus();
        
        list = new ArrayList<>();

        try {
            // 고객 주소의 좌표 가져오기
            double[] customerCoordinates = kakaoMapService.getCoordinates(order_addr);

            //System.out.println("2 : " + customerCoordinates[0] + " " + customerCoordinates[1]);

            // 모든 가게에 대해 좌표 계산 후 반경 내 가게 필터링
            for (ShopVo shop : allShops) {
                double[] shopCoordinates = kakaoMapService.getCoordinates(shop.getShop_addr1());
                double distance = kakaoMapService.calculateDistance(customerCoordinates[0], customerCoordinates[1],
                                                                    shopCoordinates[0], shopCoordinates[1]);
                if (distance <= radius) {
                    list.add(shop);
                    //System.out.println("등록완료");
                }
                if(shop.getShop_rating() !=null){
                    BigDecimal shop_rating = shop.getShop_rating().setScale(1, RoundingMode.HALF_UP);
                    shop.setShop_rating(shop_rating);
                }
                //System.out.println("3 : " + shopCoordinates[0] + " " + shopCoordinates[1]);
                //System.out.println("4 : " + distance);
            }
            //System.out.println("5 : " + list);
            //System.out.println("6 : " + list.size());
            model.addAttribute("list", list);
            model.addAttribute("order_addr", order_addr);
            model.addAttribute("food_category", food_category);
        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 전체 스택 트레이스를 출력
            System.out.println("예외 발생: " + e.getMessage());  // 구체적인 예외 메시지 출력
            model.addAttribute("error", "가게 정보를 불러오는 중 오류가 발생했습니다.");
        }
    
        return "shop/store_list";
    }

    // 메인화면
    // @RequestMapping("/shop/list2.do")
    // public String shop_list(String food_category,Model model, HttpSession session) {
    //     // 세션에서 고객의 정보(MemberVo) 가져오기
        
    //     List<ShopVo> list = shop_Service.selectList(food_category);
    //     System.out.println(food_category);

    //     for(ShopVo vo : list){
    //         if(vo.getShop_rating() !=null){
    //             BigDecimal shop_rating = vo.getShop_rating().setScale(1, RoundingMode.HALF_UP);
    //             vo.setShop_rating(shop_rating);
    //         }
    //     }
    //     model.addAttribute("list", list);
    //     return "shop/shop_list";


        // MemberVo member = (MemberVo) session.getAttribute("user");

        // List<ShopVo> list;

        // if (member == null) {
        //     // 로그인되어 있지 않을 때 기본 가게 리스트를 보여줌
        //     list = shop_Service.selectList(food_category);
        //     model.addAttribute("list", list);
        // }else{
        //     // 고객의 주소 정보를 DB에서 조회
        //     AddrVo customerAddr = addrService.getAddressByMemberId(member.getMember_id());

        //     if (customerAddr == null) {
        //         model.addAttribute("error", "등록된 주소가 없습니다.");
        //         return "redirect:/mypage";  // 마이페이지로 리다이렉트
        //     }

        //     // 고객의 전체 주소 (addr_line1 + addr_line2)
        //     String customerAddress = customerAddr.getAddr_line1() + " " + customerAddr.getAddr_line2();
        //     double radius = 10000;  // 10km 반경

        //     System.out.println("1 : " + customerAddress);

        //     // 가게 리스트 필터링
        //     List<ShopVo> allShops = shop_Service.selectList(food_category);
        //     list = new ArrayList<>();

        //     try {
        //         // 고객 주소의 좌표 가져오기
        //         double[] customerCoordinates = kakaoMapService.getCoordinates(customerAddress);

        //         //System.out.println("2 : " + customerCoordinates[0] + " " + customerCoordinates[1]);

        //         // 모든 가게에 대해 좌표 계산 후 반경 내 가게 필터링
        //         for (ShopVo shop : allShops) {
        //             double[] shopCoordinates = kakaoMapService.getCoordinates(shop.getShop_addr());
        //             double distance = kakaoMapService.calculateDistance(customerCoordinates[0], customerCoordinates[1],
        //                                                                 shopCoordinates[0], shopCoordinates[1]);
        //             if (distance <= radius) {
        //                 list.add(shop);
        //                 //System.out.println("등록완료");
        //             }
        //             if(shop.getShop_rating() !=null){
        //                 BigDecimal shop_rating = shop.getShop_rating().setScale(1, RoundingMode.HALF_UP);
        //                 shop.setShop_rating(shop_rating);
        //             }
        //             //System.out.println("3 : " + shopCoordinates[0] + " " + shopCoordinates[1]);
        //             //System.out.println("4 : " + distance);
        //         }
        //         //System.out.println("5 : " + list);
        //         //System.out.println("6 : " + list.size());
        //         model.addAttribute("list", list);

        //     } catch (Exception e) {
        //         e.printStackTrace();  // 예외 발생 시 전체 스택 트레이스를 출력
        //         System.out.println("예외 발생: " + e.getMessage());  // 구체적인 예외 메시지 출력
        //         model.addAttribute("error", "가게 정보를 불러오는 중 오류가 발생했습니다.");
        //     }
        // }
        // return "shop/shop_list";
    // }

    // 가게 등록 폼 이동
    @RequestMapping("/shop/insert_form.do")
    public String shop_insert_from() {
       
        return "shop/shop_insert_form";
    }

    // 가게 등록
    @RequestMapping("/shop/insert.do")
    public String shop_insert(ShopVo vo, RedirectAttributes ra, @RequestParam(name = "photo") MultipartFile photo)
            throws Exception {

        OwnerVo user = (OwnerVo) session.getAttribute("user");
        int owner_id = user.getOwner_id();
        System.out.println(owner_id);

        String shop_addr = vo.getShop_addr1() + " " + vo.getShop_addr2();
        vo.setShop_addr(shop_addr);
        System.err.println("vo.getShop_addr() : "  +  vo.getShop_addr());

        System.out.println("---shop_insert.do----");
        String shop_img = "no_file";

        String absPath = application.getRealPath("/resources/images/");

        // com.final_project.common.Util.createDirectoryIfNotExists(directoryPath);

        if (!photo.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            shop_img = uuid + "_" + photo.getOriginalFilename();
            File f = new File(absPath, shop_img);
            photo.transferTo(f);
        }

        vo.setShop_img(shop_img);
        vo.setOwner_id(owner_id);
        int res = shop_Service.insert(vo);

        return "redirect:modify_form.do";
    }

    // 가게 하나 선택
    @RequestMapping("/shop/select_one.do")
    public String shop_selectOne(int shop_id,
    @RequestParam(name="shop_status", required = false)String shop_status,
    Model model, RedirectAttributes ra) {
        System.out.println("shop_id : " + shop_id);
        
        MemberVo user = (MemberVo) session.getAttribute("user");
        if(user==null){
			ra.addAttribute("reason", "session_timeout");
			return "redirect:../login_form.do";
		};
        try {
            ShopVo vo = shop_Service.selectOne(shop_id);
            if(vo.getShop_rating()!=null){
                BigDecimal shop_rating = formatRating(vo.getShop_rating());
                BigDecimal shop_rate_decimal = formatRatingInt(vo.getShop_rating());
                int shop_rate = shop_rate_decimal.intValue();
                System.out.println(shop_rating);
                vo.setShop_rating(shop_rating);
                vo.setShop_rate(shop_rate);
                System.out.println(vo.getShop_rate());
            }
            
            // counts = shop_Service.selectMenuAndReviewsCount(shop_id);
    
            // Adding attributes to the model

          
            model.addAttribute("vo", vo);
            model.addAttribute("shop_status", shop_status);
          
            // if (vo != null) {
            //     vo.setMenu_count(counts != null ? counts.get("menu_count") : 0);
            //     vo.setReviews_count(counts != null ? counts.get("reviews_count") : 0);
            // }
            System.out.println("정상 실행 ");
            return "shop/shop_listOne";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "fail_select_one");
            return "error/error_page";
        }
    }

    @RequestMapping("/shop/select_info.do")
    public String shopSelectInfo(int shop_id ,Model model){
        ShopVo vo = shop_Service.selectOne(shop_id);
        model.addAttribute("vo", vo);
        return "shop/shop_info";
    }

    // 가게 정보 수정
    @RequestMapping("/shop/modify_form.do")
    public String shop_modify(Model model, RedirectAttributes ra) {

        try {
            OwnerVo user = (OwnerVo) session.getAttribute("user");
            if(user==null){
                ra.addAttribute("reason", "session_timeout");
                return "redirect:../login_form.do";
            }

            int owner_id = user.getOwner_id();
            System.out.println("owner_id : " + owner_id);

            ShopVo vo = shop_Service.select_modify_owner_id(owner_id);
            System.out.println("수행");
            // System.out.println(vo.getShop_id());
            model.addAttribute("vo", vo);

            // 가게 등록 여부 확인  
            Boolean result = shop_Service.hasShop(user.getOwner_id());
            System.out.println("result : " + result);
            Boolean hasShop = (result != null) ? result : false;
            System.out.println("hasShop : " + hasShop);
            model.addAttribute("hasShop", hasShop);
            if(hasShop == false){
                return "redirect:insert_form.do";
            }
        } catch (Exception e) {
            return "error/error_page";
        }
        return "shop/shop_modify_form";
    }

    // 가게 정보 수정 업데이트
    @RequestMapping("/shop/modify.do")
    public String shop_modify(ShopVo vo, @RequestParam MultipartFile photo, RedirectAttributes ra,
            Model model) {
        // 기존 이미지 불러오기
        String filename = shop_Service.selectOne(vo.getShop_id()).getShop_img();
        System.out.println("vo.getShop_img filename = " + filename);

        // 이미지 저장할 경로
        String absPath = application.getRealPath("/resources/images/");
        System.out.println(absPath);
        // 새로운 이미지 UUID 부여
        if (!photo.isEmpty()) {
            String shop_img = "no_file";
            UUID uuid = UUID.randomUUID();
            shop_img = uuid + "_" + photo.getOriginalFilename();

            // 기존 이미지 확인 및 삭제
            File oldFile = new File(absPath, filename);
            if (oldFile.exists()) {
                boolean result = oldFile.delete();
                if (result) {
                    System.out.println("파일이 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("파일 삭제에 실패했습니다.");
                }
            } else {
                System.out.println("파일이 존재하지 않습니다.");
            }
            System.out.println("이미지 삭제 완료");
            // if (!result) {
            // model.addAttribute("errorMessage", "Failed to delete the old image");
            // return "error/error_page";
            // }

            System.out.println(shop_img);
            // 새로운 이미지 파일 저장
            vo.setShop_img(shop_img);
            File f = new File(absPath, shop_img);

            try {
                photo.transferTo(f);
                System.out.println("이미지 저장 완료");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("변경할 이미지가 없습니다. ");
        try {
            System.out.println("업데이트전");

            if(vo.getShop_img()==null){
                vo.setShop_img(filename);
                System.out.println(vo.getShop_img());
            }
            
            int res = shop_Service.update(vo);
            res = shop_Service.updateStatus();
            System.out.println("가게수정 완료");
            ra.addAttribute("reason", "shop_modify_success");
            return "redirect:modify_form.do";
            // shop_id = vo.getShop_id();
        } catch (Exception e) {
            return "error/error_page";
        }
        // return "redirect:modify_form.do?shop_id=" + shop_id;

    }

    // 가게 삭제
    @RequestMapping("/shop/delete.do")
    @ResponseBody
    public String shop_delete(int shop_id, RedirectAttributes ra, Model model) {
        try {
            @SuppressWarnings("unused")
            int res = shop_Service.delete(shop_id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "fail_select_one");
            return "error/error_page";
        }
        return "redirect:shoplist.do";
    }

}