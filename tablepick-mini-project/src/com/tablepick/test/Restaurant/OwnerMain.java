package com.tablepick.test.Restaurant;

import java.sql.SQLException;
import java.util.Scanner;

import com.tablepick.service.TablePickSerivceCommon;
import com.tablepick.session.SessionManager;

//식당 주인의 메인 화면 입니다.
//식당 등록 화면, 식당 상세 정보 화면으로 이동할 수 있습니다. 

public class OwnerMain {
	
	//OwnerMain을 싱글톤으로 단 하나의 객체만 생성할 수 있도록 합니다.
	// 다른 페이지에서 OwnerMain을 싱글톤으로 불러올 수 있습니다. 
	// 2. 변수의 명시적 초기화 시점에 생성자를 실행해 자신의 객체를 생성
	private static OwnerMain instance = new OwnerMain();
	// 1. private 생성자로 외부에서 객체 생성하는 것을 원천 차단
	private OwnerMain() {
		//System.out.println("CompanyService 객체 생성");
	}
	//3. static 메소드로 인스턴스를 공유
		public static OwnerMain getInstance() {
				return instance;
			}

	public void run() {
		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		
		// 테스트용 아이디 (추후 삭제)
		
		try {
			
			TablePickSerivceCommon.getInstance().login("owner01", "owner");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//이 로그인 데이터로 하위 페이지에서 로그인 아이디를 사용할 수 있습니다.
	
		
		String console;
		//그림은 메인에서만 출력
		while (!exit) {
			
		
		System.out.println("                                ／三三三三三三三三＼");
		System.out.println("                              ／三三三三三三三三三三＼");
		System.out.println("                       , lllll､|￣￣|￣￣|￣￣|￣￣|￣￣| 　 ");
		System.out.println("                          l三三l O  | W  | N | E　| R  | 　 ");
		System.out.println("                          |三三|＿＿|＿＿|＿＿|＿＿|＿＿|＼");
		System.out.println("                          ヽ三ﾉ.||.ﾐ;._,.;:.　 　 　　|| Ω");
		System.out.println("                       ∬_＿_ﾂｰ ||（｀・ω・´）　　∬　∬  || []");
		System.out.println("                        ＼≠／三.||O　　 　と彡|￣￣￣| ||");
		System.out.println("                       〔二二二二二二二二二二二二二二二二〕");
		System.out.println("                          |_＿＿＿＿＿＿＿＿＿＿＿__|");
		System.out.println("                          | |ヽ.;;;;:::::::;;;,ノ| |");

		System.out.println(
				"============================================================================================");
				
		System.out.println("                          ");
	

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
			System.out.println("로그아웃 합니다.");
			break;
		case "5":
			System.out.println("이전 화면으로 돌아갑니다. (로직 추가 필요)");
			break;
		case "6":
			System.out.println("프로그램을 종료합니다.");
			exit = true;
			System.exit(0); // 시스템 종료2
			
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
