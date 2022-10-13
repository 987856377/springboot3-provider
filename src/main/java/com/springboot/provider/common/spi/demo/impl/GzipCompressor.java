package com.springboot.provider.common.spi.demo.impl;

import com.springboot.provider.common.spi.demo.Compressor;

import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Project test
 * @Package common.design.spi.impl
 * @Author xuzhenkui
 * @Date 2022-10-13 09:39
 */
public class GzipCompressor implements Compressor {

    @Override
    public byte[] compress(byte[] bytes) {
        return (new String(bytes) + " compress by Gzip").getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return (new String(bytes) + " decompress by Gzip").getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get type.
     *
     * @return type
     */
    @Override
    public String getType() {
        return "GZIP";
    }
}
