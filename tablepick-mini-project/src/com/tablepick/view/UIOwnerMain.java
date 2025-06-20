package com.tablepick.view;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.service.CommonService;
import com.tablepick.session.SessionManager;
import com.tablepick.test.owner.OwnerMainDetail;
import com.tablepick.test.owner.TestCreateRestaurant;
import com.tablepick.test.owner.TestDeleteRestaurant;


//식당 주인의 메인 화면 입니다.
//식당 등록 화면, 식당 상세 정보 화면으로 이동할 수 있습니다. 

public class UIOwnerMain {
	
	//OwnerMain을 싱글톤으로 단 하나의 객체만 생성할 수 있도록 합니다.
	// 다른 페이지에서 OwnerMain을 싱글톤으로 불러올 수 있습니다. 
	// 2. 변수의 명시적 초기화 시점에 생성자를 실행해 자신의 객체를 생성
	private static UIOwnerMain instance = new UIOwnerMain();
	// 1. private 생성자로 외부에서 객체 생성하는 것을 원천 차단
	private UIOwnerMain() {
		//System.out.println("CompanyService 객체 생성");
	}
	//3. static 메소드로 인스턴스를 공유
		public static UIOwnerMain getInstance() {
				return instance;
			}

	public void run() {
		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		
		//테스트 로그인 . 페이지 연결이 전부 완료되면 삭제해야 합니다.
		try {
			CommonService.getInstance().loginSessionManager("owner01","pw1234");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 테스트 로그인
		
		String accountId = SessionManager.getLoginDataSession().getId();
		//세션으로 id와 패스워드 가져오기

		//이 로그인 데이터로 하위 페이지에서 로그인 아이디를 사용할 수 있습니다.
	
		
		String console;
		//그림은 메인에서만 출력
		
		System.out.println("                                ／三三三三三三三三＼");
		System.out.println("                              ／三三三三三三三三三三＼");
		System.out.println("                       , lllll､|￣￣|￣￣|￣￣|￣￣|￣￣| 　 ");
		System.out.println("                          l三三l O  | W  | N | E　| R  | 　 ");
		System.out.println("                          |三三|＿＿|＿＿|＿＿|＿＿|＿＿|＼");
		System.out.println("                          ヽ三ﾉ.||.ﾐ;._,.;:.　 　 　　 || Ω");
		System.out.println("                       ∬_＿_ﾂｰ ||（｀・ω・´）　　∬　∬  || []");
		System.out.println("                        ＼≠／三.||O　　 　と彡|￣￣￣| ||");
		System.out.println("                         〔二二二二二二二二二二二二二二二二〕");
		System.out.println("                             |_＿＿＿＿＿＿＿＿＿＿＿__|");
		System.out.println("                             | |ヽ.;;;;:::::::;;;,ノ| |");

		System.out.println(
				"============================================================================================");
				
		System.out.println("                          ");
		while (!exit) {
			
		
	
	

		System.out.println("                 *** 어서오세요. 식당을 관리할 수 있는 페이지 입니다. ***             ");
		System.out.println("                          ");
		System.out.println("                          1. 내 식당 등록하기 ");
		System.out.println("                          2. 내 식당 상세 정보 확인하기 ");
		System.out.println("                          3. 내 식당 삭제하기 ");
		System.out.println("                          4. 로그아웃 ");
		System.out.println("                          5. 뒤로가기 ");
		System.out.println("                          6. 프로그램 종료하기");
		System.out.println("                          ");
		System.out.println(
				"============================================================================================");
			

		console = sc.nextLine();

		switch (console) {
		case "1":
			TestCreateRestaurant.getInstance().run();
			break;
		case "2":
			OwnerMainDetail.getInstance().run();
			break;
		case "3":
			TestDeleteRestaurant.getInstance().run();
			break;
		case "4":
//			try {
//				CommonService.getInstance().logout();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.out.println("로그아웃이 완료되었으므로 프로그램 홈으로 돌아갑니다. (로직 추가 필요)");
			System.out.println("                          ");
			break;
		case "5":
			System.out.println("이전 화면으로 돌아갑니다. (로직 추가 필요)");
			System.out.println("                          ");
			break;
		case "6":
			System.out.println("프로그램을 종료합니다.");
			exit = true;
			System.exit(0); // 시스템 종료2
			
			break;
		default:
			System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
			System.out.println("                          ");
			break;
		}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new UIOwnerMain().run();
	}

}
