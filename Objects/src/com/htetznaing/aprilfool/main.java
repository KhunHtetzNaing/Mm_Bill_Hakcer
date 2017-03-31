package com.htetznaing.aprilfool;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.aprilfool.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _ad1 = null;
public static anywheresoftware.b4a.samples.httputils2.httpjob _job1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b4 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _b1bg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _b2bg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _b3bg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _b4bg = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _b = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _i = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.objects.ButtonWrapper _share = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _sbg = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public com.htetznaing.aprilfool.mpt _mpt = null;
public com.htetznaing.aprilfool.progress _progress = null;
public com.htetznaing.aprilfool.telenor _telenor = null;
public com.htetznaing.aprilfool.ooredoo _ooredoo = null;
public com.htetznaing.aprilfool.mectel _mectel = null;
public com.htetznaing.aprilfool.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (mpt.mostCurrent != null);
vis = vis | (progress.mostCurrent != null);
vis = vis | (telenor.mostCurrent != null);
vis = vis | (ooredoo.mostCurrent != null);
vis = vis | (mectel.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 37;BA.debugLine="job1.Initialize(\"check\", Me)";
_job1._initialize(processBA,"check",main.getObject());
 //BA.debugLineNum = 38;BA.debugLine="job1.Download(\"https://www.google.com\")";
_job1._download("https://www.google.com");
 //BA.debugLineNum = 39;BA.debugLine="ProgressDialogShow(\"Please Wait...\"&CRLF&\"Checkin";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Please Wait..."+anywheresoftware.b4a.keywords.Common.CRLF+"Checking Internet Connection!"));
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 143;BA.debugLine="If I.Ready Then I.Show Else I.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 144;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _b_adscreendismissed() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub B_AdScreenDismissed";
 //BA.debugLineNum = 178;BA.debugLine="Log(\"B Dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("B Dismissed");
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public static String  _b_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub B_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 170;BA.debugLine="Log(\"B failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("B failed: "+_errorcode);
 //BA.debugLineNum = 171;BA.debugLine="End Sub";
return "";
}
public static String  _b_receivead() throws Exception{
 //BA.debugLineNum = 173;BA.debugLine="Sub B_ReceiveAd";
 //BA.debugLineNum = 174;BA.debugLine="Log(\"B received\")";
anywheresoftware.b4a.keywords.Common.Log("B received");
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
 //BA.debugLineNum = 119;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 120;BA.debugLine="StartActivity(Telenor)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._telenor.getObject()));
 //BA.debugLineNum = 121;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 125;BA.debugLine="StartActivity(MPT)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._mpt.getObject()));
 //BA.debugLineNum = 126;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _b3_click() throws Exception{
 //BA.debugLineNum = 129;BA.debugLine="Sub b3_Click";
 //BA.debugLineNum = 130;BA.debugLine="StartActivity(Ooredoo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._ooredoo.getObject()));
 //BA.debugLineNum = 131;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _b4_click() throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Sub b4_Click";
 //BA.debugLineNum = 135;BA.debugLine="StartActivity(MecTel)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._mectel.getObject()));
 //BA.debugLineNum = 136;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim b1,b2,b3,b4 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim b1bg,b2bg,b3bg,b4bg As ColorDrawable";
mostCurrent._b1bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._b2bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._b3bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
mostCurrent._b4bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 27;BA.debugLine="Dim B As AdView";
mostCurrent._b = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim I As InterstitialAd";
mostCurrent._i = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim share As Button";
mostCurrent._share = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim sbg As BitmapDrawable";
mostCurrent._sbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 33;BA.debugLine="Dim iv As ImageView";
mostCurrent._iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _i_adclosed() throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Sub I_AdClosed";
 //BA.debugLineNum = 153;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
public static String  _i_adopened() throws Exception{
 //BA.debugLineNum = 165;BA.debugLine="Sub I_adopened";
 //BA.debugLineNum = 166;BA.debugLine="Log(\"Opened\")";
anywheresoftware.b4a.keywords.Common.Log("Opened");
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _i_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub I_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 161;BA.debugLine="Log(\"not Received - \" &\"Error Code: \"&ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("not Received - "+"Error Code: "+_errorcode);
 //BA.debugLineNum = 162;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 163;BA.debugLine="End Sub";
return "";
}
public static String  _i_receivead() throws Exception{
 //BA.debugLineNum = 156;BA.debugLine="Sub I_ReceiveAd";
 //BA.debugLineNum = 157;BA.debugLine="Log(\"Received\")";
anywheresoftware.b4a.keywords.Common.Log("Received");
 //BA.debugLineNum = 158;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(anywheresoftware.b4a.samples.httputils2.httpjob _job) throws Exception{
int _height = 0;
 //BA.debugLineNum = 43;BA.debugLine="Sub JobDone (job As HttpJob)";
 //BA.debugLineNum = 44;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 45;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 46;BA.debugLine="iv.Initialize(\"iv\")";
mostCurrent._iv.Initialize(mostCurrent.activityBA,"iv");
 //BA.debugLineNum = 47;BA.debugLine="iv.Bitmap = LoadBitmap(File.DirAssets,\"icon.png\"";
mostCurrent._iv.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon.png").getObject()));
 //BA.debugLineNum = 48;BA.debugLine="iv.Gravity = Gravity.FILL";
mostCurrent._iv.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 49;BA.debugLine="Activity.AddView(iv,50%x - 50dip,50dip + 1%y,100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 51;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 52;BA.debugLine="tlb.Initialize(\"\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 53;BA.debugLine="tlb.Color = Colors.Black";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 54;BA.debugLine="tlb.Gravity = Gravity.CENTER";
mostCurrent._tlb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 55;BA.debugLine="tlb.Text = \"My4nm4r Ph0n3 Bill H4ck3r\"";
mostCurrent._tlb.setText(BA.ObjectToCharSequence("My4nm4r Ph0n3 Bill H4ck3r"));
 //BA.debugLineNum = 56;BA.debugLine="tlb.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._tlb.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 57;BA.debugLine="tlb.TextColor = Colors.White";
mostCurrent._tlb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 58;BA.debugLine="tlb.TextSize = 20";
mostCurrent._tlb.setTextSize((float) (20));
 //BA.debugLineNum = 59;BA.debugLine="Activity.AddView(tlb,0%x,1dip,100%x,50dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 61;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 62;BA.debugLine="b1.Text = \"Telenor\"";
mostCurrent._b1.setText(BA.ObjectToCharSequence("Telenor"));
 //BA.debugLineNum = 63;BA.debugLine="b1bg.Initialize(Colors.Blue,15)";
mostCurrent._b1bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Blue,(int) (15));
 //BA.debugLineNum = 64;BA.debugLine="b1.TextColor = Colors.White";
mostCurrent._b1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 65;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b1bg.getObject()));
 //BA.debugLineNum = 66;BA.debugLine="Activity.AddView(b1,20%x,iv.Top+iv.Height+5%y,60";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) (mostCurrent._iv.getTop()+mostCurrent._iv.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 68;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 69;BA.debugLine="b2.Text = \"MPT\"";
