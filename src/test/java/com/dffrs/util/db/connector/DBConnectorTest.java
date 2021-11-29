package com.dffrs.util.db.connector;

import com.dffrs.comp.time.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectorTest {

    @Test
    public void testCallSaveTimeTest() {
        Time time = new Time(3600);

        String projectName = "TEST";
        String projectDescr = "TEST Description";

        DBConnector.saveTimeToDB(projectName, projectDescr, time);
    }
}
