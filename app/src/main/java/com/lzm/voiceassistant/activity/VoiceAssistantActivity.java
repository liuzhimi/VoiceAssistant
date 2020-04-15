package com.lzm.voiceassistant.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.lzm.voiceassistant.R;
import com.lzm.voiceassistant.action.ShowWebAction;
import com.lzm.voiceassistant.adapter.OrderAdapter;
import com.lzm.voiceassistant.bean.AssistantResponse;
import com.lzm.voiceassistant.bean.Name;
import com.lzm.voiceassistant.bean.Result;
import com.lzm.voiceassistant.bean.Service;
import com.lzm.voiceassistant.util.DataConfig;
import com.lzm.voiceassistant.util.DisplayUtil;
import com.lzm.voiceassistant.util.WakeUtil;
import com.lzm.voiceassistant.view.SiriView;

public class VoiceAssistantActivity extends Activity implements View.OnClickListener, OrderAdapter.OrderCallback {

    private static String TAG = "Morris";
    private AssistantResponse mAssistantResponse;
    private SpeechRecognizer speechRecognizer;
    private Toast mToast;

    TextView mAskText;
    TextView responseText;
    RecyclerView recyclerView;
    WebView webView;
    RecyclerView rvRecommend;
    TextView tvSample;
    SiriView siriView;

    OrderAdapter adapter;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    View container;

    LinearLayout ll;

    int ret = 0;

    boolean isVirtualData = true;

    StringBuilder sb = new StringBuilder();

    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener mSpeechUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };


    /**
     * 语义理解回调。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
            sb = new StringBuilder();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            Log.i("morris", "onError: " + error.getPlainDescription(true));
            showTip(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (null != results) {
                Log.d(TAG, results.getResultString());

                // 显示
                String text = results.getResultString();
                Result result = JSONObject.parseObject(text, Result.class);
                Log.i(TAG, "onResult: " + result.getBg());
                for (Result.WsBean ws : result.getWs()) {
                    for (Result.WsBean.CwBean cw : ws.getCw()) {
                        sb.append(cw.getW());
                    }
                }

                try {
                    if (isVirtualData) {
                        mAssistantResponse = DataConfig.getAssistantResponse(sb.toString());

                    } else {
                        mAssistantResponse = (AssistantResponse) JSONObject.parse(text);
                    }

                    if (!TextUtils.isEmpty(text)) {
                        mAskText.setVisibility(View.VISIBLE);
                        mAskText.setText(mAssistantResponse.getText());
                        judgeService();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    speakAnswer("抱歉，我没有听懂");
                }


            } else {
                showTip("识别结果不正确。");
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            //showTip("播放完成");
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    private WakeUtil wakeUtil;

    private Boolean isFirst = true;

    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voice);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5e92b719");

        // 初始化对象
        speechRecognizer = SpeechRecognizer.createRecognizer(VoiceAssistantActivity.this, mSpeechUdrInitListener);

        mToast = Toast.makeText(VoiceAssistantActivity.this, "", Toast.LENGTH_SHORT);

        mTts = SpeechSynthesizer.createSynthesizer(VoiceAssistantActivity.this, mTtsInitListener);

        initLayout();

        wakeUtil = new WakeUtil(this) {
            @Override
            public void wakeUp() {
                if (container.getVisibility() != View.VISIBLE) {
                    container.setVisibility(View.VISIBLE);
                    speakAnswer("我能帮您做什么吗?");
                    wakeUtil.stopWake();
                }
                startListen();
            }
        };

        wakeUtil.wake();
    }

    private void resetView(boolean flag) {
        mAskText.setVisibility(View.GONE);
        if (!flag) {
            responseText.setVisibility(View.GONE);
        }
        recyclerView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        tvSample.setVisibility(View.GONE);
        rvRecommend.setVisibility(View.GONE);
        siriView.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化Layout。
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initLayout() {
        ll = findViewById(R.id.container);

        container = LayoutInflater.from(this).inflate(R.layout.view_assistant, ll);
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = DisplayUtil.getScreenH(this) * 2 / 3;
        container.findViewById(R.id.start_understander).setOnClickListener(VoiceAssistantActivity.this);

        mAskText = container.findViewById(R.id.tv_ask);
        responseText = container.findViewById(R.id.tv_answer);

        recyclerView = container.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this);
        adapter.setOrders(DataConfig.getOrders());

        webView = container.findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //支持js
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        rvRecommend = container.findViewById(R.id.list_recommend);
        rvRecommend.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvRecommend.setAdapter(adapter);

        tvSample = container.findViewById(R.id.tv_sample);

        siriView = container.findViewById(R.id.siri);

        container.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        // 开始语音理解
        if (view.getId() == R.id.start_understander) {
            startListen();
        }
    }

    private void startListen() {
        mTts.stopSpeaking();

        if (speechRecognizer.isListening()) {
            speechRecognizer.stopListening();
            //showTip("停止录音");
        }
        wakeUtil.stopWake();
        resetView(isFirst);
        isFirst = false;
        ret = speechRecognizer.startListening(mRecognizerListener);
        if (ret != 0) {
            showTip("语义理解失败,错误码:" + ret);
        } else {
            showTip("请开始说话…");
        }
    }

    /**
     * 帮助
     */
    public void help(View view) {
        startActivity(new Intent(VoiceAssistantActivity.this, HelpActivity.class));
    }

    public void speakAnswer(String text) {
        int code = mTts.startSpeaking(text, mTtsListener);
        responseText.setText(text);
        if (code != ErrorCode.SUCCESS) {
            showTip("语音合成失败,错误码: " + code);
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    //语义场景判断
    private void judgeService() {

        Service service = mAssistantResponse.getService();
        Log.i(TAG, "judgeService: " + service.getName());

        tvSample.setVisibility(View.VISIBLE);
        rvRecommend.setVisibility(View.VISIBLE);
        siriView.setVisibility(View.GONE);

        speakAnswer(mAssistantResponse.getAnswer().getText());
        responseText.setVisibility(View.VISIBLE);
        switch (service) {
            case OPEN_XXX:
                ShowWebAction.showWeb(mAssistantResponse.getWebPage(), webView);
                wakeUtil.wake();
                break;
            case SETTING_XXX:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirst = true;
                        container.setVisibility(View.GONE);
                        wakeUtil.wake();
                    }
                }, 2000);
                break;
            case DISPLAY_XXX:
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
                wakeUtil.wake();
                break;
            case COMMUNICATION:
                wakeUtil.wake();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        speechRecognizer.cancel();
        speechRecognizer.destroy();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();

    }

    @Override
    public void showOrderDetail(Name order) {

    }
}
