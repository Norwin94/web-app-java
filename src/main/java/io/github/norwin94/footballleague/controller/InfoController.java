package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.TeamConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
 class InfoController {
    private DataSourceProperties dataSource;
    private TeamConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSource, final TeamConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
     String url() {
        return dataSource.getUrl();
     }

    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTeams();
    }
}
