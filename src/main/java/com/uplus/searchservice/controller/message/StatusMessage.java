package com.uplus.searchservice.controller.message;

public class StatusMessage {
    public static final String NOT_AVAILABLE_SERVICE="서비스를 사용할 수 없습니다.";

    public static final String NOT_FOUND_SEARCH_PRODUCT = "검색 결과를 찾을 수 없습니다.";
    public static final String SUCCESS_FOUND_SEARCH_PRODUCT = "검색 결과를 찾았습니다.";

    public static final String NOT_FOUND_PRODUCT_ORDER = "상품 정보를 확인할 수 없습니다.";
    public static final String NOT_MATCH_PRODUCT_ORDER_PRICE = "주문 정보가 다릅니다. 주문을 다시 진행해주세요.";

    public static final String SUCCESS_PAY_PRODUCT_ORDER = "주문 결제가 완료되었습니다.";
    public static final String FAIL_PRODUCT_ORDER = "주문 결제가 실패하였습니다.";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR  = "데이터베이스 에러";
    public static final String DB_UPDATE_IS_ZERO = "데이터베이스 변경 Row가 없음";
    public static final String DB_UPDATE_IS_NOT_ONE = "데이터베이스 변경 Row가 1개가 아님";
}
