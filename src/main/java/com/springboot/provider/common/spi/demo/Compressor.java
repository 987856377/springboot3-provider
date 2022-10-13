package com.springboot.provider.common.spi.demo;


import com.springboot.provider.common.spi.complex.agent.api.spi.type.AgentTypedSPI;

public interface Compressor extends AgentTypedSPI {
    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
