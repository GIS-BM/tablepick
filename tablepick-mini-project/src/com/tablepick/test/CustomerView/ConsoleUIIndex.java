package com.tablepick.test.CustomerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.model.AccountVO;
import com.tablepick.service.TablePickSerivceCommon;

/**
 * 콘솔 UI 메인 클래스 싱글톤 패턴으로 작성
 * 
 */
public class ConsoleUIIndex {
	/*
	 * 싱글톤 패턴을 위한 3가지를 작성한다. private static ConsoleUIIndex instance; // 접근제어자
	 * private로 선언된 인스턴스 변수 private ConsoleUIIndex() {} // 접근제어자 private로 선언된 생성자
	 * public static ConsoleUIIndex getInstance() {} // 싱글톤 객체를 반환하는 메서드
	 */
	// [] 인스턴스 변수 선언부
	private static ConsoleUIIndex instance;
	// 싱글톤 패턴을 위한 ConsoleUIIndex 객체 instacne 선언
	// 외부에서 객체에 직접 접근 불가능 하게 한다.
	private TablePickSerivceCommon tablePickServiceCommon;
	private BufferedReader reader;
	// TablePickSerivceCommon 객체 선언

	// ConsoleUIIndex 생성자
	// 접근제어자 private로 외부에서 객체 생성 불가능하게 한다.
	private ConsoleUIIndex() throws ClassNotFoundException {
		// [] 생성자 호출시 필요한 객체가 선언되게 한다.
		this.tablePickServiceCommon = TablePickSerivceCommon.getInstance();
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 싱글톤 객체를 반환하는 메서드
	// 외부에서 싱글톤 객체 사용시 이 메서드를 사용해서 객체를 가져와서 사용한다.
	public static ConsoleUIIndex getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new ConsoleUIIndex();
		}
		return instance;
	}

	// Main View가 존재하는 메서드 run()
	// 입력 오류시 메서드가 다시 실행
	public void run() {
		while (true) {
			try {
				printMainMenu(); // 메뉴 출력
				String input = reader.readLine();

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
					throw new NotFoundMenuException("올바른 메뉴를 선택해 주세요.");
				// System.out.println("올바른 메뉴를 선택해 주세요.");
				}

			} catch (IOException e) {
				System.out.println("입력 오류가 발생했습니다: " + e.getMessage());
			} catch (NotFoundMenuException e) {
				// e.printStackTrace();
				// 오류문 같이 표시
				System.out.println(e.getMessage());
				// 메시지만 표시
			}
		}
	}

	// 유효한 메뉴인지 검사하는 메서드 isValidMenu
	private boolean isValidMenu(String input) {
		return input.equals("1") || input.equals("2") || input.equals("3");
	}

	// main 메뉴가 존재하는 매서드
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

	private void loginView() {
		try {
			System.out.print("ID: ");
			String id = reader.readLine();

			System.out.print("Password: ");
			String password = reader.readLine();

			if (tablePickServiceCommon.login(id, password) != null) {
				System.out.println("로그인 성공");
				AccountVO loginData = tablePickServiceCommon.getLoginData(); // 로그인 데이터 출력 또는 유지
				tablePickServiceCommon.checkAccountTypeAndMovePage(loginData);
			} else {
				System.out.println("로그인 실패");
			}
		} catch (IOException e) {
			System.out.println("입력 중 오류 발생: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("로그인 중 예외 발생: " + e.getMessage());
		}
	}

	private void registerView() {

	}

}
