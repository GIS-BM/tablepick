package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.service.TablePickSerivceCommon;

//식당 주인의 메인 화면 입니다.
//식당 등록 화면, 식당 상세 정보 화면으로 이동할 수 있습니다. 

public class OwnerMain {

	public void run() {
		Scanner sc = new Scanner(System.in);
		OwnerMainDetail omd = new OwnerMainDetail();
		boolean exit = false;
		
		// 테스트용 아이디 (추후 삭제)
		try {
			TablePickSerivceCommon.getInstance().login("owner01", "pw1234");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//이 로그인 데이터로 하위 페이지에서 로그인 아이디를 사용할 수 있습니다.
		
		String console;
		//그림은 초기 실행 시에만 출력
		
		System.out.println("                             __...--~~~~~-._   _.-~~~~~--...__");
		System.out.println("                           //               `V'               \\\\ ");
		System.out.println("                          //     OWNER       |       PAGE      \\\\ ");
		System.out.println("                         //__...--~~~~~~-._  |  _.-~~~~~~--...__\\\\ ");
		System.out.println("                        //__.....----~~~~._\\ | /_.~~~~----.....__\\\\");
		System.out.println("                       ====================\\\\|//====================");
		System.out.println("                                           `---`");
		System.out.println(
				"============================================================================================");
				
		
		while (!exit) {

		System.out.println("                 *** 어서오세요. 식당을 관리할 수 있는 페이지 입니다. ***             ");
		System.out.println("                          1. 내 식당 등록하기 ");
		System.out.println("                          2. 내 식당 상세 정보 확인하기 ");
		System.out.println("                          3. 내 식당 삭제하기 ");
		System.out.println("                          4. 로그아웃 ");
		System.out.println("                          5. 뒤로가기 ");
		System.out.println("                          6. 프로그램 종료하기");

		console = sc.nextLine();

		switch (console) {
		case "1":
			new TestCreateRestaurant().run();
			break;
		case "2":
			System.out.println("식당 상세 정보를 확인합니다.");
			new OwnerMainDetail().run();
			break;
		case "3":
			new TestDeleteRestaurant().run();
			break;
		case "4":
			System.out.println("로그아웃 합니다.");
			break;
		case "5":
			System.out.println("이전 화면으로 돌아갑니다. (로직 추가 필요)");
			break;
		case "6":
			System.out.println("프로그램을 종료합니다.");
			exit = true;
			break;
		default:
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			break;
		}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new OwnerMain().run();
	}

}
