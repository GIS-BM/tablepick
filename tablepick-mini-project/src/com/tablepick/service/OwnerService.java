package com.tablepick.service;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.model.OwnerDao;
import com.tablepick.model.RestaurantVO;

//식당 주인의 Service 입니다.
//동작을 담당하는 메소드들의 모음입니다.
public class OwnerService {

	private OwnerDao ownerDao = new OwnerDao();

	public OwnerService() {
		this.ownerDao = ownerDao;

	}

	//계정의 아이디와 비밀번호가 일치하는지 확인하는 메소드
	public void findAccount(String accountId, String password)
			throws NotMatchedPasswordException, NotFoundAccountException, SQLException {
		ownerDao.findAccount(accountId, password);
	}

	//식당을 등록하는 메소드
	public int createRestaurant(RestaurantVO restaurantVO) throws SQLException {
		return ownerDao.createRestaurant(restaurantVO);
	}

	//내 식당을 찾는 메소드
	public RestaurantVO findMyRestaurant(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMyRestaurant(accountId);

	}

	//등록된 내 식당을 삭제하는 메소드
	public void deleteMyRestaurant(String accountId, String password)
			throws SQLException, NotMatchedPasswordException, NotFoundAccountException {
		ownerDao.deleteMyRestaurant(accountId, password);

	}

	//해당 아이디로 식당이 존재하는 지 확인하는 메소드
	public boolean existRestaurant(String accountId) throws SQLException {
		return ownerDao.existRestaurant(accountId);

	}

	//예약자 명단 유무를 조회하는 메소드
	public void existRestaurantReservation(int reservationIdx) throws SQLException {
		ownerDao.existRestaurantReservation(reservationIdx);

	}

	//내 식당의 예약 리스트를 조회하는 메소드
	public List<Map<String, String>> findMyRestaurantReservationList(String accountId)
			throws NotFoundRestaurantException, SQLException {
		return ownerDao.findMyRestaurantReservationList(accountId);

	}

	//식당 정보 조회 및 식당 별 매출액을 조회하는 메소드
	public List<Map<String, String>> findMyRestaurantAndSales(String accountId, int restaurantIdx)
			throws SQLException, NotFoundRestaurantException {

		return ownerDao.findMyRestaurantAndSales(accountId, restaurantIdx);

	}

	//식당 정보를 업데이트하는 메소드
	public void updateMyRestaurantInfoAndSales(String accountId, int reservationIdx, String newName, String newType,
			String newAddress, String newTel) throws SQLException, NotFoundRestaurantException {
		ownerDao.updateMyRestaurantInfoAndSales(accountId, reservationIdx, newName, newType, newAddress, newTel);

	}

	//메뉴를 생성하는 메소드
	public void createMenu(String accountId, String name, int price) throws SQLException, NotFoundRestaurantException {
		ownerDao.createMenu(accountId, name, price);
	}

	//전체 메뉴를 조회하는 메소드
	public List<Map<String, String>> findMenu(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMenu(accountId);
	}

	//메뉴를 찾는 메소드
	public void findMenuDetail(String accountId, String name)
			throws NotFoundMenuException, SQLException, NotFoundRestaurantException, NotFoundAccountException {
		ownerDao.findMenuDetail(accountId, name);
	}
	
	//메뉴의 가격을 수정하는 메소드
	public void updateMenu(String accountId, String name, int price) throws SQLException, NotFoundMenuException,
			NotFoundAccountException, NotFoundRestaurantException, NotFoundAccountException {
		ownerDao.updateMenu(accountId, name, price);
	}

	//메뉴를 삭제하는 메소드
	public void deleteMenu(String accountId, String name)
			throws SQLException, NotFoundMenuException, NotFoundAccountException, NotFoundRestaurantException {
		ownerDao.deleteMenu(accountId, name);
	}

	//내 식당의 리뷰를 조회하는 메소드
	public List findMyRestaurantReview(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMyRestaurantReview(accountId);
	}

	//선택한 예약자의 매출액을 업데이트하는 메소드
	public void updateCustomerSale(String accountId, int reservationIdx, int newSale)
			throws NoReservationException, SQLException {
		ownerDao.updateCustomerSale(accountId, reservationIdx, newSale);
	}

	//내 식당의 최다 예약자를 조회하는 메소드
	public List<Map<String, String>> findMyRestaurantReservationMostList(String accountId)
			throws NotFoundRestaurantException, SQLException {
		return ownerDao.findMyRestaurantReservationMostList(accountId);
	}

	//선택한 예약자 명단을 조회하는 메소드
	public Map<String, String> findSelectedCustomer(String accountId, int reservationIdx)
			throws SQLException, NoReservationException {
		return ownerDao.findSelectedCustomer(accountId, reservationIdx);
	}
}