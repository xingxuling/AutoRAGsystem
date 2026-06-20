package company.taowind.aethercenter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MainActivity extends Activity {
    private static final String TERMUX_PACKAGE = "com.termux";
    private static final String TERMUX_SERVICE = "com.termux.app.RunCommandService";
    private static final String ENDPOINT = "http://127.0.0.1:8080";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView statusView;
    private TextView detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildUi());
        checkStatus();
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }

    private View buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(244, 247, 250));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(28), dp(20), dp(28));
        scroll.addView(root, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT));

        TextView title = text("Aether Runtime Center", 28, true);
        title.setTextColor(Color.rgb(22, 33, 48));
        root.addView(title);

        TextView subtitle = text("獨立模型啟動中心 · v0.1.1", 15, false);
        subtitle.setTextColor(Color.rgb(79, 97, 120));
        subtitle.setPadding(0, dp(4), 0, dp(18));
        root.addView(subtitle);

        LinearLayout card = card();
        root.addView(card);

        TextView model = text("Qwen3-VL 2B Instruct · Q4_K_M", 18, true);
        model.setTextColor(Color.rgb(25, 43, 66));
        card.addView(model);

        TextView engine = text("引擎：llama.cpp  ·  端點：127.0.0.1:8080", 14, false);
        engine.setTextColor(Color.rgb(82, 101, 125));
        engine.setPadding(0, dp(6), 0, dp(14));
        card.addView(engine);

        statusView = text("正在檢查……", 18, true);
        statusView.setTextColor(Color.rgb(180, 113, 0));
        card.addView(statusView);

        detailView = text("等待健康檢查", 13, false);
        detailView.setTextColor(Color.rgb(92, 106, 124));
        detailView.setPadding(0, dp(4), 0, dp(14));
        card.addView(detailView);

        card.addView(button("檢查引擎狀態", v -> checkStatus(), false));
        card.addView(button("安裝／更新並啟動模型", v -> runTermux(INSTALL_AND_START, true), true));
        card.addView(button("啟動模型", v -> runTermux(START_ONLY, true), true));
        card.addView(button("停止模型", v -> runTermux(STOP_ONLY, true), false));
        card.addView(button("在 Termux 查看日誌", v -> runTermux(VIEW_LOG, false), false));
        card.addView(button("複製 OpenAI 相容端點", v -> copyText(ENDPOINT + "/v1"), false));

        TextView note = text(
                "首次安裝會編譯 llama.cpp 並下載約 1.6 GB 的模型組件。若 Termux 拒絕外部命令，應用會自動複製命令並打開 Termux。",
                13,
                false);
        note.setTextColor(Color.rgb(91, 104, 121));
        note.setPadding(0, dp(18), 0, 0);
        root.addView(note);

        return scroll;
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(16);
        card.setLayoutParams(lp);
        card.setElevation(dp(3));
        return card;
    }

    private Button button(String label, View.OnClickListener listener, boolean primary) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextSize(15);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(listener);
        if (primary) {
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.rgb(35, 91, 151));
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(52));
        lp.topMargin = dp(9);
        button.setLayoutParams(lp);
        return button;
    }

    private TextView text(String value, int sp, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        if (bold) view.setTypeface(view.getTypeface(), android.graphics.Typeface.BOLD);
        return view;
    }

    private void checkStatus() {
        statusView.setText("正在檢查……");
        statusView.setTextColor(Color.rgb(180, 113, 0));
        detailView.setText("檢查 " + ENDPOINT + "/health");

        executor.execute(() -> {
            HealthResult result = requestHealth();
            mainHandler.post(() -> renderHealth(result));
        });
    }

    private HealthResult requestHealth() {
        if (!isTermuxInstalled()) {
            return new HealthResult("Termux 未安裝", "請先安裝 Termux，才能啟動本機推理引擎。", 0);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(ENDPOINT + "/health").openConnection();
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    code >= 400 ? connection.getErrorStream() : connection.getInputStream(),
                    StandardCharsets.UTF_8));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) body.append(line);

            if (code == 200) {
                return new HealthResult("服務已就緒", body.toString(), 2);
            }
            if (code == 503) {
                return new HealthResult("模型載入中", body.toString(), 1);
            }
            return new HealthResult("服務回應異常", "HTTP " + code + " · " + body, -1);
        } catch (Exception error) {
            return new HealthResult("服務未啟動", error.getClass().getSimpleName() + "：" + safeMessage(error), -1);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private void renderHealth(HealthResult result) {
        statusView.setText(result.title);
        detailView.setText(result.detail == null || result.detail.isBlank() ? "無附加資訊" : result.detail);
        if (result.level == 2) {
            statusView.setTextColor(Color.rgb(24, 133, 79));
        } else if (result.level == 1) {
            statusView.setTextColor(Color.rgb(180, 113, 0));
        } else {
            statusView.setTextColor(Color.rgb(190, 54, 54));
        }
    }

    private void runTermux(String command, boolean background) {
        if (!isTermuxInstalled()) {
            copyText(command);
            Toast.makeText(this, "未找到 Termux，命令已複製", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent("com.termux.RUN_COMMAND");
        intent.setClassName(TERMUX_PACKAGE, TERMUX_SERVICE);
        intent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash");
        intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"-lc", command});
        intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home");
        intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", background);
        intent.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");

        try {
            startService(intent);
            Toast.makeText(this, background ? "已交給 Termux 後台執行" : "已打開 Termux", Toast.LENGTH_LONG).show();
            if (background) mainHandler.postDelayed(this::checkStatus, 5000);
        } catch (Exception error) {
            copyText(command);
            try {
                Intent launch = getPackageManager().getLaunchIntentForPackage(TERMUX_PACKAGE);
                if (launch != null) startActivity(launch);
            } catch (Exception ignored) {
                // The copied command remains available even if Termux cannot be opened.
            }
            Toast.makeText(this,
                    "Termux 尚未允許外部命令，已複製命令供手動貼上",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTermuxInstalled() {
        try {
            getPackageManager().getPackageInfo(TERMUX_PACKAGE, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    private void copyText(String value) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Aether Runtime Center", value));
        Toast.makeText(this, "已複製", Toast.LENGTH_SHORT).show();
    }

    private static String safeMessage(Exception error) {
        return error.getMessage() == null ? "無詳細訊息" : error.getMessage();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private record HealthResult(String title, String detail, int level) {}

    private static final String INSTALL_AND_START = "set -e; "
            + "pkg update -y; pkg install -y git cmake clang libcurl curl; "
            + "if [ ! -d \"$HOME/llama.cpp/.git\" ]; then "
            + "git clone --depth 1 https://github.com/ggml-org/llama.cpp \"$HOME/llama.cpp\"; "
            + "else git -C \"$HOME/llama.cpp\" pull --ff-only; fi; "
            + "cmake -S \"$HOME/llama.cpp\" -B \"$HOME/llama.cpp/build\" -DGGML_VULKAN=ON -DLLAMA_CURL=ON; "
            + "cmake --build \"$HOME/llama.cpp/build\" -j 4 --target llama-server; "
            + START_ONLY;

    private static final String START_ONLY = "pkill -f 'llama-server.*127.0.0.1.*8080' 2>/dev/null || true; "
            + "nohup \"$HOME/llama.cpp/build/bin/llama-server\" "
            + "-hf Qwen/Qwen3-VL-2B-Instruct-GGUF:Q4_K_M "
            + "--host 127.0.0.1 --port 8080 -ngl 99 "
            + "> \"$HOME/aether-runtime.log\" 2>&1 & "
            + "echo $! > \"$HOME/aether-runtime.pid\"";

    private static final String STOP_ONLY = "if [ -f \"$HOME/aether-runtime.pid\" ]; then "
            + "kill $(cat \"$HOME/aether-runtime.pid\") 2>/dev/null || true; fi; "
            + "pkill -f 'llama-server.*127.0.0.1.*8080' 2>/dev/null || true; "
            + "rm -f \"$HOME/aether-runtime.pid\"";

    private static final String VIEW_LOG = "touch \"$HOME/aether-runtime.log\"; "
            + "tail -n 200 -f \"$HOME/aether-runtime.log\"";
}
