package spring05.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import spring05.user.vo.Levels;
import spring05.user.vo.User;

public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void deleteAll() {
		jdbcTemplate.update("delete from users");
	}

	public int getCount() {
		int count = jdbcTemplate.queryForObject("select count(*) from users",
				Integer.class);
		return count;
	}

	public void add(User user) {
		jdbcTemplate.update("insert into users values (?,?,?,?,?,?,?)", user
				.getId(), user.getName(), user.getPassword(), user.getLevels()
				.intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}

	public void update(User user) {
		jdbcTemplate
				.update("update users set name = ?, password = ?, levels = ?, login = ?, recommend= ?, email = ? where id = ?",
						user.getName(), user.getPassword(), user.getLevels()
								.intValue(), user.getLogin(), user
								.getRecommend(),user.getEmail(), user.getId());
	}

	RowMapper<User> mapper = new RowMapper<User>() {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevels(Levels.valueOf(rs.getInt("levels")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			
			return user;
		}

	};

	public User get(String id) {
		return jdbcTemplate.queryForObject("select * from users where id = ?",
				new Object[] { id }, this.mapper);
	}

	public List<User> getAll() {
		return jdbcTemplate.query("select * from users order by id",
				this.mapper);
	}
}
