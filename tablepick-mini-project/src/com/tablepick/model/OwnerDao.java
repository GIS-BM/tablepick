package com.tablepick.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import com.tablepick.common.DatabaseUtil;
import com.tablepick.exception.NoReservationException;
import com.tablepick.exception.NotFoundAccountException;
import com.tablepick.exception.NotFoundMenuException;
import com.tablepick.exception.NotFoundRestaurantException;
import com.tablepick.exception.NotMatchedPasswordException;

//레스토랑 Dao 입니다. 

public class OwnerDao {

	/**
	 * 식당 삭제 시 본인의 아이디와 비밀번호를 다시 입력합니다. <br>
	 * 이것이 일치해야 식당 삭제가 가능합니다. <br>
	 * 
	 * @param accountId
	 * @param password
	 * @return
	 * @throws NotMatchedPasswordException
	 * @throws AccountNotFoundException
	 * @throws SQLException
	 */
	public boolean findAccount(String accountId, String password)
			throws NotMatchedPasswordException, NotFoundAccountException, SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean loginSuccess = true;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT password FROM account WHERE id = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();

			if (rs.next() == false) {
				// 아이디가 존재하지 않을 때
				loginSuccess = false;
				throw new NotFoundAccountException("아이디가 존재하지 않습니다. 다시 입력하세요.");

			} else {// 아이디가 존재하면 비밀번호 동일 여부 확인

				if (password.equals(rs.getString("password")) == false) {
					loginSuccess = false;
					throw new NotMatchedPasswordException("비밀번호가 일치하지 않습니다. 다시 입력하세요.");
				}
			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return loginSuccess;
	}

	/**
	 * 식당을 등록하는 메소드 입니다.<br>
	 * 트랜잭션 처리 완료 <br>
	 * 
	 * @param restaurantVO
	 * @return
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public int createRestaurant(RestaurantVO restaurantVO) throws SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int restaurantId = 0;

		try {
			con = DatabaseUtil.getConnection();
			// 트랜잭션 처리합니다.
			con.setAutoCommit(false);
			String sql = "INSERT INTO restaurant(account_id, name, type, address, tel) VALUES(?,?,?,?,?);";

			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, restaurantVO.getAccountId());
			pstmt.setString(2, restaurantVO.getName());
			pstmt.setString(3, restaurantVO.getType());
			pstmt.setString(4, restaurantVO.getAddress());
			pstmt.setString(5, restaurantVO.getTel());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys(); // 발급된 레스토랑 id를 반환받는다.
			if (rs.next())
				restaurantId = rs.getInt(1);
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw e;
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return restaurantId;
	}// createRestaurant

	/**
	 * Owner의 아이디로 내 식당의 restaurantId를 찾는 메서드 입니다. <br>
	 * RestaurantVO의 객체를 반환합니다. <br>
	 * 내 식당이 존재하지 않을 시 예외 처리가 됩니다. <br>
	 * 
	 * @param accountId
	 * @return
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public RestaurantVO findMyRestaurant(String accountId) throws SQLException, NotFoundRestaurantException {

		if (existRestaurant(accountId) == false) {
			throw new NotFoundRestaurantException(accountId + "님의 식당이 존재하지 않습니다.");
		}

		RestaurantVO retaurantVO = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT idx, account_id, name, type, address, tel, opentime");
		sql.append(" FROM restaurant");
		sql.append(" WHERE account_id = ?");

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
			pstmt.setString(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					retaurantVO = new RestaurantVO(rs.getInt("idx"), rs.getString("account_id"), rs.getString("name"),
							rs.getString("type"), rs.getString("address"), rs.getString("tel"),
							rs.getTime("opentime").toLocalTime());
				}
			}
		}

		return retaurantVO;

	}

	/**
	 * 등록된 식당을 삭제하는 메소드 입니다. <br>
	 * 비밀번호가 다르면 NotMatchedPasswordException 발생시키며 예외 처리 됩니다. <br>
	 * 
	 * @param accountId
	 * @param password
	 * @throws SQLException
	 * @throws AccountNotFoundException
	 * @throws NotMatchedPasswordException
	 * @throws NotFoundAccountException 
	 */
	public void deleteMyRestaurant(String accountId, String password)
		
		throws SQLException, NotMatchedPasswordException, NotFoundAccountException {
			Connection con = null;
			PreparedStatement pstmt = null;
	
			findAccount(accountId, password);
	
			try {
				con = DatabaseUtil.getConnection();
				String sql = "DELETE FROM restaurant WHERE account_id =?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, accountId);
				pstmt.executeUpdate();
			}
	
			finally {
				DatabaseUtil.closeAll(pstmt, con);
			}
	}

