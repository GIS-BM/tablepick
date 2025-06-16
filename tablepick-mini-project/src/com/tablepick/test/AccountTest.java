package com.tablepick.test;

import java.util.ArrayList;

import com.tablepick.model.AccountDao;
import com.tablepick.model.AccountVO;

public class AccountTest {
  public static void main(String[] args) {
      try {
          AccountDao dao = new AccountDao();
          ArrayList<AccountVO> list = dao.getAllAccounts();

          if (list.isEmpty()) {
              System.out.println("등록된 계정이 없습니다.");
          } else {
              for (AccountVO vo : list) {
                  System.out.println(vo);
              }
          }

      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}