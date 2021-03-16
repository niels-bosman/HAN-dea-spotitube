package spotitube;

import org.junit.jupiter.api.Test;
import spotitube.dao.UserDAO;
import spotitube.domain.User;
import spotitube.services.LoginService;
import spotitube.dto.LoginRequestDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import spotitube.dto.LoginResponseDTO;

public class LoginTest
{

    private LoginRequestDTO loginRequestDTO;
    private LoginService loginService;

    @BeforeEach
    public void setup()
    {
        this.loginService = new LoginService();
        this.loginRequestDTO = new LoginRequestDTO();
        this.loginRequestDTO.user = "niels";
        this.loginRequestDTO.password = "niels";
    }

    @Test
    public void successfulLogin()
    {
        // Arrange
        User user = new User();
        user.setName(this.loginRequestDTO.user);
        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.authenticate(this.loginRequestDTO.user, this.loginRequestDTO.password)).thenReturn(user);
        this.loginService.setUserDAO(userDAOMock);

        // Act
        Response response = this.loginService.login(this.loginRequestDTO);
        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertEquals(this.loginRequestDTO.user, loginResponseDTO.user);
    }

    @Test
    public void failedLogin()
    {
        // Arrange
        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.authenticate(this.loginRequestDTO.user, this.loginRequestDTO.password)).thenReturn(null);
        this.loginService.setUserDAO(userDAOMock);

        // Act
        Response response = this.loginService.login(this.loginRequestDTO);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
    }
}
