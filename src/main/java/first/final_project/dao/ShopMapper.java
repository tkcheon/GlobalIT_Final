package first.final_project.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import first.final_project.vo.ShopVo;

@Mapper
public interface ShopMapper {

    // 가게 전체 출력
    List<ShopVo> selectListAll();

    // 가게 카테고리별 출력
    List<ShopVo> selectList(String food_category);

     // 가게 정렬 
    List<ShopVo> selectListValue(Map selectMap);

    // 가게 추가
    int insert(ShopVo vo);

    // 가게 상세
    ShopVo selectOne(int shop_id);

    // 가게 정보 수정
    ShopVo select_modify_owner_id(int owner_id);

    // 가게 정보 수정 업데이트
    int update(ShopVo vo);

    // 가게 삭제
    int delete(int shop_id);

    // ownerid로 shopid 검색
    Integer select_one_shop_id(int owner_id);

    // menu 갯수와 reviews 갯수 구하기 
    Map<String, Integer> selectMenuAndReviewsCount(int shop_id);

    // hasShop from Owner_id
    Boolean hasShop(int owner_id);

    // // 가게 휴무 지정 트라이중
    // int setHoliday(int shop_id);

    // 가게 status 업데이트 
    int updateStatus();

}
