package com.smart.inventory.user_service.infrastructure.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.demo.dboperation.CollectionUtils;
import com.demo.dboperation.DataType;
import com.demo.dboperation.Dboperation;
import com.demo.dboperation.ReflectionMapper;
import com.demo.dboperation.SQLParameter;
import com.demo.hashutility.HashAlgorithm;
import com.demo.hashutility.HashGenerator;
import com.smart.inventory.user_service.domain.model.User;
import com.smart.inventory.user_service.domain.repository.UserRepository;
import com.smart.inventory.user_service.infrastructure.config.DbOperationFactory;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private DbOperationFactory dbfactory = null;

	public UserRepositoryImpl(DbOperationFactory dbfactory) {
		this.dbfactory = dbfactory;
	}

	@SuppressWarnings("finally")
	@Override
	public User save(User user) throws Exception {

		Dboperation db = dbfactory.createDbOperation();

		try {
			if (user.getPassword() != null) {
				String salt = user.getUsername();
				user.setPassword(HashGenerator.hashWithSaltAndPepper(user.getPassword(), salt, HashAlgorithm.sha3512));
			}
			db.insertRecord(user, "Users");

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		}

		return user;
	}

	@Override
	public boolean existsByUsernameAndEmail(String username, String email) throws SQLException {

		Dboperation db = dbfactory.createDbOperation();

		ArrayList<SQLParameter> parameters = new ArrayList<SQLParameter>();

		parameters.add(new SQLParameter(username, DataType.String, 1));

		parameters.add(new SQLParameter(email, DataType.String, 2));

		int output = 0;

		try {
			output = db.executeScalarQuery(
					"SELECT CASE WHEN EXISTS(SELECT 1 FROM Users WITH(NOLOCK) WHERE username = ? OR Email = ?) THEN 1 ELSE 0 END",
					parameters);
		} catch (SQLException ex) {
			throw ex;
		}

		return output == 0 ? false : true;
	}

	@Override
	public User getUser(String username, String password) throws Exception {

		Dboperation db = dbfactory.createDbOperation();

		ArrayList<SQLParameter> parameters = new ArrayList<SQLParameter>();

		parameters.add(new SQLParameter(username, DataType.String, 1));
	    System.out.println("Username:" + username);	
		String salt = username;
		String hashpassword = HashGenerator.hashWithSaltAndPepper(password, salt, HashAlgorithm.sha3512);
		System.out.println("password:" + hashpassword);
		parameters.add(new SQLParameter(hashpassword, DataType.String, 2));
		
		User user = null;
		try
		{
			
			Map<Integer, Object> resultMap	= db.executeQueryGeneric("Select * from Users where username = ? and password = ?", parameters);
			System.out.println("Hello called execute method query");	
			List<User> users = ReflectionMapper.getMappedList(resultMap, 0, User.class);
			
			if(users != null && users.size() > 0)
			{
				user = users.get(0);
			}
			System.out.println("Got user from query" + user);	
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return user;
	}

}
