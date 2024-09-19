<%@ page language='java' contentType='text/html; charset=UTF-8'
pageEncoding='UTF-8'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    .reviews-item {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 15px;
        margin-top: 20px;
        background-color: #f9f9f9;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .reviews-item .stars {
        color: gold;
    }
    .reviews-item .star-empty {
        color: #ddd;
    }
    .slideshow-container {
        position: relative;
        max-width: 500px;
        margin: auto;
        margin-left: 5px;
    }
    .mySlides {
        position: absolute;
    }
    /* Default size */
    .image-size-1 img {
        width: 500px;
        height: 300px;
        border: 1px solid black;
    }
    .image-size-2 img {
        width: 250px;
        height: 300px;
        border: 1px solid black;
    }
    .image-size-3-first img {
        width: 300px;
        height: 300px;
        border: 1px solid black;
    }
    .image-size-3-second img {
        width: 200px;
        height: 150px;
        border: 1px solid black;
    }
    .image-size-3-third img {
        width: 200px;
        height: 150px;
        border: 1px solid black;
    }
    /* Layout for three images */
    .image-size-3 {
        width: 500px;
        height: 300px;
        border: 1px solid black;
    }
    .image-size-3-first {
        left: 0;
        top: 0;
    }
    .image-size-3-second {
        left: 300px;
        top: 0;
    }
    .image-size-3-third {
        left: 300px;
        top: 150px;
    }
    .prev, .next {
        cursor: pointer;
        position: absolute;
        top: 50%;
        width: auto;
        padding: 16px;
        margin-top: -22px;
        color: white;
        font-weight: bold;
        font-size: 18px;
        border-radius: 0 3px 3px 0;
        user-select: none;
    }
    .next {
        right: 0;
        border-radius: 3px 0 0 3px;
    }
    .prev:hover, .next:hover {
        background-color: rgba(0,0,0,0.8);
    }
    .btn-delete {
        margin-left: auto;
    }
</style>
</head>
<body>
    <div>
        <!-- <c:if test="${empty list}">
            <p>No reviews available.</p>
        </c:if> -->
        <c:forEach var="reviews" items="${list}">
            <div class="col-md-12">
                <div class="reviews-item d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-center">
                        <h4>${reviews.member_name} &nbsp;&nbsp; ${reviews.reviews_cdate}</h4>
                        <button class="btn btn-danger btn-sm btn-delete" onclick="reviews_del('${reviews.reviews_id}')">Delete</button>
                    </div>
                    <div class="stars">
                        <c:forEach begin="1" end="${reviews.reviews_rating}">
                            <i class="fa fa-star"></i>
                        </c:forEach>
                        <c:forEach begin="1" end="${5 - reviews.reviews_rating}">
                            <i class="fa fa-star star-empty"></i>
                        </c:forEach>
                    </div>
                    <div class="slideshow-container" id="slideshow-${reviews.reviews_id}">
                        <c:if test="${not empty reviews.reviews_img_list }">
                            <c:forEach var="img" items="${reviews.reviews_img_list}">
                                <div class="mySlides slide-${reviews.reviews_id}">
                                    <img src="${pageContext.request.contextPath}/resources/images/${img.reviews_img}" />
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                    <div class="mt-3">
                        <div>${reviews.orders_name}</div>
                        <div>${reviews.reviews_content}</div>
                    </div>
                </div>
            </div>
        </c:forEach>
</div>
</body>
</html>