mostCurrent._b2.setText(BA.ObjectToCharSequence("MPT"));
 //BA.debugLineNum = 70;BA.debugLine="b2bg.Initialize(Colors.rgb(255, 193, 7),15)";
mostCurrent._b2bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (193),(int) (7)),(int) (15));
 //BA.debugLineNum = 71;BA.debugLine="b2.TextColor = Colors.White";
mostCurrent._b2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 72;BA.debugLine="b2.Background = b2bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b2bg.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="Activity.AddView(b2,20%x,(b1.Top+b1.Height)+1%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 75;BA.debugLine="b3.Initialize(\"b3\")";
mostCurrent._b3.Initialize(mostCurrent.activityBA,"b3");
 //BA.debugLineNum = 76;BA.debugLine="b3.Text = \"Ooredoo\"";
mostCurrent._b3.setText(BA.ObjectToCharSequence("Ooredoo"));
 //BA.debugLineNum = 77;BA.debugLine="b3bg.Initialize(Colors.Red,15)";
mostCurrent._b3bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Red,(int) (15));
 //BA.debugLineNum = 78;BA.debugLine="b3.TextColor = Colors.White";
mostCurrent._b3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 79;BA.debugLine="b3.Background = b3bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b3bg.getObject()));
 //BA.debugLineNum = 80;BA.debugLine="Activity.AddView(b3,20%x,(b2.Top+b2.Height)+1%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b2.getTop()+mostCurrent._b2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 82;BA.debugLine="b4.Initialize(\"b4\")";
