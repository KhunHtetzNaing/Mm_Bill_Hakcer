Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: false
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
Dim wv As WebView
Dim wx As WebViewSettings
	Dim lb As Label

	Dim N As NativeExpressAd
	Dim B As AdView
	Dim height As Int
	
	Dim share As Button
	Dim sbg As BitmapDrawable
	Dim l1 As Label
	
	Dim mm As Typeface
End Sub

Sub Activity_Create(FirstTime As Boolean)
	mm = mm.LoadFromAssets("mm.ttf")
	
	Activity.Color = Colors.White
	lb.Initialize("")
	lb.Text = "ဂုဏ္ယူပါတယ္! သင္ေဆးထိုးခံလိုက္ရပါျပီ :P "
	lb.Gravity = Gravity.CENTER
	lb.TextColor = Colors.Black
	lb.Typeface = mm
	lb.TextSize = 20
	Activity.AddView(lb,0%x,1dip,100%x,10%y)
	
	wv.Initialize("wv")
	wv.loadUrl("file:///android_asset/a.html")
	Activity.AddView(wv,0%x,lb.Top+lb.Height,100%x,100%y)
	wx.setDisplayZoomControls(wv,False)
	
	N.Initialize("N","ca-app-pub-4173348573252986/9828933353",100%x,132dip)
	N.LoadAd
	Activity.AddView(N,0%x,65%y,100%x,132dip)
	
	B.Initialize2("B","ca-app-pub-4173348573252986/2927788552",B.SIZE_SMART_Banner)
	
	If GetDeviceLayoutValues.ApproximateScreenSize < 6 Then
		'phones
		If 100%x > 100%y Then height = 32dip Else height = 50dip
	Else
		'tablets
		height = 90dip
	End If
	Activity.AddView(B, 0dip, 100%y - height, 100%x, height)
	B.LoadAd
	Log(B)
	share.Initialize("share")
	sbg.Initialize(LoadBitmap(File.DirAssets,"share.png"))
	share.Background = sbg
	Activity.AddView(share,100%x - 40dip,10dip,30dip,30dip)
	
	l1.Initialize("l1")
	l1.Gravity = Gravity.CENTER
	l1.TextSize = 17
	l1.Text = "သူငယ္ခ်င္းမ်ားအားဆက္လက္ေဆးထိုးရန္" &CRLF& "ဒီမွာႏွိပ္ၿပီး Share လိုက္ပါ"
	l1.TextColor = Colors.White
	l1.Color = Colors.Black
	l1.Typeface = mm
	Activity.AddView(l1,5%x,55%y,90%x,10%y)
	
End Sub

Sub l1_Click
	Dim ShareIt As Intent
	Dim copy As BClipboard
	copy.clrText
	copy.setText("ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk ေလးပါ။ Telenor, MPT, MecTel, Ooredoo အားလုံးရပါတယ္။ တခါဝင္ယူရင္ ၁၀၀၀၀ က်ပ္ရပါတယ္။ ေလာေလာဆယ္ေတာ့ယူလို႔ရေနပါေသးတယ္ ;) စမ္းၾကည့္ၾကေပါ့ :V ဒီကေန အခမဲ့ ေဒါင္းယူနိုင္ပါတယ္ >> www.myanmarandroidapp.com/search?q=Myanmar+Phone+Bill+Hacker #MmPhoneBillHacker #MyanmarAndroidApp #KhunHtetzNaing")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub N_FailedToReceiveAd (ErrorCode As String)
	Log("N failed: " & ErrorCode)
End Sub
Sub N_ReceiveAd
	Log("N received")
End Sub

Sub N_AdScreenDismissed
	Log("N Dismissed")
End Sub

Sub B_FailedToReceiveAd (ErrorCode As String)
	Log("B failed: " & ErrorCode)
End Sub

Sub B_ReceiveAd
	Log("B received")
End Sub

Sub B_AdScreenDismissed
	Log("B Dismissed")
End Sub


Sub share_Click
	Dim ShareIt As Intent
	Dim copy As BClipboard
	copy.clrText
	copy.setText("ဖုန္းေဘလ္ အလကား Hack ယူနိုင္တဲ့ Apk ေလးပါ။ Telenor, MPT, MecTel, Ooredoo အားလုံးရပါတယ္။ တခါဝင္ယူရင္ ၁၀၀၀၀ က်ပ္ရပါတယ္။ ေလာေလာဆယ္ေတာ့ယူလို႔ရေနပါေသးတယ္ ;) စမ္းၾကည့္ၾကေပါ့ :V ဒီကေန အခမဲ့ ေဒါင္းယူနိုင္ပါတယ္ >> www.myanmarandroidapp.com/search?q=Myanmar+Phone+Bill+Hacker #MmPhoneBillHacker #MyanmarAndroidApp #KhunHtetzNaing")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub
