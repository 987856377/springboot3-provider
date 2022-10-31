package com.springboot.provider.common.spi.demo.compress.impl;

import com.springboot.provider.common.spi.demo.compress.Compressor;

import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Project test
 * @Package common.design.spi.impl
 * @Author xuzhenkui
 * @Date 2022-10-13 09:40
 */
public class WinRarCompressor implements Compressor {
    @Override
    public byte[] compress(byte[] bytes) {
        return (new String(bytes) + " compress by WinRar").getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return (new String(bytes) + " decompress by WinRar").getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get type.
     *
     * @return type
     */
    @Override
    public String getType() {
        return "WINRAR";
    }
}
