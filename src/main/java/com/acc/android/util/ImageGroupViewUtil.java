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
package com.acc.android.util;

import android.app.Activity;

import com.acc.android.manager.JsonManager;
import com.acc.android.model.ImageData;
import com.acc.android.util.constant.ACCALibConstant;
import com.acc.java.util.callback.ACCFileCallback;
import com.acc.java.util.callback.FileDownloadCallback;

public class ImageGroupViewUtil {
	private static ACCFileCallback accFileCallback;
	private static FileDownloadCallback fileDownloadCallback;

	public static ImageData getImageData(Activity activity) {
		// int taskTypeInt = 0;
		try {
			String imageDataString = activity.getIntent().getExtras()
					.getString(ACCALibConstant.KEY_BUNDLE_ACC_FILE_S);
			return JsonManager.getInstance().getObject(imageDataString,
					ImageData.class);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
		// TaskType taskType = null;
		// if (TaskType.INSPECT_RECHECT.ordinal() == taskTypeInt) {
		// return TaskType.INSPECT_RECHECT;
		// }
		// if (TaskType.MAINTAINCE.ordinal() == taskTypeInt) {
		// return TaskType.MAINTAINCE;
		// }
		// if (TaskType.DELIVERY.ordinal() == taskTypeInt) {
		// return TaskType.DELIVERY;
		// }
		// if(TaskType.MAINTAINCE_RECHECK.ordinal() == taskTypeInt) {
		// return TaskType.MAINTAINCE_RECHECK;
		// }
		// return null;
	}
}