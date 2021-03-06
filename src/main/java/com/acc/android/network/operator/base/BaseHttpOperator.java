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
package com.acc.android.network.operator.base;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.acc.android.manager.JsonManager;
import com.acc.android.model.UploadData;
import com.acc.android.model.UploadFile;
import com.acc.android.model.http.request.RequestMethod;
import com.acc.android.model.http.request.RequestObjectType;
import com.acc.android.util.FileUtil;
import com.acc.android.util.LogUtil;
import com.acc.android.util.constant.UploadConstant;
import com.acc.java.util.ListUtil;
import com.acc.java.util.StreamUtil;
import com.acc.java.util.constant.HttpConstant;
import com.acc.java.util.listener.RequestListener;
import com.acc.java.util.listener.RequestListener.RequestFailReason;

public abstract class BaseHttpOperator {
	// private HttpParams httpParams;
	private HttpClient httpClient;
	private static Header cookieHeader;
	protected Context context;
	// private String netError = "false";
	// private final String notNetError = "notNetError";
	// private OnNetResultListener onNetResultListener;
	private static JsonManager jsonManager;
	public static String sessionStr;
	private static String sessionKey;

	// public abstract BaseHttpOperator();

	// public abstract BaseHttpOperator getInstance(Context context);

	public static OnNetResultListener onNetResultListenerMain;

	public interface OnNetResultListener {
		void onNetNotConnect();
	}

	protected void putHandleState(Map<Object, Object> paramMap,
			String handleState) {
		paramMap.put("form.memo1", handleState);
	}

	protected void putStatus(Map<Object, Object> paramMap, Integer statusInteger) {
		paramMap.put("form.memo2", statusInteger.toString());
	}

	// private Handler handler;

	// = new Handler() {
	// public void handleMessage(Message msg) {
	// // switch (msg.what) {
	// Toast.makeText(context, R.string.net_not_connect,
	// Toast.LENGTH_SHORT).show();
	// }
	// };

	// private Header[] cookieHeaders;

	// protected BaseHttpOperator(Context context) {
	// this(context, null);
	// }

	public BaseHttpOperator(Context context
	// ,
	// OnNetResultListener onNetResultListener
	) {
		// this.onNetResultListener = onNetResultListener;
		this.context = context;
		// Looper.prepare();
		// this.handler = new Handler() {
		// public void handleMessage(Message msg) {
		// // switch (msg.what) {
		// Toast.makeText(context, R.string.net_not_connect,
		// Toast.LENGTH_SHORT).show();
		// }
		// };
		this.getHttpClient();
		// this.initAPI(context);
		this.initJsonManager(context);
	}

	// private Response enableSSO(String userName) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("userName", userName));
	// return this.getResultObject(api.getAPI_SSO(), params,
	// RequestMethod.GET, Response.class);
	// }

	// public void makeSureSSO() {
	// if (!isSSO() && CurrentUser.getInstance().getCurrentUser() != null) {
	// this.sessionStr = null;
	// Response response = enableSSO(CurrentUser.getInstance()
	// .getCurrentUser().getLoginName());
	// if (response.isSuccess()) {
	// enableSSO();
	// }
	// }
	// }

	// public void makeSureSSOTssl() {
	// if (!isSSO()) {
	// this.sessionStr = null;
	// Response response =
	// enableSSOTssl(CurrentUser.getInstance().getCurrentUser()
	// .getLoginName());
	// if (response.isSuccess()) {
	// enableSSO();
	// }
	// }
	// }
	// private Response enableSSOTssl(String userName) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("userName", userName));
	// return this.getResultObject(api.getAPI_SSO_TSSL(), params,
	// RequestMethod.GET, Response.class);
	// }

	private void initJsonManager(Context context) {
		jsonManager = JsonManager.getInstance();
	}

	// private void initAPI(Context context) {
	// if (api == null) {
	// api = WebServiceApi.getInstance(context);
	// }
	// }

	public <T> T getResultObject(String url, Object paramObject,
			RequestMethod requestMethod, Object classOrTypeObject) {
		return this.getResultObject(url, paramObject, requestMethod, false,
				classOrTypeObject);
	}

	public <T> T getRequest(String url, Object paramObject,
			Object classOrTypeObject) {
		return this.getResultObject(url, paramObject, RequestMethod.GET, false,
				classOrTypeObject);
	}

	public <T> T getRequest(String url, Object classOrTypeObject) {
		return this.getResultObject(url, null, RequestMethod.GET, false,
				classOrTypeObject);
	}

	public <T> T postRequest(String url, Object paramObject,
			Object classOrTypeObject) {
		return this.getResultObject(url, paramObject, RequestMethod.POST,
				false, classOrTypeObject);
	}

	public <T> T multiRequest(String url, Object paramObject,
			Object classOrTypeObject) {
		return this.getResultObject(url, paramObject, RequestMethod.POST, true,
				classOrTypeObject);
	}

	// public <T> T tempGetResultObject(String url, Object paramObject,
	// RequestMethod requestMethod, Object classOrTypeObject) {
	// return this.tempGetResultObject(url, paramObject, requestMethod, false,
	// classOrTypeObject);
	// }

