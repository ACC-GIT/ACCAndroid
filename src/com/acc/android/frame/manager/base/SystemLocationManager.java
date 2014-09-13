package com.acc.android.frame.manager.base;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.acc.android.frame.model.GeoData;
import com.acc.android.frame.model.GeoDataWithoutAddress;
import com.acc.android.frame.model.GeoStatus;

public class SystemLocationManager extends LocationManager {
	// private List<GPSCallback> gpsCallbacks;
	private android.location.LocationManager locationManager;
	// private LocationListener gpsLocationListener;
	// private LocationListener netLocationListener;
	private LocationListener locationListener;
	private boolean isReaquesting;

	private static SystemLocationManager instance;

	// private Context context;

	private SystemLocationManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static SystemLocationManager getInstance(Context context) {
		if (instance == null) {
			instance = new SystemLocationManager(context);
		}
		return instance;
	}

	public void requestGPSUpdate() {
		int minTime = 0;
		int minDistance = 0;
		String gpsProvider = android.location.LocationManager.GPS_PROVIDER;
		if (this.locationManager.getProvider(gpsProvider) != null) {
			this.locationManager.requestLocationUpdates(gpsProvider, minTime,
					minDistance, this.locationListener);
		}
	}

	public void requestNETUpdate() {
		int minTime = 3000;
		int minDistance = 1;
		String gpsProvider = android.location.LocationManager.NETWORK_PROVIDER;
		if (this.locationManager.getProvider(gpsProvider) != null) {
			this.locationManager.requestLocationUpdates(gpsProvider, minTime,
					minDistance, this.locationListener);
		}
	}

	// public void requestLocationUpdates() {
	// // if (TestConstant.isTestGPS) {
	// // Toast.makeText(context, "开启位置更新", Toast.LENGTH_SHORT).show();
	// // }
	// int minTime = 0;
	// int minDistance = 0;
	// String netProvider = android.location.LocationManager.NETWORK_PROVIDER;
	// String gpsProvider = android.location.LocationManager.GPS_PROVIDER;
	// if (this.locationManager.getProvider(gpsProvider) != null) {
	// // if (TestConstant.isTestGPS) {
	// // Toast.makeText(context, "开启gpsProvider", Toast.LENGTH_SHORT)
	// // .show();
	// // }
	// this.locationManager.requestLocationUpdates(gpsProvider, minTime,
	// minDistance, this.gpsLocationListener);
	// }
	// if (this.locationManager.getProvider(netProvider) != null) {
	// // if (TestConstant.isTestGPS) {
	// // Toast.makeText(context, "开启netProvider", Toast.LENGTH_SHORT)
	// // .show();
	// // }
	// this.locationManager.requestLocationUpdates(netProvider, minTime,
	// minDistance, this.netLocationListener);
	// }
	// }

	// public void removeUpdates() {
	// this.locationManager.removeUpdates(this.gpsLocationListener);
	// this.locationManager.removeUpdates(this.netLocationListener);
	// this.isReaquesting = false;
	// }

	// public void removeNETUpdate() {
	// this.locationManager.removeUpdates(this.locationListener);
	// this.isReaquesting = false;
	// }

	// public void removeGPSUpdate() {
	// this.locationManager.removeUpdates(this.locationListener);
	// this.isReaquesting = false;
	// }

	@Override
	public void removeUpdates() {
		// this.removeGPSUpdate();
		// this.removeNETUpdate();
		this.locationManager.removeUpdates(this.locationListener);
		this.isReaquesting = false;
	}

	public void requestUpdates() {
		this.requestGPSUpdate();
		this.requestNETUpdate();
	}

