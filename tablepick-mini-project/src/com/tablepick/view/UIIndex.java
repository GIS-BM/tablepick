package com.tablepick.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.service.CommonService;
import com.tablepick.test.AdminUnitTest.AdminCRUDUnitTest;
import com.tablepick.test.customer.CustomerViewUnitTest;

public class UIIndex {
	private final AccountDao accountDao;
	private final CommonService commonService;
	private final BufferedReader reader;

	public UIIndex() {
        try {
            this.accountDao = new AccountDao();
            this.commonService = CommonService.getInstance();
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (ClassNotFoundException e) {
            System.err.println("DB 드라이버 로딩 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("시스템 초기화 실패");
        }
    }

	public void run() {
		while (true) {
			printMainMenu();
			try {
				String input = reader.readLine().trim();
				switch (input) {
				case "1":
					loginView();
					break;
				case "2":
					registerView();
					break;
				case "3":
					System.out.println("******* 음식점 예약 시스템을 종료합니다. *******");
					return;
				default:
					System.out.println("올바른 메뉴를 선택해 주세요.");
				}
			} catch (IOException e) {
				System.err.println("입력 오류: " + e.getMessage());
			}
		}
	}

	private void loginView() {
		try {
			System.out.println("[로그인]");
			System.out.print("ID: ");
			String id = reader.readLine();
			System.out.print("Password: ");
			String password = reader.readLine();

			if (commonService.login(id, password) != null) {
				AccountVO loginData = commonService.getLoginDataSession();
				System.out.println("[" + loginData.getType() + "] " + loginData.getName() + "님 환영합니다.");

				// 사용자 타입에 따라 분기
				switch (loginData.getType().toLowerCase()) {
				case "customer":
					try {
						CustomerViewUnitTest cust = new CustomerViewUnitTest();
						cust.run(reader);
					} catch (Exception e) {
						System.out.println("customer 실행 중 오류가 발생했습니다.");
						e.printStackTrace();
					}
					break;
				case "owner":
					System.out.println("ownerView 구현해야 한다");
					// new OwnerView().run(reader);
					break;
				case "admin":
					try {
						AdminCRUDUnitTest admin = new AdminCRUDUnitTest();
						// admin.run(reader);
					} catch (Exception e) {
						System.out.println("admin 실행 중 오류가 발생했습니다.");
						e.printStackTrace();
					}
					break;
				default:
					System.out.println("알 수 없는 사용자 유형입니다.");
				}
			} else {
				System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
			}
		} catch (IOException | SQLException e) {
			System.err.println("로그인 오류: " + e.getMessage());
		}
	}

	private void registerView() {
		try {
			System.out.println("[회원 가입]");
			System.out.print("ID: ");
			String id = reader.readLine();
			System.out.print("Type (예: customer, owner): ");
			String type = reader.readLine();
			System.out.print("Name: ");
			String name = reader.readLine();
			System.out.print("Password: ");
			String password = reader.readLine();
			System.out.print("Tel: ");
			String tel = reader.readLine();

			AccountVO accountVO = new AccountVO(id, type, name, password, tel);
			boolean success = accountDao.insertAccount(accountVO);
			if (success) {
				System.out.println("회원가입이 성공적으로 완료되었습니다.");
			} else {
				System.out.println("회원가입 실패: 이미 존재하는 ID일 수 있습니다.");
			}
		} catch (IOException | SQLException e) {
			System.err.println("회원가입 오류: " + e.getMessage());
		}
	}

	private void printMainMenu() {
		System.out.println("============================================================================================");
		System.out.println(" /$$$$$$$$ /$$$$$$  /$$$$$$$  /$$       /$$$$$$$$ /$$$$$$$  /$$$$$$  /$$$$$$  /$$   /$$");
		System.out.println("|__  $$__//$$__  $$| $$__  $$| $$      | $$_____/| $$__  $$|_  $$_/ /$$__  $$| $$  /$$/");
		System.out.println("   | $$  | $$  \\ $$| $$  \\ $$| $$      | $$      | $$  \\ $$  | $$  | $$  \\__/| $$ /$$/ ");
		System.out.println("   | $$  | $$$$$$$$| $$$$$$$ | $$      | $$$$$   | $$$$$$$/  | $$  | $$      | $$$$$/  ");
		System.out.println("   | $$  | $$__  $$| $$__  $$| $$      | $$__/   | $$____/   | $$  | $$      | $$  $$  ");
		System.out.println("   | $$  | $$  | $$| $$  \\ $$| $$      | $$      | $$        | $$  | $$    $$| $$\\  $$ ");
		System.out.println("   | $$  | $$  | $$| $$$$$$$/| $$$$$$$$| $$$$$$$$| $$       /$$$$$$|  $$$$$$/| $$ \\  $$");
		System.out.println("   |__/  |__/  |__/|_______/ |________/|________/|__/      |______/ \\______/ |__/  \\__/");
		System.out.println("============================================================================================");
		System.out.println("                         *** 어서오세요! TABLE-PICK 서비스입니다. ***");
		System.out.println("                           *** 원하는 식당을 예약할 수 있습니다! ***");
		System.out.println("============================================================================================");
		System.out.println("                                1. 로그인");
		System.out.println("                                2. 회원 가입");
		System.out.println("                                3. 서비스 종료하기");
		System.out.println("============================================================================================");
		System.out.print("메뉴를 선택하세요: ");
	}
}