	private static void addUploadDataContent(List<UploadFile> uploadFiles,
			DataOutputStream dataOutputStream
	// , String tagString
	) {
		if (uploadFiles == null || uploadFiles.size() == 0) {
			return;
		}
		for (UploadFile uploadFile : uploadFiles) {
			// StringBuilder fileEntity = new StringBuilder();
			// fileEntity.append("--");
			// fileEntity.append(BOUNDARY);
			// fileEntity.append("\r\n");
			// fileEntity.append("Content-Disposition: form-data;name=\""
			// + uploadFile.getParameterName() + "\";filename=\""
			// + uploadFile.getFilname() + "\"\r\n");
			// fileEntity.append("Content-Type: " + uploadFile.getContentType()
			// + "\r\n\r\n");
			// outStream.write(fileEntity.toString().getBytes());

			// sb.append(UploadConstant.TWOHYPHENS + UploadConstant.BOUNDARY
			// + UploadConstant.LINEEND);
			// sb.append("Content-Disposition: form-data; name=\""
			// + param.getKey() + "\"" + UploadConstant.LINEEND);
			// sb.append(UploadConstant.LINEEND);
			// sb.append(param.getValue() + UploadConstant.LINEEND);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(UploadConstant.TWOHYPHENS
					+ UploadConstant.BOUNDARY + UploadConstant.LINEEND);
			stringBuilder
					.append("Content-Disposition: form-data;")
					// + tagString
					.append("name=\"" + uploadFile.getName() + "\";")
					// + tagString
					.append("filename=\""
							+ FileUtil.getFileRealName(uploadFile.getFilePath())
							// uploadFile.getFilePath()
							// .substring(
							// uploadFile.getFilePath()
							// .lastIndexOf("/") + 1,
							// uploadFile.getFilePath().length())
							+ "\";")
					.append(UploadConstant.LINEEND)
					.append("Content-Type:\"" + uploadFile.getContentType()
							+ "\"").append(UploadConstant.LINEEND)
					.append(UploadConstant.LINEEND);
			// + tagString
			// + "ContentType=\""
			// + uploadFile.getFilePath().substring(
			// uploadFile.getFilePath().lastIndexOf(".") + 1,
			// uploadFile.getFilePath().length()) + "\";"
			// + UploadConstant.LINEEND)
			// // + tagString
			// // + "Content-Type:\""
			// // + uploadFile.getFilePath().substring(
			// // uploadFile.getFilePath().lastIndexOf(".") + 1,
			// // uploadFile.getFilePath().length()) + "\";"
			// + "Content-Type:\"image/jpg\"" + UploadConstant.LINEEND);
			// stringBuilder.append(UploadConstant.LINEEND);
			// stringBuilder.append(UploadConstant.LINEEND).append(
			// UploadConstant.LINEEND);
			// LogUtil.systemOut(stringBuilder.toString());
			FileInputStream fileInputStream = null;
			try {
				dataOutputStream.writeBytes(stringBuilder.toString());
				fileInputStream = new FileInputStream(uploadFile.getFilePath());
				// fileInputStream.available();
				// 杩涘害鏉℃樉绀�
				byte[] buffer = new byte[102400]; // 8k
				int count = 0;
				while ((count = fileInputStream.read(buffer)) != -1) {
					// LogUtil.systemOut("count:" + count);
					dataOutputStream.write(buffer, 0, count);
				}
				dataOutputStream.writeBytes(UploadConstant.LINEEND);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static void addFormField(Map<Object, Object> params,
			DataOutputStream output) {
		if (params == null || params.size() == 0) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Object, Object> param : params.entrySet()) {
			try {
				String encodeValue = URLEncoder.encode(param.getValue()
						.toString(), HttpConstant.ENCODE);
				sb.append(UploadConstant.TWOHYPHENS + UploadConstant.BOUNDARY
						+ UploadConstant.LINEEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ param.getKey() + "\"" + UploadConstant.LINEEND);
				sb.append(UploadConstant.LINEEND);
				sb.append(encodeValue + UploadConstant.LINEEND);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			output.writeBytes(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// private static void handleVersionBug(String urlString) throws IOException
	// {
	// if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
	// // if (true) {
	// // InputStream inputStream = new InputStream() {
	// //
	// // @Override
	// // public int read() throws IOException {
	// // // TODO Auto-generated method stub
	// // return 0;
	// // }
	// // };
	// // inputStream.close();
	// // return;
	// // } else {
	// //
	// // }
	// URL url = new URL(urlString);
	// HttpURLConnection httpURLConnection = (HttpURLConnection) url
	// .openConnection();
	// httpURLConnection.setRequestMethod("POST");
	// // DataOutputStream dataOutputStream = new DataOutputStream(
	// // httpURLConnection.getOutputStream());
	// InputStream inputStream = httpURLConnection.getInputStream();
	// // if (httpURLConnection != null) {
	// httpURLConnection.disconnect();
	// // dataOutputStream.close();
	// inputStream.close();
	// }
	// }

	// public static String post(String actionUrl, UploadData uploadData) {
	// HttpURLConnection httpURLConnection = null;
	// DataOutputStream dataOutputStream = null;
	// InputStream inputStream = null;
	// String resultString = null;
	// LogUtil.info("actionUrl", actionUrl);
	// LogUtil.info("uploadData", uploadData);
	// LogUtil.info("sessionStr", sessionStr);
	// try {
	// // if (true
	// // // Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13
	// // // && resultString == null
	// // ) {
	// // URL url = new URL(actionUrl);
	// // httpURLConnection = (HttpURLConnection) url.openConnection();
	// // httpURLConnection.setRequestMethod("POST");
	// // dataOutputStream = new DataOutputStream(
	// // httpURLConnection.getOutputStream());
	// // inputStream = httpURLConnection.getInputStream();
	// // // if (httpURLConnection != null) {
	// // httpURLConnection.disconnect();
	// // dataOutputStream.close();
	// // inputStream.close();
	// // // }
	// // // httpURLConnection = (HttpURLConnection) url.openConnection();
	// // }
	// // handleVersionBug(actionUrl);
	// URL url = new URL(actionUrl);
	// httpURLConnection = (HttpURLConnection) url.openConnection();
	// // httpURLConnection.setRequestProperty("Cookie",
	// // "JSESSIONID=320C57C083E7F678ED14B8974732225E");
	// httpURLConnection.setDoInput(true);
	// httpURLConnection.setDoOutput(true);
	// httpURLConnection.setUseCaches(false);
	// httpURLConnection.setRequestMethod("POST");
	// httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	// httpURLConnection.setRequestProperty("Charset", "UTF-8");
	// httpURLConnection.setRequestProperty("Content-Type",
	// UploadConstant.MULTIPART_FORM_DATA + ";boundary="
	// + UploadConstant.BOUNDARY);
	// // if (Build.VERSION.SDK != null
	// // && Build.VERSION. > 13) {
	// // httpURLConnection.setRequestProperty("Connection", "close");
	// // }
	// // httpURLConnection.setChunkedStreamingMode(0);
	// sessonInject(httpURLConnection);
	// httpURLConnection.connect();
	// dataOutputStream = new DataOutputStream(
	// httpURLConnection.getOutputStream());
	// addFormField(uploadData.getParamMap(), dataOutputStream);
	// if (!ListUtil.isEmpty(uploadData.getUploadFiles())) {
	// addUploadDataContent(uploadData.getUploadFiles(),
	// dataOutputStream
	// // , TAGSTRING
	// );
	// }
	// dataOutputStream.writeBytes(UploadConstant.LINEEND);
	// dataOutputStream.writeBytes(UploadConstant.TWOHYPHENS
	// + UploadConstant.BOUNDARY + UploadConstant.TWOHYPHENS
	// + UploadConstant.LINEEND);
	// // try {
	// dataOutputStream.flush();
	// // dataOutputStream.close();
	// // } catch (Exception exception) {
	// // if (dataOutputStream != null) {
	// // dataOutputStream.close();
	// // }
	// // dataOutputStream = new DataOutputStream(
	// // httpURLConnection.getOutputStream());
	// // addFormField(uploadData.getParamMap(), dataOutputStream);
	// // if (!ListUtil.isEmpty(uploadData.getUploadFiles())) {
	// // addUploadDataContent(uploadData.getUploadFiles(),
	// // dataOutputStream
	// // // , TAGSTRING
	// // );
	// // }
	// // dataOutputStream.writeBytes(UploadConstant.LINEEND);
	// // dataOutputStream.writeBytes(UploadConstant.TWOHYPHENS
	// // + UploadConstant.BOUNDARY + UploadConstant.TWOHYPHENS
	// // + UploadConstant.LINEEND);
	// // dataOutputStream.flush();
	// // }
	// // if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
	// // httpURLConnection.setRequestProperty("Connection", "close");
	// // }
	// inputStream = httpURLConnection.getInputStream();
	// // inputStream.close();
	// // inputStream = httpURLConnection.getInputStream();
	//
	// resultString = read(inputStream);
	// // InputStreamReader isr = new InputStreamReader(inputStream,
	// // "utf-8");
	// // BufferedReader br = new BufferedReader(isr, size);
	// // resultString = br.readLine();
	// LogUtil.info("resultString", resultString);
	// // LogUtil.systemOut(resultString);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (dataOutputStream != null) {
	// dataOutputStream.close();
	// }
	// if (inputStream != null) {
	// inputStream.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// if (httpURLConnection != null) {
	// httpURLConnection.disconnect();
	// }
	// }
	// return resultString;
	// }

	public static String post(String actionUrl, UploadData uploadData) {
		HttpURLConnection httpURLConnection = null;
		DataOutputStream dataOutputStream = null;
		InputStream inputStream = null;
		String resultString = null;
		// if (AppLibConstant.isUseLog()) {
		LogUtil.info("actionUrl", actionUrl);
		LogUtil.info("uploadData", uploadData);
		LogUtil.info("sessionStr", sessionStr);
		// }
		try {
			URL url = new URL(actionUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// httpURLConnection.setRequestProperty("Cookie",
			// "JSESSIONID=320C57C083E7F678ED14B8974732225E");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					UploadConstant.MULTIPART_FORM_DATA + ";boundary="
							+ UploadConstant.BOUNDARY);
			// httpURLConnection.setChunkedStreamingMode(0);
			sessonInject(httpURLConnection);
			dataOutputStream = new DataOutputStream(
					httpURLConnection.getOutputStream());
			addFormField(uploadData.getParamMap(), dataOutputStream);
			if (!ListUtil.isEmpty(uploadData.getUploadFiles())) {
				addUploadDataContent(uploadData.getUploadFiles(),
						dataOutputStream
				// , TAGSTRING
				);
			}
			dataOutputStream.writeBytes(UploadConstant.LINEEND);
			dataOutputStream.writeBytes(UploadConstant.TWOHYPHENS
					+ UploadConstant.BOUNDARY + UploadConstant.TWOHYPHENS
					+ UploadConstant.LINEEND);
			// try {
			dataOutputStream.flush();
			// } catch (Exception exception) {
			// if (dataOutputStream != null) {
			// dataOutputStream.close();
			// }
			// dataOutputStream = new DataOutputStream(
			// httpURLConnection.getOutputStream());
			// addFormField(uploadData.getParamMap(), dataOutputStream);
			// if (!ListUtil.isEmpty(uploadData.getUploadFiles())) {
			// addUploadDataContent(uploadData.getUploadFiles(),
			// dataOutputStream
			// // , TAGSTRING
			// );
			// }
			// dataOutputStream.writeBytes(UploadConstant.LINEEND);
			// dataOutputStream.writeBytes(UploadConstant.TWOHYPHENS
			// + UploadConstant.BOUNDARY + UploadConstant.TWOHYPHENS
			// + UploadConstant.LINEEND);
			// dataOutputStream.flush();
			// }
			inputStream = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			resultString = br.readLine();
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info("resultString", resultString);
			// }
			// LogUtil.systemOut(resultString);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return resultString;
	}

	// private void sdfsd() {
	//
	// }

	public <T> T getResultObject(String url, Object paramObject,
			RequestObjectType requestObjectType, RequestMethod requestMethod,
			Object classOrTypeObject) {
		if (requestObjectType == RequestObjectType.JSON) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(
					HttpConstant.DEFAULT_KEY_REQUEST_PARAM, JsonManager
							.getInstance().getJson(paramObject)));
			return getResultObject(url, params, requestMethod,
					classOrTypeObject);
		} else if (requestObjectType == RequestObjectType.STRING) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (paramObject != null) {
				params.add(new BasicNameValuePair(
						HttpConstant.DEFAULT_KEY_REQUEST_PARAM, paramObject
								.toString()));
			}
			return getResultObject(url, params, requestMethod,
					classOrTypeObject);
		} else {
			return getResultObject(url, paramObject, requestMethod,
					classOrTypeObject);
		}
	}

	// public <T> T tempGetResultObject(String url, Object paramObject,
	// RequestMethod requestMethod, boolean isMultipart,
	// Object classOrTypeObject) {
	// String responseString = null;
	// if (isMultipart) {
	// responseString = UploadHttpOpera
	// .post(url, (UploadData) paramObject);
	// // return responseString;
	// } else {
	// // Map<String, String> paramMap = this.getParamMap(paramObject);
	// responseString = this.tempoPenRequest(url, requestMethod,
	// // isMultipart,
	// paramObject);
	// if (responseString == null) {
	// return null;
	// }
	// }
	// System.out.println(responseString);
	// // System.out.println("HHHHHHHHHHHHHHHH");
	// // System.out.println(String.class);
	// // System.out.println(classOrTypeObject);
	// try {
	// if (String.class.equals(classOrTypeObject)) {
	// return (T) responseString;
	// }
	// // else {
	// return getObject(responseString, (Type) classOrTypeObject);
	// // }
	//
	// // if (classOrTypeObject instanceof Type) {
	// // } else if (String.class.getName().equals(
	// // classOrTypeObject.getClass().getName())) {
	// // // if (classOrTypeObject instanceof Class
	// // // && classOrTypeObject.getClass() == String.class) {
	// // // } else
	// // }
	// // if (classOrTypeObject instanceof Class) {
	// // return getObject(responseString, (Class<T>) classOrTypeObject);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// // }
	// // return null;
	// }

	public <T> T getResultObject(String url, Object paramObject,
			RequestMethod requestMethod, boolean isMultipart,
			Object classOrTypeObject) {
		String responseString = null;
		if (isMultipart) {
			responseString = post(url, (UploadData) paramObject);
			// return responseString;
		} else {
			// Map<String, String> paramMap = this.getParamMap(paramObject);
			responseString = this.openRequest(url, requestMethod,
			// isMultipart,
					paramObject);
			if (responseString == null) {
				return null;
			}
		}
		// System.out.println(url);
		// System.out.println(responseString);
		// System.out.println("HHHHHHHHHHHHHHHH");
		// System.out.println(String.class);
		// System.out.println(classOrTypeObject);
		try {
			if (String.class.equals(classOrTypeObject)) {
				return (T) responseString;
			}
			// else {
			return getObject(responseString, (Type) classOrTypeObject);
			// }

			// if (classOrTypeObject instanceof Type) {
			// } else if (String.class.getName().equals(
			// classOrTypeObject.getClass().getName())) {
			// // if (classOrTypeObject instanceof Class
			// // && classOrTypeObject.getClass() == String.class) {
			// // } else
			// }
			// if (classOrTypeObject instanceof Class) {
			// return getObject(responseString, (Class<T>) classOrTypeObject);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// }
		// return null;
	}

	// /**
	// * 鍚戞寚瀹� URL 鍙戦�丳OST鏂规硶鐨勮姹�
	// *
	// * @param url
	// * 鍙戦�佽姹傜殑 URL
	// * @param param
	// * 璇锋眰鍙傛暟锛岃姹傚弬鏁板簲璇ユ槸 name1=value1&name2=value2 鐨勫舰寮忋��
	// * @return 鎵�浠ｈ〃杩滅▼璧勬簮鐨勫搷搴旂粨鏋�
	// */
	// public static String post(String url, String param, String charset) {
	// PrintWriter out = null;
	// BufferedReader in = null;
	// String result = "";
	// try {
	// URL realUrl = new URL(url);
	// // 鎵撳紑鍜孶RL涔嬮棿鐨勮繛鎺�
	// URLConnection conn = realUrl.openConnection();
	// // 璁剧疆閫氱敤鐨勮姹傚睘鎬�
	// conn.setRequestProperty("accept", "*/*");
	// conn.setRequestProperty("connection", "Keep-Alive");
	// conn.setRequestProperty("user-agent",
	// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	// // 鍙戦�丳OST璇锋眰蹇呴』璁剧疆濡備笅涓よ
	// conn.setDoOutput(true);
	// conn.setDoInput(true);
	// // 鑾峰彇URLConnection瀵硅薄瀵瑰簲鐨勮緭鍑烘祦
	// out = new PrintWriter(conn.getOutputStream());
	// // 鍙戦�佽姹傚弬鏁�
	// out.print(param);
	// // flush杈撳嚭娴佺殑缂撳啿
	// out.flush();
	// // 瀹氫箟BufferedReader杈撳叆娴佹潵璇诲彇URL鐨勫搷搴�
	// in = new BufferedReader(new InputStreamReader(
	// conn.getInputStream(), charset));
	// String line;
	// while ((line = in.readLine()) != null) {
	// result += line;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (out != null) {
	// out.close();
	// }
	// if (in != null) {
	// in.close();
	// }
	// } catch (IOException ex) {
	// }
	// }
	// return result;
	// }

	// public <T> T getResultObjectNew(String url, String paramObject,
	// Map<String, String> paramMap, RequestMethod requestMethod,
	// Object classOrTypeObject) {
	// url = this.api.getUrl(url, paramMap);
	// String responseString = null;
	// responseString = post(url, paramObject, "utf-8");
	// // if (isMultipart) {
	// // responseString = UploadHttpOpera
	// // .post(url, (UploadData) paramObject);
	// // // return responseString;
	// // } else {
	// // // Map<String, String> paramMap = this.getParamMap(paramObject);
	// // responseString = this.openRequest(url, requestMethod,
	// // // isMultipart,
	// // paramObject);
	// // if (responseString == null) {
	// // return null;
	// // }
	// // }
	// // System.out.println(responseString);
	// // System.out.println("HHHHHHHHHHHHHHHH");
	// // System.out.println(String.class);
	// // System.out.println(classOrTypeObject);
	// try {
	// if (String.class.equals(classOrTypeObject)) {
	// return (T) responseString;
	// }
	// // else {
	// return getObject(responseString, (Type) classOrTypeObject);
	// // }
	//
	// // if (classOrTypeObject instanceof Type) {
	// // } else if (String.class.getName().equals(
	// // classOrTypeObject.getClass().getName())) {
	// // // if (classOrTypeObject instanceof Class
	// // // && classOrTypeObject.getClass() == String.class) {
	// // // } else
	// // }
	// // if (classOrTypeObject instanceof Class) {
	// // return getObject(responseString, (Class<T>) classOrTypeObject);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// // }
	// // return null;
	// }

	// public <T> T getResultObject(String url, Object paramObject,
	// Map<String, String> paramMap, RequestMethod requestMethod,
	// boolean isMultipart, Object classOrTypeObject) {
	// url = this.api.getUrl(url, paramMap);
	// String responseString = null;
	// if (isMultipart) {
	// responseString = post(url, (UploadData) paramObject);
	// // return responseString;
	// } else {
	// // Map<String, String> paramMap = this.getParamMap(paramObject);
	// responseString = this.openRequest(url, requestMethod,
	// // isMultipart,
	// paramObject);
	// if (responseString == null) {
	// return null;
	// }
	// }
	// // System.out.println(responseString);
	// // System.out.println("HHHHHHHHHHHHHHHH");
	// // System.out.println(String.class);
	// // System.out.println(classOrTypeObject);
	// try {
	// if (String.class.equals(classOrTypeObject)) {
	// return (T) responseString;
	// }
	// // else {
	// return getObject(responseString, (Type) classOrTypeObject);
	// // }
	//
	// // if (classOrTypeObject instanceof Type) {
	// // } else if (String.class.getName().equals(
	// // classOrTypeObject.getClass().getName())) {
	// // // if (classOrTypeObject instanceof Class
	// // // && classOrTypeObject.getClass() == String.class) {
	// // // } else
	// // }
	// // if (classOrTypeObject instanceof Class) {
	// // return getObject(responseString, (Class<T>) classOrTypeObject);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// // }
	// // return null;
	// }

	// private String tempoPenRequest(String url, RequestMethod requestMethod,
	// Object paramObject) {
	// if (!this.checkNetWork()) {
	// // return this.notNetError;
	// }
	// HttpURLConnection conn = null;
	// InputStream inputStream = null;
	// String response = null;
	// PrintWriter out = null;
	// try {
	// LogUtil.info(this, "Begin a request--->>>>>>>>>>");
	// // try {
	// // url = URLEncoder.encode(url, HttpConstant.ENCODE);
	// // } catch (UnsupportedEncodingException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// if (requestMethod == requestMethod.GET && paramObject != null) {
	// // try {
	// url = url + "?" + this.encodeUrlParam(paramObject);
	// // } catch (UnsupportedEncodingException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// //
	// http://192.168.15.204:82/agcom/rest/roadRest/getBestRoad?id=2&xy=56536.156809,13853.484204
	// // 56364.173164,12955.347386
	// }
	// LogUtil.info(this, "url:", url);
	// LogUtil.info(this, "requestMethod:", requestMethod);
	// LogUtil.info(this, "paramObject:", paramObject);
	// // url = url.replace(" ", "%20");
	// conn = (HttpURLConnection) new URL(url).openConnection();
	// // System.out
	// // .println("this.sessionStr == null" + this.sessionStr == null);
	// // System.out.println(this.sessionStr == null);
	// if (this.sessionStr != null) {
	// conn.setRequestProperty("cookie", sessionStr);
	// }
	// conn.setConnectTimeout(5 * 1000);
	// if (requestMethod == RequestMethod.GET) {
	// // conn.setRequestMethod(method);
	// // if (isMultipart) {
	// // conn.setRequestProperty("Content-Type",
	// // "multipart/form-data;");
	// // } else {
	// conn.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencode");
	// // }
	// conn.connect();
	// } else {
	// conn.setRequestMethod("POST");
	// conn.setDoOutput(true);
	// conn.setUseCaches(false);
	// // http: //
	// // 192.168.16.16:8089/agcom/rest/system/locateDiscode/231/10//
	// // 閰嶇疆璇锋眰Content-Type
	// // if (isMultipart) {
	// // conn.setRequestProperty("Content-Type",
	// // "multipart/form-data;");
	// // } else {
	// // conn.setRequestProperty("Content-Type",
	// // "application/x-www-form-urlencode");
	//
	// // }
	// // conn.setRequestProperty("Content-Type",
	// // "application/x-www-form-urlencode");
	// // conn.connect();
	// if (paramObject != null) {
	// conn.getOutputStream().write(
	// this.encodeUrlParam(paramObject).getBytes(
	// HttpConstant.ENCODE));
	// // out = new PrintWriter(conn.getOutputStream());
	// // out.print(paramObject);
	// // out.flush();
	// }
	// }
	// inputStream = conn.getInputStream();
	// response = read(inputStream);
	// LogUtil.info(this, "response:", response);
	// if (this.sessionStr == null) {
	// Map<String, List<String>> cookies = conn.getHeaderFields();
	// // this.cookieHeader = conn.getg
	// for (String key : cookies.keySet()) {
	// // System.out.println("sdfdsfd:" + key);
	// if (key == null) {
	// continue;
	// }
	// if ("set-cookie".equals(key.toLowerCase())) {
	// List<String> values = cookies.get(key);
	// String sessionStrTemp = "";
	// for (String value : values) {
	// sessionStrTemp += value;
	// sessionStrTemp += ";";
	// // conn.addRequestProperty("set-cookie", string);
	// }
	// // loginSessionStrTemp =
	// // loginSessionStrTemp.substring(0,
	// // loginSessionStrTemp.length() - 1);
	// // System.out.println("loginSessionStrTemp:"
	// // + loginSessionStrTemp);
	// this.sessionStr = sessionStrTemp;
	// this.sessionKey = key;
	// // OldHttpUtil.LOGINSESSIONSTR = sessionStrTemp;
	// // BaseHttpManager.sessionStr = loginSessionStrTemp;
	// break;
	// }
	// }
	// // System.out.println("sessionStr:" + sessionStr);
	// }
	// // System.out.println("openUrl:response:" + response);
	// } catch (Exception e) {
	// LogUtil.info(this,
	// "End a request with exception below---XXXXXXXXXX");
	// e.printStackTrace();
	// return response;
	// } finally {
	// if (conn != null) {
	// conn.disconnect();
	// }
	// if (inputStream != null) {
	// try {
	// inputStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (out != null) {
	// try {
	// out.close();
	// } catch (Exception exception) {
	// exception.printStackTrace();
	// }
	// }
	// }
	// LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
	// return response;
	// }

	private String openRequest(String url, RequestMethod requestMethod,
			Object paramObject) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		String response = null;
		PrintWriter out = null;
		try {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "Begin a request--->>>>>>>>>>");
			// }
			// try {
			// url = URLEncoder.encode(url, HttpConstant.ENCODE);
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (requestMethod == requestMethod.GET && paramObject != null) {
				// try {
				url = url + "?" + this.encodeUrlParam(paramObject);
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// http://192.168.15.204:82/agcom/rest/roadRest/getBestRoad?id=2&xy=56536.156809,13853.484204
				// 56364.173164,12955.347386
			}
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "url:", url);
			LogUtil.info(this, "requestMethod:", requestMethod);
			LogUtil.info(this, "paramObject:", paramObject);
			// }
			// url = url.replace(" ", "%20");
			conn = (HttpURLConnection) new URL(url).openConnection();
			// System.out
			// .println("this.sessionStr == null" + this.sessionStr == null);
			// System.out.println(this.sessionStr == null);
			if (this.sessionStr != null) {
				conn.setRequestProperty("cookie", sessionStr);
			}
			conn.setConnectTimeout(5 * 1000);
			if (requestMethod == RequestMethod.GET) {
				// conn.setRequestMethod(method);
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				// }
				conn.connect();
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				// http: //
				// 192.168.16.16:8089/agcom/rest/system/locateDiscode/231/10//
				// 閰嶇疆璇锋眰Content-Type
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				// conn.setRequestProperty("Content-Type",
				// "application/x-www-form-urlencode");

				// }
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				// conn.connect();
				if (paramObject != null) {
					conn.getOutputStream().write(
							this.encodeUrlParam(paramObject).getBytes(
									HttpConstant.ENCODE));
					// out = new PrintWriter(conn.getOutputStream());
					// out.print(paramObject);
					// out.flush();
				}
			}
			inputStream = conn.getInputStream();
			response = read(inputStream);
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "response:", response);
			// }
			if (this.sessionStr == null) {
				Map<String, List<String>> cookies = conn.getHeaderFields();
				// this.cookieHeader = conn.getg
				for (String key : cookies.keySet()) {
					// System.out.println("sdfdsfd:" + key);
					if (key == null) {
						continue;
					}
					if ("set-cookie".equals(key.toLowerCase())) {
						List<String> values = cookies.get(key);
						String sessionStrTemp = "";
						for (String value : values) {
							sessionStrTemp += value;
							sessionStrTemp += ";";
							// conn.addRequestProperty("set-cookie", string);
						}
						// loginSessionStrTemp =
						// loginSessionStrTemp.substring(0,
						// loginSessionStrTemp.length() - 1);
						// System.out.println("loginSessionStrTemp:"
						// + loginSessionStrTemp);
						this.sessionStr = sessionStrTemp;
						this.sessionKey = key;
						// OldHttpUtil.LOGINSESSIONSTR = sessionStrTemp;
						// BaseHttpManager.sessionStr = loginSessionStrTemp;
						break;
					}
				}
				// System.out.println("sessionStr:" + sessionStr);
			}
			// System.out.println("openUrl:response:" + response);
		} catch (Exception e) {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this,
					"End a request with exception below---XXXXXXXXXX");
			// }
			e.printStackTrace();
			return response;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		// if (AppLibConstant.isUseLog()) {
		LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
		// }
		return response;
	}

