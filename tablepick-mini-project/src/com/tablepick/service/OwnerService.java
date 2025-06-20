package com.tablepick.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

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
	

	
	
	public void findAccount(String accountId, String password) throws NotMatchedPasswordException, NotFoundAccountException, SQLException {
		ownerDao.findAccount(accountId, password);
	}
	
	public int createRestaurant(RestaurantVO restaurantVO) throws SQLException {
		return ownerDao.createRestaurant(restaurantVO);
	}
	
	public RestaurantVO findMyRestaurant(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMyRestaurant(accountId);
		
	}
	public void deleteMyRestaurant(String accountId, String password) throws SQLException, NotMatchedPasswordException, NotFoundAccountException {
		ownerDao.deleteMyRestaurant(accountId,password);
		
	}
	
	public boolean existRestaurant(String accountId) throws SQLException {
		return ownerDao.existRestaurant(accountId);
		
	}
	
	public void existRestaurantReservation(int reservationIdx) throws SQLException {
		ownerDao.existRestaurantReservation(reservationIdx);
		
	}
	public List<Map<String, String>> findMyRestaurantReservationList(String accountId) throws NotFoundRestaurantException, SQLException {
		return ownerDao.findMyRestaurantReservationList(accountId);
		
	}
	
	public List<Map<String, String>> findMyRestaurantAndSales(String accountId, int restaurantIdx) throws SQLException, NotFoundRestaurantException {
		
		return ownerDao.findMyRestaurantAndSales(accountId, restaurantIdx);
		
	}
	
	public void updateMyRestaurantInfoAndSales(String accountId, int reservationIdx, String newName, String newType, String newAddress, String newTel) throws SQLException, NotFoundRestaurantException {
		ownerDao.updateMyRestaurantInfoAndSales(accountId,  reservationIdx, newName, newType,
				newAddress,  newTel);
		
	}
	
//	public void updateRestaurantSales() {
//		ownerDao.updateRestaurantSales( accountId,  reservationIdx,  newSales);
//	}
	
	public void createMenu(String accountId, String name, int price) throws SQLException, NotFoundRestaurantException {
		ownerDao.createMenu(accountId, name, price);
	}
	
	public List<Map<String, String>> findMenu(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMenu(accountId);
	}
	
	public void findMenuDetail(String accountId, String name) throws NotFoundMenuException, SQLException, AccountNotFoundException, NotFoundRestaurantException {
		ownerDao.findMenuDetail(accountId, name);
	}
	
	public void updateMenu(String accountId, String name, int price) throws SQLException, NotFoundMenuException, AccountNotFoundException, NotFoundRestaurantException {
		ownerDao.updateMenu(accountId, name, price);
	}
	
	public void deleteMenu(String accountId, String name) throws SQLException, NotFoundMenuException, AccountNotFoundException, NotFoundRestaurantException {
		ownerDao.deleteMenu(accountId, name);
	}
	
	public List findMyRestaurantReview(String accountId) throws SQLException, NotFoundRestaurantException {
		return ownerDao.findMyRestaurantReview(accountId);
	}
	
	public void updateCustomerSale(String accountId, int reservationIdx, int newSale) throws NoReservationException, SQLException {
		ownerDao.updateCustomerSale(accountId, reservationIdx,  newSale);
	}
	
	public List<Map<String, String>> findMyRestaurantReservationMostList(String accountId) throws NotFoundRestaurantException, SQLException {
		return ownerDao.findMyRestaurantReservationMostList(accountId);
	}

	public Map<String, String> findSelectedCustomer(String accountId, int reservationIdx) throws SQLException, NoReservationException {
		return ownerDao.findSelectedCustomer(accountId, reservationIdx);
	}
}

