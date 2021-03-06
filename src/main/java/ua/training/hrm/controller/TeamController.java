package ua.training.hrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.entity.Team;
import ua.training.hrm.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamService teamService;


    @GetMapping
    public List<Team> showAllTeamsList() {
        return teamService.getAllTeams();
    }

    @PostMapping
    public Team addTeam(@RequestBody Team newTeam) {
        return teamService.create(newTeam);
    }

    @GetMapping("/{id}")
    public Team showOneTeam(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PutMapping("/{id}")
    public Team editTeam(@RequestBody Team newTeam, @PathVariable Long id) {
        newTeam.setId(id);
        return teamService.edit(newTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        boolean wasDeleted = teamService.deleteTeam(id);

        if (wasDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/{id}/members")
    public List<Employee> showTeamMembersList(@PathVariable Long id) {
        return teamService.getTeamMembers(id);
    }
}
