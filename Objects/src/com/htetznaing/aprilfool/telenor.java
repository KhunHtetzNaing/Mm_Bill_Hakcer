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

public class telenor extends Activity implements B4AActivity{
	public static telenor mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.telenor");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (telenor).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.aprilfool", "com.htetznaing.aprilfool.telenor");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.aprilfool.telenor", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (telenor) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (telenor) Resume **");
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
		return telenor.class;
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
        BA.LogInfo("** Activity (telenor) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (telenor) Resume **");
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
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edt = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edt1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _b1bg = null;
public anywheresoftware.b4a.objects.PanelWrapper _pn = null;
public de.donmanfred.GoogleProgressBarWrapper _go = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public com.htoophyoe.anitext.animatetext _a1 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.NativeExpressAdWrapper _n = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _i = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _b = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public com.htetznaing.aprilfool.main _main = null;
public com.htetznaing.aprilfool.mpt _mpt = null;
public com.htetznaing.aprilfool.progress _progress = null;
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
public static String  _a1_end() throws Exception{
 //BA.debugLineNum = 128;BA.debugLine="Sub a1_End";
 //BA.debugLineNum = 129;BA.debugLine="StartActivity(Progress)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._progress.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="pn.Visible = False";
mostCurrent._pn.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 131;BA.debugLine="b1.Visible = True";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="edt.Visible = True";
mostCurrent._edt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 133;BA.debugLine="edt1.Visible = True";
mostCurrent._edt1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _height = 0;
 //BA.debugLineNum = 30;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 31;BA.debugLine="mm = mm.LoadFromAssets(\"mm.ttf\")";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("mm.ttf")));
 //BA.debugLineNum = 33;BA.debugLine="tlb.Initialize(\"\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 34;BA.debugLine="tlb.Color = Colors.Black";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 35;BA.debugLine="tlb.Gravity = Gravity.CENTER";
mostCurrent._tlb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 36;BA.debugLine="tlb.Text = \"Telenor\"";
mostCurrent._tlb.setText(BA.ObjectToCharSequence("Telenor"));
 //BA.debugLineNum = 37;BA.debugLine="tlb.TextColor = Colors.White";
mostCurrent._tlb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 38;BA.debugLine="tlb.TextSize = 20";
mostCurrent._tlb.setTextSize((float) (20));
 //BA.debugLineNum = 39;BA.debugLine="Activity.AddView(tlb,0%x,1dip,100%x,50dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 41;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 42;BA.debugLine="lb.Initialize(\"lb\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"lb");
 //BA.debugLineNum = 43;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 44;BA.debugLine="lb.Text = \"ေအာက္ပါကြက္လပ္ထဲတြင္ မိမိလိုခ်င္ေသာပမာ";
mostCurrent._lb.setText(BA.ObjectToCharSequence("ေအာက္ပါကြက္လပ္ထဲတြင္ မိမိလိုခ်င္ေသာပမာဏအားရိုက္ထည့္ပါ။"+anywheresoftware.b4a.keywords.Common.CRLF+"Start Hack ကိုႏွိပ္ၿပီး ၁၀ စကၠန႔္ခန႔္ေစာင့္ေပးပါ။"+anywheresoftware.b4a.keywords.Common.CRLF+"[ မွတ္ခ်က္။  ။ က်ပ္ ၁၀၀၀၀ ထက္မပိုေစရ! ]"));
 //BA.debugLineNum = 45;BA.debugLine="Activity.AddView(lb,0%x,50dip+1%y,100%x,20%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 46;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 47;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 49;BA.debugLine="edt.Initialize(\"edt\")";
mostCurrent._edt.Initialize(mostCurrent.activityBA,"edt");
 //BA.debugLineNum = 50;BA.debugLine="edt.Hint = \"Enter phone\"";
mostCurrent._edt.setHint("Enter phone");
 //BA.debugLineNum = 51;BA.debugLine="edt.InputType = edt.INPUT_TYPE_PHONE";
mostCurrent._edt.setInputType(mostCurrent._edt.INPUT_TYPE_PHONE);
 //BA.debugLineNum = 52;BA.debugLine="edt.Color = Colors.Black";
mostCurrent._edt.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 53;BA.debugLine="edt.Gravity = Gravity.CENTER";
mostCurrent._edt.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 54;BA.debugLine="edt.HintColor = Colors.White";
mostCurrent._edt.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 55;BA.debugLine="edt.TextColor = Colors.Red";
mostCurrent._edt.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 56;BA.debugLine="Activity.AddView(edt,20%x,(lb.Top+lb.Height)+1%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._edt.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._lb.getTop()+mostCurrent._lb.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 58;BA.debugLine="edt1.Initialize(\"edt1\")";
mostCurrent._edt1.Initialize(mostCurrent.activityBA,"edt1");
 //BA.debugLineNum = 59;BA.debugLine="edt1.Hint = \"Enter amount\"";
mostCurrent._edt1.setHint("Enter amount");
 //BA.debugLineNum = 60;BA.debugLine="edt1.InputType = edt1.INPUT_TYPE_PHONE";
mostCurrent._edt1.setInputType(mostCurrent._edt1.INPUT_TYPE_PHONE);
 //BA.debugLineNum = 61;BA.debugLine="edt1.HintColor = Colors.White";
mostCurrent._edt1.setHintColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 62;BA.debugLine="edt1.TextColor = Colors.Green";
mostCurrent._edt1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 63;BA.debugLine="edt1.Gravity = Gravity.CENTER";
mostCurrent._edt1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 64;BA.debugLine="edt1.Color = Colors.Black";
mostCurrent._edt1.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 65;BA.debugLine="Activity.AddView(edt1,20%x,(edt.Top+edt.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._edt1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._edt.getTop()+mostCurrent._edt.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 68;BA.debugLine="b1bg.Initialize(Colors.Black,15)";
mostCurrent._b1bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (15));
 //BA.debugLineNum = 69;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 70;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b1bg.getObject()));
 //BA.debugLineNum = 71;BA.debugLine="b1.Text = \"Start Hack\"";
mostCurrent._b1.setText(BA.ObjectToCharSequence("Start Hack"));
 //BA.debugLineNum = 72;BA.debugLine="Activity.AddView(b1,20%x,edt1.Top+edt1.Height+4%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) (mostCurrent._edt1.getTop()+mostCurrent._edt1.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 74;BA.debugLine="pn.Initialize(\"pn\")";
mostCurrent._pn.Initialize(mostCurrent.activityBA,"pn");
 //BA.debugLineNum = 75;BA.debugLine="pn.Color = Colors.Black";
mostCurrent._pn.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 76;BA.debugLine="Activity.AddView(pn,0%x,0%y,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 77;BA.debugLine="go.Initialize(\"\",3,Array As Int(Colors.Blue,Color";
mostCurrent._go.Initialize(processBA,"",(int) (3),new int[]{anywheresoftware.b4a.keywords.Common.Colors.Blue,anywheresoftware.b4a.keywords.Common.Colors.White,anywheresoftware.b4a.keywords.Common.Colors.Green,anywheresoftware.b4a.keywords.Common.Colors.Magenta});
 //BA.debugLineNum = 78;BA.debugLine="pn.AddView(go,50%x - 25dip,20%y - 25dip,50dip,50d";
mostCurrent._pn.AddView((android.view.View)(mostCurrent._go.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25))),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 79;BA.debugLine="pn.Visible = False";
mostCurrent._pn.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 81;BA.debugLine="l1.Initialize(\"\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 82;BA.debugLine="l1.TextColor = Colors.Green";
mostCurrent._l1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 83;BA.debugLine="l1.Gravity = Gravity.CENTER_HORIZONTAL";
mostCurrent._l1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 84;BA.debugLine="pn.AddView(l1,0%x,go.Top+go.Height+1%y,100%x,25%y";
mostCurrent._pn.AddView((android.view.View)(mostCurrent._l1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (mostCurrent._go.getTop()+mostCurrent._go.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 86;BA.debugLine="B.Initialize2(\"B\",\"ca-app-pub-4173348573252986/29";
mostCurrent._b.Initialize2(mostCurrent.activityBA,"B","ca-app-pub-4173348573252986/2927788552",mostCurrent._b.SIZE_SMART_BANNER);
 //BA.debugLineNum = 87;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 88;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 90;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 93;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 95;BA.debugLine="Activity.AddView(B, 0dip, 100%y - height, 100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 96;BA.debugLine="B.LoadAd";
mostCurrent._b.LoadAd();
 //BA.debugLineNum = 97;BA.debugLine="Log(B)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._b));
 //BA.debugLineNum = 99;BA.debugLine="I.Initialize(\"Interstitial\",\"ca-app-pub-417334857";
mostCurrent._i.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/4404521752");
 //BA.debugLineNum = 100;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 102;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 103;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="N.Initialize(\"N\",\"ca-app-pub-4173348573252986/982";
mostCurrent._n.Initialize(mostCurrent.activityBA,"N","ca-app-pub-4173348573252986/9828933353",(float) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)),(float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (132))));
 //BA.debugLineNum = 108;BA.debugLine="N.LoadAd";
mostCurrent._n.LoadAd();
 //BA.debugLineNum = 109;BA.debugLine="pn.AddView(N,0%x,l1.Top+l1.Height+3%y,100%x,132di";
mostCurrent._pn.AddView((android.view.View)(mostCurrent._n.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (mostCurrent._l1.getTop()+mostCurrent._l1.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (132)));
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 141;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 137;BA.debugLine="If I.Ready Then I.Show Else I.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 138;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _b_adscreendismissed() throws Exception{
 //BA.debugLineNum = 186;BA.debugLine="Sub B_AdScreenDismissed";
 //BA.debugLineNum = 187;BA.debugLine="Log(\"B Dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("B Dismissed");
 //BA.debugLineNum = 188;BA.debugLine="End Sub";
return "";
}
public static String  _b_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 178;BA.debugLine="Sub B_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 179;BA.debugLine="Log(\"B failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("B failed: "+_errorcode);
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _b_receivead() throws Exception{
 //BA.debugLineNum = 182;BA.debugLine="Sub B_ReceiveAd";
 //BA.debugLineNum = 183;BA.debugLine="Log(\"B received\")";
anywheresoftware.b4a.keywords.Common.Log("B received");
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
 //BA.debugLineNum = 112;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 113;BA.debugLine="If edt.Text = \"\" Then";
if ((mostCurrent._edt.getText()).equals("")) { 
 //BA.debugLineNum = 114;BA.debugLine="Msgbox(\"Please Fill Your Phone No. & Amount Firs";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please Fill Your Phone No. & Amount First!!"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 //BA.debugLineNum = 115;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 117;BA.debugLine="b1.Visible = False";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 118;BA.debugLine="edt.Visible = False";
mostCurrent._edt.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 119;BA.debugLine="edt1.Visible = False";
mostCurrent._edt1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 120;BA.debugLine="pn.Visible = True";
mostCurrent._pn.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 121;BA.debugLine="a1.initialize(\"a1\",Me,500)";
mostCurrent._a1._initialize(mostCurrent.activityBA,"a1",telenor.getObject(),(int) (500));
 //BA.debugLineNum = 122;BA.debugLine="a1.Run(\"-Please Wait-\"& CRLF & \"--Request--\"& CR";
mostCurrent._a1._run("-Please Wait-"+anywheresoftware.b4a.keywords.Common.CRLF+"--Request--"+anywheresoftware.b4a.keywords.Common.CRLF+"---Sending---"+anywheresoftware.b4a.keywords.Common.CRLF+"----Getting----"+anywheresoftware.b4a.keywords.Common.CRLF+"Completed! Enjoy :)",(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._l1.getObject())));
 //BA.debugLineNum = 123;BA.debugLine="a1.Endable = True";
mostCurrent._a1._endable = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim lb As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim edt,edt1 As EditText";
mostCurrent._edt = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._edt1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim b1 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim b1bg As ColorDrawable";
mostCurrent._b1bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 18;BA.debugLine="Dim pn As Panel";
mostCurrent._pn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim go As GoogleProgressBar";
mostCurrent._go = new de.donmanfred.GoogleProgressBarWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim l1 As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim a1 As AnimateText";
mostCurrent._a1 = new com.htoophyoe.anitext.animatetext();
 //BA.debugLineNum = 23;BA.debugLine="Dim N As NativeExpressAd";
mostCurrent._n = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.NativeExpressAdWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim I As InterstitialAd";
mostCurrent._i = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim B As AdView";
mostCurrent._b = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim mm As Typeface";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _i_adclosed() throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub I_AdClosed";
 //BA.debugLineNum = 151;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _i_adopened() throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub I_adopened";
 //BA.debugLineNum = 164;BA.debugLine="Log(\"Opened\")";
anywheresoftware.b4a.keywords.Common.Log("Opened");
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _i_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Sub I_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 159;BA.debugLine="Log(\"not Received - \" &\"Error Code: \"&ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("not Received - "+"Error Code: "+_errorcode);
 //BA.debugLineNum = 160;BA.debugLine="I.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _i_receivead() throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub I_ReceiveAd";
 //BA.debugLineNum = 155;BA.debugLine="Log(\"Received\")";
anywheresoftware.b4a.keywords.Common.Log("Received");
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
return "";
}
public static String  _n_adscreendismissed() throws Exception{
 //BA.debugLineNum = 174;BA.debugLine="Sub N_AdScreenDismissed";
 //BA.debugLineNum = 175;BA.debugLine="Log(\"N Dismissed\")";
anywheresoftware.b4a.keywords.Common.Log("N Dismissed");
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _n_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub N_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 168;BA.debugLine="Log(\"N failed: \" & ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("N failed: "+_errorcode);
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _n_receivead() throws Exception{
 //BA.debugLineNum = 170;BA.debugLine="Sub N_ReceiveAd";
 //BA.debugLineNum = 171;BA.debugLine="Log(\"N received\")";
anywheresoftware.b4a.keywords.Common.Log("N received");
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ad1 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
