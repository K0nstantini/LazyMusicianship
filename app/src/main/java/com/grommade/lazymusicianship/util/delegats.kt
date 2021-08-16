package com.grommade.lazymusicianship.util

inline fun <T> lazyUnsafe(noinline initializer: () -> T) =
    lazy(mode = LazyThreadSafetyMode.NONE) { initializer }