/*
 * @file injection_mode_transition.cpp
 *
 *  Created on: Jul 19, 2020
 * @author Andrey Belomutskiy, (c) 2012-2020
 */

#include "engine_test_helper.h"


// https://github.com/rusefi/rusefi/issues/1592
TEST(fuelControl, transitionIssue1592) {

	WITH_ENGINE_TEST_HELPER(TEST_ENGINE);
	setupSimpleTestEngineWithMafAndTT_ONE_trigger(&eth, IM_SEQUENTIAL);

	eth.fireTriggerEvents2(4 /* count */ , 600 /* ms */);

	ASSERT_EQ(CRANKING, engine->rpmCalculator.getState());
	ASSERT_EQ( 100,  GET_RPM()) << "spinning-RPM#1";

}

