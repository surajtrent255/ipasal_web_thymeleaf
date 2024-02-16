package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.UserDTO;

public interface UserService {
    public Response<?> registerUser(UserDTO user);
    public Response<?> checkUserName(String username);
    public Response<?> checkDuplicateEmail(String email);
    public Response<?> activateUserAccount(String token);
    public Response<?> verifyPasswordResetToken(String token);
    public Response<?> sendPasswordResetEmail(String email);
    public Response<?> resetPasswordConfirm(String password);
    public Response<?> updateUserPassword(Integer userId, String password);
    public Response<?> getAllWholeSeller();
    public Response<?> searchWholeSeller(Integer id);
    public Response<?> getAllCustomers();
	public Response<?> updateUserInfoById(Integer userId, UserDTO user);
}