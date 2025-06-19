package com.tablepick.service;

import com.tablepick.model.RestaurantDao;
import com.tablepick.model.RestaurantVO;

//식당 주인의 Service 입니다.
//동작을 담당하는 메소드들의 모음입니다.
public class OwnerService {
	
	private RestaurantDao restaurantDao;
	private String accountId;
	private String password;
	private RestaurantVO restaurantVO;
	
	
	public void findAccount() {
		restaurantDao.findAccount(accountId, password);
	}
	
	public void createRestaurant() {
		restaurantDao.createRestaurant(restaurantVO);
	}
	
	public void findMyRestaurant() {
		restaurantDao.findMyRestaurant(accountId);
		
	}
	public void deleteMyRestaurant() {
		restaurantDao.deleteMyRestaurant(accountId,password);
		
	}
	
	public void existRestaurant(accountId) {
		restaurantDao.existRestaurant(accountId);
		
	}
	
	public void existRestaurantReservation(reservationIdx) {
		restaurantDao.existRestaurantReservation(reservationIdx);
		
	}
	public void findMyRestaurantReservationList(accountId) {
		restaurantDao.findMyRestaurantReservationList(accountId);
		
	}
	
	public void findMyRestaurantAndSales(accountId, restaurantIdx) {
		restaurantDao.findMyRestaurantAndSales(accountId, restaurantIdx);
		
	}
	
	public void updateMyRestaurantInfoAndSales() {
		restaurantDao.updateMyRestaurantInfoAndSales(accountId,  reservationIdx, newName, newType,
				newAddress,  newTel);
		
	}
	
//	public void updateRestaurantSales() {
//		restaurantDao.updateRestaurantSales( accountId,  reservationIdx,  newSales);
//	}
	
	public void createMenu() {
		restaurantDao.createMenu(accountId, name, price);
	}
	
	public void findMenu() {
		restaurantDao.findMenu(accountId);
	}
	
	public void findMenuDetail() {
		restaurantDao.findMenuDetail(accountId, name);
	}
	
	public void updateMenu() {
		restaurantDao.updateMenu(accountId, name, price);
	}
	
	public void deleteMenu() {
		restaurantDao.deleteMenu(accountId, name);
	}
	
	public void findMyRestaurantReview() {
		restaurantDao.findMyRestaurantReview(accountId);
	}
	
	public void updateCustomerSale() {
		restaurantDao.updateCustomerSale(accountId, reservationIdx,  newSales);
	}
}

