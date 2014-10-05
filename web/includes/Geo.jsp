import java.lang.Math;


<%!
	private static class Geo{
		private Double lat = 0.00;
		private Double long = 0.00;
		
		// 1 degree = * pi / 180 * 6360Km = 110.947Km where pi = 3.14.
		// 1 * DEGREES_PER_METRE = 1 metre converted to degrees
		public final Double DEGREES_PER_METRE = 180 / Math.PI / 6360 / 1000;
		
		// 1 * METRES_PER_DEGREE  = 1 degree converted to metres
		public final Double METRES_PER_DEGREE = Math.PI * 6360 * 1000 / 180;
		
		public Geo(Double lat, Double long){
			this.lat = lat;
			this.long = long;
		}
		
		public Double getLat(){
			return this.lat;
		}
		
		public Double getLong(){
			return this.long;
		}
		
		public setLat(Double lat){
			this.lat = lat;
		}
		
		public setLong(Double long){
			this.long = long;
		}
		
		// To be used on a fence to calculate a new point that represents the radius		
		public  Double geoAddLong(Double metres){			
			this.long += DEGREES_PER_METRE * metres;
			return this.long;
		}
		
		// Calculate distance between this point and  another point. 
		// To be used to determine radius
		public  Double dist(Geo point){
			Double dew = this.lat - point.lat;
			Double dns = this.long - point.long;
			Double dist = Math.sqrt( dew * dew + dns * dns );
			dist = dist * METRES_PER_DEGREE;
			return dist;
		}	
	
		// To be used on a patient 
		public  bool isInsideFence(Geo fence, Geo radiusPoint){	
			Double radius = fence.dist( radiusPoint );
			Double distanceFromFence = this.dist( fence );
			if (distanceFromFence <= radius){
				return true
			} else {
				return false;
			}
		}
	}
	
%>