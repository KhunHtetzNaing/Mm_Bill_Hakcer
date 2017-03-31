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

public class progress extends Activity implements B4AActivity{
	public static progress mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.progress");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (progress).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.progress");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.aprilfool.progress", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (progress) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (progress) Resume **");
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
		return progress.class;
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
        BA.LogInfo("** Activity (progress) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (progress) Resume **");
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
public anywheresoftware.b4a.objects.WebViewWrapper _wv = null;
public uk.co.martinpearman.b4a.webviewsettings.WebViewSettings _wx = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.NativeExpressAdWrapper _n = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _b = null;
public static int _height = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _share = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _sbg = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public com.htetznaing.aprilfool.main _main = null;
public com.htetznaing.aprilfool.mpt _mpt = null;
public com.htetznaing.aprilfool.telenor _telenor = null;
public com.htetznaing.aprilfool.ooredoo _ooredoo = null;
public com.htetznaing.aprilfool.mectel _mectel = null;
public com.htetznaing.aprilfool.starter _starter = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="mm = mm.LoadFromAssets(\"mm.ttf\")";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("mm.ttf")));
 //BA.debugLineNum = 31;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 32;BA.debugLine="lb.Initialize(\"\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 33;BA.debugLine="lb.Text = \"ဂုဏ္ယူပါတယ္! သင္ေဆးထိုးခံလိုက္ရပါျပီ :";
mostCurrent._lb.setText(BA.ObjectToCharSequence("ဂုဏ္ယူပါတယ္! သင္ေဆးထိုးခံလိုက္ရပါျပီ :P "));
 //BA.debugLineNum = 34;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 35;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 36;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 37;BA.debugLine="lb.TextSize = 20";
mostCurrent._lb.setTextSize((float) (20));
 //BA.debugLineNum = 38;BA.debugLine="Activity.AddView(lb,0%x,1dip,100%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 40;BA.debugLine="wv.Initialize(\"wv\")";
mostCurrent._wv.Initialize(mostCurrent.activityBA,"wv");
 //BA.debugLineNum = 41;BA.debugLine="wv.loadUrl(\"file:///android_asset/a.html\")";
mostCurrent._wv.LoadUrl("file:///android_asset/a.html");
 //BA.debugLineNum = 42;BA.debugLine="Activity.AddView(wv,0%x,lb.Top+lb.Height,100%x,10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._wv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (mostCurrent._lb.getTop()+mostCurrent._lb.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 43;BA.debugLine="wx.setDisplayZoomControls(wv,False)";
mostCurrent._wx.setDisplayZoomControls((android.webkit.WebView)(mostCurrent._wv.getObject()),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 45;BA.debugLine="N.Initialize(\"N\",\"ca-app-pub-4173348573252986/982";
mostCurrent._n.Initialize(mostCurrent.activityBA,"N","ca-app-pub-4173348573252986/9828933353",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (132))));
 //BA.debugLineNum = 46;BA.debugLine="N.LoadAd";
mostCurrent._n.LoadAd();
 //BA.debugLineNum = 47;BA.debugLine="Activity.AddView(N,0%x,65%y,100%x,132dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._n.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (65),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (132)));
 //BA.debugLineNum = 49;BA.debugLine="B.Initialize2(\"B\",\"ca-app-pub-4173348573252986/29";
mostCurrent._b.Initialize2(mostCurrent.activityBA,"B","ca-app-pub-4173348573252986/2927788552",mostCurrent._b.SIZE_SMART_BANNER);
 //BA.debugLineNum = 51;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 53;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 56;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 58;BA.debugLine="Activity.AddView(B, 0dip, 100%y - height, 100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 59;BA.debugLine="B.LoadAd";
mostCurrent._b.LoadAd();
 //BA.debugLineNum = 60;BA.debugLine="Log(B)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._b));
 //BA.debugLineNum = 61;BA.debugLine="share.Initialize(\"share\")";
mostCurrent._share.Initialize(mostCurrent.activityBA,"share");
 //BA.debugLineNum = 62;BA.debugLine="sbg.Initialize(LoadBitmap(File.DirAssets,\"share.p";
mostCurrent._sbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()));
 //BA.debugLineNum = 63;BA.debugLine="share.Background = sbg";
mostCurrent._share.setBackground((android.graphics.drawable.Drawable)(mostCurrent._sbg.getObject()));
 //BA.debugLineNum = 64;BA.debugLine="Activity.AddView(share,100%x - 40dip,10dip,30dip,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._share.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 66;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 67;BA.debugLine="l1.Gravity = Gravity.CENTER";
