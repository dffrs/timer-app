package com.dffrs.util.db.connector;

import com.dffrs.comp.time.Time;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectorTest {

    private void initDBWithData() {
        DBConnector.saveTimeToDB("TEST", "TEST Description", new Time(3600));
    }


    @Test
    public void testCallSaveTimeTest() {
        Time time = new Time(86200);

        String projectName = "dummy";
        String projectDesc = "dummy Description";

        DBConnector.saveTimeToDB(projectName, projectDesc, time);
    }

    @Test
    public void testGetUserTimeSpentPerProjectTest() {
        initDBWithData();

        Map<String, List<String>> map = DBConnector.getUserProjects();
        System.out.println(map);

        assertFalse(map.isEmpty());
    }

}
