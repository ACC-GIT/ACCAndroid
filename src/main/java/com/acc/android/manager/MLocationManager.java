/**
 * 
 * ACCAndroid - ACC Android Development Platform
 * Copyright (c) 2014, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.acc.android.manager;

import android.content.Context;

import com.acc.android.manager.base.BaiduLocationManager;
import com.acc.android.manager.base.LocationManager;
import com.acc.android.manager.base.SystemLocationManager;
import com.acc.android.model.MGeoData;
import com.acc.java.model.GeoData;
import com.acc.java.model.GeoDataWithoutAddress;
import com.acc.java.model.GeoStatus;
import com.acc.java.util.callback.LocationCallback;

public class MLocationManager extends LocationManager {
	private static MLocationManager instance;
	// private final Context context;
	private BaiduLocationManager baiduLocationManager;
	private SystemLocationManager systemLocationManager;
	private MGeoData currentMGeoData;
	private Boolean isSureNeedGps;
	private Boolean isSureNeedBaidu;

	// private boolean isReaquesting;

	// private GPSCallback baiduGpsCallback;
	// private GPSCallback systemGpsCallback;

	// private LocationManager locationManager;

	private MLocationManager(Context context
	// , boolean isSureNeedGps
	// , LocationManager locationManager
	) {
		super(context);
		// this.setSureNeedGps(true);
		// this.context = context;
		// this.init(context);
		// this.setLocationManager(locationManager);
	}

	// private LocationManager getDefaultLocationManager() {
	// return new BaiduLocationManager();
	// }

	public static MLocationManager getInstance(Context context) {
		if (instance == null) {
			instance = new MLocationManager(context
			// , true
			// , null
			);
		}
		return instance;
	}

	// public static MLocationManager getInstance(Context context,
	// boolean isSureNeedGps) {
	// if (instance == null) {
	// instance = new MLocationManager(context, isSureNeedGps
	// // , null
	// );
	// }
	// return instance;
	// }

	@Override
	public void start() {
		// this.locationManager.start();
		if (this.isSureNeedBaidu) {
			this.baiduLocationManager.start();
		}
		if (this.isSureNeedGps) {
			this.systemLocationManager.start();
		}
		this.rigitst();
	}

	@Override
	public void stop() {
		if (this.isSureNeedBaidu) {
			this.baiduLocationManager.stop();
		}
		if (this.isSureNeedGps) {
			this.systemLocationManager.stop();
		}
		this.clearLocationCallback();
		this.isSureNeedBaidu = true;
		this.isSureNeedGps = true;
		// this.mGeoData = null;
		// this.locationManager.stop();
	}

	// @Override
	// public void rigist(GPSCallback gpsCallback) {
	// // this.locationManager.rigist(gpsCallback);
	// }
	//
	// @Override
	// public void unRigist(GPSCallback gpsCallback) {
	// // this.locationManager.unRigist(gpsCallback);
	// }

	@Override
	public void request() {
		this.currentMGeoData = null;
		if (this.isSureNeedBaidu) {
			this.baiduLocationManager.request();
		}
		if (this.isSureNeedGps) {
			this.systemLocationManager.request();
		}
		// this.locationManager.request();
	}

	private void makeSureInitGeoData() {
		if (this.currentMGeoData == null) {
			this.currentMGeoData = new MGeoData();
		}
	}

	private void checkLocationCallback() {
		// if (this.mGeoData != null && this.mGeoData.getSystemGeoData() != null
		// && this.mGeoData.getBaiduGeoData() != null) {
		// // LogUtil.systemOut(this.mGeoData);
		// // this.systemLocationManager.removeUpdates();
		// super.onReceiveGeoData(this.mGeoData);
		// this.mGeoData = null;
		// }
		if (false) {
			if (this.currentMGeoData != null
					&& this.currentMGeoData.getBaiduGeoData() != null) {
				GeoData systemGeoData = new GeoData();
				systemGeoData.setAddress("xxxxxxxxxxxfffffffffff");
				GeoDataWithoutAddress geoDataWithoutAddress = new GeoDataWithoutAddress();
				geoDataWithoutAddress.setAccuracy(100);
				geoDataWithoutAddress.setGeoStatus(GeoStatus._GPS_NET);
				geoDataWithoutAddress.setLatitude(100);
				geoDataWithoutAddress.setLongitude(100);
				systemGeoData.setGeoDataWithoutAddress(geoDataWithoutAddress);
				this.currentMGeoData.setSystemGeoData(systemGeoData);
				super.onReceiveGeoData(this.currentMGeoData);
			}
			return;
		}
		if (this.currentMGeoData != null
				&& (!this.isSureNeedBaidu || this.currentMGeoData
						.getBaiduGeoData() != null)
				&& (!this.isSureNeedGps || (this.currentMGeoData
						.getSystemGeoData() != null || this.currentMGeoData
						.getBaiduGeoData().getGeoDataWithoutAddress()
						.getGeoStatus() != GeoStatus.GPSNET
						&& this.currentMGeoData.getBaiduGeoData()
								.getGeoDataWithoutAddress().getGeoStatus() != GeoStatus.NET))) {
			// LogUtil.systemOut(this.mGeoData);
			// LogUtil.systemOut(this.mGeoData);
			super.onReceiveGeoData(this.currentMGeoData);
			// this.mGeoData = null;
			// this.systemLocationManager.removeUpdates();
		}
	}

	@Override
	public void removeUpdates() {
		// this.currentMGeoData = null;
		if (this.isSureNeedBaidu) {
			this.baiduLocationManager.removeUpdates();
		}
		if (this.isSureNeedGps) {
			this.systemLocationManager.removeUpdates();
		}
	}

	// public void setLocationManager(LocationManager locationManager) {
	// this.locationManager = locationManager == null ? this
	// .getDefaultLocationManager() : locationManager;
	// }

	private void rigitst() {
		if (this.isSureNeedBaidu) {
			LocationCallback baiduLocationCallback = new LocationCallback() {

				@Override
				public void receive(String address) {
				}

				@Override
				public void receive(GeoData geoData) {
					MLocationManager.this.makeSureInitGeoData();
					// LogUtil.systemOut("22222222hhhhh");
					// Toast.makeText(MLocationManager.this.context,
					// "222222222hhhhh",
					// Toast.LENGTH_SHORT).show();
					// LogUtil.systemOut(geoData);
					MLocationManager.this.currentMGeoData
							.setBaiduGeoData(geoData);
					MLocationManager.this.checkLocationCallback();
				}

				@Override
				public void receive(GeoDataWithoutAddress geoPoint) {
				}
			};
			this.baiduLocationManager.rigist(baiduLocationCallback);
		}
		if (this.isSureNeedGps) {
			LocationCallback systemLocationCallback = new LocationCallback() {

				@Override
				public void receive(String address) {
				}

				@Override
				public void receive(GeoData geoData) {
					MLocationManager.this.makeSureInitGeoData();
					// LogUtil.systemOut("hhhhh");
					// Toast.makeText(MLocationManager.this.context, "hhhhh",
					// Toast.LENGTH_SHORT).show();
					// LogUtil.systemOut(geoData);
					MLocationManager.this.currentMGeoData
							.setSystemGeoData(geoData);
					// MLocationManager.this.systemLocationManager.removeUpdates();
					MLocationManager.this.checkLocationCallback();
				}

				@Override
				public void receive(GeoDataWithoutAddress geoDataWithoutAddress) {
				}
			};
			this.systemLocationManager.rigist(systemLocationCallback);
		}
	}

	@Override
	protected void init(Context context) {
		this.baiduLocationManager = BaiduLocationManager.getInstance(context);
		this.systemLocationManager = SystemLocationManager.getInstance(context);
		this.isSureNeedGps = true;
		this.isSureNeedBaidu = true;
	}

	// public Boolean getSureNeedGps() {
	// return sureNeedGps;
	// }

	public void setSureNeedGps(Boolean sureNeedGps) {
		this.isSureNeedGps = sureNeedGps;
		// if (!sureNeedGps) {
		// this.systemLocationManager = null;
		// }
	}

	public void setSureNeedBaidu(Boolean sureNeedBaidu) {
		this.isSureNeedBaidu = sureNeedBaidu;
		// if (!sureNeedGps) {
		// this.systemLocationManager = null;
		// }
	}
}