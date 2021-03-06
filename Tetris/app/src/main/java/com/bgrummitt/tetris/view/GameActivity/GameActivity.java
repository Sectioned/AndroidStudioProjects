package com.bgrummitt.tetris.view.GameActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Window;
import android.view.WindowManager;

import com.bgrummitt.tetris.controller.Other.SwipeGestureDetection;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make the title bar go away programmatically
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Make the screen orientation only Portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(new GameView(this));
    }

}
