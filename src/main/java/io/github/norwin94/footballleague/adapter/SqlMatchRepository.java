package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlMatchRepository extends MatchRepository, JpaRepository<Match, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from match where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    @Query(nativeQuery = true, value =
                    "SELECT t.TEAM_NAME,\n" +
            "               SUM(CASE WHEN m.HOME_TEAM_ID = t.ID THEN 1 ELSE 0 END) homeMatches,\n" +
            "               SUM(CASE\n" +
            "\t                   WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 3\n" +
            "\t                   WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
            "\t\t           ELSE 0\n" +
            "                       END) pointsHome,\n" +
            "               SUM(CASE\n" +
            "\t\t           WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 1\n" +
            "\t\t           ELSE 0\n" +
            "                       END) wonHome,\n" +
            "             SUM(CASE\n" +
            "\t\t          WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
            "\t\t          ELSE 0\n" +
            "                     END) drawHome,\n" +
            "             SUM(CASE\n" +
            "\t\t         WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 1\n" +
            "\t\t         ELSE 0\n" +
            "                 END) lostHome,\n" +
            "             SUM(m.HOME_SCORE) scored\n" +
            "          FROM match m,\n" +
            "                     team t\n" +
            "      WHERE t.ID = m.HOME_TEAM_ID \n" +
            "GROUP BY t.TEAM_NAME \n" +
            "ORDER BY pointsHome DESC, scored DESC")
    List<String[]> getTableHome();

    @Override
    @Query(nativeQuery = true, value =
            "                    SELECT t.TEAM_NAME,\n" +
            "                           SUM(CASE WHEN m.HOME_TEAM_ID = t.ID THEN 1 ELSE 0 END) awayMatches,\n" +
            "                           SUM(CASE\n" +
            "                               WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 3\n" +
            "                               WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
            "                       ELSE 0\n" +
            "                                   END) pointsAway,\n" +
            "                           SUM(CASE\n" +
            "                       WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 1\n" +
            "                       ELSE 0\n" +
            "                                   END) wonHome,\n" +
            "                         SUM(CASE\n" +
            "                      WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
            "                      ELSE 0\n" +
            "                                 END) drawAway,\n" +
            "                         SUM(CASE\n" +
            "                     WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 1\n" +
            "                     ELSE 0\n" +
            "                             END) lostAway,\n" +
            "                         SUM(m.AWAY_SCORE) scored\n" +
            "                      FROM match m,\n" +
            "                                 team t\n" +
            "                  WHERE t.ID = m.HOME_TEAM_ID \n" +
            "            GROUP BY t.TEAM_NAME \n" +
            "            ORDER BY pointsAway DESC, scored DESC")
    List<String[]> getTableAway();


            @Override
    @Query(nativeQuery = true, value =
            "SELECT tmp.TEAM_NAME, " +
                    "SUM(homeMatches) matches, " +
                    "SUM(tmp.pointsHome) points, " +
                    "SUM(tmp.wonHome) won, " +
                    "SUM(tmp.drawHome) draw, " +
                    "SUM(tmp.lostHome) lost, " +
                    "SUM(tmp.scored) scored " +
            "FROM (\n" +
                                    "SELECT t.TEAM_NAME,\n" +
                "                           SUM(CASE\n" +
                "                               WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 3\n" +
                "                               WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
                "                       ELSE 0\n" +
                "                                   END) pointsHome,\n" +
                "                           SUM(CASE\n" +
                "                       WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 1\n" +
                "                       ELSE 0\n" +
                "                                   END) wonHome,\n" +
                "                         SUM(CASE\n" +
                "                      WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
                "                      ELSE 0\n" +
                "                                 END) drawHome,\n" +
                "                         SUM(CASE\n" +
                "                     WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 1\n" +
                "                     ELSE 0\n" +
                "                             END) lostHome,\n" +
                "                         SUM(m.HOME_SCORE) scored,\n" +
                "                         SUM(CASE WHEN m.HOME_TEAM_ID = t.ID THEN 1 ELSE 0 END) homeMatches\n" +
                "                      FROM match m,\n" +
                "                                 team t\n" +
                "                  WHERE t.ID = m.HOME_TEAM_ID \n" +
                "            GROUP BY t.TEAM_NAME \n" +
                "UNION ALL\n" +
                                    "SELECT t.TEAM_NAME,\n" +
                "                           SUM(CASE\n" +
                "                               WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 3\n" +
                "                               WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
                "                       ELSE 0\n" +
                "                                   END) pointsAway,\n" +
                "                           SUM(CASE\n" +
                "                       WHEN m.HOME_SCORE < m.AWAY_SCORE THEN 1\n" +
                "                       ELSE 0\n" +
                "                                   END) wonAway,\n" +
                "                         SUM(CASE\n" +
                "                      WHEN m.HOME_SCORE = m.AWAY_SCORE THEN 1\n" +
                "                      ELSE 0\n" +
                "                                 END) drawAway,\n" +
                "                         SUM(CASE\n" +
                "                     WHEN m.HOME_SCORE > m.AWAY_SCORE THEN 1\n" +
                "                     ELSE 0\n" +
                "                             END) lostAway,\n" +
                "                         SUM(m.AWAY_SCORE) scored,\n" +
                "                        SUM(CASE WHEN m.HOME_TEAM_ID = t.ID THEN 1 ELSE 0 END) awayMatches\n" +
                "                      FROM match m,\n" +
                "                                 team t\n" +
                "                  WHERE t.ID = m.AWAY_TEAM_ID \n" +
                "            GROUP BY t.TEAM_NAME ) tmp\n" +
            "GROUP BY tmp.TEAM_NAME ORDER BY points DESC, scored DESC")
    List<String[]> getFullTable();

}
