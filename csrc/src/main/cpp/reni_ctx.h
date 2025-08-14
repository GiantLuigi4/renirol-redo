#include "pch.h"

#ifndef RENI_CTX
#define RENI_CTX

class ReniCtx {
private:
    // WindowSystem
    // GraphicsSystem
    const char* graphicsApi;
    bool legacyOpt;

public:
    ReniCtx(int v);
};

ReniCtx* create(const char* windowing, const char* graphics);

#endif