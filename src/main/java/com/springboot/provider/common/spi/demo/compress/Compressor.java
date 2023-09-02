package com.springboot.provider.common.spi.demo.compress;

import com.springboot.provider.common.spi.simple.agent.type.AgentTypedSPI;

public interface Compressor extends AgentTypedSPI {
    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
