package com.reconinstruments.camerasample;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "CameraPreview";

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private CamcorderProfile mProfile;

	public CameraPreview(Context context) {
		super(context);
		init();
	}
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mCamera != null) {
			refreshCamera();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera = null;
		}
	}

	public void refreshCamera() {
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mProfile.videoFrameWidth,mProfile.videoFrameHeight);
			mCamera.setParameters(parameters);

			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void setCamera(Camera camera, CamcorderProfile profile) {
		mCamera = camera;
		mProfile = profile;
		refreshCamera();
	}
}