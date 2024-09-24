<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>Member List</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/alpinejs@2.8.2" defer></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
      body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 20px;
      }

      .container {
        max-width: 800px;
        margin: auto;
        background: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      }

      h1 {
        text-align: center;
        color: #333;
      }

      .bg-white {
        background-color: #fff;
        border-radius: 5px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }

      .form-control {
        width: calc(100% - 20px);
        padding: 10px;
        margin-top: 5px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }

      .form-control:focus {
        border-color: #007bff;
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
      }

      .btn {
        background-color: #5cb85c;
        color: white;
        border: none;
        padding: 10px 15px;
        cursor: pointer;
        border-radius: 5px;
        transition: background-color 0.3s;
      }

      .btn:hover {
        background-color: #4cae4c;
      }

      .flex {
        display: flex;
        justify-content: flex-end;
      }

      .flex > input {
        margin-left: 10px;
      }

      label {
        font-weight: bold;
      }
    </style>

  </head>
  <body>

    <%@include file="../common.jsp" %>

    <%@include file="../header.jsp" %>

    <%@include file="../sidebar.jsp" %>

    <main id="main" class="main">
      <div class="pagetitle">

        <h1>회원리스트</h1>

        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="index.html">Home</a></li>
            <li class="breadcrumb-item">Forms</li>
            <li class="breadcrumb-item active">Layouts</li>
          </ol>
        </nav>

      </div><!-- End Page Title -->

      <table border="1">
      <tr>
        <th>번호</th>
        <th>이름</th>
        <th>닉네임</th>
        <th>아이디</th>
        <th>이메일</th>
        <th>전화번호</th>
        <th>삭제</th>
      </tr>
      <c:forEach var="member" items="${member_list}">
        <tr>
          <td>${member.member_id}</td>
          <td>${member.member_name}</td>
          <td>${member.member_nickname}</td>
          <td>${member.member_accountId }</td>
          <td>${member.member_email}</td>
          <td>${member.member_phone}</td>
          <td>
            <a href="delete_member.do?member_id=${member.member_id}">Delete</a>
          </td>
        </tr>
      </c:forEach>
    </table>
      
    </main><!-- End #main -->

   
  </body>
</html>
