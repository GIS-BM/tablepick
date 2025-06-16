package com.tablepick.test.AccountCRUDView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;

public class AccountCRUDUnitTest {
	private AccountDao accountdao;

	public AccountCRUDUnitTest() throws ClassNotFoundException {
		accountdao = new AccountDao();
	}

	public static void main(String[] args) {
		try {
			AccountCRUDUnitTest crud = new AccountCRUDUnitTest();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println("\n=== Account 관리 메뉴 ===");
				System.out.println("c: 계정 등록");
				System.out.println("r: 계정 조회(id)");
				System.out.println("ra: 전체 계정 조회");
				System.out.println("u: 계정 수정");
				System.out.println("d: 계정 삭제");
				System.out.println("exit: 종료");
				System.out.print("입력 : ");
				
				String main = reader.readLine().trim();
				switch (main) {
					case "c":
						crud.accountAddView(reader);
						break;
					case "r":
						crud.accountIdFindView(reader);
						break;
					case "ra":
						crud.accountAllView();
						break;
					case "u":
						crud.accountUpdateView(reader);
						break;
					case "d":
						crud.accountDeleteView(reader);
						break;
					case "exit":
						System.out.println("종료합니다.");
						return;
					default:
						System.out.println("잘못된 입력입니다.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void accountAddView(BufferedReader reader) {
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

	private void accountIdFindView(BufferedReader reader) {
		try {
			System.out.print("조회할 ID 입력: ");
			String id = reader.readLine();
			AccountVO vo = accountdao.findAccountById(id);
			if (vo != null) {
				System.out.println("조회 결과: " + vo);
			} else {
				System.out.println("해당 ID의 계정이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void accountAllView() {
		try {
			System.out.println("[전체 계정 조회]");
			ArrayList<AccountVO> list = accountdao.getAllAccounts();
			if (list.isEmpty()) {
				System.out.println("등록된 계정이 없습니다.");
			} else {
				for (AccountVO vo : list) {
					System.out.println(vo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void accountUpdateView(BufferedReader reader) {
		try {
			System.out.print("수정할 계정 ID: ");
			String id = reader.readLine();
			AccountVO old = accountdao.findAccountById(id);
			if (old == null) {
				System.out.println("해당 ID의 계정이 존재하지 않습니다.");
				return;
			}

			System.out.print("새 Type (기존: " + old.getType() + "): ");
			String type = reader.readLine();
			System.out.print("새 Name (기존: " + old.getName() + "): ");
			String name = reader.readLine();
			System.out.print("새 Password (기존: " + old.getPassword() + "): ");
			String password = reader.readLine();
			System.out.print("새 Tel (기존: " + old.getTel() + "): ");
			String tel = reader.readLine();

			AccountVO updated = new AccountVO(id,
					type.isEmpty() ? old.getType() : type,
					name.isEmpty() ? old.getName() : name,
					password.isEmpty() ? old.getPassword() : password,
					tel.isEmpty() ? old.getTel() : tel);

			if (accountdao.updateAccount(updated)) {
				System.out.println("계정이 성공적으로 수정되었습니다.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void accountDeleteView(BufferedReader reader) {
		try {
			System.out.print("삭제할 계정 ID: ");
			String id = reader.readLine();
			if (accountdao.deleteAccount(id)) {
				System.out.println("계정이 성공적으로 삭제되었습니다.");
			} else {
				System.out.println("삭제 실패 또는 존재하지 않는 ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}