	@Override
	public void init(Context context) {
		this.context = context;
		this.locationManager = (android.location.LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		final Context contex = context;
		this.locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// if (TestConstant.isTestGPS) {
				// String toastString = "GPS-onStatusChanged-provider:"
				// + provider + "|status:" + status;
				// Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
				// .show();
				// }
			}

			@Override
			public void onProviderEnabled(String provider) {
				// if (TestConstant.isTestGPS) {
				// String toastString = "GPS-onProviderEnabled-provider:"
				// + provider;
				// Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
				// .show();
				// }
			}

			@Override
			public void onProviderDisabled(String provider) {
				// if (TestConstant.isTestGPS) {
				// String toastString = "GPS-onProviderDisabled-provider:"
				// + provider;
				// Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
				// .show();
				// }
			}

			@Override
			public void onLocationChanged(Location location) {
				SystemLocationManager.this.onReceiveLocation(location);
				// GeoData geoData = new GeoData();
				// GeoDataWithoutAddress geoDataWithoutAddress = new
				// GeoDataWithoutAddress();
				// geoDataWithoutAddress.setLatitude(location.getLatitude());
				// geoDataWithoutAddress.setLongitude(location.getLongitude());
				// geoData.setGeoDataWithoutAddress(geoDataWithoutAddress);
				// SystemLocationManager.this.onReceiveGeoData(geoData);
				// if (TestConstant.isTestGPS) {
				// String toastString =
				// "GPS-onProviderDisabled-location.getAccuracy:"
				// + location.getAccuracy();
				// Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
				// .show();
				// }
			}
		};
		// this.netLocationListener = new LocationListener() {
		// @Override
		// public void onStatusChanged(String provider, int status,
		// Bundle extras) {
		// // if (TestConstant.isTestGPS) {
		// // String toastString = "NET-onStatusChanged-provider:"
		// // + provider + "|status:" + status;
		// // Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
		// // .show();
		// // }
		// }
		//
		// @Override
		// public void onProviderEnabled(String provider) {
		// // if (TestConstant.isTestGPS) {
		// // String toastString = "NET-onProviderEnabled-provider:"
		// // + provider;
		// // Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
		// // .show();
		// // }
		// }
		//
		// @Override
		// public void onProviderDisabled(String provider) {
		// // if (TestConstant.isTestGPS) {
		// // String toastString = "NET-onProviderDisabled-provider:"
		// // + provider;
		// // Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
		// // .show();
		// // }
		// }
		//
		// @Override
		// public void onLocationChanged(Location location) {
		// SystemLocationManager.this.onReceiveLocation(location);
		// // GeoData geoData = new GeoData();
		// // GeoDataWithoutAddress geoDataWithoutAddress = new
		// // GeoDataWithoutAddress();
		// // geoDataWithoutAddress.setLatitude(location.getLatitude());
		// // geoDataWithoutAddress.setLongitude(location.getLongitude());
		// // geoData.setGeoDataWithoutAddress(geoDataWithoutAddress);
		// // SystemLocationManager.this.onReceiveGeoData(geoData);
		// // LogUtil.systemOut(location);
		// // if (TestConstant.isTestGPS) {
		// // String toastString =
		// // "NET-onLocationChanged-location.getAccuracy:"
		// // + location.getAccuracy();
		// // Toast.makeText(contex, toastString, Toast.LENGTH_SHORT)
		// // .show();
		// // }
		// }
		// };
	}

	private void onReceiveLocation(Location location) {
		GeoData geoData = new GeoData();
		GeoDataWithoutAddress geoDataWithoutAddress = new GeoDataWithoutAddress();
		geoDataWithoutAddress.setLatitude(location.getLatitude());
		geoDataWithoutAddress.setLongitude(location.getLongitude());
		// ToastManager.getInstance(context).shortToast(location.getProvider());
		if (android.location.LocationManager.NETWORK_PROVIDER.equals(location
				.getProvider())) {
			geoDataWithoutAddress.setGeoStatus(GeoStatus.NET);
		}
		geoData.setGeoDataWithoutAddress(geoDataWithoutAddress);
		SystemLocationManager.this.onReceiveGeoData(geoData);
		// this.removeUpdates();
	}

	@Override
	public void start() {
	}

	@Override
	public void request() {
		if (this.isReaquesting) {
			return;
		}
		// else {
		// this.requestLocationUpdates();
		// this.requestNETUpdate();
		// this.requestGPSUpdate();
		this.requestUpdates();
		this.isReaquesting = true;
		// }
	}

	@Override
	public void stop() {
		this.clearLocationCallback();
		// this.removeGPSUpdate();
	}

	// @Override
	// public void rigist(GPSCallback gpsCallback) {
	// this.add(gpsCallback);
	// }
	//
	// @Override
	// public void unRigist(GPSCallback gpsCallback) {
	// this.remove(gpsCallback);
	// }
	//
	// private void add(GPSCallback gpsCallback) {
	// gpsCallbacks = ListUtil.makeSureInit(gpsCallbacks);
	// gpsCallbacks.add(gpsCallback);
	// }
	//
	// private void remove(GPSCallback gpsCallback) {
	// if (!ListUtil.isEmpty(this.gpsCallbacks)) {
	// this.gpsCallbacks.remove(gpsCallback);
	// }
	// }
	//
	// private void clearGPSCallback() {
	// if (this.gpsCallbacks != null) {
	// this.gpsCallbacks.clear();
	// this.gpsCallbacks = null;
	// }
	// }
}