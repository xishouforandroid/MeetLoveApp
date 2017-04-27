package com.lbins.meetlove.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, InternetURL.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Log.d(TAG, "resp.errStr +\";code=\" + String.valueOf(resp.errCode)) = " + resp.errStr + ";code=" + String.valueOf(resp.errCode));
			if(resp.errCode == 0){
				if(MeetLoveApplication.is_dxk_order.equals("0")){
					save("rzstate2", "1");
					Intent intent1 = new Intent("rzstate2_success");
					sendBroadcast(intent1);
				}else if(MeetLoveApplication.is_dxk_order.equals("1")){
					save("rzstate3", "1");
					Intent intent1 = new Intent("rzstate3_success");
					sendBroadcast(intent1);
				}
				showMsg(WXPayEntryActivity.this, "支付成功");
				finish();

//				ActivityTack.getInstanse().popUntilActivity(OrderMakeActivity.class);
			}else {
				//支付失败
				showMsg(WXPayEntryActivity.this, "支付失败");
				finish();
//				ActivityTack.getInstanse().popUntilActivity(OrderMakeActivity.class);
			}
		}
	}

}