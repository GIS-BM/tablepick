package com.tablepick.view;

import java.sql.SQLException;
import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.model.AccountVO;
import com.tablepick.service.CommonService;
import com.tablepick.test.owner.TestCreateRestaurant;
import com.tablepick.test.owner.TestDeleteRestaurant;

//식당 주인의 메인 화면 입니다.
//식당 등록 화면, 식당 상세 정보 화면으로 이동할 수 있습니다. 

public class UIOwnerMain {

	// OwnerMain을 싱글톤으로 단 하나의 객체만 생성할 수 있도록 합니다.
	private static UIOwnerMain instance = new UIOwnerMain();

	private UIOwnerMain() {
	}

	public static UIOwnerMain getInstance() {
		return instance;
	}

	public void run() throws NotFoundAccountException, AccountNotFoundException, NotFoundRestaurantException {
		Scanner sc = new Scanner(System.in);
		boolean exit = false;
		UIIndex ui = new UIIndex();
		// 세션으로 id와 패스워드 가져오기
		AccountVO loginData = null;
		try {
			loginData = CommonService.getInstance().getLoginDataSession();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String accountId = loginData.getId();

		String console;
		System.out.println("                          ");
		System.out.println(
				"============================================================================================");

		System.out.println("                          ");
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

		while (!exit) {
			System.out.println(
					"============================================================================================");

			System.out.println("                          ");
			System.out.println("                 *** 어서오세요. 식당을 관리할 수 있는 페이지 입니다. ***             ");
			System.out.println("                          ");
			System.out.println("                          1. 내 식당 등록하기 ");
			System.out.println("                          2. 내 식당 상세 정보 확인하기 ");
			System.out.println("                          3. 내 식당 삭제하기 ");
			System.out.println("                          4. 로그아웃 ");
			System.out.println("                          5. 이전 화면으로 돌아가기");
			System.out.println("                          0. 프로그램 종료하기");
			System.out.println("                          ");
			System.out.println(
					"============================================================================================");
			System.out.print("메뉴를 선택하세요: ");
			console = sc.nextLine();

			switch (console) {
			case "1":
				TestCreateRestaurant.getInstance().run();
				break;
			case "2":
				UIOwnerMainDetail.getInstance().run();
				break;
			case "3":
				TestDeleteRestaurant.getInstance().run();
				break;
			case "4":
				try {
					CommonService.getInstance().logoutSession();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println("로그아웃이 완료되었으므로 프로그램 홈으로 돌아갑니다.");
				System.out.println("                          ");
				ui.run();
				break;
			case "5":
				System.out.println("이전 화면으로 돌아갑니다.");
				System.out.println("                          ");
				ui.run();
				break;
			case "0":
				System.out.println("프로그램을 종료합니다.");
				exit = true;
				System.exit(0);

				break;
			default:
				System.out.println("없는 선택지 입니다. 다시 선택해 주세요.");
				System.out.println("                          ");
				break;
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, NotFoundAccountException,
			AccountNotFoundException, NotFoundRestaurantException {
		new UIOwnerMain().run();
	}

}