	public Bitmap openRequestForBitmap(String url, RequestMethod requestMethod,
			Object paramObject) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		Bitmap bitmap = null;
		try {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "Begin a request--->>>>>>>>>>");
			// }
			// try {
			// url = URLEncoder.encode(url, HttpConstant.ENCODE);
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (requestMethod == requestMethod.GET && paramObject != null) {
				// try {
				url = url + "?" + this.encodeUrlParam(paramObject);
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// http://192.168.15.204:82/agcom/rest/roadRest/getBestRoad?id=2&xy=56536.156809,13853.484204
				// 56364.173164,12955.347386
			}
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "url:", url);
			LogUtil.info(this, "requestMethod:", requestMethod);
			LogUtil.info(this, "paramObject:", paramObject);
			// }
			// url = url.replace(" ", "%20");
			conn = (HttpURLConnection) new URL(url).openConnection();
			// System.out
			// .println("this.sessionStr == null" + this.sessionStr == null);
			// System.out.println(this.sessionStr == null);
			if (this.sessionStr != null) {
				conn.setRequestProperty("cookie", sessionStr);
			}
			conn.setConnectTimeout(5 * 1000);
			if (requestMethod == RequestMethod.GET) {
				// conn.setRequestMethod(method);
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				// }
				conn.connect();
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				// http: //
				// 192.168.16.16:8089/agcom/rest/system/locateDiscode/231/10//
				// 閰嶇疆璇锋眰Content-Type
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				// }
				// conn.setRequestProperty("Content-Type",
				// "application/x-www-form-urlencode");
				// conn.connect();
				if (paramObject != null) {
					conn.getOutputStream().write(
							this.encodeUrlParam(paramObject).getBytes("GBK"));
				}
			}
			inputStream = conn.getInputStream();
			// in = new BufferedInputStream(uRLConnection
			// .getInputStream());
			bitmap = BitmapFactory.decodeStream(inputStream);
			// response = read(inputStream);
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "response:", bitmap == null);
			// }
			if (this.sessionStr == null) {
				Map<String, List<String>> cookies = conn.getHeaderFields();
				// this.cookieHeader = conn.getg
				for (String key : cookies.keySet()) {
					// System.out.println("sdfdsfd:" + key);
					if (key == null) {
						continue;
					}
					if ("set-cookie".equals(key.toLowerCase())) {
						List<String> values = cookies.get(key);
						String sessionStrTemp = "";
						for (String value : values) {
							sessionStrTemp += value;
							sessionStrTemp += ";";
							// conn.addRequestProperty("set-cookie", string);
						}
						// loginSessionStrTemp =
						// loginSessionStrTemp.substring(0,
						// loginSessionStrTemp.length() - 1);
						// System.out.println("loginSessionStrTemp:"
						// + loginSessionStrTemp);
						this.sessionStr = sessionStrTemp;
						this.sessionKey = key;
						// OldHttpUtil.LOGINSESSIONSTR = sessionStrTemp;
						// BaseHttpManager.sessionStr = loginSessionStrTemp;
						break;
					}
				}
				// System.out.println("sessionStr:" + sessionStr);
			}
			// System.out.println("openUrl:response:" + response);
		} catch (Exception e) {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this,
					"End a request with exception below---XXXXXXXXXX");
			// }
			e.printStackTrace();
			return bitmap;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// if (AppLibConstant.isUseLog()) {
		LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
		// }
		return bitmap;
	}

	public interface HttpReqestProgressListener {
		void onRequestProgress(Float downloadPercent);

		//
		void onRequestFail();
		//
		// void onSuccess();
	}

	public byte[] openRequestForByteArray(String url,
			RequestMethod requestMethod, Object paramObject,
			HttpReqestProgressListener httpReqestProgressListener) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		// Bitmap bitmap = null;
		byte[] bytes = null;
		try {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "Begin a request--->>>>>>>>>>");
			// }
			// try {
			// url = URLEncoder.encode(url, HttpConstant.ENCODE);
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (requestMethod == requestMethod.GET && paramObject != null) {
				// try {
				url = url + "?" + this.encodeUrlParam(paramObject);
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// http://192.168.15.204:82/agcom/rest/roadRest/getBestRoad?id=2&xy=56536.156809,13853.484204
				// 56364.173164,12955.347386
			}
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "url:", url);
			LogUtil.info(this, "requestMethod:", requestMethod);
			LogUtil.info(this, "paramObject:", paramObject);
			// }
			// url = url.replace(" ", "%20");
			conn = (HttpURLConnection) new URL(url).openConnection();
			// System.out
			// .println("this.sessionStr == null" + this.sessionStr == null);
			// System.out.println(this.sessionStr == null);
			if (this.sessionStr != null) {
				conn.setRequestProperty("cookie", sessionStr);
			}
			conn.setConnectTimeout(5 * 1000);
			if (requestMethod == RequestMethod.GET) {
				// conn.setRequestMethod(method);
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				// }
				conn.connect();
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				// http: //
				// 192.168.16.16:8089/agcom/rest/system/locateDiscode/231/10//
				// 閰嶇疆璇锋眰Content-Type
				// if (isMultipart) {
				// conn.setRequestProperty("Content-Type",
				// "multipart/form-data;");
				// } else {
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");

				// }
				// conn.setRequestProperty("Content-Type",
				// "application/x-www-form-urlencode");
				// conn.connect();
				if (paramObject != null) {
					conn.getOutputStream().write(
							this.encodeUrlParam(paramObject).getBytes(
									HttpConstant.ENCODE));
				}
			}
			inputStream = conn.getInputStream();
			if (inputStream != null) {
				byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] buf = new byte[128];
				long contentLength = conn.getContentLength();
				int read = -1;
				long downloadCount = 0;
				while ((read = inputStream.read(buf)) != -1) {
					byteArrayOutputStream.write(buf, 0, read);
					downloadCount += read;
					// publishProgress(count * 1.0f / length);
					if (httpReqestProgressListener != null) {
						httpReqestProgressListener
								.onRequestProgress(downloadCount * 1f
										/ contentLength);
					}
				}
				bytes = byteArrayOutputStream.toByteArray();
				// File file = new File(params[1]);
				// if (file != null) {
				// FileOutputStream fos = new
				// FileOutputStream(
				// file);
				// fos.write(data);
				// fos.close();
				// }
				// bitmap = BitmapFactory.decodeByteArray(
				// data, 0, data.length);
				// return bit;
				// return bytes;
			}
			// in = new BufferedInputStream(uRLConnection
			// .getInputStream());
			// bitmap = BitmapFactory.decodeStream(inputStream);
			// response = read(inputStream);
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this, "response:", bytes == null);
			// }
			if (this.sessionStr == null) {
				Map<String, List<String>> cookies = conn.getHeaderFields();
				// this.cookieHeader = conn.getg
				for (String key : cookies.keySet()) {
					// System.out.println("sdfdsfd:" + key);
					if (key == null) {
						continue;
					}
					if ("set-cookie".equals(key.toLowerCase())) {
						List<String> values = cookies.get(key);
						String sessionStrTemp = "";
						for (String value : values) {
							sessionStrTemp += value;
							sessionStrTemp += ";";
							// conn.addRequestProperty("set-cookie", string);
						}
						// loginSessionStrTemp =
						// loginSessionStrTemp.substring(0,
						// loginSessionStrTemp.length() - 1);
						// System.out.println("loginSessionStrTemp:"
						// + loginSessionStrTemp);
						this.sessionStr = sessionStrTemp;
						this.sessionKey = key;
						// OldHttpUtil.LOGINSESSIONSTR = sessionStrTemp;
						// BaseHttpManager.sessionStr = loginSessionStrTemp;
						break;
					}
				}
				// System.out.println("sessionStr:" + sessionStr);
			}
			// System.out.println("openUrl:response:" + response);
		} catch (Exception e) {
			// if (AppLibConstant.isUseLog()) {
			LogUtil.info(this,
					"End a request with exception below---XXXXXXXXXX");
			// }
			if (httpReqestProgressListener != null) {
				httpReqestProgressListener.onRequestFail();
			}
			e.printStackTrace();
			return bytes;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// if (AppLibConstant.isUseLog()) {
		LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
		// }
		return bytes;
	}

	private static String read(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream), 1024 * 4);
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader
					.readLine()) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	// private String openUrl(String url, RequestMethod requestMethod,
	// Object paramObject) {
	// LogUtil.info(this, "Begin a request--->>>>>>>>>>");
	// LogUtil.info(this, "url:", url);
	// LogUtil.info(this, "requestMethod:", requestMethod);
	// LogUtil.info(this, "paramObject:", paramObject);
	// String responseString = null;
	// try {
	// switch (requestMethod) {
	// case GET:
	// try {
	// if (paramObject != null) {
	// url = url + "?" + this.encodeUrlParam(paramObject);
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// responseString = this.doGet(url);
	// break;
	// case POST:
	// if (paramObject instanceof List) {
	// List list = (List) paramObject;
	// Object object = list.get(0);
	// if (object instanceof NameValuePair) {
	// responseString = doPost(url,
	// (List<NameValuePair>) paramObject);
	// } else {
	// responseString = doPost(url,
	// this.encodeUrlParam(paramObject));
	// }
	// } else {
	// responseString = doPost(url,
	// this.encodeUrlParam(paramObject));
	// }
	// break;
	// }
	// } catch (UnsupportedEncodingException e) {
	// LogUtil.info(this,
	// "End a request with exception below---XXXXXXXXXX");
	// e.printStackTrace();
	// return responseString;
	// }
	// LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
	// LogUtil.info(this, "responseString:", responseString);
	// return responseString;
	// }

	private String encodeUrlParam(Object paramObject)
			throws UnsupportedEncodingException {
		if (paramObject == null)
			return HttpConstant.DEFAULTPARAM;
		StringBuilder sb = new StringBuilder();
		if (paramObject instanceof Bundle) {
			Bundle bundle = (Bundle) paramObject;
			if (bundle.size() == 0) {
				return HttpConstant.DEFAULTPARAM;
			} else {
				boolean isFirst = true;
				for (String key : bundle.keySet()) {
					if (isFirst)
						isFirst = false;
					else
						sb.append("&");
					// Object object;
					sb.append(key
							+ "="
							+ URLEncoder.encode(bundle.getString(key),
									HttpConstant.ENCODE));
					// + paramBundle.getString(key));
				}
			}
		} else if (paramObject instanceof Map) {
			HashMap<String, String> map = (HashMap<String, String>) paramObject;
			if (map.size() == 0) {
				return HttpConstant.DEFAULTPARAM;
			} else {
				boolean isFirst = true;
				for (Object key : map.keySet()) {
					if (isFirst)
						isFirst = false;
					else
						sb.append("&");
					// Object object;
					sb.append(key
							+ "="
							+ URLEncoder.encode(map.get(key).toString(),
									HttpConstant.ENCODE));
					// + paramBundle.getString(key));
				}
			}
		} else if (paramObject instanceof List
				&& ((List) paramObject).get(0) instanceof NameValuePair) {
			// if (paramObject instanceof List) {
			// List list = (List) paramObject;
			// Object object = list.get(0);
			// if (object instanceof NameValuePair) {
			boolean isFirst = true;
			for (NameValuePair nameValuePair : (List<NameValuePair>) paramObject) {
				if (isFirst)
					isFirst = false;
				else
					sb.append("&");
				// Object object;
				sb.append(nameValuePair.getName()
						+ "="
						+ URLEncoder.encode(nameValuePair.getValue(),
								HttpConstant.ENCODE));
				// + paramBundle.getString(key));
			}
		} else {
			// sb.append("&");
			// Object object;
			sb.append(HttpConstant.DEFAULT_KEY_REQUEST_PARAM
					+ "="
					+ URLEncoder.encode(this.jsonManager.getJson(paramObject),
							HttpConstant.ENCODE));
		}
		return sb.toString();
	}

	public <T> T getObject(String jsonString, Type type) {
		return this.jsonManager.getObject(jsonString, type);
	}

	// public <T> T getObjectFromPage(String jsonString, Type type) {
	// try {
	// JSONObject jo = new JSONObject(jsonString);
	// JSONArray jsonArray = (JSONArray) jo.get("result");
	// return this.getObject(jsonArray.toString(), type);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	private boolean checkNetWork() {
		boolean hasInternet = hasInternet(context);
		if (!hasInternet) {
			if (onNetResultListenerMain != null) {
				onNetResultListenerMain.onNetNotConnect();
			}
			// else if (this.getOnNetResultListener() != null) {
			// this.getOnNetResultListener().onNetNotConnect();
			// }
			// this.handler.obtainMessage().sendToTarget();
			return false;
		}
		return true;
	}

	public static boolean hasInternet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}

	private void sessonInject(HttpRequest httpRequest) {
		Header header = BaseHttpOperator.cookieHeader;
		if (header != null) {
			// System.out.println("setHeaderName:" + header.getName());
			// System.out.println(header.getValue());
			// httpRequest.addHeader(this.getCookieHeader());
			// httpRequest.setHeaders(this.getCookieHeaders());
			httpRequest.addHeader(header);
		}
	}

	public static void sessonInject(HttpURLConnection httpURLConnection) {
		if (httpURLConnection == null) {
			return;
		}
		if (BaseHttpOperator.sessionKey == null) {
			return;
		}
		httpURLConnection.setRequestProperty(
		// BaseHttpOpera.cookieHeader.getName()
				"Cookie",
				// BaseHttpOpera.cookieHeader.getValue()
				sessionStr);
	}

	private void sessonSet(HttpResponse httpResponse) {
		if (BaseHttpOperator.cookieHeader != null) {
			return;
		}
		Header[] headers = httpResponse.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			// System.out.println("headName:+" + header.getName());
			// System.out.println("headName:-" + header.getValue());
			if ("set-cookie".equals(header.getName().toLowerCase())) {
				BaseHttpOperator.cookieHeader = header;
				// System.out.println(header.getName() + "+++++++++++++++++"
				// + header.getValue());
				break;
			}
		}
	}

	// private void printHead(HttpResponse httpResponse) {
	// Header[] headers = httpResponse.getAllHeaders();
	// for (int i = 0; i < headers.length; i++) {
	// Header header = headers[i];
	// System.out.println("PPPPheadName:+" + header.getName());
	// System.out.println("PPPPPPPPheadName:-" + header.getValue());
	// // if ("set-cookie".equals(header.getName().toLowerCase())) {
	// // // System.out.println(header.getName() + "-----------"
	// // // + header.getValue());
	// // break;
	// }
	// }

	// @Override
	public String doGet(String url) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		// TODO Auto-generated method stub
		// System.out.println("++++++++++++++++doGet" + url);
		HttpGet httpRequest = new HttpGet(url);
		// Header header = this.getCookieHeader();
		// if (header != null) {
		// // System.out.println(header.getName());
		// // System.out.println(header.getValue());
		// httpRequest.addHeader(this.getCookieHeader());
		// httpRequest.setHeaders(this.getCookieHeaders());
		// httpRequest.addHeader(header);
		// }
		this.sessonInject(httpRequest);
		String strResult = "doGetError";
		try {
			/* 鍙戦�佽姹傚苟绛夊緟鍝嶅簲 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 鑻ョ姸鎬佺爜涓�200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 璇昏繑鍥炴暟鎹� */
				// strResult = EntityUtils.toString(httpResponse.getEntity());
				strResult = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				this.sessonSet(httpResponse);
				// this.printHead(httpResponse);
				// Header[] headers = httpResponse.getAllHeaders();
				// for (int i = 0; i < headers.length; i++) {
				// Header header = headers[i];
				// // System.out.println("headName:+" + header.getName());
				// // System.out.println("headName:-" + header.getValue());
				// if ("set-cookie".equals(header.getName().toLowerCase())) {
				// // System.out.println(header.getName() + "-----------"
				// // + header.getValue());
				// break;
				// }
				// }
			} else {
				strResult = "false";
				// strResult = "Error Response: "
				// + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "false";
			e.printStackTrace();
		}
		// Log.v("strResult", strResult);
		return strResult;
	}

	// doGet澶氫釜鍙傛暟
	// public String doGet(String url, List<NameValuePair> params) {
	// if (!this.checkNetWork()) {
	// }
	// /** 寤虹珛HTTPGet瀵硅薄 **/
	// String paramStr = "";
	// if (params == null)
	// params = new ArrayList<NameValuePair>();
	// /** 杩唬璇锋眰鍙傛暟闆嗗悎 **/
	//
	// for (NameValuePair obj : params) {
	// paramStr += paramStr = "&" + obj.getName() + "="
	// + URLEncoder.encode(obj.getValue());
	// }
	// if (!paramStr.equals("")) {
	// paramStr = paramStr.replaceFirst("&", "?");
	// url += paramStr;
	// }
	// return doGet(url);
	// }

	// @Override
	public String doPost(String url, String info) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		// System.out
		// .println("++++++++++++++++doGetdoPost(String url, String info");
		// TODO Auto-generated method stub
		/* 寤虹珛HTTPPost瀵硅薄 */
		HttpPost httpRequest = new HttpPost(url);
		String strResult = "doPostError";
		try {
			StringEntity stringEntity = new StringEntity(info, HTTP.UTF_8);
			httpRequest.setEntity(stringEntity);
			this.sessonInject(httpRequest);
			/* 鍙戦�佽姹傚苟绛夊緟鍝嶅簲 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 鑻ョ姸鎬佺爜涓�200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 璇昏繑鍥炴暟鎹� */
				strResult = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				this.sessonSet(httpResponse);
				// this.printHead(httpResponse);
			} else {
				strResult = "false";
				// strResult = "Error Response: "
				// + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "false";
			e.printStackTrace();
		}
		// Log.v("strResult", strResult);
		return strResult;
	}

	// @Override
	public String doPost(String url, List<NameValuePair> params) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		// System.out
		//
		// .println("++++++++++++++++doPost(String url, List<NameValuePair> params");
		// System.out
		//
		// .println("--------------------begin--String doPost(String url, List<NameValuePair> params)");
		/* 寤虹珛HTTPPost瀵硅薄 */
		HttpPost httpRequest = new HttpPost(url);
		String strResult = "doPostError";
		try {
			/* 娣诲姞璇锋眰鍙傛暟鍒拌姹傚璞� */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			this.sessonInject(httpRequest);
			// Header[] headers = this.getCookieHeaders();
			// if (headers != null) {
			// for (int i = 0; i < headers.length; i++) {
			// Header header2 = headers[i];
			// if (!"transfer-encoding".equals(header2.getName()
			// .toLowerCase())
			// && !"date".equals(header2.getName().toLowerCase())
			// && !"content-type".equals(header2.getName()
			// .toLowerCase())) {
			// httpRequest.addHeader(header2);
			// }
			// System.out.println("---------" + header2.getName());
			// System.out.println("+++++++++" + header2.getValue());
			// }
			// }
			/* 鍙戦�佽姹傚苟绛夊緟鍝嶅簲 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 鑻ョ姸鎬佺爜涓�200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 璇昏繑鍥炴暟鎹� */
				strResult = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				// this.printHead(httpResponse);
				// System.out
				//
				// .println("VVVVVVVVVVVVVVVHHHHHHHHHHString doPost(String urlHHHHHHHVVVVVV"
				// + strResult);
			} else {
				strResult = "false";
				// strResult = "Error Response: "
				// + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "false";
			e.printStackTrace();
		}
		// Log.v("strResult", strResult);
		// System.out.println(strResult);
		return strResult;
	}

	public String loginPost(String url, List<NameValuePair> params) {
		if (!this.checkNetWork()) {
			// return this.notNetError;
		}
		/* 寤虹珛HTTPPost瀵硅薄 */
		HttpPost httpRequest = new HttpPost(url);
		String strResult = "doPostError";
		try {
			/* 娣诲姞璇锋眰鍙傛暟鍒拌姹傚璞� */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 鍙戦�佽姹傚苟绛夊緟鍝嶅簲 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 鑻ョ姸鎬佺爜涓�200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 璇昏繑鍥炴暟鎹� */
				strResult = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			} else {
				strResult = "false";
				// strResult = "Error Response: "
				// + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			strResult = "false";
			e.printStackTrace();
		} catch (IOException e) {
			strResult = "IOException";
			e.printStackTrace();
		} catch (Exception e) {
			strResult = "false";
			e.printStackTrace();
		}
		// Log.v("strResult", strResult);
		// System.out.println("+++++++++++++++" + url);
		return strResult;
	}

	public byte[] getByteArray(String url, RequestMethod requestMethod,
			Object paramObject, RequestListener fileDownloadListener) {
		if (!this.hasInternet(context)) {
			fileDownloadListener.onFail(RequestFailReason._NET);
			return null;
		}
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byte[] bytes = null;
			LogUtil.info(this, "Begin a request--->>>>>>>>>>");
			if (requestMethod == requestMethod.GET && paramObject != null) {
				url = url + "?" + this.encodeUrlParam(paramObject);
			}
			LogUtil.info(this, "url:", url);
			LogUtil.info(this, "requestMathod:", requestMethod);
			LogUtil.info(this, "paramObject:", paramObject);
			httpURLConnection = (HttpURLConnection) new URL(url)
					.openConnection();
			if (this.sessionStr != null) {
				httpURLConnection.setRequestProperty("cookie", sessionStr);
				// httpURLConnection.setRequestProperty("Accept-Encoding",
				// "identity");
			}
			httpURLConnection.setConnectTimeout(5 * 1000);
			if (requestMethod == RequestMethod.GET) {
				httpURLConnection.setRequestMethod("GET");
				// httpURLConnection.setRequestProperty("Content-Type",
				// "application/x-www-form-urlencode");
				httpURLConnection.setRequestProperty("Accept-Encoding",
						"identity");
				// System.out.println("BBBBBBBBBBVVVVVVVVVVV");
				httpURLConnection.connect();
			} else {
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencode");
				if (paramObject != null) {
					httpURLConnection.getOutputStream().write(
							this.encodeUrlParam(paramObject).getBytes(
									HttpConstant.ENCODE));
				}
			}
			double contentLength = httpURLConnection.getContentLength();
			inputStream = httpURLConnection.getInputStream();
			if (inputStream != null) {
				byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] buf = new byte[128];
				// System.out.println(contentLength);
				int read = -1;
				long downloadCount = 0;
				while ((read = inputStream.read(buf)) != -1) {
					byteArrayOutputStream.write(buf, 0, read);
					downloadCount += read;
					fileDownloadListener.onProgress(downloadCount,
							contentLength);
				}
				bytes = byteArrayOutputStream.toByteArray();
			}
			LogUtil.info(this, "End a request successfully---VVVVVVVVVV");
			LogUtil.info(this, "response:", bytes == null);
			fileDownloadListener.onSuccess(bytes);
			return bytes;
		} catch (Exception e) {
			LogUtil.info(this,
					"End a request with exception below---XXXXXXXXXX");
			fileDownloadListener.onFail(RequestFailReason.EXCEPTION);
			e.printStackTrace();
			return null;
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			StreamUtil.closeStream(byteArrayOutputStream);
			StreamUtil.closeStream(inputStream);
		}
	}

	// public String doPost_2(String url, List<NameValuePair> params) {
	// System.out
	// .println("++++++++++++++++public String doPost_2(String url, List<NameValuePair>");
	// /* 寤虹珛HTTPPost瀵硅薄 */
	// HttpPost httpRequest = new HttpPost(url);
	// String strResult = "doPostError";
	// try {
	// /* 娣诲姞璇锋眰鍙傛暟鍒拌姹傚璞� */
	// httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	// this.sessonInject(httpRequest);
	// /* 鍙戦�佽姹傚苟绛夊緟鍝嶅簲 */
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// /* 鑻ョ姸鎬佺爜涓�200 ok */
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// /* 璇昏繑鍥炴暟鎹� */
	// strResult = EntityUtils.toString(httpResponse.getEntity(),
	// HTTP.UTF_8);
	// } else {
	// strResult = "false";
	// // strResult = "Error Response: "
	// // + httpResponse.getStatusLine().toString();
	// }
	// } catch (ClientProtocolException e) {
	// strResult = "false";
	// e.printStackTrace();
	// } catch (IOException e) {
	// strResult = "IOException";
	// e.printStackTrace();
	// } catch (Exception e) {
	// strResult = "false";
	// e.printStackTrace();
	// }
	// // Log.v("strResult", strResult);
	// return strResult;
	// }

	public HttpClient getHttpClient() {
		// 鍒涘缓 HttpParams 浠ョ敤鏉ヨ缃� HTTP 鍙傛暟锛堣繖涓�閮ㄥ垎涓嶆槸蹇呴渶鐨勶級
		HttpParams httpParams = new BasicHttpParams();
		// 璁剧疆杩炴帴瓒呮椂鍜� Socket 瓒呮椂锛屼互鍙� Socket 缂撳瓨澶у皬
		HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 60 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 璁剧疆閲嶅畾鍚戯紝缂虹渷涓� true
		HttpClientParams.setRedirecting(httpParams, true);
		// 璁剧疆 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 鍒涘缓涓�涓� HttpClient 瀹炰緥
		// 娉ㄦ剰 HttpClient httpClient = new HttpClient(); 鏄疌ommons HttpClient
		// 涓殑鐢ㄦ硶锛屽湪 Android 1.5 涓垜浠渶瑕佷娇鐢� Apache 鐨勭己鐪佸疄鐜� DefaultHttpClient
		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	// public OnNetResultListener getOnNetResultListener() {
	// return onNetResultListener;
	// }
	//
	// public void setOnNetResultListener(OnNetResultListener
	// onNetResultListener) {
	// this.onNetResultListener = onNetResultListener;
	// }

	// public Context getContext() {
	// return context;
	// }
	//
	// public void setContext(Context context) {
	// this.context = context;
	// }

	// protected boolean isLogin(String name, String pwd) {
	// String url = (new WebServiceApi(context)).getAPI_USER_CHECKED();
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("name", name));
	// params.add(new BasicNameValuePair("passwd", pwd));
	// String result = loginPost(url, params);
	// // System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGG:" + result);
	// if (!result.equals("false")) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	// public void setCookieHeaders(Header[] cookieHeaders) {
	// this.cookieHeaders = cookieHeaders;
	// }
	//
	// public Header[] getCookieHeaders() {
	// return cookieHeaders;
	// }
}