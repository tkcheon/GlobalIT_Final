<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <!-- Bootstrap CSS -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <!-- iamport  -->
    <script
    type="text/javascript"
    src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"
></script>
    <!-- 인증SDK -->
    <script src=""https://cdn.iamport.kr/v1/iamport.js"></script>
    <script>
        var IMP = window.IMP;
        IMP.init("impXXXXXXXXX");

        function requestPay() {
            IMP.request_pay(
                {
                    pg: "html5_inicis",		//KG이니시스 pg파라미터 값
                    pay_method: "card",		//결제 방법
                    merchant_uid: "1234578",//주문번호
                    name: "당근 10kg",		//상품 명
                    amount: 200,			//금액
      				buyer_email: "gildong@gmail.com",
      				buyer_name: "홍길동",
      				buyer_tel: "010-4242-4242",
      				buyer_addr: "서울특별시 강남구 신사동",
      				buyer_postcode: "01181"
     	
                },
                function (rsp) {
      				//rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
                    if (rsp.success) {
                        //서버 검증 요청 부분
                    } else {
                        alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
                    }
                }
            );
        }
    </script>
    <meta charset="UTF-8"/>
    <title>Sample Payment</title>
</head>
<body>
<button onclick="requestPay()">결제하기</button>
<!-- 결제하기 버튼 생성 -->
</body>
</html>