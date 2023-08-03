/*
 * Copyright 2010-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#include "GCImpl.hpp"

#include "Common.h"
#include "GC.hpp"
#include "GCStatistics.hpp"
#include "Logging.hpp"
#include "ObjectOps.hpp"
#include "ThreadData.hpp"
#include "std_support/Memory.hpp"

using namespace kotlin;

gc::GC::ThreadData::ThreadData(GC& gc, mm::ThreadData& threadData) noexcept {}

gc::GC::ThreadData::~ThreadData() = default;

void gc::GC::ThreadData::OnSuspendForGC() noexcept { }

void gc::GC::ThreadData::safePoint() noexcept {}

gc::GC::GC(alloc::Allocator& allocator, gcScheduler::GCScheduler&) noexcept {
    RuntimeLogInfo({kTagGC}, "No-op GC initialized");
}

gc::GC::~GC() = default;

void gc::GC::ClearForTests() noexcept {
    GCHandle::ClearForTests();
}

// static
ALWAYS_INLINE void gc::GC::processObjectInMark(void* state, ObjHeader* object) noexcept {}

// static
ALWAYS_INLINE void gc::GC::processArrayInMark(void* state, ArrayHeader* array) noexcept {}

// static
ALWAYS_INLINE void gc::GC::processFieldInMark(void* state, ObjHeader* field) noexcept {}

int64_t gc::GC::Schedule() noexcept {
    return 0;
}

void gc::GC::WaitFinished(int64_t epoch) noexcept {}

void gc::GC::WaitFinalizers(int64_t epoch) noexcept {}

void gc::GC::onFinalized(int64_t epoch) noexcept {}

bool gc::isMarked(ObjHeader* object) noexcept {
    RuntimeAssert(false, "Should not reach here");
    return true;
}

ALWAYS_INLINE OBJ_GETTER(gc::tryRef, std::atomic<ObjHeader*>& object) noexcept {
    RETURN_OBJ(object.load(std::memory_order_relaxed));
}

ALWAYS_INLINE bool gc::tryResetMark(GC::ObjectData& objectData) noexcept {
    RuntimeAssert(false, "Should not reach here");
    return true;
}

// static
ALWAYS_INLINE uint64_t type_layout::descriptor<gc::GC::ObjectData>::type::size() noexcept {
    return 0;
}

// static
ALWAYS_INLINE size_t type_layout::descriptor<gc::GC::ObjectData>::type::alignment() noexcept {
    return 1;
}

// static
ALWAYS_INLINE gc::GC::ObjectData* type_layout::descriptor<gc::GC::ObjectData>::type::construct(uint8_t* ptr) noexcept {
    return reinterpret_cast<gc::GC::ObjectData*>(ptr);
}
