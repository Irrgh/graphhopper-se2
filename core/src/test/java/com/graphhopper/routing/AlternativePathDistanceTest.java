package com.graphhopper.routing;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.LMProfile;
import com.graphhopper.util.Helper;
import com.graphhopper.util.shapes.GHPoint;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;



public class AlternativePathDistanceTest {

    final static String GH_LOCATION = "../target/alternative-path-distance-test";

    public static GraphHopper graphHopper;

    @BeforeAll
    public static void setup() {
        Helper.removeDir(new File(GH_LOCATION));
        graphHopper = new GraphHopper();
        graphHopper.setOSMFile("../map-matching/files/alternative-path-distance-test.osm");
        graphHopper.setGraphHopperLocation(GH_LOCATION);
        graphHopper.setProfiles(TestProfiles.accessAndSpeed("my_profile", "car"));
        graphHopper.getLMPreparationHandler().setLMProfiles(new LMProfile("my_profile"));
        graphHopper.importOrLoad();
    }

    @Test
    public void alternativePathDistanceTest() {
        GHResponse res =graphHopper.route(new GHRequest(
                new GHPoint(53.541446,-113.390626),
                new GHPoint(53.527409,-113.414379))
                .setProfile("my_profile"));


        List<ResponsePath> routes = res.getAll();

        System.out.println(res.getDebugInfo());
        System.out.println("Has alternatives: " + res.hasAlternatives());

        for (ResponsePath route: routes) {
            System.out.println(route.getDistance());
        }





        assertThat("",is(equalTo("")));
    }

    @AfterAll
    public static void cleanup() {
        graphHopper = null;
    }






}
