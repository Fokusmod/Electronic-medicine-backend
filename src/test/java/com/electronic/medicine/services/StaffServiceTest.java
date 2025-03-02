package com.electronic.medicine.services;

import com.electronic.medicine.DTO.ReceptionDto;
import com.electronic.medicine.DTO.ReceptionRequest;
import com.electronic.medicine.DTO.SpecialistReception;
import com.electronic.medicine.entity.Reception;
import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.Speciality;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @InjectMocks
    private StaffService staffService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private UserService userService;
    @Mock
    private ReceptionService receptionService;

    @Test
    void loadStaffServiceContext() {
        assertNotNull(staffService);
        assertNotNull(userRepository);
        assertNotNull(userService);
        assertNotNull(roleService);
    }


    @Test
    @DisplayName("getUsersByRole успешное выполнение")
    void getUsersByRole_Success() {
        String title = "ROLE";
        Role role = new Role(1L, title);
        List<User> testList = List.of(
                mock(User.class),
                mock(User.class),
                mock(User.class));
        when(roleService.getByTitle(title)).thenReturn(role);
        when(userRepository.findAllByRoles(role)).thenReturn(testList);
        var result = staffService.getUsersByRole(title);
        assertEquals(testList, result);
        verify(roleService).getByTitle(title);
        verify(userRepository).findAllByRoles(role);
        verifyNoMoreInteractions(roleService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("findStaffUsers успешное выполнение")
    void findStaffUsers_Success() {
        String title = "ROLE";
        String findText = "E";
        var result = staffService.findStaffUsers(title, findText);
        assertEquals(new ArrayList<>(), result);
        verify(roleService).getByTitle(title);
        verify(userRepository).findAllByRoles(null);

    }

    @Test
    @DisplayName("getAllStaff успешное выполнение")
    void getAllStaff_Success() {
        String titleSpec = "SPECIALIST";
        String titleAdmin = "ADMIN";
        Role role1 = new Role(1L, titleAdmin);
        Role role2 = new Role(2L, titleSpec);
        when(roleService.getByTitle(titleAdmin)).thenReturn(role1);
        when(roleService.getByTitle(titleSpec)).thenReturn(role2);
        var result = staffService.getAllStaff();
        assertEquals(new ArrayList<>(), result);
        verify(roleService, times(2)).getByTitle(titleAdmin);
        verify(roleService, times(2)).getByTitle(titleSpec);
    }


    @Test
    @DisplayName("getAllSpecialistByParam успешное выполнение")
    void getAllSpecialistByParam_Success() {
        String title = "SPECIALIST";
        Role role = new Role(1L, title);
        List<User> testList = List.of(
                mock(User.class),
                mock(User.class),
                mock(User.class));
        when(roleService.getByTitle(title)).thenReturn(role);
        when(userRepository.findAllByRoles(role)).thenReturn(testList);
        var result = staffService.getAllSpecialistByParam("Онколог");
        assertEquals(new ArrayList<>(), result);
        verify(roleService).getByTitle(title);
        verify(userRepository).findAllByRoles(role);
    }


    @Test
    @DisplayName("setSpecialistReception успешное выполнение")
    void setSpecialistReception() {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime = LocalDateTime.of(2025,2,28,10,0);
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);
        User user = new User();
        user.setId(1L);
        user.setReceptions(new HashSet<>());
        Reception reception = new Reception(null, Date.from(localDateTime.toInstant(zoneOffset)));

        ReceptionRequest request = new ReceptionRequest();
        request.setId(1L);
        request.setTime(localDateTime.getHour() + ":" + localDateTime.getMinute());
        request.setDate(localDateTime.getYear() + "-" + localDateTime.getMonth().getValue() + "-" + localDateTime.getDayOfMonth());

        when(userService.findById(1L)).thenReturn(user);
        staffService.setSpecialistReception(request);
        verify(receptionService).saveReception(reception);
        verify(userService).saveUser(user);
    }

    @Test
    @DisplayName("getSpecialistReceptionByDateThreeParam успешное выполнение")
    void getSpecialistReceptionByDateThreeParam_Success() {
        String spec = "someOne";
        String role = "SPECIALIST";
        String fullName = "false";
        Date date = new Date();
        User user = new User();
        user.setRoles(Set.of(new Role(1L,role)));
        user.setSpecialities(Set.of(new Speciality(1L,spec)));
        user.setReceptions(new HashSet<>());

        when(staffService.getUsersByRole(role)).thenReturn(List.of(user));
        when(staffService.findStaffUsers("SPECIALIST","text")).thenReturn(List.of(user));
        var result = staffService.getSpecialistReceptionByDate(spec,fullName,date);
        assertEquals(new SpecialistReception(null,new ArrayList<>()),result);
    }


    @Test
    @DisplayName("getSpecialistReceptionByDateTwoParam успешное выполнение")
    void getSpecialistReceptionByDateTwoParam_Success() {
        String role = "SPECIALIST";
        String spec = "someOne";
        Date date = new Date();

        User user = new User();
        user.setId(1L);
        user.setRoles(Set.of(new Role(1L,role)));
        user.setSpecialities(Set.of(new Speciality(1L,spec)));
        user.setReceptions(new HashSet<>());

        when(userService.findById(1L)).thenReturn(user);
        var result = staffService.getSpecialistReceptionByDate(1L,date);
        assertEquals(new SpecialistReception(1L,new ArrayList<>()),result);
    }

    @Test
    @DisplayName("getReceptionByDate успешное выполнение")
    void getReceptionByDate_Success() {
        Date date = new Date();
        User user = new User();
        Set<Reception> receptions = new HashSet<>();
        receptions.add(new Reception(1L, date));
        receptions.add(new Reception(2L, date));
        receptions.add(new Reception(3L, date));
        receptions.add(new Reception(4L, date));
        user.setReceptions(receptions);
        var result = staffService.getReceptionByDate(user,date);
        assertEquals(receptions.size(),result.size());
    }

    @Test
    @DisplayName("setAdminRole успешное выполнение")
    void setAdminRole_Success() {
        Long id = 123L;
        String roleTitle = "ADMIN";
        Role role = new Role(1L,roleTitle);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User();
        user.setRoles(roles);
        when(roleService.getByTitle(roleTitle)).thenReturn(role);
        when(userService.findById(id)).thenReturn(user);
        staffService.setAdminRole(id);
        verify(roleService).getByTitle(roleTitle);
        verify(userService).findById(id);
        verify(userService).saveUser(user);
    }


    @Test
    @DisplayName("deleteAdminRole успешное выполнение")
    void deleteAdminRole_Success() {
        Long id = 321L;
        String roleTitle = "ADMIN";
        String userRole = "USER";
        Role role = new Role(1L,roleTitle);
        Role role2 = new Role(2L,userRole);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User();
        user.setRoles(roles);
        when(roleService.getByTitle(roleTitle)).thenReturn(role);
        when(userService.findById(id)).thenReturn(user);
        when(roleService.getByTitle(userRole)).thenReturn(role2);
        staffService.deleteAdminRole(id);
        verify(roleService).getByTitle(roleTitle);
        verify(userService).findById(id);
        verify(roleService).getByTitle(userRole);
    }

    @Test
    void downgradeSpecialistToUser() {
        Long id = 111L;
        String roleTitle = "USER";
        Role role = new Role(1L,roleTitle);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User();
        user.setId(id);
        user.setRoles(roles);
        when(roleService.getByTitle(roleTitle)).thenReturn(role);
        when(userService.findById(id)).thenReturn(user);
        staffService.downgradeSpecialistToUser(id);
        verify(roleService).getByTitle(roleTitle);
        verify(userService).findById(id);
    }
}