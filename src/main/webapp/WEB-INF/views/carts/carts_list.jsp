<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!DOCTYPE html>
    <html lang="ko">

    <head>
      <meta charset="UTF-8">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
      <style>
        .cart-summary {
          margin-top: 20px;
        }
      </style>

      <script>
        function delete_carts(carts_id) {
          if (confirm("정말 삭제하시겠습니까?") == false) return;

          location.href = "delete.do?carts_id=" + carts_id;
        }
      </script>

    </head>


    <body>

      <%@include file="../common.jsp" %>

        <%@include file="../header.jsp" %>

          <%@include file="../sidebar.jsp" %>

            <main id="main" class="main">
              <div class="pagetitle">

                <h1>장바구니목록</h1>

                <nav>
                  <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="index.html">Home</a></li>
                    <li class="breadcrumb-item">Forms</li>
                    <li class="breadcrumb-item active">Layouts</li>
                  </ol>
                </nav>

              </div><!-- End Page Title -->

              <section class="section">
                <div class="row align-items-top">
                  <div class="col-lg-10">

                    <!-- Card with an image on left -->
                    <!-- 장바구니 항목 테이블 -->
                    <table class="table table-bordered">
                      <thead>
                        <tr>
                          <th>이미지</th>
                          <th>메뉴명</th>
                          <th>메뉴개수</th>
                          <th>메뉴가격</th>
                          <th>총 가격</th>
                          <th>추가일자</th>
                          <th></th>
                        </tr>
                      </thead>
                      <tbody id="cartItems">
                        <c:set var="currentShopName" value="" />
                        <c:forEach var="vo" items="${list}">
                          <!-- Check if the shop name has changed -->
                          <c:if test="${currentShopName != vo.shop_name}">
                            <c:set var="currentShopName" value="${vo.shop_name}" />
                            <!-- Display shop name as a header -->
                            <tr>
                              <th colspan="7" style="background-color: #f8f9fa; text-align: left; padding: 10px;">
                                ${vo.shop_name}
                              </th>
                            </tr>
                          </c:if>

                          <tr>
                            <td class="cart_menuimg">
                              <div>
                                <img src="../resources/images/${vo.menu_img}" class="img-fluid rounded" alt="..."
                                  width="150px">
                              </div>
                            </td>
                            <td class="cart_menuname">${vo.menu_name}</td>

                            <td class="cart_quantity">
                              <input type="number" class="form-control quantity-input" id="quantity_${vo.carts_id}"
                                value="${vo.carts_quantity}" min="1" max="50" disabled>
                            </td>

                            <td class="cart_price">${vo.menu_price}</td>
                            <td class="cart_total_price">
                              ${vo.menu_price * vo.carts_quantity}
                            </td>
                            <td class="cart_cdate">${vo.carts_cdate}</td>

                            <td>
                              <input class="btn btn-danger" type="button" value="삭제"
                                onclick="delete_carts('${vo.carts_id}');">
                              <input class="btn btn-info" type="button" id="edit_${vo.carts_id}" value="수정"
                                onclick="editQuantity('${vo.carts_id}');">
                              <button class="btn btn-success" id="save_${vo.carts_id}"
                                onclick="saveQuantity('${vo.carts_id}')" style="display: none;">저장</button>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>

                      <tfoot>
                        <tr>
                          <td colspan="3" class="text-right"><strong>총 개수:</strong></td>
                          <td id="total_quantity">0</td>
                          <td colspan="2" class="text-right"><strong>총 가격:</strong></td>
                          <td id="total_price">0</td>
                        </tr>
                      </tfoot>

                      <script>
                        document.addEventListener('DOMContentLoaded', function () {
                          const currencyFormatter = new Intl.NumberFormat('ko-KR', {
                            style: 'currency',
                            currency: 'KRW',
                            minimumFractionDigits: 0
                          });

                          function updateTotals() {
                            let totalQuantity = 0;
                            let totalPrice = 0;

                            // 각 행을 순회하며 총 개수와 총 가격 계산
                            document.querySelectorAll('#cartItems tr').forEach(row => {
                              const quantityInput = row.querySelector('.quantity-input');
                              const priceElement = row.querySelector('.cart_price');
                              const totalPriceElement = row.querySelector('.cart_total_price');

                              if (quantityInput && priceElement && totalPriceElement) {
                                const quantity = parseInt(quantityInput.value) || 0;
                                const price = parseFloat(priceElement.textContent) || 0;
                                const total = quantity * price;

                                totalQuantity += quantity;
                                totalPrice += total;

                                totalPriceElement.textContent = currencyFormatter.format(total);
                              }
                            });

                            document.getElementById('total_quantity').textContent = totalQuantity;
                            document.getElementById('total_price').textContent = currencyFormatter.format(totalPrice);
                          }

                          updateTotals();

                          window.editQuantity = function (id) {
                            const quantityInput = document.getElementById("quantity_" + id);
                            const saveButton = document.getElementById("save_" + id);
                            const editButton = document.getElementById("edit_" + id);

                            if (quantityInput && saveButton) {
                              quantityInput.disabled = false;
                              saveButton.style.display = 'inline';
                              editButton.style.display = 'none';
                            }
                          };

                          window.saveQuantity = function (id) {
                            const quantityInput = document.getElementById("quantity_" + id);
                            const saveButton = document.getElementById("save_" + id);
                            const editButton = document.getElementById("edit_" + id);

                            if (quantityInput && saveButton) {
                              const newQuantity = quantityInput.value;

                              $.ajax({
                                url: '/carts/modify.do',
                                type: 'POST',
                                data: {
                                  carts_id: id,
                                  carts_quantity: newQuantity
                                },
                                success: function (response) {
                                  quantityInput.disabled = true;
                                  saveButton.style.display = 'none';
                                  editButton.style.display = 'inline';
                                  updateTotals();
                                },
                                error: function (xhr, status, error) {
                                  console.error(`Error updating quantity for ID ${id}: ${error}`);
                                }
                              });
                            }
                          };
                        });
                      </script>

                  </div>
                </div>

              </section>
    </body>

    </html>