package com.tablepick.test.CustomerView;

// UI가 시작되는 클래스
public class ConsoleUIMain {
	public static void main(String[] args) {
		try {
			ConsoleUIIndex.getInstance().run();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
