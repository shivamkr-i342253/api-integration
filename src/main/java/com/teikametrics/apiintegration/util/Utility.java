package com.teikametrics.apiintegration.util;

import java.util.List;

public class Utility {

    public static String toCommaSeparatedString(List<String> stringList) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < stringList.size()-1; ++i) {
            result.append(stringList.get(i));
            result.append(", ");
        }
        result.append(stringList.get(stringList.size()-1));

        return result.toString();
    }

}
