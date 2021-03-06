#pragma once

#include "stored_value_sensor.h"
#include "global.h"

struct MockSensor final : public StoredValueSensor
{
	MockSensor(SensorType type) : StoredValueSensor(type, MS2NT(50))
	{
	}

	void set(float value) {
		setValidValue(value, getTimeNowNt());
	}

	void invalidate() {
		StoredValueSensor::invalidate();
	}

	void showInfo(Logging* logger, const char* name) const override {}
};
