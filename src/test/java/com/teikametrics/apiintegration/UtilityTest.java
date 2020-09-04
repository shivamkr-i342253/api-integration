package com.teikametrics.apiintegration;

import com.teikametrics.apiintegration.util.Utility;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilityTest {

    @Test
    void testScopeString() {
        List<String> stringList = new ArrayList<>();
        stringList.add("user");
        stringList.add("repo");
        stringList.add("gist");

        assertEquals("user, repo, gist", Utility.toCommaSeparatedString(stringList));
    }
}
