package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.model.MenuVO;
import com.tablepick.model.RestaurantDao;

//메뉴를 조회 및 생성하는 클래스 입니다.
public class TestMenu{

	public static void main(String[] args) {

		String console;
		String name;
		int price;
		//메뉴를 생성할 시 해당 식당의 id를 받아와야 합니다.
		//따라서 로그인 정보로 식당 주인의 식당 id를 가져오는 로직이 필요합니다.
		//현재는 하드코딩 한 상태
		int restaurantId = 1;
		boolean create = true;

		RestaurantDao dao = new RestaurantDao();

		Scanner sc = new Scanner(System.in);

		while (create) {
			System.out.println("메뉴를 관리할 수 있는 화면입니다.");
			System.out.println("1. 메뉴 조회하기 2. 메뉴 생성하기 3. 메뉴 수정하기 4. 메뉴 삭제하기 5. 이전 화면으로 돌아가기 6. 프로그램 종료하기");
			console = sc.nextLine();

			switch (console) {
			
			case "1":
			
				try {
					
					List<Map<String, String>> list = dao.checkMenu(restaurantId);

					System.out.println("등록된 메뉴를 조회합니다.");

					for(int i = 0; i<list.size();i++) {
						System.out.println(list.get(i));
					}
					
					
					break;
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				
			case "2":

				System.out.println("1. 메뉴 이름 : ");
				name = sc.nextLine();
				System.out.println("2. 메뉴의 가격을 입력하세요. :");
				price = sc.nextInt();
				sc.nextLine();
				System.out.println("등록하려는 메뉴입니다. [" + name + " : " + price + "]");
				System.out.println("등록하시려면 예, 등록하지 않으려면 아니오를 입력해 주세요. : ");
				String yesOrNo = sc.nextLine();

				if (yesOrNo.equals("예")) {

					try {
						dao.createMenu(new MenuVO(restaurantId, name, price));
						System.out.println("메뉴가 성공적으로 등록되었습니다.");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					System.out.println("메뉴 등록을 취소했습니다.");

				}

				break;
			case "3":
				
				System.out.println("수정하려는 메뉴를 입력해 주세요.");
				name = sc.nextLine();
				System.out.println("수정하려는 가격을 입력해 주세요.");
				price = sc.nextInt();
				sc.nextLine();
				
			
				try {
					dao.UpdateMenu(new MenuVO(restaurantId, name, price));
					System.out.println("메뉴가 성공적으로 수정되었습니다.");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundMenuException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
				
				
			case "4":
				System.out.println("삭제하려는 메뉴를 입력해 주세요.");
				name = sc.nextLine();
				try {
					dao.deleteMenu(restaurantId, name);
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundMenuException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case "5":
				System.out.println("이전 화면으로 돌아갑니다.");
				break;
			case "6":
				create = false;
				System.out.println("프로그램을 종료합니다.");
				break;
			default:
				System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
				break;
			}

		}
	}

}
