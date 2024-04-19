package com.graphhopper.routing;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.LMProfile;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.GHPoint;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.hamcrest.CoreMatchers.*;



public class FerryTimeTest {

    final static String GH_LOCATION = "../target/ferry-time-test";

    public static GraphHopper graphHopper;

    @BeforeAll
    public static void setup() {
        Helper.removeDir(new File(GH_LOCATION));
        graphHopper = new GraphHopper();
        graphHopper.setOSMFile("../map-matching/files/ferry.osm");
        graphHopper.setGraphHopperLocation(GH_LOCATION);
        graphHopper.setProfiles(TestProfiles.accessAndSpeed("foot", "foot"),
                                TestProfiles.accessAndSpeed("car","car"));

        graphHopper.getLMPreparationHandler().setLMProfiles(new LMProfile("foot"), new LMProfile("car"));
        graphHopper.importOrLoad();
    }

    @Test
    public void alternativePathDistanceTest() {
        GHRequest req = new GHRequest(
                new GHPoint(57.592079,9.96769),
                new GHPoint(58.142808,7.990837));

        ResponsePath footPath = graphHopper.route(req.setProfile("foot")).getBest();
        ResponsePath carPath = graphHopper.route(req.setProfile("car")).getBest();

        assertThat(footPath.getTime(),new similarTime(carPath.getTime(),600*1000));
    }

    @AfterAll
    public static void cleanup() {
        graphHopper = null;
    }






}

class similarTime extends BaseMatcher<Long> {

    private final long first;
    private final long precision;



    @Override
    public boolean matches(Object sec) {

        long second = (long)sec;
        System.out.println(first + " " +second);

        return Math.abs(first - second) < precision;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(precision);
    }

    public similarTime (long other, long precision) {
        this.first = other;
        this.precision = precision;

    }

}

