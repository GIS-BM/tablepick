package com.tablepick.refer.ohs;
//
//import java.util.Scanner;
//
//import school.exception.DuplicateTelException;
//import school.exception.MemberNotFoundException;
//import school.model.Employee;
//import school.model.Person;
//import school.model.SchoolService;
//import school.model.Student;
//import school.model.Teacher;
///**
// *  UI : User Interface  사용자와의 소통역할<br>
// *  	  시스템 콘솔창에서 입출력을 지원한다 <br>
// *  ConsoleUI 는  자신의 역할을 수행하기 위해서는 <br>
// *  필수적으로  비즈니스 로직을 담당하는 SchoolService 와 
// *  Scanner 가 필요함. <br>  클래스간의 relation은  composition 으로<br> 
// *  설계하고 ConsoleUI 가 생성되는 시점에 SchoolService 와 Scanner가<br>
// *  우선적으로 생성되도록 구현한다 
// */
//public class ConsoleUIReview {
//	private SchoolService service; 
//	private Scanner scanner;
//	public ConsoleUIReview() {
//		this.service = new SchoolService();
//		this.scanner = new Scanner(System.in);
//	}
//	public void execute() {
//		exit: // 레이블을 이용해 특정 제어문을 벗어나도록 한다 
//		while(true) {
//			System.out.println("***학사관리프로그램을 시작합니다***");
//			System.out.println("1. 추가 2. 삭제 3. 검색 4. 전체회원보기 5.종료");
//			String menu = scanner.nextLine();
//			switch(menu) {
//			case "1":
//				addView();
//				break;
//			case "2":
//				deleteView();
//				break;
//			case "3":
//				findView();
//				break;
//			case "4":	
//				service.printAll();
//				break;			
//			case "5":
//				System.out.println("***학사관리프로그램을 종료합니다***");
//				//break;// 해당 switch 구문을 벗어난다 
//				break exit; // 위 레이블이 명시된 while 반복문을 벗어난다 
//			default:
//				System.out.println("잘못된 입력값입니다");
//			}//switch
//		}//while
//		scanner.close();		
//	}//execute	
//	private void findView() {
//		System.out.println("조회할 구성원의 전화번호를 입력하세요");
//		String tel = scanner.nextLine();
//		try {
//			Person person = service.findMemberByTel(tel);
//			System.out.println("조회결과:"+person);
//		} catch (MemberNotFoundException e) {
//			System.out.println(e.getMessage());// 검색 불가 메세지를 보여준다 
//		}
//	}
//	private void deleteView() {
//		System.out.println("삭제할 구성원의 전화번호를 입력하세요");
//		String tel = scanner.nextLine();
//		try {
//			service.deleteMemberByTel(tel);
//			System.out.println(tel+" tel 구성원 정보를 정상적으로 삭제하였습니다");//삭제 완료했을 경우 메세지
//		} catch (MemberNotFoundException e) {
//			System.out.println(e.getMessage());// 삭제 예외에 대한 메세지를 제공한다 
//		}
//	}
//	/**
//	 *  execute() 의 add 역할을 분리해 구성원 추가 로직을 전담하는 메서드 <br>
//	 *  execute  메서드가 방대해지는 것을 막고 전문화를 시킴 -> 응집도를 높임  <br>
//	 *  기능 분리로 결합도가 느슨해지면서 이후 변경시 보다 유연하게 대처할 수 있음 <br> 
//	 *  SchoolService 와 연동하여 프로그램상에 구성원 정보가 등록되게 한다 <br>  <br>  <br> 
//	 *  
//	 *  2차 개발 추가 구현  <br> 
//	 *  tel 입력 즉시 중복확인 <br> 
//	 */
//	private void addView() {		
//		System.out.println("입력할 구성원 종류를 선택하세요 1.학생 2.선생님 3.직원");
//		String type = scanner.nextLine();
//		String tel = null;
//		while(true) {
//			System.out.println("1.전화번호를 입력하세요");
//			tel = scanner.nextLine();
//			int index = service.findIndexByTel(tel); // -1 은 tel에 해당하는 구성원이 존재하지 않을 때 반환값 
//			if(index != -1) // tel 에 해당하는 구성원이 리스트에 존재한다 
//				System.out.println("입력하신 "+tel+" tel 정보는 중복됩니다. 다시 입력하세요");
//			else // -1이면 등록 가능함 
//				break; // while 문을 벗어난다    
//		}
//		System.out.println("2.이름을 입력하세요");
//		String name = scanner.nextLine();
//		System.out.println("3.주소를 입력하세요");
//		String address = scanner.nextLine();
//		Person person=null;
//		switch(type) {
//		case "1":
//			System.out.println("4.학번을 입력하세요");
//			String stuId = scanner.nextLine();
//			person = new Student(tel, name, address, stuId);
//			break;
//		case "2":
//			System.out.println("4.과목을 입력하세요");
//			String subject = scanner.nextLine();
//			person = new Teacher(tel, name, address, subject);
//			break;	
//		case "3":
//			System.out.println("4.부서를 입력하세요");
//			String department = scanner.nextLine();
//			person = new Employee(tel, name, address, department);
//			break;		
//		}//switch
//		// SchoolService 와 연동하여 프로그램상에 구성원 정보가 
//		// 등록되게 한다 
//		try {  // ====> !!!  왜 DuplicateTelException 을 throws 하지 않고 try catch 하는 선택을 했을까? 
//			service.addMember(person);
//			System.out.println("리스트에 추가:"+person);
//		} catch (DuplicateTelException e) {			
//			//e.printStackTrace();// 예외 발생 경로와 메세지를 모두 보여준다 
//			System.out.println(e.getMessage());// 사용자에게 SchoolService에서 설정한 메세지를 제공한다 
//		}
//		
//	}
//}//class
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
