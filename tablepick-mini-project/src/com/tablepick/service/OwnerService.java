package com.tablepick.service;

import java.sql.SQLException;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NoReservationException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.exception.NotMatchedPasswordException;
import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

//식당 주인의 Service 입니다.
//동작을 담당하는 메소드들의 모음입니다.
public class OwnerService {
	
	private RestaurantDao restaurantDao;
	private String accountId;
	private String password;
	private RestaurantVO restaurantVO;
	private int reservationIdx;
	private int restaurantIdx;
	private String newName;
	private String newType;
	private String newAddress;
	private String newTel;
	private int newSales;
	private String name;
	private int price;

	
	
	public void findAccount() throws NotMatchedPasswordException, AccountNotFoundException, SQLException {
		restaurantDao.findAccount(accountId, password);
	}
	
	public void createRestaurant() throws SQLException {
		restaurantDao.createRestaurant(restaurantVO);
	}
	
	public void findMyRestaurant() throws SQLException, NotFoundRestaurantException {
		restaurantDao.findMyRestaurant(accountId);
		
	}
	public void deleteMyRestaurant() throws SQLException, NotMatchedPasswordException, AccountNotFoundException {
		restaurantDao.deleteMyRestaurant(accountId,password);
		
	}
	
	public void existRestaurant() throws SQLException {
		restaurantDao.existRestaurant(accountId);
		
	}
	
	public void existRestaurantReservation() throws SQLException {
		restaurantDao.existRestaurantReservation(reservationIdx);
		
	}
	public void findMyRestaurantReservationList() throws NotFoundRestaurantException, SQLException {
		restaurantDao.findMyRestaurantReservationList(accountId);
		
	}
	
	public void findMyRestaurantAndSales() throws SQLException, NotFoundRestaurantException {
		
		restaurantDao.findMyRestaurantAndSales(accountId, restaurantIdx);
		
	}
	
	public void updateMyRestaurantInfoAndSales() throws SQLException, NotFoundRestaurantException {
		restaurantDao.updateMyRestaurantInfoAndSales(accountId,  reservationIdx, newName, newType,
				newAddress,  newTel);
		
	}
	
//	public void updateRestaurantSales() {
//		restaurantDao.updateRestaurantSales( accountId,  reservationIdx,  newSales);
//	}
	
	public void createMenu() throws SQLException, NotFoundRestaurantException {
		restaurantDao.createMenu(accountId, name, price);
	}
	
	public void findMenu() throws SQLException, NotFoundRestaurantException {
		restaurantDao.findMenu(accountId);
	}
	
	public void findMenuDetail() throws NotFoundMenuException, SQLException, AccountNotFoundException, NotFoundRestaurantException {
		restaurantDao.findMenuDetail(accountId, name);
	}
	
	public void updateMenu() throws SQLException, NotFoundMenuException, AccountNotFoundException, NotFoundRestaurantException {
		restaurantDao.updateMenu(accountId, name, price);
	}
	
	public void deleteMenu() throws SQLException, NotFoundMenuException, AccountNotFoundException, NotFoundRestaurantException {
		restaurantDao.deleteMenu(accountId, name);
	}
	
	public void findMyRestaurantReview() throws SQLException, NotFoundRestaurantException {
		restaurantDao.findMyRestaurantReview(accountId);
	}
	
	public void updateCustomerSale() throws NoReservationException, SQLException {
		restaurantDao.updateCustomerSale(accountId, reservationIdx,  newSales);
	}
}

