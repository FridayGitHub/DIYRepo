package hmm.flashlight;

/*import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}*/

import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 *  �����ֵ�Ͳ
 * @author huahua
 */
public class MainActivity extends Activity implements SurfaceHolder.Callback{
	private static final String TAG = "huahua";      
	/**
	 * ����APPʱ�ı�������ֵ
	 */
	int normal;
	/**
	 * ����APPʱ���Ƿ�Ϊ�Զ���������״̬
	 */
	boolean AutoBrightnessOpen = false;
	
	private Camera camera; 
	
	private SurfaceView surfaceView;    
	private SurfaceHolder surfaceHolder;      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//ȥ��title   
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		         
        //ȥ��Activity�����״̬��   
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        WindowManager.LayoutParams.FLAG_FULLSCREEN); 

		setContentView(R.layout.activity_main);
		
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);      
		surfaceHolder = surfaceView.getHolder();      
		surfaceHolder.addCallback(this);      
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);     
		
		if(isAutoBrightness(getContentResolver()))
		{
			AutoBrightnessOpen = true;
		}
		
		normal = Settings.System.getInt(getContentResolver(),  
		        Settings.System.SCREEN_BRIGHTNESS, 255); 
		
		PackageManager pm= this.getPackageManager();
		 FeatureInfo[]  features=pm.getSystemAvailableFeatures();
		 for(FeatureInfo f : features)
		 {
		   if(PackageManager.FEATURE_CAMERA_FLASH.equals(f.name))   //�ж��豸�Ƿ�֧�������
		   {
			   Log.d("huahua","֧�������");
		   }
		 }
		 
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
        Closeshoudian();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Openshoudian();
	}
	
	/**
     * �ж��Ƿ������Զ����ȵ���
     * 
    * @param aContext
     * @return
     */
    public boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }
    
    /**
     * ֹͣ�Զ����ȵ���
     * 
    * @param activity
     */
    public void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
    
    /**
     * �ָ��Զ����ȵ���
     * 
    * @param activity
     */
    public void setAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }
    
    /**
     * ���ֵ�Ͳ
     */
    public void Openshoudian()
    {
    	//�쳣����һ��Ҫ�ӣ�����Camera��ʧ�ܵĻ���������
		try {
	        Log.d("huahua","camera��");
			camera = Camera.open(); 
		} catch (Exception e) {
			Log.d("huahua","Camera��������");
			Toast.makeText(MainActivity.this, "Camera��ռ�ã����ȹر�", Toast.LENGTH_SHORT).show();
		}
		
		if(camera != null)
		{
			//�������
			 camera.startPreview();    
			 Camera.Parameters parameter = camera.getParameters();  
			 parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); 
			 camera.setParameters(parameter);
			 Log.d("huahua","����ƴ�");
			 
			 //�ȹر��Զ����ڱ��⹦�ܣ��ſ��Ե��ڱ���
			 if(AutoBrightnessOpen)
			 {
				stopAutoBrightness(MainActivity.this);
			 }
			 
			//����������Ϊ����
	        WindowManager.LayoutParams lp = getWindow().getAttributes();
	        lp.screenBrightness = Float.valueOf(255) * (1f / 255f);
	        getWindow().setAttributes(lp);
		}
    }
    
    /**
     * �ر��ֵ�Ͳ
     */
    public void Closeshoudian()
    {
        if (camera != null)
        {
        	//�ر������
        	Log.d("huahua", "closeCamera()");
			camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
			camera.setParameters(camera.getParameters());
	        camera.stopPreview();
	        camera.release();
	        camera = null;
	        
	        //�ָ��������ǰ�ı���ֵ
	        WindowManager.LayoutParams lp = getWindow().getAttributes();
	        lp.screenBrightness = Float.valueOf(normal) * (1f / 255f);
	        getWindow().setAttributes(lp);
	        
	        //�������APPʱ����Ϊ�Զ����ڣ����˳�ʱ��Ҫ�ָ�Ϊ�Զ�����״̬
			 if(AutoBrightnessOpen)
			 {
				 setAutoBrightness(MainActivity.this);
			 }
        }
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {        
			if(camera != null)
			{
			camera.setPreviewDisplay(holder);      
			}
		} catch (IOException e) {        
			e.printStackTrace();      
		}  
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}


}

