package com.tablepick.test.Restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.tablepick.exception.NoReservationException;
import com.tablepick.model.RestaurantDao;

public class TestInputCustomerSales {
    public static void main(String[] args) throws NoReservationException {
        // 해당 식당의 예약을 조회하고 예약 당 매출액을 입력 가능하게 한다.
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            RestaurantDao resDao = new RestaurantDao();
            String accountId = "owner01";
            String reservationIdxStr = null;
            String sale = null;
            
            List<Map<String, String>> reservationList = resDao.checkMyRestaurantReservationList(accountId);
            System.out.println("** 식당 예약 자 명단 ** ");
            for (int i = 0; i < reservationList.size(); i++) {
                System.out.println(reservationList.get(i));
            }
            
            System.out.println("매출액 입력할 예약 번호 입력");
            System.out.print(">> ");
            reservationIdxStr = br.readLine();
            int reservationIdx = Integer.parseInt(reservationIdxStr);
            
            // 예약자 조회
            System.out.println("** 선택한 예약자 정보 **");
            Map<String, String> selectedCustomer = resDao.checkSelectedCustomer(accountId, reservationIdx);
            for (Map.Entry<String, String> entry : selectedCustomer.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            
            System.out.println("매출액 입력 >> ");
            sale = br.readLine();
            int newSale = Integer.parseInt(sale);
            
            resDao.updateCustomerSale(accountId, reservationIdx, newSale);
            
        } catch (NumberFormatException  e) {
            System.out.println("숫자 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("매출액이 성공적으로 입력되었습니다.");
    }

}