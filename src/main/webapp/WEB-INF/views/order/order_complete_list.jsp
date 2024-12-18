<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <title>주문 수락 목록</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <style>
    .assign-button {
        width: 200px;
        height: 100px;
        padding: 10px 15px;
        background-color: #f0a8d0;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease;
        font-size: 30px;
    }

    .assign-button:hover {
        background-color: #e090b5;
    }

    .cancel-button {
        width: 200px;
        height: 100px;
        padding: 10px 15px;
        background-color: red;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease;
        font-size: 30px;
    }

    .button-container {
        display: flex;
        gap: 10px;
    }
    </style>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.1/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>

    <script>
    var socket = new SockJS('${pageContext.request.contextPath}/ws-orders');
    var stompClient = Stomp.over(socket);

    // WebSocket 연결 설정
    stompClient.connect({}, function (frame) {
        // 주문 상태 업데이트 메시지 구독
        stompClient.subscribe('/topic/orders', function (message) {
            location.reload(); // 메시지 수신 시 페이지 새로고침
        });
    });
    </script>
</head>
<body>
    <%@include file="../common.jsp" %>
  
    <%@include file="../header.jsp" %>
  
    <%@include file="../sidebar.jsp" %>

    <main id="main" class="main">
        <div class="pagetitle">
            <h1>완료 주문 목록</h1><br>
            <nav>
                <ol class="breadcrumb">
                  <li class="breadcrumb-item"><a href="../order/accept.do">Home</a></li>
                    <li class="breadcrumb-item">주문 관리</li>
                    <li class="breadcrumb-item active">완료 주문 목록</li>
                </ol>
            </nav>
        </div><!-- End Page Title -->
    <!-- 주문 목록이 없을 때 메시지 표시 -->
    <c:if test="${empty orders}">
        <p>현재 진행 중인 주문이 없습니다.</p>
    </c:if>

    <!-- 주문 목록 테이블 -->
    <section class="section">
        <div class="row align-items-top">
          <div class="col-lg-12">
            <c:forEach var="order" items="${orders}">
              <!-- 주문 카드 -->
              <div class="card mb-3">
                <div class="row g-0" onclick="show_menu('${order.orders_id}');">
                  <div class="col-md-8">
                    <div class="card-body">
                      <h5 class="card-title">
                        주문 번호: ${order.orders_id}
                      </h5>
                      <p class="card-text">결제 금액 : ${order.orders_price} 원</p>
                      <p class="card-text">고객 주소 : ${order.addr_line1} ${order.addr_line2}</p>
                      <p class="card-text">고객 네임 : ${order.member_nickname}</p>
                      <p class="card-text">고객 전화 : ${order.member_phone}</p>
                      <p class="card-text">주문 메뉴 : ${order.orders_name}</p>
                    </div>
                  </div>
                </div>
              </div><!-- End 주문 카드 -->
            </c:forEach>
          </div>
        </div>
      </section>

</body>
</html>
