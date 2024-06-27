// JdbcApplication.java

package com.thesun4sky.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) throws SQLException {
		// 어플리케이션 실행 컨텍스트 생성
		SpringApplication.run(JdbcApplication.class, args);

		// 데이터베이스 연결정보
		String url = "jdbc:h2:mem:JAKE"; 	// spring.datasource.url
		String username = "sa";				// spring.datasource.username

		// connection 얻어오기
		// Try문 안에 괄호를 통해서 사용할 리소스를 선언하여 자동으로 close하게 만듭니다.
		try (Connection connection = DriverManager.getConnection(url, username, null)) {
			try {
				// 테이블 생성 (statement 생성)
				String creatSql = "CREATE TABLE USERS (id SERIAL, username varchar(255))";
				// 쿼리문을 미리 보내서 준비를 하게 만드는 것 입니다.
				try (PreparedStatement statement = connection.prepareStatement(creatSql)) {
					// 위의 코드로 만든 쿼리를 실행하게 해줍니다.
					statement.execute();
				}

				// 데이터 추가 (statement 생성)
				String insertSql = "INSERT INTO USERS (username) VALUES ('teasun kim')";
				try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
					for (int i = 0; i < 10; i++) {
						statement.execute();
					}
				}

				// 데이터 조회 (statement 생성 후 rs = resultSet 수신 & next() 조회)
				String selectSql = "SELECT * FROM USERS";
				try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
					// resultSet 객체로 받아서 쿼리의 결과를 보려고 하는 것 입니다.
					// createStatement의 경우 쿼리를 만들 준비를 하고 excuteQuery의 괄호안의 쿼리를 읽어 분석후 사용합니다.
					var rs = statement.executeQuery();
					// low 별로 값을 읽어 출력합니다.
					while (rs.next()) {
						System.out.printf("%d. %s\n", rs.getInt("id"), rs.getString("username"));
					}
				}
				// 쿼리의 오류를 잡는 오류처리
			} catch (SQLException e) {
				if (e.getMessage().equals("ERROR: relation \"account\" already exists")) {
					System.out.println("USERS 테이블이 이미 존재합니다.");
				} else {
					throw new RuntimeException();
				}
			}
		}
	}
}