package com.virtualpairprogrammers.tracker.repository;

import com.virtualpairprogrammers.tracker.domain.VehicleNotFoundException;
import pe.fabiosalasm.learning.istio.commons.VehiclePositionModel;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public interface Repository {

    void updatePosition(VehiclePositionModel position);

    VehiclePositionModel getLatestPositionFor(String vehicleName) throws VehicleNotFoundException;

    void addAllReports(VehiclePositionModel[] allReports);

    Collection<VehiclePositionModel> getLatestPositionsOfAllVehiclesUpdatedSince(Date since);

    TreeSet<VehiclePositionModel> getAllReportsForVehicleSince(String name, Date timestamp) throws VehicleNotFoundException;

    Collection<VehiclePositionModel> getHistoryFor(String vehicleName) throws VehicleNotFoundException;

    void reset();
}