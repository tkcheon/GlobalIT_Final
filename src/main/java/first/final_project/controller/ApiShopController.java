package first.final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import first.final_project.service.ShopService;
import first.final_project.vo.ShopVo;

@RestController@RequestMapping("/api/shop")public class ApiShopController {

    @Autowired
    ShopService shop_Service;

    @PostMapping("/register")
    public ResponseEntity<?> registerShop(@RequestBody ShopVo vo) {
        try {
            shop_Service.insert(vo);
            return ResponseEntity.ok().body("Shop successfully registered");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the shop.");
        }
    }

    @PutMapping("/modify/{shop_id}") // Change to @PutMapping if it's a full update
    public ResponseEntity<?> modifyShop(@PathVariable int shop_id, @RequestBody ShopVo vo) {
        System.out.println("here in api/modify");
        try {
            // Ensure the shop_id is correctly handled within your update logic
            vo.setShop_id(shop_id); // Assuming your ShopVo has a method to set the ID
            shop_Service.update(vo);
            return ResponseEntity.ok().body("Shop successfully modified");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while modifying the shop.");
        }
    }
}