package io.github.norwin94.footballleague;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("team")
public class TeamConfigurationProperties {
    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public static class Template {
        private boolean allowMultipleTeams;

        public boolean isAllowMultipleTeams() {
            return allowMultipleTeams;
        }

        public void setAllowMultipleTeams(boolean allowMultipleTeams) {
            this.allowMultipleTeams = allowMultipleTeams;
        }
    }
}
