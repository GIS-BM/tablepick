package com.tablepick.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.service.TablePickSerivceCommon;

public class ConsoleUIIndex {
    private AccountDao accountdao;
    private TablePickSerivceCommon tablePickServiceCommon;

    public ConsoleUIIndex() {
      try {
          accountdao = new AccountDao();
          tablePickServiceCommon = new TablePickSerivceCommon();
      } catch (ClassNotFoundException e) {
          System.err.println("DB 드라이버 로딩 실패: " + e.getMessage());
          e.printStackTrace();
          System.exit(1); // 프로그램 종료 (선택사항)
      }
  }

    public void execute() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            exit:
            while (true) {
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

                String inputoption = reader.readLine().trim();

                switch (inputoption) {
                    case "1":
                        loginView(reader);
                        break;
                    case "2":
                        addAccountView(reader);
                        break;
                    case "3":
                        System.out.println("******* 음식점 예약 시스템을 종료합니다. *******");
                        break exit;
                    default:
                        System.out.println("올바른 메뉴를 선택해 주세요.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginView(BufferedReader reader) {
        try {
            System.out.println("[로그인]");
            System.out.print("ID: ");
            String id = reader.readLine();
            System.out.print("Password: ");
            String password = reader.readLine();

            if (tablePickServiceCommon.login(id, password)) {
                System.out.println("로그인 성공");
            } else {
                System.out.println("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAccountView(BufferedReader reader) {
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
            if (accountdao.insertAccount(accountVO)) {
                System.out.println("회원가입이 성공적으로 완료되었습니다.");
            } else {
                System.out.println("회원가입 실패: 이미 존재하는 ID일 수 있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}