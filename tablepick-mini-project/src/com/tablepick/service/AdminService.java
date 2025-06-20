package com.tablepick.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.InfoNotEnoughException;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.model.AdminDao;
import com.tablepick.model.ReserveVO;

public class AdminService {
	private AdminDao adminDao;

	public AdminService() throws ClassNotFoundException {
		adminDao = new AdminDao();

	}
	
	// 전체 계정 유효성 검사 (예외 발생 유도)
	public void findAccounts() throws SQLException, NotFoundAccountException {
		adminDao.findAccounts();  // 내부에서 NotFoundAccountException 발생 가능
	}

	// 전체 계정 조회
	public List<AccountVO> getAllAccounts() throws SQLException, NotFoundAccountException {
		findAccounts();
		return adminDao.findAllAccounts();
	}

	// ID로 계정 조회
	public AccountVO getAccountById(String accountId) throws SQLException, NotFoundAccountException {
		return adminDao.findAccount(accountId);
	}

	// 계정 수정
	public boolean updateAccount(AccountVO accountVO) throws SQLException, InfoNotEnoughException {
		return adminDao.updateAccount(accountVO);
	}

	// 계정 삭제
	public boolean deleteAccount(String accountId) throws SQLException, InfoNotEnoughException {
		return adminDao.deleteAccount(accountId);
	}

	// 레스토랑 ID로 이름 조회
	public String getRestaurantNameById(int restaurantId) throws SQLException, NotFoundRestaurantException {
		return adminDao.findRestaurantNameById(restaurantId);
	}

	// 전체 예약 조회
	public List<ReserveVO> getAllReserves() throws SQLException {
		return adminDao.getAllReserves();
	}

	// 예약 최다 계정 조회
	public Map<String, Integer> getTopReserveAccounts() throws SQLException {
		return adminDao.getMostReservesAccount();
	}
}
