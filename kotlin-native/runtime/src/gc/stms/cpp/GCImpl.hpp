/*
 * Copyright 2010-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#pragma once

#include "GC.hpp"

#include "AllocatorImpl.hpp"
#include "SameThreadMarkAndSweep.hpp"

namespace kotlin {
namespace gc {

class GC::Impl : private Pinned {
public:
    Impl(alloc::Allocator& allocator, gcScheduler::GCScheduler& gcScheduler) noexcept : gc_(allocator, gcScheduler) {}

    SameThreadMarkAndSweep& gc() noexcept { return gc_; }

private:
    SameThreadMarkAndSweep gc_;
};

class GC::ThreadData::Impl {};

} // namespace gc
} // namespace kotlin
