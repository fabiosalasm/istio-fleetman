package com.virtualpairprogrammers;

import org.junit.Assert;
import org.junit.Test;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class VehiclePositionTests {

    private static final Date TIMESTAMP = TestUtils.getDateFrom("Wed Feb 01 10:26:12 BST 2017");

    @Test
    public void testEqualityOfVehiclePositions() {
        // If two vehicle positions for a named vehicle have the same timestamp, then they're equal
        // This was decided after some agonising: surely we should also discriminate
        // on the lat/longs as well? Well yes but in reality, if we have two vehiclepositions
        // with exactly the same time, then it must be the same VehiclePosition: a vehicle
        // can't be in two places at the same time.
        var now = OffsetDateTime.now();
        VehiclePositionModel one = VehiclePositionModel.builder()
                .name("truck")
                .timestamp(now)
                .build();

        VehiclePositionModel two = VehiclePositionModel.builder()
                .name("truck")
                .timestamp(now)
                .build();

        Assert.assertEquals(one, two);
    }

    @Test
    public void testNonEquality() {
        VehiclePositionModel one = VehiclePositionModel.builder()
                .name("truck")
                .timestamp(OffsetDateTime.now())
                .build();

        VehiclePositionModel two = VehiclePositionModel.builder()
                .name("truck")
                .timestamp(OffsetDateTime.now())
                .build();

        Assert.assertNotEquals(one, two);
    }

    @Test
    public void testDifferentVehiclesAreNeverEqual() {
        VehiclePositionModel one = VehiclePositionModel.builder()
                .name("truck")
                .timestamp(OffsetDateTime.now())
                .build();

        VehiclePositionModel two = VehiclePositionModel.builder()
                .name("truckdifferent")
                .timestamp(OffsetDateTime.now())
                .build();

        Assert.assertNotEquals(one, two);
    }
}
