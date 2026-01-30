#!/usr/bin/env python3
"""
測試項目 - 簡單的 Python 應用
"""

def hello():
    """打招呼函數"""
    return "Hello, RAG System!"

def add(a, b):
    """加法函數"""
    return a + b

if __name__ == "__main__":
    print(hello())
    print(f"1 + 2 = {add(1, 2)}")
