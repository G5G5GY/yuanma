package com.yuanma.module.system.encrption;

public interface IEncrption {
    String encode(String str);
    String decode(String str);
    String strategy();
}