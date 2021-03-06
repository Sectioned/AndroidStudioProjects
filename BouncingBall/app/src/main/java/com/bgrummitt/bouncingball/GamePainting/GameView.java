package com.bgrummitt.bouncingball.GamePainting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bgrummitt.bouncingball.Game.SinglePlayerPong;

//Surface view holds the canvas
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = GameView.class.getSimpleName();

    private MainThread thread;
    private SinglePlayerPong game;

    public GameView(Context context) {
        super(context);

        //This allows you intercept events
        getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //Create new thread class with the SurfaceHolder and context
        thread = new MainThread(getHolder(), this);
        //Keep the inputs on this thread and not on the new one
        setFocusable(true);

        game = new SinglePlayerPong(getResources());

        //Start the games update infinite loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //To stop the thread it may take a few attempts
        boolean retry = true;
        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update(){
        game.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            game.screenTouched(event.getX(), event.getY());
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            game.screenReleased(event.getX(), event.getY());
        }
        return true;
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(canvas != null){
            canvas.drawColor(Color.WHITE);
            game.draw(canvas);
        }
    }
}
