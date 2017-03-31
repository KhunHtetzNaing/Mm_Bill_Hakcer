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
	Dim ad1 As Timer
End Sub

Sub Globals
	Dim lb As Label
	Dim edt,edt1 As EditText
	Dim b1 As Button
	Dim b1bg As ColorDrawable

	Dim pn As Panel
	Dim go As GoogleProgressBar
	Dim l1 As Label
	Dim a1 As AnimateText
	
	Dim N As NativeExpressAd
	Dim I As InterstitialAd
	Dim B As AdView
	Dim tlb As Label
	Dim mm As Typeface
End Sub

Sub Activity_Create(FirstTime As Boolean)
	mm = mm.LoadFromAssets("mm.ttf")
	
	tlb.Initialize("")
	tlb.Color = Colors.Black
	tlb.Gravity = Gravity.CENTER
	tlb.Text = "MPT"
	tlb.TextColor = Colors.White
	tlb.TextSize = 20
	Activity.AddView(tlb,0%x,1dip,100%x,50dip)
	
	Activity.Color = Colors.White
	lb.Initialize("lb")
	lb.Typeface = mm
	lb.Text = "ေအာက္ပါကြက္လပ္ထဲတြင္ မိမိလိုခ်င္ေသာပမာဏအားရိုက္ထည့္ပါ။" &CRLF& "Start Hack ကိုႏွိပ္ၿပီး ၁၀ စကၠန႔္ခန႔္ေစာင့္ေပးပါ။"& CRLF& "[ မွတ္ခ်က္။  ။ က်ပ္ ၁၀၀၀၀ ထက္မပိုေစရ! ]"
	Activity.AddView(lb,0%x,50dip+1%y,100%x,20%y)
	lb.Gravity = Gravity.CENTER
	lb.TextColor = Colors.Black
	
	edt.Initialize("edt")
	edt.Hint = "Enter phone"
	edt.InputType = edt.INPUT_TYPE_PHONE
	edt.Color = Colors.Black
	edt.Gravity = Gravity.CENTER
	edt.HintColor = Colors.White
	edt.TextColor = Colors.Red
	Activity.AddView(edt,20%x,(lb.Top+lb.Height)+1%y,60%x,10%y)

	edt1.Initialize("edt1")
	edt1.Hint = "Enter amount"
	edt1.InputType = edt1.INPUT_TYPE_PHONE
	edt1.HintColor = Colors.White
	edt1.TextColor = Colors.Green
	edt1.Gravity = Gravity.CENTER
	edt1.Color = Colors.Black
	Activity.AddView(edt1,20%x,(edt.Top+edt.Height)+1%y,60%x,10%y)


	b1bg.Initialize(Colors.Black,15)
	b1.Initialize("b1")
	b1.Background = b1bg
	b1.Text = "Start Hack"
	Activity.AddView(b1,20%x,edt1.Top+edt1.Height+4%y,60%x,50dip)

	pn.Initialize("pn")
	pn.Color = Colors.Black
	Activity.AddView(pn,0%x,0%y,100%x,100%y)
	go.Initialize("",3,Array As Int(Colors.Blue,Colors.White,Colors.Green, Colors.Magenta))
	pn.AddView(go,50%x - 25dip,20%y - 25dip,50dip,50dip)
	pn.Visible = False
	
	l1.Initialize("")
	l1.TextColor = Colors.Green
	l1.Gravity = Gravity.CENTER_HORIZONTAL
	pn.AddView(l1,0%x,go.Top+go.Height+1%y,100%x,25%y)
	
	B.Initialize2("B","ca-app-pub-4173348573252986/2927788552",B.SIZE_SMART_Banner)
	Dim height As Int
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
	
	I.Initialize("Interstitial","ca-app-pub-4173348573252986/4404521752")
	I.LoadAd
		
	ad1.Initialize("ad1",100)
	ad1.Enabled = False
	
	
	'Native Ads
	N.Initialize("N","ca-app-pub-4173348573252986/9828933353",100%x,132dip)
	N.LoadAd
	pn.AddView(N,0%x,l1.Top+l1.Height+3%y,100%x,132dip)
End Sub

Sub b1_Click
	If edt.Text = "" Then
		Msgbox("Please Fill Your Phone No. & Amount First!!","Attention!")
		ad1.Enabled = True
	Else
		b1.Visible = False
		edt.Visible = False
		edt1.Visible = False
		pn.Visible = True
		a1.initialize("a1",Me,500)
		a1.Run("-Please Wait-"& CRLF & "--Request--"& CRLF & "---Sending---"&CRLF&"----Getting----"&CRLF&"Completed! Enjoy :)",l1)
		a1.Endable = True
	End If
End Sub


Sub a1_End
	StartActivity(Progress)
	pn.Visible = False
	b1.Visible = True
	edt.Visible = True
	edt1.Visible = True
End Sub

Sub ad1_Tick
	If I.Ready Then I.Show Else I.LoadAd
	ad1.Enabled = False
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub I_AdClosed
	I.LoadAd
End Sub

Sub I_ReceiveAd
	Log("Received")
End Sub

Sub I_FailedToReceiveAd (ErrorCode As String)
	Log("not Received - " &"Error Code: "&ErrorCode)
	I.LoadAd
End Sub

Sub I_adopened
	Log("Opened")
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