mostCurrent._l1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 68;BA.debugLine="l1.TextSize = 17";
mostCurrent._l1.setTextSize((float) (17));
 //BA.debugLineNum = 69;BA.debugLine="l1.Text = \"သူငယ္ခ်င္းမ်ားအားဆက္လက္ေဆးထိုးရန္\" &CR";
mostCurrent._l1.setText(BA.ObjectToCharSequence("သူငယ္ခ်င္းမ်ားအားဆက္လက္ေဆးထိုးရန္"+anywheresoftware.b4a.keywords.Common.CRLF+"ဒီမွာႏွိပ္ၿပီး Share လိုက္ပါ"));
 //BA.debugLineNum = 70;BA.debugLine="l1.TextColor = Colors.White";
mostCurrent._l1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 71;BA.debugLine="l1.Color = Colors.Black";
mostCurrent._l1.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 72;BA.debugLine="l1.Typeface = mm";
mostCurrent._l1.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 73;BA.debugLine="Activity.AddView(l1,5%x,55%y,90%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._l1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (55),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _b_adscreendismissed() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub B_AdScreenDismissed";
 //BA.debugLineNum = 118;BA.debugLine="Log(\"B Dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("B Dismissed");
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _b_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 109;BA.debugLine="Sub B_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 110;BA.debugLine="Log(\"B failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("B failed: "+_errorcode);
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _b_receivead() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub B_ReceiveAd";
 //BA.debugLineNum = 114;BA.debugLine="Log(\"B received\")";
anywheresoftware.b4a.keywords.Common.Log("B received");
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim wv As WebView";
mostCurrent._wv = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim wx As WebViewSettings";
mostCurrent._wx = new uk.co.martinpearman.b4a.webviewsettings.WebViewSettings();
 //BA.debugLineNum = 15;BA.debugLine="Dim lb As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim N As NativeExpressAd";
mostCurrent._n = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.NativeExpressAdWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim B As AdView";
mostCurrent._b = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 21;BA.debugLine="Dim share As Button";
mostCurrent._share = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim sbg As BitmapDrawable";
mostCurrent._sbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 23;BA.debugLine="Dim l1 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim mm As Typeface";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _l1_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 77;BA.debugLine="Sub l1_Click";
 //BA.debugLineNum = 78;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 80;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 81;BA.debugLine="copy.setText(\"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk";
_copy.setText(mostCurrent.activityBA,"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk ေလးပါ။ Telenor, MPT, MecTel, Ooredoo အားလုံးရပါတယ္။ တခါဝင္ယူရင္ ၁၀၀၀၀ က်ပ္ရပါတယ္။ ေလာေလာဆယ္ေတာ့ယူလို႔ရေနပါေသးတယ္ ;) စမ္းၾကည့္ၾကေပါ့ :V ဒီကေန အခမဲ့ ေဒါင္းယူနိုင္ပါတယ္ >> www.myanmarandroidapp.com/search?q=Myanmar+Phone+Bill+Hacker #MmPhoneBillHacker #MyanmarAndroidApp #KhunHtetzNaing");
 //BA.debugLineNum = 82;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 83;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 84;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 85;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 86;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 87;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _n_adscreendismissed() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub N_AdScreenDismissed";
 //BA.debugLineNum = 106;BA.debugLine="Log(\"N Dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("N Dismissed");
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _n_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub N_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 99;BA.debugLine="Log(\"N failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("N failed: "+_errorcode);
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public static String  _n_receivead() throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Sub N_ReceiveAd";
 //BA.debugLineNum = 102;BA.debugLine="Log(\"N received\")";
anywheresoftware.b4a.keywords.Common.Log("N received");
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 122;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 123;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 125;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 126;BA.debugLine="copy.setText(\"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk";
_copy.setText(mostCurrent.activityBA,"ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk ေလးပါ။ Telenor, MPT, MecTel, Ooredoo အားလုံးရပါတယ္။ တခါဝင္ယူရင္ ၁၀၀၀၀ က်ပ္ရပါတယ္။ ေလာေလာဆယ္ေတာ့ယူလို႔ရေနပါေသးတယ္ ;) စမ္းၾကည့္ၾကေပါ့ :V ဒီကေန အခမဲ့ ေဒါင္းယူနိုင္ပါတယ္ >> www.myanmarandroidapp.com/search?q=Myanmar+Phone+Bill+Hacker #MmPhoneBillHacker #MyanmarAndroidApp #KhunHtetzNaing");
 //BA.debugLineNum = 127;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 128;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 129;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 130;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 131;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 132;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
}