	/**
	 * Owner의 아이디로 식당이 존재하는지 확인하는 메서드 입니다. <br>
	 * 다른 메서드에서 식당의 유무를 조회할 때 사용합니다. <br>
	 * 
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public boolean existRestaurant(String accountId) throws SQLException {
		boolean existRestaurant = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT 1 FROM restaurant WHERE account_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountId);
			rs = pstmt.executeQuery();
			existRestaurant = rs.next();
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		
		return existRestaurant;
	}

	/**
	 * Owner가 내 식당의 예약이 존재하는지 확인할 때 사용하는 메서드 입니다. <br>
	 * 다른 메서드에서 예약의 유무를 조회할 때 사용합니다. <br>
	 * 
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 */
	public boolean existRestaurantReservation(int reservationIdx) throws SQLException {
		boolean existRestaurantReservation = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT 1 FROM reserve WHERE idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, reservationIdx);
			rs = pstmt.executeQuery();
			existRestaurantReservation = rs.next();
		} finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
		return existRestaurantReservation;
	}

	/**
	 * Owner가 내 식당의 예약 리스트를 조회하는 메서드입니다. <br>
	 * 내 식당이 존재하는지 먼저 existRestaurant() 메서드 실행 후 메서드를 실행합니다. <br>
	 * 
	 * @param accountId
	 * @return reservationList
	 * @throws RestaurantNotFoundException
	 * @throws SQLException
	 */
	public List<Map<String, String>> findMyRestaurantReservationList(String accountId)
			throws NotFoundRestaurantException, SQLException {
		if (existRestaurant(accountId) == false) {
			throw new NotFoundRestaurantException(accountId + "님의 식당이 존재하지 않습니다.");
		}

		List<Map<String, String>> reservationList = new ArrayList<Map<String, String>>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rs.idx AS reserve_idx, rs.account_id AS customer_id, a.name AS customer_name, rs.reservepeople, rs.reservedate, ");
		sql.append("rs.reservetime, rs.registerdate, rt.idx AS restaurant_idx, IFNULL(s.sales, 0) AS sales_amount ");
		sql.append("FROM reserve rs ");
		sql.append("LEFT JOIN restaurant rt ON rs.restaurant_idx = rt.idx ");
		sql.append("LEFT JOIN account a ON rs.account_id = a.id ");
		sql.append("LEFT JOIN account o ON rt.account_id = o.id ");
		sql.append("LEFT JOIN sales s ON rs.idx = s.reserve_idx ");
		sql.append("WHERE rt.account_id = ? ");
		sql.append("ORDER BY rs.reservedate, rs.reservetime;"); 

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
			pstmt.setString(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					map.put("예약 번호", Integer.toString(rs.getInt("reserve_idx")));
					map.put("예약 일자", rs.getTimestamp("registerdate").toString());
					map.put("예약 시간", String.format("%02d:00", rs.getInt("reservetime")));
					map.put("예약자 아이디", rs.getString("customer_id"));
					map.put("예약자 명", rs.getString("customer_name"));
					map.put("예약 인원", rs.getString("reservepeople"));
					map.put("매출액", rs.getString("sales_amount"));

					reservationList.add(map);
				}
			}
		}
		return reservationList;
	}


	/**
	 * Owner가 내 식당의 정보와 매출액을 조회하는 메서드 입니다. <br>
	 * 내 식당이 존재하는지 existRestaurant() 메서드 먼저 실행 후 메서드를 실행합니다. <br>
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public List<Map<String, String>> findMyRestaurantAndSales(String accountId, int restaurantIdx) throws SQLException, NotFoundRestaurantException {
		if (existRestaurant(accountId) == false) {
			throw new NotFoundRestaurantException(accountId + "님의 식당이 존재하지 않습니다.");
		}

		List<Map<String, String>> listM = new ArrayList<Map<String, String>>();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT r.name AS name, r.type AS type, r.address AS address, r.tel AS tel, COALESCE(SUM(s.sales), 0) AS sales ");
		sql.append("FROM restaurant r ");
		sql.append("LEFT JOIN reserve rv ON r.idx = rv.restaurant_idx ");
		sql.append("LEFT JOIN sales s ON rv.idx = s.reserve_idx ");
		sql.append("WHERE r.account_id =? AND r.idx = ? ");
		sql.append("GROUP BY r.idx, r.name, r.type, r.address, r.tel;");

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
			
			con.setAutoCommit(false);
			pstmt.setString(1, accountId);
			pstmt.setInt(2, restaurantIdx);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", rs.getString("name"));
					map.put("type", rs.getString("type"));
					map.put("address", rs.getString("address"));
					map.put("tel", rs.getString("tel"));
					map.put("sales", String.valueOf(rs.getLong("sales")));

					listM.add(map);
				}
			}
		}

		return listM;
	}

	/**
	 * 나의 식당 정보를 변경하는 메서드입니다. <br>
	 * existRestaurant() 메서드를 실행한 후 식당이 존재할 경우에 해당 메서드가 실행이 됩니다. <br>
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @param newName
	 * @param newType
	 * @param newAddress
	 * @param newTel
	 * @param newSales
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public void updateMyRestaurantInfo(String accountId, int reservationIdx, String newName, String newType,
			String newAddress, String newTel) throws SQLException, NotFoundRestaurantException {

		if (existRestaurant(accountId) == false) {
			throw new NotFoundRestaurantException(accountId + "님의 식당이 존재하지 않습니다.");
		}

		Connection con = DatabaseUtil.getConnection();
		PreparedStatement pstmt = null;
		try {

			 con.setAutoCommit(false); // 트랜젝션 처리를 위한 자동 커밋 모드 해제

			String updateRestaurantInfoSql = "UPDATE restaurant SET name = ?, type = ?, address = ?, tel = ? WHERE account_id = ?";
			String updateSalesSql = "UPDATE sales SET sales = 0 WHERE reserve_idx = ?";

			pstmt = con.prepareStatement(updateRestaurantInfoSql);
			pstmt.setString(1, newName);
			pstmt.setString(2, newType);
			pstmt.setString(3, newAddress);
			pstmt.setString(4, newTel);
			pstmt.setString(5, accountId);
			pstmt .executeUpdate();
			
			pstmt.close();
			
			pstmt = con.prepareStatement(updateSalesSql);
			pstmt.setInt(1, reservationIdx);

			pstmt.executeUpdate();
			 con.commit(); // 정보 업데이트 내의 모든 세부 작업 정상 처리
			 
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			DatabaseUtil.closeAll(pstmt, con);
		}
	}

	/**
	 * Owner가 식당 메뉴를 생성하는 메소드 입니다. <br> 
	 * 메뉴 데이터 뿐만 아니라 레스토랑 ID도 필요합니다. <br>
	 * 
	 * @param menuVO
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public void createMenu(String accountId, String name, int price) throws SQLException, NotFoundRestaurantException {
		RestaurantVO restaurantVo = findMyRestaurant(accountId);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int menuId = 0;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "INSERT INTO menu(restaurant_idx, name, price) VALUES(?,?,?);";
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			pstmt.setString(2, name);
			pstmt.setInt(3, price);

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys(); // 발급된 메뉴 id를 반환받는다.
			if (rs.next())
				menuId = rs.getInt(1);
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}
	}

	/**
	 * 내 식당의 전체 메뉴를 조회하는 메서드 입니다. <br>
	 * Owner의 accountId를 통해 해당 계정의 restaurantId 정보를 알아냅니다. <br>
	 * "내 식당을 조회하는 메서드" 를 사용하여 식당 정보 객체를 받아온 후, id만 뺍니다. <br>
	 * 
	 * @param restaurantId
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public List<Map<String, String>> findMenu(String accountId) throws SQLException, NotFoundRestaurantException {

		RestaurantVO restaurantVo = findMyRestaurant(accountId);

		List<Map<String, String>> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 계속 맵을 만들어줘야 함
				// 한 객체의 주솟값을 계속 받아버리면 이전의 데이터는 사라짐 (덮어쓰기가 되므로)
				Map<String, String> map = new HashMap<String, String>();
				map.put("메뉴", rs.getString("name"));
				map.put("가격", rs.getString("price"));
				list.add(map);
			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return list;
	}

	/**
	 * Owner가 내 식당의 메뉴를 찾는 메소드 입니다. <br>
	 * 가격 수정 및 삭제 시 이 메소드가 먼저 호출됩니다. <br>
	 * 
	 * @throws SQLException
	 * @throws AccountNotFoundException
	 * @throws RestaurantNotFoundException
	 */

	public boolean findMenuDetail(String accountId, String name)
			throws NotFoundMenuException, SQLException, NotFoundAccountException, NotFoundRestaurantException {
		RestaurantVO restaurantVo = findMyRestaurant(accountId);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean menuExist = true;

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT name, price FROM menu WHERE restaurant_idx =? AND name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();

			if (rs.next() == false) {
				// 메뉴가 존재하지 않을 때
				menuExist = false;
				throw new NotFoundMenuException("메뉴가 존재하지 않습니다. 다시 입력하세요.");

			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return menuExist;
	}

	/**
	 * Owner가 내 식당의 메뉴 가격을 수정하는 메소드 입니다. <br>
	 * 
	 * @param menuVO
	 * @throws SQLException
	 * @throws NotFoundMenuException
	 * @throws NotFoundAccountException 
	 * @throws RestaurantNotFoundException
	 */

	public void updateMenu(String accountId, String name, int price)
			throws SQLException, NotFoundMenuException, NotFoundAccountException, NotFoundRestaurantException, NotFoundAccountException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		RestaurantVO restaurantVo = findMyRestaurant(accountId);
		// 메뉴가 존재하는지 확인
		findMenuDetail(accountId, name);

		try {
			con = DatabaseUtil.getConnection();
			String sql = "UPDATE menu SET price = ? WHERE name = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, restaurantVo.getRestaurantId());
			pstmt.setString(2, restaurantVo.getName());

			result = pstmt.executeUpdate();

		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}
	}

	/**
	 * Owner가 내 식당의 메뉴를 삭제하는 메소드 입니다. <br>
	 * 
	 * @param restaurantId
	 * @param name
	 * @throws SQLException
	 * @throws NotFoundMenuException
	 * @throws AccountNotFoundException
	 * @throws RestaurantNotFoundException
	 */

	public void deleteMenu(String accountId, String name)
			throws SQLException, NotFoundMenuException, NotFoundAccountException, NotFoundRestaurantException {
		Connection con = null;
		PreparedStatement pstmt = null;

		// 메뉴가 존재하는지 확인
		findMenuDetail(accountId, name);

		try {
			con = DatabaseUtil.getConnection();
			String sql = "DELETE FROM menu WHERE name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.executeUpdate();

		}

		finally {
			DatabaseUtil.closeAll(pstmt, con);
		}

	}

	/**
	 * Owner가 내 식당의 리뷰를 조회하는 메소드 입니다. <br>
	 * 리뷰 메소드는 예약 id를 가지고 있으므로 이를 가지고 리뷰를 조회해야 합니다. <br>
	 * 
	 * @param restaurantId
	 * @throws SQLException
	 * @throws RestaurantNotFoundException
	 */
	public List findMyRestaurantReview(String accountId) throws SQLException, NotFoundRestaurantException {

		List<Map<String, String>> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		RestaurantVO resVo = findMyRestaurant(accountId);

		try {
			con = DatabaseUtil.getConnection();
			String sql = "SELECT rsv.account_id, rvw.star, rvw.comment, rvw.registerdate FROM reserve rsv JOIN review rvw ON rsv.idx = rvw.idx WHERE rsv.restaurant_idx = ?;";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, resVo.getRestaurantId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 계속 맵을 만들어줘야 함
				// 한 객체의 주솟값을 계속 받아버리면 이전의 데이터는 사라짐 (덮어쓰기가 되므로)
				Map<String, String> map = new HashMap<String, String>();
				map.put("작성자", rs.getString("rsv.account_id"));
				map.put("별점", rs.getString("rvw.star"));
				map.put("내용", rs.getString("rvw.comment"));
				map.put("작성일자", rs.getString("rvw.registerdate"));
				list.add(map);
			}
		}

		finally {
			DatabaseUtil.closeAll(rs, pstmt, con);
		}

		return list;

	}

	/**
	 * Owner가 예약자 리스트를 확인 한 후 선택한 예약자의 매출액을 업데이트 시켜주는 메서드 입니다. <br>
	 * existRestaurantReservation() 메서드를 먼저 실행한 후 예약이 존재할 경우 해당 메서드를 실행합니다. <br>
	 * 예약이 존재하지 않을 경우 NoReservationException로 예외 처리를 합니다. <br>
	 * 
	 * @param accountId
	 * @param reservationIdx
	 * @param newSale
	 * @throws RestaurantNotFoundException
	 * @throws SQLException
	 */
	public void updateCustomerSale(String accountId, int reservationIdx, int newSale)
			throws NoReservationException, SQLException {

		if (existRestaurantReservation(reservationIdx) == false) {
			throw new NoReservationException(reservationIdx + " 번의 예약은 존재하지 않습니다.");
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE sales SET sales = ? WHERE reserve_idx = ? ");
		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
			pstmt.setInt(1, newSale);
			pstmt.setInt(2, reservationIdx);

			int row = pstmt.executeUpdate();
			if (row == 0) {
				throw new SQLException("매출 정보 삽입에 실패했습니다.");
			}
		}
	}

	/**
	 * Owner가 예약자 명단에 매출액을 입력하는 메소드를 실행할 때 선택된 예약자 명단을 조회하기 위해 실행하는 메서드 입니다. <br>
	 * 매출액을 업데이트 하기 위해서 이 메소드를 먼저 실행합니다. <br>
	 * 
	 * @param accountId
	 * 
	 * @param reservationIdx
	 * @return
	 * @throws SQLException
	 * @throws NoReservationException
	 */
	public Map<String, String> findSelectedCustomer(String accountId, int reservationIdx)
			throws SQLException, NoReservationException {

		if (existRestaurantReservation(reservationIdx) == false) {
			throw new NoReservationException(reservationIdx + " 번의 예약은 존재하지 않습니다.");
		}

		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT rs.idx AS reserve_idx, rs.account_id AS customer_id, a.name AS customer_name, rs.reservepeople, rs.reservedate, ");
		sql.append(
				"rs.reservetime, rs.registerdate, rt.idx AS restaurant_idx, rt.name AS restaurant_name, o.name AS owner_name ");
		sql.append("FROM reserve rs ");
		sql.append("LEFT JOIN restaurant rt ON rs.restaurant_idx = rt.idx ");
		sql.append("LEFT JOIN account a ON rs.account_id = a.id ");
		sql.append("LEFT JOIN account o ON rt.account_id = o.id ");
		sql.append("WHERE rt.account_id = ? AND rs.idx = ? ");
		sql.append("ORDER BY rs.reservedate, rs.reservetime;");

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
			pstmt.setString(1, accountId);
			pstmt.setInt(2, reservationIdx);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					map.put("예약 번호", Integer.toString(rs.getInt("reserve_idx")));
					map.put("예약 일자", rs.getTimestamp("registerdate").toString());
					map.put("예약 시간", String.format("%02d:00", rs.getInt("reservetime")));
					map.put("예약자 아이디", rs.getString("customer_id"));
					map.put("예약자 명", rs.getString("customer_name"));
					map.put("예약 인원", rs.getString("reservepeople"));
					map.put("식당 이름", rs.getString("restaurant_name"));
					map.put("식당 대표자", rs.getString("owner_name"));
				}
			}
		}

		return map;
	}

	/**
	 * Owner가 내 식당의 최다 예약자를 조회할 수 있는 메소드 입니다. <br>
	 * 내 식당이 존재하는지 existRestaurant() 메서드를 실행 후 존재할 경우 이 메서드를 실행합니다. <br>
	 * 
	 * @param accountId
	 * @return
	 * @throws RestaurantNotFoundException
	 * @throws SQLException
	 */
	public List<Map<String, String>> findMyRestaurantReservationMostList(String accountId)
			throws NotFoundRestaurantException, SQLException {
		if (existRestaurant(accountId) == false) {
			throw new NotFoundRestaurantException(accountId + "님의 식당이 존재하지 않습니다.");
		}

		List<Map<String, String>> reservationList = new ArrayList<Map<String, String>>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rs.restaurant_idx, rs.account_id, a.name AS customer_name,");
		sql.append(" COUNT(*) AS reserve_count, rt.idx, rt.account_id FROM reserve rs");
		sql.append(" LEFT JOIN account a ON rs.account_id = a.id ");
		sql.append(" LEFT JOIN restaurant rt ON rt.idx = rs.restaurant_idx");
		sql.append(" WHERE rt.account_id = ? GROUP BY rs.restaurant_idx, rs.account_id, a.name, rt.idx, rt.account_id");
		sql.append(" ORDER BY reserve_count DESC LIMIT 1;");

		try (Connection con = DatabaseUtil.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql.toString());) {
			pstmt.setString(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					map.put("예약자 아이디", rs.getString("rs.account_id"));
					map.put("예약자 명", rs.getString("customer_name"));
					map.put("총 예약 횟수", Integer.toString(rs.getInt("reserve_count")));

					reservationList.add(map);
				}
			}
		}
		return reservationList;
	}

}