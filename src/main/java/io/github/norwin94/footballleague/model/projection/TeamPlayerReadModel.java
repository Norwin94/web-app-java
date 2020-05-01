package io.github.norwin94.footballleague.model.projection;

import io.github.norwin94.footballleague.model.Player;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

public class TeamPlayerReadModel {
    private String firstName;
    private String lastName;
    private Date birthDate;

    public TeamPlayerReadModel(Player source) {
        firstName = source.getFirstName();
        lastName = source.getLastName();
        birthDate = source.getBirthDate();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
