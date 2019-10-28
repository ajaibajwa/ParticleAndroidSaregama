package com.example.androidparticlestarter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {
    // MARK: Debug info
    private final String TAG="SMARTLIGHT";

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "letv5050@gmail.com";
    private final String PARTICLE_PASSWORD = "passion100";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "38002a000247363333343435";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();

    }


    /**
     * Custom function to connect to the Particle Cloud and get the device
     */
    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                return -1;

            }

            @Override
            public void onSuccess(Object o) {

                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }



    public void turnLightsOnPressed(View view) {
        Toast.makeText(getApplicationContext(), "On pressed", Toast.LENGTH_SHORT)
                .show();

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------

                // what functions are "public" on the particle?
                Log.d(TAG, "Availble functions: " + mDevice.getFunctions());


                List<String> functionParameters = new ArrayList<String>();
                //functionParameters.add();
                try {
                    mDevice.callFunction("turnLightsOn", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });







    }
    public void playNoteSound(View view){
        Toast.makeText(getApplicationContext(), "btn pressed", Toast.LENGTH_SHORT)
                .show();

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------

                // what functions are "public" on the particle?
                Log.d(TAG, "Availble functions: " + mDevice.getFunctions());


                List<String> functionParameters = new ArrayList<String>();
                int btnId = view.getId();
                //functionParameters.add();
                try {
                    if (btnId == R.id.btn1){
                        mDevice.callFunction("playNote1", functionParameters);
                    }
                    if (btnId == R.id.btn2){
                        mDevice.callFunction("playNote2", functionParameters);
                    }
                    if (btnId == R.id.btn3){
                        mDevice.callFunction("playNote3", functionParameters);
                    }
                    if (btnId == R.id.btn4){
                        mDevice.callFunction("playNote4", functionParameters);
                    }
                    if (btnId == R.id.btn5){
                        mDevice.callFunction("playNote5", functionParameters);
                    }
                    if (btnId == R.id.btn6){
                        mDevice.callFunction("playNote6", functionParameters);
                    }
                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }
    int count = 0;
    public void subscribeBtnPressed(View view){

        TextView t = findViewById(R.id.resultsLabel);
        //t.setText("Subscribe pressed");
        Toast.makeText(getApplicationContext(), "Subscribe Pressed", Toast.LENGTH_SHORT)
                .show();

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                subscriptionId = ParticleCloudSDK.getCloud().subscribeToAllEvents(
                        "abc",  // the first argument, "eventNamePrefix", is optional
                        new ParticleEventHandler() {
                            public void onEvent(String eventName, ParticleEvent event) {
                                count = count + 1;
                                Log.i(TAG,
                                        "Received event with payload: " + event.dataPayload);
                                try {
                                    // code runs in a thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            t.setText(event.dataPayload + ": " + count);
                                        }
                                    });
                                } catch (final Exception ex) {
                                    Log.i("---","Exception in thread");
                                }

                            }

                            public void onEventError(Exception e) {
                                Log.e(TAG, "Event error: ", e);
                            }
                        });


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }

    public void turnLightsOffPressed(View view) {
        Toast.makeText(getApplicationContext(), "Off pressed", Toast.LENGTH_SHORT)
                .show();



        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                // put your logic here to talk to the particle
                // --------------------------------------------

                // what functions are "public" on the particle?
                Log.d(TAG, "Availble functions: " + mDevice.getFunctions());


                List<String> functionParameters = new ArrayList<String>();
                //functionParameters.add();
                try {
                    mDevice.callFunction("turnLightsOff", functionParameters);

                } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }


                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                // put your success message here
                Log.d(TAG, "Success!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                // put your error handling code here
                Log.d(TAG, exception.getBestMessage());
            }
        });



    }

}
