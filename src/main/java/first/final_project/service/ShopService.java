package first.final_project.service;

import first.final_project.vo.ShopVo;
import java.util.List;

public interface ShopService {

    // 가게 추가하기
    int insert(ShopVo vo);

    // 가게 전체 리스트 출력
    List<ShopVo> selectList();

    // 가게 1개 상세보기
    ShopVo selectOne(int shop_id);

    // 가게 정보 수정하기
    ShopVo select_modify_shop_id(int shop_id);

    // 가게 삭제하기
    int delete(int shop_id);
}
