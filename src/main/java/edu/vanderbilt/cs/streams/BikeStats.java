package edu.vanderbilt.cs.streams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.vanderbilt.cs.streams.BikeRide.LatLng;

public class BikeStats {

    private BikeRide ride;

    public BikeStats(BikeRide ride) {
        this.ride = ride;
    }

    /**
     * @ToDo:
     *
     * Create a stream of DataFrames representing the average of the
     * sliding windows generated from the given window size.
     *
     * For example, if a windowSize of 3 was provided, the BikeRide.DataFrames
     * would be fetched with the BikeRide.fusedFramesStream() method. These
     * frames would be divided into sliding windows of size 3 using the
     * StreamUtils.slidingWindow() method. Each sliding window would be a
     * list of 3 DataFrame objects. You would produce a new DataFrame for
     * each window by averaging the grade, altitude, velocity, and heart
     * rate for the 3 DataFrame objects.
     *
     * You should use the coordinate of the first DataFrame in the window
     * for the location.
     *
     * @param windowSize
     * @return
     */
    public Stream<BikeRide.DataFrame> averagedDataFrameStream(int windowSize){

    	// Stream of data frames 
    	
    	Stream<BikeRide.DataFrame> dataStream = ride.fusedFramesStream();
    	
    	// Turn stream into list 
    	
    	List<BikeRide.DataFrame> dataList = dataStream.collect(Collectors.toList());
    	

        return StreamUtils.slidingWindow(dataList, windowSize)
                .map(i ->
                        new BikeRide.DataFrame(
                                i.get(0).coordinate,
                                StreamUtils.averageOfProperty(BikeRide.DataFrame::getGrade).apply(i),
                                StreamUtils.averageOfProperty(BikeRide.DataFrame::getAltitude).apply(i),
                                StreamUtils.averageOfProperty(BikeRide.DataFrame::getVelocity).apply(i),
                                StreamUtils.averageOfProperty(BikeRide.DataFrame::getHeartRate).apply(i))
                        );
    }

    // @ToDo:
    //
    // Determine the number of unique locations that the
    // rider stopped. A location is unique if there are no
    // other stops at the same latitude / longitude.
    // Print out the location of each stop.
    //
    // For the purposes of this assignment, you should use
    // LatLng.equals() to determine if two locations are
    // the same.
    //
    public Stream<LatLng> locationsOfStops() {
    	
    	//filter data to ones where rider stopped (velocity == 0) 
    	
    	Stream<BikeRide.DataFrame> stopLocations = ride.fusedFramesStream()
    			.filter(i -> i.velocity == 0);
    	
    	//create a stream of those coordinates with distinct values
    	Stream<BikeRide.LatLng> distinctLoc = stopLocations.map(BikeRide.DataFrame::getCoordinate).distinct();
    			
    			
        return distinctLoc;
    }

}