mostCurrent._b4.Initialize(mostCurrent.activityBA,"b4");
 //BA.debugLineNum = 83;BA.debugLine="b4.Text = \"MecTel\"";
mostCurrent._b4.setText(BA.ObjectToCharSequence("MecTel"));
 //BA.debugLineNum = 84;BA.debugLine="b4bg.Initialize(Colors.Black,15)";
mostCurrent._b4bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (15));
 //BA.debugLineNum = 85;BA.debugLine="b4.TextColor = Colors.White";
mostCurrent._b4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 86;BA.debugLine="b4.Background = b4bg";
mostCurrent._b4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b4bg.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="Activity.AddView(b4,20%x,(b3.Top+b3.Height)+1%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b3.getTop()+mostCurrent._b3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 89;BA.debugLine="B.Initialize2(\"B\",\"ca-app-pub-4173348573252986/2";
mostCurrent._b.Initialize2(mostCurrent.activityBA,"B","ca-app-pub-4173348573252986/2927788552",mostCurrent._b.SIZE_SMART_BANNER);
 //BA.debugLineNum = 90;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 91;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 93;BA.debugLine="If 100%x > 100%y Then height = 32dip Else heigh";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 96;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 98;BA.debugLine="Activity.AddView(B, 0dip, 100%y - height, 100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 99;BA.debugLine="B.LoadAd";
mostCurrent._b.LoadAd();
 //BA.debugLineNum = 100;BA.debugLine="Log(B)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._b));
 //BA.debugLineNum = 102;BA.debugLine="I.Initialize(\"Interstitial\",\"ca-app-pub-41733485";
mostCurrent._i.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/4404521752");
 //BA.debugLineNum = 103;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 105;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 106;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="share.Initialize(\"share\")";
mostCurrent._share.Initialize(mostCurrent.activityBA,"share");
 //BA.debugLineNum = 108;BA.debugLine="sbg.Initialize(LoadBitmap(File.DirAssets,\"share.";
mostCurrent._sbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()));
 //BA.debugLineNum = 109;BA.debugLine="share.Background = sbg";
mostCurrent._share.setBackground((android.graphics.drawable.Drawable)(mostCurrent._sbg.getObject()));
 //BA.debugLineNum = 110;BA.debugLine="Activity.AddView(share,100%x - 40dip,10dip,30dip";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._share.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 }else {
 //BA.debugLineNum = 112;BA.debugLine="Msgbox(\"အင္တာနက္ ခ်ိတ္ဆက္ထားရန္လိုအပ္ပါသည္ :)\"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("အင္တာနက္ ခ်ိတ္ဆက္ထားရန္လိုအပ္ပါသည္ :)"),BA.ObjectToCharSequence("FAILED!"),mostCurrent.activityBA);
 //BA.debugLineNum = 113;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 //BA.debugLineNum = 115;BA.debugLine="job1.Release";
_job1._release();
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        anywheresoftware.b4a.samples.httputils2.httputils2service._process_globals();
main._process_globals();
mpt._process_globals();
progress._process_globals();
telenor._process_globals();
ooredoo._process_globals();
mectel._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="Dim ad1 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 20;BA.debugLine="Dim job1 As HttpJob";
_job1 = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 181;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 182;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 183;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 184;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 185;BA.debugLine="copy.setText(\"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk";
_copy.setText(mostCurrent.activityBA,"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk ေလးပါ။ Telenor, MPT, MecTel, Ooredoo အားလုံးရပါတယ္။ တခါဝင္ယူရင္ ၁၀၀၀၀ က်ပ္ရပါတယ္။ ေလာေလာဆယ္ေတာ့ယူလို႔ရေနပါေသးတယ္ ;) စမ္းၾကည့္ၾကေပါ့ :V ဒီကေန အခမဲ့ ေဒါင္းယူနိုင္ပါတယ္ >> www.myanmarandroidapp.com/search?q=Myanmar+Phone+Bill+Hacker #MmPhoneBillHacker #MyanmarAndroidApp #KhunHtetzNaing");
 //BA.debugLineNum = 186;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 187;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 188;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 189;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 190;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 191;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
}
