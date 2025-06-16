package com.tablepick.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;

public class TablePickService {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int num = Integer.parseInt(br.readLine());
		switch (num) {
		case (1):
			account(br);
			break;
		 /*
		 * 여기에 아마 case문으로 선택
		 */
		}
		br.close();
	}
	public static void account(BufferedReader br) throws IOException{
		// 회원 가입
		AccountDao accountDao = new AccountDao();
		AccountVO accountVO = new AccountVO();
		// BufferedReader로 데이터입력해서 accountVO에 저장
		try {
			accountDao.createAccount(accountVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 로그인
		String loginId = br.readLine();
		String loginPassword = br.readLine();
		try {
			String type = accountDao.login(loginId,loginPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//type 에 따라서 사용자, 식당 주인, 관리자로 또 나눠서 진행
		
		// 관리자 - 회원 조회
		String checkId = br.readLine();
		try {
			accountVO = accountDao.checkAccount(checkId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 관리자 - 회원 업데이트
		String updateId = br.readLine();
		AccountVO updateAccount = new AccountVO();
		try {
			accountDao.updateAccount(updateId, updateAccount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 관리자 - 회원 삭제
		String deleteId = br.readLine();
		try {
			accountDao.deleteAccount(deleteId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
