package com.rozsa.controller;

import com.rozsa.business.api.UserBusiness;
import com.rozsa.controller.mapper.UserDtoMapper;
import com.rozsa.dto.UserDto;
import com.rozsa.repository.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserBusiness userBusiness;
    private final UserDtoMapper userDtoMapper;

    @PostMapping
    public void create(@RequestBody UserDto userDto) {
        User user = userDtoMapper.toUser(userDto);
        userBusiness.create(user);
    }

    @PatchMapping
    public void update(@RequestBody UserDto userDto) {
        User user = userDtoMapper.toUser(userDto);
        userBusiness.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userBusiness.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        User user = userBusiness.get(id);
        return userDtoMapper.fromUser(user);
    }

    @GetMapping("/all")
    public List<UserDto> getAll(@Param("start") int start, @Param("limit") int limit) {
        List<User> users = userBusiness.getAll(start, limit);

        return userDtoMapper.fromUsers(users);
    }
}
