package hmm.sensortest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends Activity {

    //�߳��Ƿ�ֹͣ��־λ
    private boolean STOP = true;
    private boolean END  = false;
    
    private float acceValusX = 0f;
    private float acceValusY = 0f;
    private float acceValusZ = 0f;

    private mHandler handler;
    private mThread thread;
    private Button startButton;
    private Button stopButton;
    private TextView xTextView;
    private TextView yTextView;
    private TextView zTextView;
    
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private OnSensorEventListener mOnSensorEventListener = new OnSensorEventListener();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new mHandler();
        thread = new mThread();

        xTextView = (TextView)findViewById(R.id.x_textView);
        yTextView = (TextView)findViewById(R.id.y_textView);
        zTextView = (TextView)findViewById(R.id.z_textView);
        startButton = (Button)findViewById(R.id.button_start);
        stopButton = (Button)findViewById(R.id.button_stop);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        
        //���á���ʼ���ԡ���ť�¼�����
        startButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                
                if (END)
                	thread = new mThread();
                
                //���ñ�־λ
                STOP = false;
                //�����µ��߳�
                thread.start();
            }
        });
        
        //���á�ֹͣ���ԡ���ť�¼�����
        stopButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                
                //���ñ�־λ
                STOP = true;
            }
        });

        //��ϵͳ�����л�ô������ܷ���
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //ע�ᴫ��������
        mSensorManager.registerListener(mOnSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    //�Լ������Handler��
    private class mHandler extends Handler
    {
        @Override
        public void handleMessage (Message msg)
        {
            switch(msg.what)
            {
                case 1:
                {
                    //��ʾ
                    xTextView.setText(Float.toString(acceValusX));
                    yTextView.setText(Float.toString(acceValusY));
                    zTextView.setText(Float.toString(acceValusZ));
                    break;
                }
            }
        }
    }

    private class OnSensorEventListener implements SensorEventListener
    {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) 
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onSensorChanged(SensorEvent event) 
        {
            // TODO Auto-generated method stub
            
            //���x���ֵ
            acceValusX = event.values[0];
            //���y���ֵ
            acceValusY = event.values[1];
            //���z���ֵ
            acceValusZ = event.values[2];
            
        }
        
    }
    
    //�Լ������Thread��
    private class mThread extends Thread
    {

        @Override
        //�߳�����ʱִ���������
        public void run()
        {
            //һֱѭ����ֱ����־λΪ���桱
            while(!STOP)
            {
                try {
                    //��ʱ500ms
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Message msg = new Message();
                //��Ϣ��־
                msg.what = 1;
                //���������Ϣ
                handler.sendMessage(msg);
            }
            
            END = true;
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //ע������������
        mSensorManager.unregisterListener(mOnSensorEventListener, mAccelerometer);
    }
}
