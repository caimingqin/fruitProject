package com.cmd;

import org.springframework.beans.factory.annotation.Autowired;

public class SaveCityCommand {

	@Autowired
	private JdbcQuery jdbcQuery;

	public JdbcQuery getJdbcQuery() {
		return jdbcQuery;
	}
	
}
