package service;


import dto.LoginDTO;
import dto.UserDTO;
import com.example.Registation.payload.response.LoginMesage;

public class EmployeeService {
    String addEmployee(UserDTO employeeDTO);
    LoginMesage loginEmployee(LoginDTO loginDTO);
}
