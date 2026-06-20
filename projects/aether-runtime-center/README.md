# Aether Runtime Center v0.1.1

這是一個獨立 Android 應用，不是 Aether Mobile 的改名外殼。

## 身份驗證

- applicationId：`company.taowind.aethercenter`
- Launcher：`company.taowind.aethercenter.MainActivity`
- 來源：本目錄內 Android Java 源碼
- CI：`.github/workflows/aether-runtime-center-build.yml`

## v0.1.1 能力

- 檢查 `llama.cpp` 的 `/health`
- 經 Termux `RUN_COMMAND` 安裝、更新與啟動 Qwen3-VL 2B
- 啟動、停止與查看日誌
- 複製 OpenAI Compatible API 端點
- Termux 外部命令未開啟時，複製命令並打開 Termux 作為兜底

首次使用前，Termux 需要允許外部應用執行命令：

```properties
allow-external-apps=true
```

檔案位置：`~/.termux/termux.properties`。
