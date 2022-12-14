package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.RoleService;
import com.petrovskiy.epm.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Page<RoleDto> findAllRoles(Pageable pageable){
        return roleService.findAll(pageable);
    }

    @GetMapping("{id}")
    public RoleDto findRoleById(@PathVariable Long id){
        RoleDto roleDto = roleService.findById(id);
        return roleDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDto createRole(@RequestBody RoleDto role){
        return roleService.create(role);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleDto updateRole(@PathVariable Long id,@RequestBody RoleDto roleDto){
        return roleService.update(id,roleDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRole(@PathVariable Long id){
        roleService.delete(id);
    }
}
