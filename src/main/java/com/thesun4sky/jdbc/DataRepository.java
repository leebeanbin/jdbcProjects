package com.thesun4sky.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 테이블 생성
	public void createTable() {
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (id SERIAL, name VARCHAR(255))");
	}

	// 사용자 추가 (Create)
	public void insertUser(String name) {
		jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", name);
	}

	// 사용자 ID로 이름 조회 (Read)
	public String findUserNameById(Long id) {
		// 쿼리를 실행할 때의 객체를 Object type으로 받고 리턴합니다.
		return jdbcTemplate.queryForObject(
			"SELECT name FROM users WHERE id = ?",
			new Object[]{id},
			String.class
		);
	}

	// 사용자 이름 변경 (Update)
	public void updateUser(Long id, String newName) {
		jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", newName, id);
	}

	// 사용자 삭제 (Delete)
	public void deleteUser(Long id) {
		jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
	}
}
