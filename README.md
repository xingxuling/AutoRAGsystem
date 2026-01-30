# RAG 自動化系統

一個基於 RAG（檢索增強生成）分析的項目自動化優化系統，能夠自動分析項目、判斷迭代方向、優化並打包。

## 🚀 功能特點

### 1. **智能分析 (RAG)**
- 項目結構分析
- 代碼質量評估
- 功能完整性檢查
- 自動化配置審查

### 2. **智能決策**
- 優先級評估和排序
- 迭代重點確定
- 實施計劃生成
- 風險評估

### 3. **自動化優化**
- 項目結構優化
- 配置文件更新
- 測試基礎設施添加
- 文檔改進

### 4. **一鍵打包**
- 自動生成優化版本
- 多格式輸出 (ZIP, TAR.GZ)
- 桌面自動放置
- 詳細報告生成

## 📁 系統結構

```
auto-rag-system/
├── modules/              # 核心模塊
│   ├── rag_analyzer.py   # RAG 分析模塊
│   ├── decision_engine.py # 判斷引擎
│   └── auto_packager.py  # 自動化打包
├── config/               # 配置文件
├── output/               # 輸出結果
├── logs/                 # 執行日誌
├── main.py              # 主程序
├── run_system.sh        # Linux/macOS 啟動腳本
├── run_system.bat       # Windows 啟動腳本
└── README.md           # 本文檔
```

## 🛠️ 快速開始

### 環境要求
- Python 3.8+
- 無需額外依賴（使用標準庫）

### 使用方法

#### 方法 1: 使用啟動腳本 (推薦)

**Linux/macOS:**
```bash
# 設置執行權限
chmod +x run_system.sh

# 運行系統
./run_system.sh /path/to/your/project
```

**Windows:**
```bat
# 雙擊運行
run_system.bat C:\path\to\your\project

# 或命令行
run_system.bat "C:\path\to\your\project"
```

#### 方法 2: 直接運行 Python

```bash
# 進入系統目錄
cd /path/to/auto-rag-system

# 運行主程序
python main.py /path/to/your/project

# 或使用 python3
python3 main.py /path/to/your/project
```

## 📊 工作流程

### 階段 1: RAG 分析
```
1. 項目結構分析
2. 代碼質量評估
3. 功能完整性檢查
4. 自動化配置審查
5. 生成分析報告
```

### 階段 2: 智能決策
```
1. 優先級評估
2. 迭代重點確定
3. 實施計劃生成
4. 風險評估
5. 最終決策
```

### 階段 3: 自動化優化
```
1. 創建優化副本
2. 應用基礎優化
3. 應用優先級優化
4. 更新配置和文檔
```

### 階段 4: 打包輸出
```
1. 創建壓縮包
2. 生成詳細報告
3. 放置到桌面
4. 創建執行摘要
```

## 📋 輸出文件

### 桌面文件
- `項目名_optimized_時間戳.zip` - 優化後的項目包
- `auto_packaging_report.json` - 打包詳細報告
- `RAG_系統結果_時間戳.txt` - 執行摘要

### 系統目錄
- `output/時間戳/` - 完整分析結果
  - `analysis_report.json` - 詳細分析報告
  - `decisions.json` - 決策結果
  - `final_report.json` - 最終報告
- `logs/execution_時間戳.log` - 執行日誌

## ⚙️ 配置選項

編輯 `config/system_config.json` 自定義系統行為：

```json
{
  "analysis": {
    "depth": "comprehensive",  // 分析深度
    "include_code_quality": true
  },
  "decision": {
    "strategy": "balanced",    // 決策策略
    "consider_risks": true
  },
  "packaging": {
    "output_formats": ["zip"], // 輸出格式
    "output_location": "desktop"
  }
}
```

## 🎯 使用場景

### 1. 項目質量評估
```bash
# 評估 React Native 項目
./run_system.sh ~/projects/my-react-native-app
```

### 2. 自動化優化
```bash
# 自動優化並打包
./run_system.sh ~/projects/legacy-project
```

### 3. 迭代規劃
```bash
# 獲取迭代建議和計劃
./run_system.sh ~/projects/current-project
```

### 4. 代碼審查輔助
```bash
# 快速分析代碼質量
./run_system.sh ~/projects/code-review-target
```

## 🔧 自定義擴展

### 添加新的分析規則
編輯 `modules/rag_analyzer.py` 中的 `ProjectAnalyzer` 類。

### 修改決策邏輯
編輯 `modules/decision_engine.py` 中的 `DecisionEngine` 類。

### 添加優化措施
編輯 `modules/auto_packager.py` 中的 `AutoPackager` 類。

## 📊 評估指標

### 項目分數 (0-100)
- **90+**: 優秀 - 生產就緒
- **70-89**: 良好 - 需要少量優化
- **50-69**: 一般 - 需要中等優化
- **30-49**: 較差 - 需要重大改進
- **<30**: 很差 - 需要重構

### 成熟度等級
- **beginner**: 初學者級別
- **basic**: 基礎級別
- **intermediate**: 中級級別
- **advanced**: 高級級別

## 🆘 故障排除

### 常見問題

#### 1. Python 未找到
```bash
# 檢查 Python 安裝
python --version
python3 --version

# 安裝 Python
# Ubuntu/Debian: sudo apt install python3
# macOS: brew install python
# Windows: 從 python.org 下載
```

#### 2. 權限問題
```bash
# Linux/macOS: 設置執行權限
chmod +x run_system.sh
chmod +x modules/*.py
```

#### 3. 項目路徑錯誤
```bash
# 使用絕對路徑
./run_system.sh /absolute/path/to/project

# 或相對路徑
./run_system.sh ../relative/path/to/project
```

#### 4. 輸出文件未生成
- 檢查日誌文件 `logs/execution_*.log`
- 確保有寫入桌面目錄的權限
- 檢查項目是否包含有效內容

### 獲取幫助
1. 查看日誌文件了解詳細錯誤
2. 檢查輸出目錄中的報告
3. 確保項目路徑正確且可讀

## 📈 性能優化

### 大型項目處理
```bash
# 調整分析深度（修改 config）
"analysis": {
  "depth": "standard",  # 改為標準深度
  "include_code_quality": false  # 跳過代碼質量分析
}
```

### 批量處理
```bash
# 創建批量處理腳本
for project in /path/to/projects/*; do
  ./run_system.sh "$project"
done
```

## 🔮 未來擴展

### 計劃功能
- [ ] 支持更多項目類型（Web, 後端, 移動端）
- [ ] 集成 AI 模型進行深度分析
- [ ] 添加可視化報告界面
- [ ] 支持自定義優化模板
- [ ] 添加版本對比功能

### 貢獻指南
1. Fork 項目
2. 創建功能分支
3. 提交更改
4. 創建 Pull Request

## 📄 許可證

MIT License

## 📞 支持

如有問題或建議：
1. 查看日誌文件和輸出報告
2. 檢查項目是否符合預期格式
3. 確保有足夠的系統權限

---

**提示**: 首次運行前，建議備份重要項目。系統會創建項目副本進行優化，不會修改原始項目。