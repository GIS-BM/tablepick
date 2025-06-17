package com.tablepick.session;

import com.tablepick.model.AccountVO;

public class SessionManager {
    private static AccountVO loginDataSession;
    // 스테틱 변수 처리, getLoginDataSession() 메서드 통해서만 데이터 가져올 수 있음

    // 로그인 시 호출
    public static void login(AccountVO user) {
    	loginDataSession = user;
    }

    // 로그아웃 시 호출
    public static void logout() {
    	loginDataSession = null;
    }

    // 로그인 상태 확인
    public static boolean isLoggedIn() {
        return loginDataSession != null;
    }

    // 로그인한 사용자 정보 반환
    public static AccountVO getLoginDataSession() {
        return loginDataSession;
    }
}