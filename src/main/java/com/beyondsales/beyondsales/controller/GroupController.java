package com.beyondsales.beyondsales.controller;

import com.beyondsales.beyondsales.entity.Group;
import com.beyondsales.beyondsales.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // === GET - TOUS LES GROUPES ===
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    // === GET - GROUPES ACTIFS ===
    @GetMapping("/active")
    public ResponseEntity<List<Group>> getActiveGroups() {
        List<Group> groups = groupService.findActiveGroups();
        return ResponseEntity.ok(groups);
    }

    // === GET - GROUPE PAR CODE ===
    @GetMapping("/{groupCode}")
    public ResponseEntity<Group> getGroupByCode(@PathVariable String groupCode) {
        Optional<Group> group = groupService.findByGroupCode(groupCode);
        return group.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST - CRÉER UN GROUPE ===
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group,
                                         @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Group savedGroup = groupService.createGroup(group, currentUser);
            return ResponseEntity.ok(savedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === PUT - METTRE À JOUR UN GROUPE ===
    @PutMapping("/{groupCode}")
    public ResponseEntity<?> updateGroup(@PathVariable String groupCode,
                                         @RequestBody Group groupDetails,
                                         @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            Group updatedGroup = groupService.updateGroup(groupCode, groupDetails, currentUser);
            return ResponseEntity.ok(updatedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // === DELETE - SUPPRIMER UN GROUPE ===
    @DeleteMapping("/{groupCode}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupCode,
                                         @RequestHeader(value = "X-Username", required = false) String username) {
        try {
            String currentUser = username != null ? username : "system";
            groupService.deleteGroup(groupCode, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === RECHERCHE PAR DESCRIPTION ===
    @GetMapping("/search/description")
    public ResponseEntity<List<Group>> searchByDescription(@RequestParam String keyword) {
        List<Group> groups = groupService.findByGroupDescriptionContainingIgnoreCase(keyword);
        return ResponseEntity.ok(groups);
    }

    // === RECHERCHE PAR CODE ===
    @GetMapping("/search/code")
    public ResponseEntity<List<Group>> searchByCode(@RequestParam String groupCode) {
        List<Group> groups = groupService.findByGroupCodeContainingIgnoreCase(groupCode);
        return ResponseEntity.ok(groups);
    }
}