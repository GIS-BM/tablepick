package com.tablepick.test.AccountLoginAddView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;
import com.tablepick.service.TablePickSerivceCommon;

public class AccountLoginAdd {
	private AccountDao accountdao;
	private TablePickSerivceCommon tablePickServiceCommon;
	
	public AccountLoginAdd() throws ClassNotFoundException {
		accountdao = new AccountDao();
		tablePickServiceCommon = new TablePickSerivceCommon();
	}
	
	public static void main(String[] args) {
		try {
			AccountLoginAdd accountLoginAdd = new AccountLoginAdd();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				
				while (true) {
					System.out.println("로그인 회원가입 매뉴");
					System.out.println("login: 로그인");
					System.out.println("addAccount: 회원가입");
					System.out.println("입력 :");
					String inputoption = reader.readLine().trim();					
					
					switch (inputoption) {
					case "login" :
						accountLoginAdd.loginView(reader);
						break;
					case "addAccount" :
						accountLoginAdd.addAccountView(reader);
						break;
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	} // main
	
	public void loginView(BufferedReader reader) {
		try {
			System.out.println("아이디 입력");
			System.out.print("ID: ");
			String id;
			id = reader.readLine();
			System.out.println("비밀번호 입력");
			System.out.println("Password: ");
			String password = reader.readLine();
			
			if (tablePickServiceCommon.login(id, password))
				System.out.println("로그인 성공");
			else
				System.out.println("로그인 실패");
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addAccountView(BufferedReader reader) {
		try {
			System.out.println("[계정 등록]");
			System.out.print("ID: ");
			String id = reader.readLine();
			System.out.print("Type: ");
			String type = reader.readLine();
			System.out.print("Name: ");
			String name = reader.readLine();
			System.out.print("Password: ");
			String password = reader.readLine();
			System.out.print("Tel: ");
			String tel = reader.readLine();

			AccountVO accountVO = new AccountVO(id, type, name, password, tel);
			if (accountdao.insertAccount(accountVO)) {
				System.out.println("계정이 성공적으로 등록되었습니다.");
			} else {
				System.out.println("계정 등록 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
