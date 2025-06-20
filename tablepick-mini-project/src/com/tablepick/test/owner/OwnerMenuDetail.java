package com.tablepick.test.owner;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.tablepick.exception.AccountNotFoundException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.service.OwnerService;
import com.tablepick.session.SessionManager;

//메뉴를 조회 및 생성하는 클래스 입니다.
public class OwnerMenuDetail {

	private static OwnerMenuDetail instance = new OwnerMenuDetail();

	private OwnerMenuDetail() {
	}

	public static OwnerMenuDetail getInstance() {
		return instance;
	}

	public void run() {

		String console;
		String name;
		int price;

		
		//세션으로 id 가져오기
		String accountId = SessionManager.getLoginDataSession().getId();
		

		boolean create = true;

		OwnerService service = new OwnerService();

		Scanner sc = new Scanner(System.in);

		while (create) {
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.println("                          ");
			System.out.println("                  *** 메뉴를 관리할 수 있는 화면입니다. ***");
			System.out.println("                          1. 메뉴 조회하기");
			System.out.println("                          2. 메뉴 생성하기");
			System.out.println("                          3. 메뉴 수정하기");
			System.out.println("                          4. 메뉴 삭제하기");
			System.out.println("                          5. 이전 화면으로 돌아가기");
			System.out.println("                          6. 프로그램 종료하기");
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.println("                          ");
			console = sc.nextLine();

			switch (console) {

			case "1":

				try {

					List<Map<String, String>> list = service.findMenu(accountId);

					System.out.println("등록된 메뉴를 조회합니다.");
					System.out.println("                     ");
					if (list.isEmpty()) {
						System.out.println("현재 등록된 메뉴가 없습니다.");
					} else {
						for (int i = 0; i < list.size(); i++) {
							System.out.println(list.get(i));
						}
						System.out.println("                     ");
					}

					break;

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			case "2":
				System.out.println("새로운 메뉴를 등록합니다. 등록할 메뉴의 정보를 입력하세요.");

				System.out.println("메뉴 이름 : ");
				name = sc.nextLine();
				System.out.println("메뉴의 가격을 입력하세요. :");
				price = sc.nextInt();
				sc.nextLine();
				System.out.println("등록하려는 메뉴입니다. [" + name + " : " + price + "]");
				System.out.println("등록하시려면 예, 등록하지 않으려면 아니오를 입력해 주세요. : ");
				String yesOrNo = sc.nextLine();

				if (yesOrNo.equals("예")) {

					try {
						service.createMenu(accountId, name, price);
						System.out.println("메뉴가 성공적으로 등록되었습니다.");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotFoundRestaurantException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					System.out.println("잘못된 입력입니다. 메뉴 등록을 취소했습니다.");

				}

				break;
			case "3":
				try {

					List<Map<String, String>> list = service.findMenu(accountId);

					System.out.println("등록된 메뉴를 조회합니다.");
					System.out.println("                     ");
					if (list.isEmpty()) {
						System.out.println("등록된 메뉴가 없습니다. 메뉴를 생성해 주세요.");
						break;
					} else {
						for (int i = 0; i < list.size(); i++) {
							System.out.println(list.get(i));
						}
					
					}

				

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("수정하려는 메뉴를 입력해 주세요.");
				name = sc.nextLine();
				System.out.println("수정하려는 가격을 입력해 주세요.");
				price = sc.nextInt();
				sc.nextLine();

				try {
					service.updateMenu(accountId, name, price);
					System.out.println("메뉴가 성공적으로 수정되었습니다.");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				} catch (NotFoundMenuException e) {
					// TODO Auto-generated catch block
					System.out.println("해당하는 메뉴가 없습니다. 다시 입력해 주세요.");
					//e.printStackTrace();
				} catch (AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case "4":
				
				try {

					List<Map<String, String>> list = service.findMenu(accountId);

					System.out.println("등록된 메뉴를 조회합니다.");
					System.out.println("                     ");
					if (list.isEmpty()) {
						System.out.println("등록된 메뉴가 없습니다. 메뉴를 생성해 주세요.");
						break;
					} else {
						for (int i = 0; i < list.size(); i++) {
							System.out.println(list.get(i));
						}
						System.out.println("                     ");
					}

				

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("삭제하려는 메뉴를 입력해 주세요.");
				name = sc.nextLine();
				try {
					service.deleteMenu(accountId, name);
					System.out.println("메뉴가 삭제되었습니다.");

				} catch (SQLException e) {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				} catch (NotFoundMenuException e) {
					// TODO Auto-generated catch block
					System.out.println("해당하는 메뉴가 없습니다. 다시 입력해 주세요.");
					//e.printStackTrace();
				} catch (AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundRestaurantException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "5":
				System.out.println("이전 화면으로 돌아갑니다.");
				OwnerMainDetail.getInstance().run();
				break;
			case "6":
				create = false;
				System.out.println("프로그램을 종료합니다.");
				System.exit(0); // 시스템 종료
				break;
			default:
				System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
				break;
			}

		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		new OwnerMenuDetail().run();

	}
}