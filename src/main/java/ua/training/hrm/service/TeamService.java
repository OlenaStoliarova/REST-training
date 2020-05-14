package ua.training.hrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.training.hrm.entity.Employee;
import ua.training.hrm.entity.Team;
import ua.training.hrm.exception.NoEntityFoundException;
import ua.training.hrm.repository.TeamRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NoEntityFoundException("There is no team with provided id (" + id + ")"));
    }

    public Team create(Team team) {
        ensureTeamIsNew(team);
        return teamRepository.save(team);
    }

    public Team edit(Team teamUpdate) {
        Optional<Team> team = teamRepository.findById(teamUpdate.getId());

        if (team.isPresent()){
            Team t = team.get();
            copyUpdatedFields(teamUpdate, t);
            return teamRepository.save(t);
        }

        return teamRepository.save(teamUpdate);
    }

    public boolean deleteTeam(Long teamId) {

        if (teamRepository.existsById(teamId))
            teamRepository.deleteById(teamId);

        return !teamRepository.existsById(teamId);
    }

    public List<Employee> getTeamMembers(Long id) {
        return teamRepository.findById(id)
                .map(Team::getMembers)
                .map(Collection::stream)
                .map(s -> s.collect(Collectors.toList()))
                .orElseThrow(() -> new NoEntityFoundException("There is no team with provided id (" + id + ")"));
    }


    private void ensureTeamIsNew(Team team) {
        if (team.getId() != null) {
            team.setId(null);
        }
    }

    private void copyUpdatedFields(Team updateSource, Team targetTeam) {
        if (updateSource.getName() != null)
            targetTeam.setName(updateSource.getName());

        if (updateSource.getProject() != null)
            targetTeam.setProject(updateSource.getProject());

        if (updateSource.getScrumMaster() != null)
            targetTeam.setScrumMaster(updateSource.getScrumMaster());

        if (updateSource.getMembers() != null)
            targetTeam.setMembers(updateSource.getMembers());
    }
}
