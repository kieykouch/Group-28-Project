package com.group28.cs160.noms4two;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.group28.cs160.shared.NutritionFacts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BarcodeFragment extends Fragment implements View.OnClickListener{

    public BarcodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BarcodeFragment.
     */
    public static BarcodeFragment newInstance() {
        BarcodeFragment fragment = new BarcodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);
        textureView = (TextureView) rootView.findViewById(R.id.preview);
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        textureView.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        Log.d("Barcode", "Attached");
    }

    @Override
    public void onDetach() {
        Log.d("Barcode", "detached");
        closeCamera();
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Barcode", "resumed!");
        if (textureView.isAvailable()) {
            Log.d("Barcode", "TextureView already available");
            openCamera();
            startBackgroundHandler();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Barcode", "Paused!");
        stopBackgroundHandler();
        closeCamera();
    }

    @Override
    public void onClick(View v) {
        Log.d("Barcode", "clicked");
        try {
            mCaptureSession.stopRepeating();
            captureStillPicture();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String getBackCameraId(CameraManager manager) {
        try {
            String[] cameraIds = manager.getCameraIdList();
            for (String cameraId : cameraIds) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (orientation != null && orientation == CameraCharacteristics.LENS_FACING_BACK) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void requestCameraPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_ID);
        }
    }

    private Size getSize(String cameraId) {
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            return Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            String backCameraId = getBackCameraId(manager);
            size = getSize(backCameraId);
            assert size != null;
            Log.d("Barcode", String.format("%d, %d", size.getHeight(), size.getWidth()));
            try {
                assert backCameraId != null;
                manager.openCamera(backCameraId, mStateCallback, handler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCameraPreviewSession() {
        try {
            Log.d("Barcode", "Creating preview session and starting");
            // This is the output Surface we need to start preview.
//            surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            assert surfaceTexture != null;
            Surface surface = new Surface(surfaceTexture);

            mImageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), ImageFormat.JPEG, 2);
            mImageReader.setOnImageAvailableListener(listener, handler);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
//            mPreviewRequestBuilder.addTarget(reader.getSurface());

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
//                                setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                CaptureRequest mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, handler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != mCaptureSession) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    public void startBackgroundHandler() {
        handlerThread = new HandlerThread("Camera");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public void stopBackgroundHandler() {
        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private void captureStillPicture() {
        Log.d("Barcode","Tried capturing");
        try {
            final Activity activity = getActivity();
            if (null == activity || null == cameraDevice) {
                return;
            }

            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // Orientation
//            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
//                    showToast("Saved: " + mFile);
//                    Log.d(TAG, mFile.toString());
//                    unlockFocus();
                    Log.d("Barcode", "Capture Complete");
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void getFoodInfo(String barcode) {
        new AsyncTask<String, String, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                String query = String.format("upc=%s&appId=afec6ece", params[0]);
//                JSONObject returnObject = getFromURL("https://api.nutritionix.com/v1_1/item",
//                        query, "appKey=2c2086c1e91e303595827289d5a25630");
                JSONObject returnObject = fakeGetFromURL("https://api.nutritionix.com/v1_1/item",
                        query, "appKey=2c2086c1e91e303595827289d5a25630");
                assert returnObject != null;

                Log.d("Barcode", returnObject.toString());
                return returnObject;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                String name = result.optString("item_name");
                Log.d("Barcode", String.format("Product name is %s", name));
                ArrayList<String> allergens = new ArrayList<>();
                HashMap<String, Integer> ingredients = new HashMap<>();
                for (String allergenName : getResources().getStringArray(R.array.allergens))
                    if (result.optBoolean(allergenName))
                        allergens.add(allergenName);
                String[] ingredientsJson = getResources().getStringArray(R.array.ingredients_json);
                String[] ingredientsNames = getResources().getStringArray(R.array.ingredients_names);
                for (int i = 0; i < ingredientsJson.length; i ++) {
                    int amount = result.optInt(ingredientsJson[i]);
                    if (amount > 0) {
                        ingredients.put(ingredientsNames[i], amount);
                    }
                }

                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                NutritionFacts facts = NutritionFacts.fromHashMap(name, ingredients);
                intent.putExtra("nutrient_facts", (Serializable) facts);
                intent.putStringArrayListExtra("allergens", allergens);
                startActivity(intent);
            }

        }.execute(barcode);
    }

    static JSONObject fakeGetFromURL(String url, String query, String apikey) {
        JSONObject ret = null;
        try {
            ret = new JSONObject("{\"old_api_id\":null,\"item_id\":\"51c38f3c97c3e6d3d972ef8d\",\"item_name\":\"Cereal For Baby, Rice, Stage 1\",\"leg_loc_id\":null,\"brand_id\":\"51db37c3176fe9790a8991f6\",\"brand_name\":\"Beech-Nut\",\"item_description\":\"Rice, Stage 1\",\"updated_at\":\"2014-11-24T20:24:24.000Z\",\"nf_ingredient_statement\":\"Rice Flour, Contains Less Than 1% of the Following: Sunflower Oil and Rice Bran Extract. Vitamins and Minerals: Tricalcium Phosphate, ascorbic Acid (Vitamin C), Electrolytic Iron, Zinc Sulfate, D-Alpha Tocopherol Acetate (Vitamin E), Niacinamide, Mixed Tocopherols, Mononitrate (Vitamin B1), Riboflavin (Vitamin B2), Pyridoxine Hydrochloride (Vitamin B6), Vitamin B12 and Folic Acid.\",\"nf_water_grams\":null,\"nf_calories\":60,\"nf_calories_from_fat\":0,\"nf_total_fat\":null,\"nf_saturated_fat\":null,\"nf_trans_fatty_acid\":null,\"nf_polyunsaturated_fat\":null,\"nf_monounsaturated_fat\":null,\"nf_cholesterol\":null,\"nf_sodium\":null,\"nf_total_carbohydrate\":null,\"nf_dietary_fiber\":null,\"nf_sugars\":0,\"nf_protein\":1,\"nf_vitamin_a_dv\":0,\"nf_vitamin_c_dv\":25,\"nf_calcium_dv\":25,\"nf_iron_dv\":45,\"nf_refuse_pct\":null,\"nf_servings_per_container\":7,\"nf_serving_size_qty\":0.25,\"nf_serving_size_unit\":\"cup\",\"nf_serving_weight_grams\":15,\"allergen_contains_milk\":null,\"allergen_contains_eggs\":null,\"allergen_contains_fish\":null,\"allergen_contains_shellfish\":null,\"allergen_contains_tree_nuts\":null,\"allergen_contains_peanuts\":null,\"allergen_contains_wheat\":null,\"allergen_contains_soybeans\":true,\"allergen_contains_gluten\":null,\"usda_fields\":null}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    static JSONObject getFromURL(String url, String query, String apikey) {
        Log.d("Info", String.format("loading url %s?%s&%s", url, query, apikey));
        JSONObject json = null;
        try {
            HttpsURLConnection connection =
                    (HttpsURLConnection) new URL(String.format("%s?%s&%s",
                            url, query, apikey)).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            json = new JSONObject(builder.toString());
        } catch (IOException |JSONException e) {
            Log.e("Info", e.getMessage());
        }
        return json;
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("Barcode", "Texture is Available");
            openCamera();
            startBackgroundHandler();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };


    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
        }
    };

    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {


        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
//            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
//            process(result);
        }
    };

    private final ImageReader.OnImageAvailableListener listener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.d("Barcode", "Image available");
            BarcodeDetector detector = new BarcodeDetector.Builder(getContext()).build();
            Image image = reader.acquireNextImage();
            ByteBuffer array = image.getPlanes()[0].getBuffer();
            byte[] arr = new byte[array.remaining()];
            array.get(arr);
            Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);

            SparseArray<Barcode> barcodes = detector.detect(
                    new Frame.Builder().setBitmap(bitmap).build());
            // TODO change this
            if (barcodes.size() == 0) {
                getFoodInfo("52200004265");
                return;
            }
            if (barcodes.size() == 0) {
                showToast("No barcode detected");
                try {
                    mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, handler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                String barcode = barcodes.get(barcodes.keyAt(0)).rawValue;
                Log.d("Barcode", String.format("barcode detected is %s", barcode));
                getFoodInfo(barcode);
            }
        }
    };

    final int CAMERA_REQUEST_ID = 0;
    private CameraDevice cameraDevice;
    private CameraManager manager;
    private TextureView textureView;
    private CameraCaptureSession mCaptureSession;
    private HandlerThread handlerThread;
    private Handler handler;
    private ImageReader mImageReader;
    private Size size;
    CaptureRequest.Builder mPreviewRequestBuilder;
}
